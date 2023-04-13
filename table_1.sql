-- 请注意，这些表创建语句不包括在插入数据时为不在作者表中的评论者生成 ID 及合理注册时间并添加至作者表的逻辑。
-- 这需要在插入数据时通过应用程序逻辑来实现，例如使用 Python 或其他编程语言与数据库进行交互。

--请注意，这里没有自动为不在作者表中的评论和次级评论作者生成ID及注册时间。
-- 在实际应用中，这种逻辑需要在应用程序代码中实现，而不是数据库模式中实现。
-- 一般来说，当用户提交评论或次级评论时，应用程序应检查作者是否已经存在，如果不存在，则在插入评论或次级评论之前先插入作者。

-- 1. 作者表
create table authors
(
    author_id         serial primary key,
    author_name       varchar(255) unique not null,
    registration_time timestamp           not null,
    phone             varchar(20) unique  not null
);

-- 2. 文章表
create table posts
(
    post_id      serial primary key,
    author_name  varchar(255) not null references authors (author_name),
    title        varchar(255) not null,
    content      text         not null,
    posting_time timestamp    not null,
    posting_city varchar(255) not null
);

-- 3. 评论表
create table replies
(
    reply_id    serial primary key,
    content     text         not null,
    stars       integer      not null,
    author_name varchar(255) not null references authors (author_name),
    post_id     integer      not null references posts (post_id)
);

-- 4. 次级评论表
create table secondary_replies
(
    secondary_reply_id serial primary key,
    content            text         not null,
    stars              integer      not null,
    author_name        varchar(255) not null references authors (author_name),
    reply_id           integer      not null references replies (reply_id)
);

-- 5. 关注表
create table followed_authors
(
    author_id          integer primary key references authors (author_id),
    follower_author_id integer not null
);

-- 6. 收藏表
create table favorited_posts
(
    post_id             integer primary key references posts (post_id),
    favorited_author_id integer not null
);

-- 7. 分享表
create table shared_posts
(
    post_id           integer primary key references posts (post_id),
    sharing_author_id integer not null
);

-- 8. 点赞表
create table liked_posts
(
    post_id          integer primary key references posts (post_id),
    liking_author_id integer not null
);

-- 9. 分类表
create table categories
(
    category_id   serial primary key,
    category_name varchar(255) unique not null
);

-- 10. 文章分类表
create table post_categories
(
    post_id     integer primary key references posts (post_id),
    category_id integer not null references categories (category_id)
);

-- 11. 城市表
create table cities
(
    city_id   serial primary key,
    city_name varchar(255) unique not null
);


-- 12. 文章城市表
create table post_cities
(
    post_id integer primary key references posts (post_id),
    city_id integer not null references cities (city_id)
);
