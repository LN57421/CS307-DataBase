-- 请注意，这些表创建语句不包括在插入数据时为不在作者表中的评论者生成 ID 及合理注册时间并添加至作者表的逻辑。
-- 这需要在插入数据时通过应用程序逻辑来实现，例如使用 Python 或其他编程语言与数据库进行交互。
--
-- 1. 作者（Author）表
CREATE TABLE authors (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    registration_time TIMESTAMP NOT NULL,
    phone VARCHAR(20) UNIQUE NOT NULL
);

-- 2. 文章（Post）表
CREATE TABLE posts (
    post_id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    posting_time TIMESTAMP NOT NULL,
    posting_city VARCHAR(255) NOT NULL,
    author_id INTEGER NOT NULL,
    FOREIGN KEY (author_id) REFERENCES authors (id)
);

-- 3. 评论（Reply）表
CREATE TABLE replies (
    reply_id SERIAL PRIMARY KEY,
    reply_content TEXT NOT NULL,
    reply_stars INTEGER NOT NULL,
    reply_author_id INTEGER NOT NULL,
    post_id INTEGER NOT NULL,
    FOREIGN KEY (post_id) REFERENCES posts (post_id)
);

-- 4. 次级评论（Secondary Reply）表
CREATE TABLE secondary_replies (
    secondary_reply_id SERIAL PRIMARY KEY,
    secondary_reply_content TEXT NOT NULL,
    secondary_reply_stars INTEGER NOT NULL,
    secondary_reply_author_id INTEGER NOT NULL,
    reply_id INTEGER NOT NULL,
    FOREIGN KEY (reply_id) REFERENCES replies (reply_id)
);

-- 5. 关注（Followed_Authors) 表
CREATE TABLE followed_authors (
    author_id INTEGER PRIMARY KEY,
    follower_author_id INTEGER NOT NULL
);

-- 6. 收藏（Favorited_Posts）表
CREATE TABLE favorited_posts (
    post_id INTEGER PRIMARY KEY,
    favorited_author_id INTEGER NOT NULL,
    FOREIGN KEY (post_id) REFERENCES posts (post_id)
);

-- 7. 分享（Shared_Posts) 表
CREATE TABLE shared_posts (
    post_id INTEGER PRIMARY KEY,
    sharing_author_id INTEGER NOT NULL,
    FOREIGN KEY (post_id) REFERENCES posts (post_id)
);

-- 8. 点赞（Liked_Posts）表
CREATE TABLE liked_posts (
    post_id INTEGER PRIMARY KEY,
    liking_author_id INTEGER NOT NULL,
    FOREIGN KEY (post_id) REFERENCES posts (post_id)
);
