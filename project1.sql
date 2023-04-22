-- 请注意，这些表创建语句不包括在插入数据时为不在作者表中的评论者生成 ID 及合理注册时间并添加至作者表的逻辑。
-- 这需要在插入数据时通过应用程序逻辑来实现，例如使用 Python 或其他编程语言与数据库进行交互。

--请注意，这里没有自动为不在作者表中的评论和次级评论作者生成ID及注册时间。
-- 在实际应用中，这种逻辑需要在应用程序代码中实现，而不是数据库模式中实现。
-- 一般来说，当用户提交评论或次级评论时，应用程序应检查作者是否已经存在，如果不存在，则在插入评论或次级评论之前先插入作者。

-- 1. 作者表
create table authors
(
    author_id         varchar primary key ,
    author_name       varchar(255) not null unique ,
    registration_time timestamp           not null,
    phone             bigint unique  --删除notnull，改变为bigint类型
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
create table replies
(
    reply_id    integer      not null primary key,
    content     text         not null,
    stars       integer      not null,
    author_id   varchar      not null references authors (author_id), -- 改为authorID
    post_id     integer      not null references posts (post_id)
);

-- 5. 次级评论表
create table secondary_replies
(
    secondary_reply_id integer      primary key,
    content            text         not null,
    stars              integer      not null,
    author_id          varchar      not null references authors (author_id),
    reply_id           integer      not null references replies (reply_id)
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
