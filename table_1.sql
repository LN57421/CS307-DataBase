-- author
CREATE TABLE author (
    author_id SERIAL PRIMARY KEY,
    author_name VARCHAR(255) NOT NULL UNIQUE,
    author_registration_time TIMESTAMP NOT NULL,
    author_phone VARCHAR(20) NOT NULL UNIQUE
);

-- post
CREATE TABLE post (
    post_id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    category JSONB,
    content TEXT NOT NULL,
    posting_time TIMESTAMP NOT NULL,
    posting_city VARCHAR(255),
    author_id INTEGER NOT NULL REFERENCES author(author_id)
);

-- favorites
CREATE TABLE favorites (
    id SERIAL PRIMARY KEY,
    author_id INTEGER NOT NULL REFERENCES author(author_id),
    post_id INTEGER NOT NULL REFERENCES post(post_id)
);

-- shares
CREATE TABLE shares (
    id SERIAL PRIMARY KEY,
    author_id INTEGER NOT NULL REFERENCES author(author_id),
    post_id INTEGER NOT NULL REFERENCES post(post_id)
);

-- likes
CREATE TABLE likes (
    id SERIAL PRIMARY KEY,
    author_id INTEGER NOT NULL REFERENCES author(author_id),
    post_id INTEGER NOT NULL REFERENCES post(post_id)
);

-- follows
CREATE TABLE follows (
    id SERIAL PRIMARY KEY,
    follower_id INTEGER NOT NULL REFERENCES author(author_id),
    followed_id INTEGER NOT NULL REFERENCES author(author_id)
);

-- reply
CREATE TABLE reply (
    id SERIAL PRIMARY KEY,
    post_id INTEGER NOT NULL REFERENCES post(post_id),
    reply_content TEXT NOT NULL,
    reply_stars INTEGER NOT NULL,
    reply_author_id INTEGER NOT NULL REFERENCES author(author_id),
    parent_reply_id INTEGER REFERENCES reply(id)
);
