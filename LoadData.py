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


def init():
    sqlSelectName = """select author_name from authors;"""
    sqlSelectID = """select author_id from authors;"""
    sqlSelectCategories = """select * from categories"""
    sqlSelectReplySize = """select max(reply_id) from replies"""
    sqlSelectSecondReplySize = """select max(secondary_reply_id) from secondary_replies"""
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
    reply_size = out4[0]
    secondary_reply_size = out5[0]
    author_name = []
    author_id = []
    author_name_id = {}
    categoryAndID = {}
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
    return author_name, author_id, author_name_id, categoryAndID, reply_size, secondary_reply_size


def initPostAuthor():
    for post in posts:
        sqlPost = """INSERT INTO authors (author_id, author_name, registration_time, phone) VALUES (%s, %s, %s, %s)"""
        param = [post['AuthorID'], post['Author'], post['AuthorRegistrationTime'], post['AuthorPhone']]
        try:
            cur.execute(sqlPost, param)
            author_name.append(post['Author'])
            author_id.append(post['AuthorID'])
            author_name_id[post['Author']] = post['AuthorID']
            print('OK')
            connect.commit()
        except psql.Error as e:
            print('already Exist')
            print(e)
            connect.rollback()


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
                print("OK")
                connect.commit()
            except Exception as e:
                print(e)
                connect.rollback()


def initCity():
    sqlCity = """INSERT INTO cities (city_name, city_state) VALUES (%s, %s)"""
    for post in posts:
        city_name_state = str(post['PostingCity']).rsplit(", ", 1)
        try:
            cur.execute(sqlCity, city_name_state)
            print(city_name_state)
            print("OK")
            connect.commit()
        except psql.Error as e:
            print(e)
            connect.rollback()


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
                    connect.commit()
                    print('OK')
                except psql.Error as e:
                    print(e)
                    connect.rollback()


def initPost():
    sqlPost = """INSERT INTO posts (post_id, author_id, title, content, posting_time, posting_city) VALUES (%s, %s, %s, %s, %s, %s);"""
    sqlFavor = """INSERT INTO favorite_posts (post_id, favorite_author_id) VALUES (%s, %s);"""
    sqlShare = """INSERT INTO shared_posts (post_id, sharing_author_id) VALUES (%s, %s);"""
    sqlLiked = """INSERT INTO liked_posts (post_id, liking_author_id) VALUES (%s, %s);"""
    for post in posts:
        post_id = post['PostID']
        authorID = author_name_id[post['Author']]
        title = post['Title']
        content = post['Content']
        posting_time = post['PostingTime']
        posting_city = str(post['PostingCity']).rsplit(", ", 1)[0]
        param = [post_id, authorID, title, content, posting_time, posting_city]
        favorParam = post['AuthorFavorite']
        shareParam = post['AuthorShared']
        likedParam = post['AuthorLiked']
        try:
            cur.execute(sqlPost, param)
            connect.commit()
            print('OK')
        except psql.Error as e:
            print(e)
            connect.rollback()
        for author in favorParam:
            param = [post_id, author_name_id[author]]
            try:
                cur.execute(sqlFavor, param)
                connect.commit()
                print('OK')
            except psql.Error as e:
                print(e)
                connect.rollback()
        for author in shareParam:
            param = [post_id, author_name_id[author]]
            try:
                cur.execute(sqlShare, param)
                connect.commit()
                print('OK')
            except psql.Error as e:
                print(e)
                connect.rollback()
        for author in likedParam:
            param = [post_id, author_name_id[author]]
            try:
                cur.execute(sqlLiked, param)
                connect.commit()
                print('OK')
            except psql.Error as e:
                print(e)
                connect.rollback()


def initPost_categories():
    sqlP_C = """INSERT INTO post_categories (post_id, category_id) VALUES (%s, %s);"""
    for post in posts:
        post_id = post['PostID']
        category = post['Category']
        for val in category:
            param = [post_id, categoryAndID[val]]
            try:
                cur.execute(sqlP_C, param)
                connect.commit()
                print('OK')
            except psql.Error as e:
                print(e)
                connect.rollback()


def initFollowed_authors():
    sqlPost = """INSERT INTO followed_authors (author_id, follower_author_id) VALUES (%s, %s);"""
    for post in posts:
        author = post['Author']
        AuthorFollowedBy = post['AuthorFollowedBy']
        for follow in AuthorFollowedBy:
            param = [author_name_id[author], author_name_id[follow]]
            try:
                cur.execute(sqlPost, param)
                connect.commit()
                print('OK')
            except psql.Error as e:
                connect.rollback()
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
        connect.commit()
    except psql.Error as e:
        print(e)
        connect.rollback()
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
                    connect.commit()
                    print('OK')
                except psql.Error as e:
                    print(e)
                    connect.rollback()
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
                    connect.commit()
                    print('OK')
                except psql.Error as e:
                    print(e)
                    connect.rollback()
                try:
                    cur.execute(sqlSecondReply, param2)
                    connect.commit()
                    print('OK')
                except psql.Error as e:
                    print(e)
                    connect.rollback()
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
                connect.commit()
            except psql.Error as e:
                print(e)
                connect.rollback()
            try:
                cur.execute(sqlSecondReply, param2)
                connect.commit()
            except psql.Error as e:
                print(e)
                connect.rollback()


if __name__ == '__main__':
    connect = psql.connect(host="localhost", port=5432, user="postgres", password="123456", database="Project1")
    cur = connect.cursor()
    author_name, author_id, author_name_id, categoryAndID, reply_size, secondary_reply_size = init()
    initPostAuthor()
    initAllAuthor()
    initCategories()
    initCity()
    initPost()
    initPost_categories()
    initFollowed_authors()
    initReplies(reply_size, secondary_reply_size)
    connect.close()
