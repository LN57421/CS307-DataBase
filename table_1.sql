-- 请注意，这些表创建语句不包括在插入数据时为不在作者表中的评论者生成 ID 及合理注册时间并添加至作者表的逻辑。
-- 这需要在插入数据时通过应用程序逻辑来实现，例如使用 Python 或其他编程语言与数据库进行交互。

--请注意，这里没有自动为不在作者表中的评论和次级评论作者生成ID及注册时间。
-- 在实际应用中，这种逻辑需要在应用程序代码中实现，而不是数据库模式中实现。
-- 一般来说，当用户提交评论或次级评论时，应用程序应检查作者是否已经存在，如果不存在，则在插入评论或次级评论之前先插入作者。

-- 1. 作者表
CREATE TABLE authors (
    author_id SERIAL PRIMARY KEY,
    author_name VARCHAR(255) NOT NULL,
    registration_time TIMESTAMP NOT NULL,
    phone VARCHAR(20) UNIQUE NOT NULL
);

-- 2. 文章表
CREATE TABLE posts (
    post_id SERIAL PRIMARY KEY,
    author_id INTEGER NOT NULL REFERENCES authors (author_id),
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    posting_time TIMESTAMP NOT NULL,
    posting_city VARCHAR(255) NOT NULL
);

-- 3. 评论表
CREATE TABLE replies (
    reply_id SERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    stars INTEGER NOT NULL,
    author_id INTEGER NOT NULL REFERENCES authors (author_id),
    post_id INTEGER NOT NULL REFERENCES posts (post_id)
);

-- 4. 次级评论表
CREATE TABLE secondary_replies (
    secondary_reply_id SERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    stars INTEGER NOT NULL,
    author_id INTEGER NOT NULL REFERENCES authors (author_id),
    reply_id INTEGER NOT NULL REFERENCES replies (reply_id)
);

-- 5. 关注表
CREATE TABLE followed_authors (
    author_id INTEGER PRIMARY KEY REFERENCES authors (author_id),
    follower_author_id INTEGER NOT NULL
);

-- 6. 收藏表
CREATE TABLE favorited_posts (
    post_id INTEGER PRIMARY KEY REFERENCES posts (post_id),
    favorited_author_id INTEGER NOT NULL
);

-- 7. 分享表
CREATE TABLE shared_posts (
    post_id INTEGER PRIMARY KEY REFERENCES posts (post_id),
    sharing_author_id INTEGER NOT NULL
);

-- 8. 点赞表
CREATE TABLE liked_posts (
    post_id INTEGER PRIMARY KEY REFERENCES posts (post_id),
    liking_author_id INTEGER NOT NULL
);

-- 9. 分类表
CREATE TABLE categories (
    category_id SERIAL PRIMARY KEY,
    category_name VARCHAR(255) NOT NULL
);

-- 10. 文章分类表
CREATE TABLE post_categories (
    post_id INTEGER PRIMARY KEY REFERENCES posts (post_id),
    category_id INTEGER NOT NULL REFERENCES categories (category_id)
);
