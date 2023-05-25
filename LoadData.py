import json
import random
import sys
import time
import psycopg2 as psql

filename = 'posts.json'  # the path of json file
with open(filename) as f:
    posts = json.load(f)
filename = 'replies.json'  # the path of json file
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
                + "drop table if exists categories;"
                + "drop table if exists blacklist;")
    cur.execute("""
-- 1. 作者表
create table authors
(
    author_id         varchar(20) primary key ,
    author_name       varchar(255) not null unique,
    author_key        varchar(20) not null,
    registration_time timestamp           not null,
    phone             varchar(255) unique  --变长字符串
);

-- 2. 城市表，调整顺序
create table cities
(
    city_name varchar(255) primary key ,--城市名字
    city_state varchar(255) not null --添加国家
);

-- 3. 文章表
create table posts
(
    post_id      integer unique primary key, --去掉自增
    author_id    varchar not null references authors (author_id), -- 更改为id，考虑到可扩展性（比如作者改名字）
    title        varchar(255) not null,
    content      text         not null,
    posting_time timestamp    not null,
    posting_city varchar(255) not null references cities(city_name) --post与城市是一对多的关系，一个post只能对应一个城市，但一个城市能对应多个post
);

-- 4. 评论表
CREATE TABLE replies
(
    reply_id    integer      not null primary key,
    content     text         not null,
    stars       integer      not null,
    author_id   varchar      not null references authors (author_id), -- 改为authorID
    post_id     integer      not null references posts (post_id),
    is_anonymous boolean      not null default false -- 新添加的列
);

-- 5. 次级评论表
create table secondary_replies
(
    secondary_reply_id integer      primary key,
    content            text         not null,
    stars              integer      not null,
    author_id          varchar      not null references authors (author_id),
    reply_id           integer      not null references replies (reply_id),
    is_anonymous boolean      not null default false -- 新添加的列
);

-- 6. 关注表
create table followed_authors
(
    author_id          varchar references authors (author_id) not null ,
    follower_author_id varchar references authors(author_id), --改不改？
    constraint followed primary key (author_id, follower_author_id)
);

-- 7. 收藏表
create table favorite_posts
(
    post_id             integer references posts (post_id),
    favorite_author_id  varchar not null,
    constraint favorite_pk primary key (post_id, favorite_author_id)
);

-- 8. 分享表
create table shared_posts
(
    post_id           integer references posts (post_id),
    sharing_author_id varchar not null,
    constraint shared_pk primary key (post_id, sharing_author_id)
);

-- 9. 点赞表
create table liked_posts
(
    post_id          integer references posts (post_id),
    liking_author_id varchar not null,
    constraint liked_pk primary key (post_id, liking_author_id)
);

-- 10. 分类表 --满足扩展性（可在categories新建类别）
create table categories
(
    category_id   integer primary key,
    category_name varchar(255) unique not null
);

-- 11. 文章分类表
create table post_categories
(
    post_id     integer references posts (post_id) not null ,
    category_id integer not null references categories (category_id),
    constraint post_categories_pk primary key (post_id, category_id)
);

-- 12. 拉黑表
CREATE TABLE blacklist
(
    id                 SERIAL PRIMARY KEY,
    author_id          VARCHAR NOT NULL,
    blocked_author_id  VARCHAR NOT NULL,
    CONSTRAINT unique_blacklist UNIQUE (author_id, blocked_author_id)
);


        """)


def initPostAuthor(cnt):
    for post in posts:
        sqlPost = """INSERT INTO authors (author_id, author_name,author_key ,registration_time, phone) VALUES (%s, %s,123456, %s, %s)"""
        param = [post['AuthorID'], post['Author'], post['AuthorRegistrationTime'], post['AuthorPhone']]
        try:
            cur.execute(sqlPost, param)
            # print('OK')
            cnt += 1
        except psql.Error as e:
            print('already Exist')
            print(e)
    return cnt


# for post in posts:
#     #print(type(post)) # it is a dist type
#     print(post['Post ID'])
#     #print(json.dumps(post, indent=1))

def initAllAuthor(cnt):
    sqlPost = """INSERT INTO authors (author_id, author_name,author_key, registration_time, phone) VALUES (%s, %s,123456, %s, %s)"""
    for author in OtherAuthor.keys():
        diffTime = random.randint(0, 100000000)
        now = time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time() - diffTime))
        phone = None
        values = [OtherAuthor[author], author, now, phone]
        try:
            cur.execute(sqlPost, values)
            # print("OK")
            cnt += 1
        except Exception as e:
            print(e)
    return cnt


def initCity(cnt):
    sqlCity = """INSERT INTO cities (city_name, city_state) VALUES (%s, %s)"""
    for city in Cities.keys():
        city_name_state = [city, Cities[city]]
        try:
            cur.execute(sqlCity, city_name_state)
            # print("OK")
            cnt += 1
        except psql.Error as e:
            print(e)
    return cnt


def initCategories(cnt):
    sqlCate = """INSERT INTO categories (category_id, category_name) VALUES (%s, %s)"""
    for param in Cats.keys():
        vaules = [Cats[param], param]
        try:
            cur.execute(sqlCate, vaules)
            # print('OK')
            cnt += 1
        except psql.Error as e:
            print(e)
    return cnt


def initPost(cnt):
    sqlPost = """INSERT INTO posts (post_id, author_id, title, content, posting_time, posting_city) VALUES (%s, %s, %s, %s, %s, %s);"""
    for post in posts:
        post_id = post['PostID']
        authorID = AllAuthor[post['Author']]
        title = post['Title']
        content = post['Content']
        posting_time = post['PostingTime']
        posting_city = str(post['PostingCity']).rsplit(", ", 1)[0]
        param = [post_id, authorID, title, content, posting_time, posting_city]
        try:
            cur.execute(sqlPost, param)
            # print('OK')
            cnt += 1
        except psql.Error as e:
            print(e)
    return cnt


def initPost_categories(cnt):
    sqlP_C = """INSERT INTO post_categories (post_id, category_id) VALUES (%s, %s);"""
    for post in posts:
        post_id = post['PostID']
        category = post['Category']
        for val in category:
            param = [post_id, Cats[val]]
            try:
                cur.execute(sqlP_C, param)
                cnt += 1
                # print('OK')
            except psql.Error as e:
                print(e)
    return cnt


def initLiked_authors(cnt):
    sqlLiked = """INSERT INTO liked_posts (post_id, liking_author_id) VALUES (%s, %s);"""
    for post in posts:
        post_id = post['PostID']
        likedParam = post['AuthorLiked']
        for author in likedParam:
            param = [post_id, AllAuthor[author]]
            try:
                cur.execute(sqlLiked, param)
                cnt += 1
                # print('OK')
            except psql.Error as e:
                print(e)
    return cnt


def initShare_authors(cnt):
    sqlShare = """INSERT INTO shared_posts (post_id, sharing_author_id) VALUES (%s, %s);"""
    for post in posts:
        post_id = post['PostID']
        shareParam = post['AuthorShared']
        for author in shareParam:
            param = [post_id, AllAuthor[author]]
            try:
                cur.execute(sqlShare, param)
                # print('OK')
                cnt += 1
            except psql.Error as e:
                print(e)
    return cnt


def initFavor_authors(cnt):
    sqlFavor = """INSERT INTO favorite_posts (post_id, favorite_author_id) VALUES (%s, %s);"""
    for post in posts:
        post_id = post['PostID']
        favorParam = post['AuthorFavorite']
        for author in favorParam:
            param = [post_id, AllAuthor[author]]
            try:
                cur.execute(sqlFavor, param)
                cnt += 1
                # print('OK')
            except psql.Error as e:
                print(e)
    return cnt


def initFollowed_authors(cnt):
    sqlPost = """INSERT INTO followed_authors (author_id, follower_author_id) VALUES (%s, %s);"""
    for post in posts:
        author = post['Author']
        AuthorFollowedBy = post['AuthorFollowedBy']
        for follow in AuthorFollowedBy:
            param = [AllAuthor[author], AllAuthor[follow]]
            try:
                cur.execute(sqlPost, param)
                cnt += 1
                # print('OK')
            except psql.Error as e:
                print(e)
    return cnt


def initReplies(replySize, secondaryReplySize, cnt):
    sqlReply = """INSERT INTO replies (reply_id, content, stars, author_id, post_id) VALUES (%s, %s, %s, %s, %s)"""
    sqlSecondReply = """INSERT INTO secondary_replies (secondary_reply_id, content, stars, author_id, reply_id) VALUES (%s, %s, %s, %s, %s)"""
    currentPostID = replies[0]['PostID']
    currentReply = [replies[0]['ReplyContent'], replies[0]['ReplyStars'], AllAuthor[replies[0]['ReplyAuthor']]]
    replySize = replySize + 1
    try:
        cur.execute(sqlReply, [replySize, replies[0]['ReplyContent'], replies[0]['ReplyStars'],
                               AllAuthor[replies[0]['ReplyAuthor']], currentPostID])
        cnt += 1
    except psql.Error as e:
        print(e)
    for reply in replies:
        post_id = reply['PostID']
        single_reply = [reply['ReplyContent'], reply['ReplyStars'], AllAuthor[reply['ReplyAuthor']]]
        if post_id == currentPostID:
            if single_reply == currentReply:
                secondaryReplySize = secondaryReplySize + 1  # 目前未处理secondaryReply为空的情况
                param = [secondaryReplySize, reply['SecondaryReplyContent'], reply['SecondaryReplyStars'],
                         AllAuthor[reply['SecondaryReplyAuthor']], replySize]
                try:
                    cur.execute(sqlSecondReply, param)
                    cnt += 1
                    # print('OK')
                except psql.Error as e:
                    print(e)
            else:
                currentReply = single_reply
                replySize = replySize + 1
                secondaryReplySize = secondaryReplySize + 1
                param1 = [replySize, reply['ReplyContent'], reply['ReplyStars'], AllAuthor[reply['ReplyAuthor']],
                          currentPostID]
                param2 = [secondaryReplySize, reply['SecondaryReplyContent'], reply['SecondaryReplyStars'],
                          AllAuthor[reply['SecondaryReplyAuthor']], replySize]
                try:
                    cur.execute(sqlReply, param1)
                    cnt += 1
                    # print('OK')
                except psql.Error as e:
                    print(e)
                try:
                    cur.execute(sqlSecondReply, param2)
                    cnt += 1
                    # print('OK')
                except psql.Error as e:
                    print(e)
        else:
            currentPostID = post_id
            currentReply = single_reply
            replySize = replySize + 1
            secondaryReplySize = secondaryReplySize + 1
            param1 = [replySize, reply['ReplyContent'], reply['ReplyStars'], AllAuthor[reply['ReplyAuthor']],
                      currentPostID]
            param2 = [secondaryReplySize, reply['SecondaryReplyContent'], reply['SecondaryReplyStars'],
                      AllAuthor[reply['SecondaryReplyAuthor']], replySize]
            try:
                cur.execute(sqlReply, param1)
                cnt += 1
            except psql.Error as e:
                print(e)
            try:
                cur.execute(sqlSecondReply, param2)
                cnt += 1
            except psql.Error as e:
                print(e)
    return cnt


def loadOtherAuthor():
    l1 = []
    l2 = {}
    l3 = {}
    for post in posts:
        l1.extend(post['AuthorFavorite'])
        l1.extend(post['AuthorShared'])
        l1.extend(post['AuthorLiked'])
        l1.extend(post['AuthorFollowedBy'])
        l2[post['Author']] = post['AuthorID']
    for reply in replies:
        l1.append(reply['SecondaryReplyAuthor'])
        l1.append(reply['ReplyAuthor'])
    l1 = set(l1)
    for author in l1:
        newID = random.randint(0, sys.maxsize)
        while newID in l2.values() or newID in l3.values():
            newID = random.randint(sys.maxsize)
        l3[author] = newID
    for author in l2:
        try:
            l3.pop(author)
        except Exception as e:
            pass

    return l2, l3


def loadCategory():
    l1 = {}
    for post in posts:
        param = post['Category']
        for cat in param:
            if cat not in l1.keys():
                l1[cat] = len(l1)
    return l1


def loadCity():
    l1 = {}
    for post in posts:
        city_name_state = str(post['PostingCity']).rsplit(", ", 1)
        l1[city_name_state[0]] = city_name_state[1]
    return l1


if __name__ == '__main__':
    connect = psql.connect(host="localhost", port=5432, user="postgres", password="123456", database="Project1")
    cur = connect.cursor()
    connect.autocommit = False
    timeNeed = []
    testcase = 3
    PostAuthor, OtherAuthor = loadOtherAuthor()
    AllAuthor = {}
    AllAuthor.update(PostAuthor)
    AllAuthor.update(OtherAuthor)
    Cats = loadCategory()
    Cities = loadCity()
    for i in range(testcase):
        cnt = 0
        initDB()
        now = time.time()
        cnt = initPostAuthor(cnt)
        cnt = initAllAuthor(cnt)
        cnt = initCategories(cnt)
        cnt = initCity(cnt)
        cnt = initPost(cnt)
        cnt = initPost_categories(cnt)
        cnt = initShare_authors(cnt)
        cnt = initFavor_authors(cnt)
        cnt = initLiked_authors(cnt)
        cnt = initFollowed_authors(cnt)
        cnt = initReplies(0, 0, cnt)
        connect.commit()
        then = time.time()
        print(cnt, "records", '{:.3f} records/s'.format(cnt / (then - now)))
        print('single import time: {:.3f}/s'.format(then - now))
        timeNeed.append(then - now)
    avgTime = 0
    for t in timeNeed:
        avgTime += t
    print('average import time: {:.3f}/s'.format(avgTime / testcase))
    # print(len(set(author_name)))
    # print(len(list({}.fromkeys(author_name).keys()))) 验证名字不重复
    connect.close()
