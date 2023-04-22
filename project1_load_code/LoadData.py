import json
import random
import sys
import time
import psycopg2 as psql

filename = 'resource/posts.json'  # the path of json file
with open(filename) as f:
    posts = json.load(f)
filename = 'resource/replies.json'  # the path of json file
with open(filename) as f:
    replies = json.load(f)


def initDB():
    cur.execute("drop table if exists secondary_replies;"
        + "drop table if exists replies;"
        + "drop table if exists followed_authors;"
        + "drop table if exists favorite_posts;"
        + "drop table if exists shared_posts;"
        + "drop table if exists liked_posts;"
        + "drop table if exists post_categories;"
        + "drop table if exists posts;"
        + "drop table if exists authors;"
        + "drop table if exists cities;"
        + "drop table if exists categories;")
    cur.execute("""create table authors
        (
            author_id         varchar primary key ,
            author_name       varchar(255) not null unique ,
            registration_time timestamp           not null,
            phone             bigint unique  --删除notnull，改变为bigint类型
        );
        
        -- 11. 城市表，调整顺序
        create table cities
        (
            city_name varchar(255) primary key ,--城市名字
            city_state varchar(255) not null --添加国家
        );
        
        -- 2. 文章表
        create table posts
        (
            post_id      integer unique primary key, --去掉自增
            author_id    varchar not null references authors (author_id), -- 更改为id，考虑到可扩展性（比如作者改名字）
            title        varchar(255) not null,
            content      text         not null,
            posting_time timestamp    not null,
            posting_city varchar(255) not null references cities(city_name) --post与城市是一对多的关系，一个post只能对应一个城市，但一个城市能对应多个post
        );
        
        -- 3. 评论表
        create table replies
        (
            reply_id    integer      not null primary key,
            content     text         not null,
            stars       integer      not null,
            author_id   varchar      not null references authors (author_id), -- 改为authorID
            post_id     integer      not null references posts (post_id)
        );
        
        -- 4. 次级评论表
        create table secondary_replies
        (
            secondary_reply_id integer      primary key,
            content            text         not null,
            stars              integer      not null,
            author_id          varchar      not null references authors (author_id),
            reply_id           integer      not null references replies (reply_id)
        );
        
        -- 5. 关注表
        create table followed_authors
        (
            author_id          varchar references authors (author_id),
            follower_author_id varchar references authors(author_id), --改不改？
            constraint followed primary key (author_id, follower_author_id)
        );
        
        -- 6. 收藏表
        create table favorite_posts
        (
            post_id             integer references posts (post_id),
            favorite_author_id  varchar not null,
            constraint favorite_pk primary key (post_id, favorite_author_id)
        );
        
        -- 7. 分享表
        create table shared_posts
        (
            post_id           integer references posts (post_id),
            sharing_author_id varchar not null,
            constraint shared_pk primary key (post_id, sharing_author_id)
        );
        
        -- 8. 点赞表
        create table liked_posts
        (
            post_id          integer references posts (post_id),
            liking_author_id varchar not null,
            constraint liked_pk primary key (post_id, liking_author_id)
        );
        
        -- 9. 分类表 --满足扩展性（可在categories新建类别）
        create table categories
        (
            category_id   integer primary key,
            category_name varchar(255) unique not null
        );
        
        -- 10. 文章分类表
        create table post_categories
        (
            post_id     integer references posts (post_id),
            category_id integer not null references categories (category_id),
            constraint post_categories_pk primary key (post_id, category_id)
        );
        """)

def init():
    sqlSelectName = """select author_name from authors;"""
    sqlSelectID = """select author_id from authors;"""
    sqlSelectCategories = """select * from categories"""
    sqlSelectReplySize = """select max(reply_id) from replies"""
    sqlSelectSecondReplySize = """select max(secondary_reply_id) from secondary_replies"""
    sqlSelectCity = """select * from cities"""
    cur.execute(sqlSelectName)
    out1 = cur.fetchall()
    cur.execute(sqlSelectID)
    out2 = cur.fetchall()
    cur.execute(sqlSelectCategories)
    out3 = cur.fetchall()
    cur.execute(sqlSelectReplySize)
    out4 = cur.fetchone()
    cur.execute(sqlSelectSecondReplySize)
    out5 = cur.fetchone()
    cur.execute(sqlSelectCity)
    out6 = cur.fetchall()
    reply_size = out4[0]
    secondary_reply_size = out5[0]
    author_name = []
    author_id = []
    author_name_id = {}
    categoryAndID = {}
    cities = {}
    connect.commit()
    for param in out1:
        author_name.append(param[0])
    for param in out2:
        author_id.append(param[0])
    for param in out3:
        categoryAndID[param[1]] = param[0]
    for i in range(len(author_name)):
        author_name_id[author_name[i]] = author_id[i]
    if reply_size is None:
        reply_size = 0
    if secondary_reply_size is None:
        secondary_reply_size = 0
    for param in out6:
        cities[param[0]] = param[1]
    return author_name, author_id, author_name_id, categoryAndID, reply_size, secondary_reply_size, cities


def initPostAuthor():
    for post in posts:
        sqlPost = """INSERT INTO authors (author_id, author_name, registration_time, phone) VALUES (%s, %s, %s, %s)"""
        param = [post['AuthorID'], post['Author'], post['AuthorRegistrationTime'], post['AuthorPhone']]
        try:
            cur.execute(sqlPost, param)
            author_name.append(post['Author'])
            author_id.append(post['AuthorID'])
            author_name_id[post['Author']] = post['AuthorID']
            #print('OK')
        except psql.Error as e:
            print('already Exist')
            print(e)


# for post in posts:
#     #print(type(post)) # it is a dist type
#     print(post['Post ID'])
#     #print(json.dumps(post, indent=1))

def initAllAuthor():
    sqlPost = """INSERT INTO authors (author_id, author_name, registration_time, phone) VALUES (%s, %s, %s, %s)"""
    param = []
    for post in posts:
        param.extend(post['AuthorFollowedBy'])
        param.extend(post['AuthorFavorite'])
        param.extend(post['AuthorShared'])
        param.extend(post['AuthorLiked'])
    for reply in replies:
        param.append(reply['ReplyAuthor'])
        param.append(reply['SecondaryReplyAuthor'])
    param = set(param)
    for author in param:
        if author not in author_name:
            newID = random.randint(0, sys.maxsize)
            while newID in author_id:
                newID = random.randint(sys.maxsize)
            diffTime = random.randint(0, 100000000)
            now = time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time() - diffTime))
            phone = None
            values = [newID, author, now, phone]
            author_name.append(author)
            author_id.append(newID)
            author_name_id[author] = newID
            try:
                cur.execute(sqlPost, values)
                #print("OK")
            except Exception as e:
                print(e)


def initCity():
    sqlCity = """INSERT INTO cities (city_name, city_state) VALUES (%s, %s)"""
    for post in posts:
        city_name_state = str(post['PostingCity']).rsplit(", ", 1)
        if city_name_state[0] not in cities.keys():
            try:
                cur.execute(sqlCity, city_name_state)
                cities[city_name_state[0]] = city_name_state[1]
                #print("OK")
            except psql.Error as e:
                print(e)


def initCategories():
    sqlCate = """INSERT INTO categories (category_id, category_name) VALUES (%s, %s)"""
    for post in posts:
        category = post['Category']
        for param in category:
            if param not in categoryAndID:
                cID = len(categoryAndID) + 1
                categoryAndID[param] = cID
                vaules = [cID, param]
                try:
                    cur.execute(sqlCate, vaules)
                    #print('OK')
                except psql.Error as e:
                    print(e)


def initPost():
    sqlPost = """INSERT INTO posts (post_id, author_id, title, content, posting_time, posting_city) VALUES (%s, %s, %s, %s, %s, %s);"""
    for post in posts:
        post_id = post['PostID']
        authorID = author_name_id[post['Author']]
        title = post['Title']
        content = post['Content']
        posting_time = post['PostingTime']
        posting_city = str(post['PostingCity']).rsplit(", ", 1)[0]
        param = [post_id, authorID, title, content, posting_time, posting_city]
        try:
            cur.execute(sqlPost, param)
            #print('OK')
        except psql.Error as e:
            print(e)


def initPost_categories():
    sqlP_C = """INSERT INTO post_categories (post_id, category_id) VALUES (%s, %s);"""
    for post in posts:
        post_id = post['PostID']
        category = post['Category']
        for val in category:
            param = [post_id, categoryAndID[val]]
            try:
                cur.execute(sqlP_C, param)
                #print('OK')
            except psql.Error as e:
                print(e)


def initLiked_authors():
    sqlLiked = """INSERT INTO liked_posts (post_id, liking_author_id) VALUES (%s, %s);"""
    for post in  posts:
        post_id = post['PostID']
        likedParam = post['AuthorLiked']
        for author in likedParam:
            param = [post_id, author_name_id[author]]
            try:
                cur.execute(sqlLiked, param)
                # print('OK')
            except psql.Error as e:
                print(e)


def initShare_authors():
    sqlShare = """INSERT INTO shared_posts (post_id, sharing_author_id) VALUES (%s, %s);"""
    for post in posts:
        post_id = post['PostID']
        shareParam = post['AuthorShared']
        for author in shareParam:
            param = [post_id, author_name_id[author]]
            try:
                cur.execute(sqlShare, param)
                #print('OK')
            except psql.Error as e:
                print(e)


def initFavor_authors():
    sqlFavor = """INSERT INTO favorite_posts (post_id, favorite_author_id) VALUES (%s, %s);"""
    for post in posts:
        post_id = post['PostID']
        favorParam = post['AuthorFavorite']
        for author in favorParam:
            param = [post_id, author_name_id[author]]
            try:
                cur.execute(sqlFavor, param)
                #print('OK')
            except psql.Error as e:
                print(e)


def initFollowed_authors():
    sqlPost = """INSERT INTO followed_authors (author_id, follower_author_id) VALUES (%s, %s);"""
    for post in posts:
        author = post['Author']
        AuthorFollowedBy = post['AuthorFollowedBy']
        for follow in AuthorFollowedBy:
            param = [author_name_id[author], author_name_id[follow]]
            try:
                cur.execute(sqlPost, param)
                #print('OK')
            except psql.Error as e:
                print(e)


def initReplies(replySize, secondaryReplySize):
    sqlReply = """INSERT INTO replies (reply_id, content, stars, author_id, post_id) VALUES (%s, %s, %s, %s, %s)"""
    sqlSecondReply = """INSERT INTO secondary_replies (secondary_reply_id, content, stars, author_id, reply_id) VALUES (%s, %s, %s, %s, %s)"""
    currentPostID = replies[0]['PostID']
    currentReply = [replies[0]['ReplyContent'], replies[0]['ReplyStars'], author_name_id[replies[0]['ReplyAuthor']]]
    replySize = replySize + 1
    try:
        cur.execute(sqlReply, [replySize, replies[0]['ReplyContent'], replies[0]['ReplyStars'],
                               author_name_id[replies[0]['ReplyAuthor']], currentPostID])
    except psql.Error as e:
        print(e)
    for reply in replies:
        post_id = reply['PostID']
        single_reply = [reply['ReplyContent'], reply['ReplyStars'], author_name_id[reply['ReplyAuthor']]]
        if post_id == currentPostID:
            if single_reply == currentReply:
                secondaryReplySize = secondaryReplySize + 1  # 目前未处理secondaryReply为空的情况
                param = [secondaryReplySize, reply['SecondaryReplyContent'], reply['SecondaryReplyStars'],
                         author_name_id[reply['SecondaryReplyAuthor']], replySize]
                try:
                    cur.execute(sqlSecondReply, param)
                    #print('OK')
                except psql.Error as e:
                    print(e)
            else:
                currentReply = single_reply
                replySize = replySize + 1
                secondaryReplySize = secondaryReplySize + 1
                param1 = [replySize, reply['ReplyContent'], reply['ReplyStars'], author_name_id[reply['ReplyAuthor']],
                          currentPostID]
                param2 = [secondaryReplySize, reply['SecondaryReplyContent'], reply['SecondaryReplyStars'],
                          author_name_id[reply['SecondaryReplyAuthor']], replySize]
                try:
                    cur.execute(sqlReply, param1)
                    #print('OK')
                except psql.Error as e:
                    print(e)
                try:
                    cur.execute(sqlSecondReply, param2)
                    #print('OK')
                except psql.Error as e:
                    print(e)
        else:
            currentPostID = post_id
            currentReply = single_reply
            replySize = replySize + 1
            secondaryReplySize = secondaryReplySize + 1
            param1 = [replySize, reply['ReplyContent'], reply['ReplyStars'], author_name_id[reply['ReplyAuthor']],
                      currentPostID]
            param2 = [secondaryReplySize, reply['SecondaryReplyContent'], reply['SecondaryReplyStars'],
                      author_name_id[reply['SecondaryReplyAuthor']], replySize]
            try:
                cur.execute(sqlReply, param1)
            except psql.Error as e:
                print(e)
            try:
                cur.execute(sqlSecondReply, param2)
            except psql.Error as e:
                print(e)


if __name__ == '__main__':
    connect = psql.connect(host="localhost", port=5432, user="postgres", password="123456", database="Project1")
    cur = connect.cursor()
    connect.autocommit = False
    timeNeed = []
    testcase = 3
    for i in range(testcase):
        initDB()
        now = time.time()
        author_name, author_id, author_name_id, categoryAndID, reply_size, secondary_reply_size, cities = init()
        initPostAuthor()
        initAllAuthor()
        initCategories()
        initCity()
        initPost()
        initPost_categories()
        initShare_authors()
        initFavor_authors()
        initLiked_authors()
        initFollowed_authors()
        initReplies(reply_size, secondary_reply_size)
        connect.commit()
        then = time.time()
        print('single import time: {:.3f}/s'.format(then - now))
        timeNeed.append(then - now)
    avgTime = 0
    for t in timeNeed:
        avgTime += t
    print('average import time: {:.3f}/s'.format(avgTime / testcase))
    # print(len(set(author_name)))
    # print(len(list({}.fromkeys(author_name).keys()))) 验证名字不重复
    connect.close()
