CREATE TABLE yandex (
  id          SERIAL       NOT NULL PRIMARY KEY,
  date        CHAR(11)     NOT NULL UNIQUE,
  url         VARCHAR(256) NOT NULL,
  title       VARCHAR(64)  NOT NULL,
  description VARCHAR(512) NOT NULL,
  author_name VARCHAR(32)  NOT NULL,
  author_link VARCHAR(256) NOT NULL,
  partner     VARCHAR(32)  NOT NULL,
  hash_date   CHAR(6)      NOT NULL
);
