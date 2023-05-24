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