CREATE TABLE Post
(
    post_id bigint auto_increment primary key,
    title   varchar(255) not null,
    writer  varchar(255) not null,
    content varchar(255) not null
);