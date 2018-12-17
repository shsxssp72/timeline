DROP DATABASE IF EXISTS timeline;
CREATE DATABASE timeline CHARACTER SET utf8;;
USE timeline;
CREATE TABLE UserInfo (
  user_id       INT PRIMARY KEY,
  username      VARCHAR(20),
  display_name  VARCHAR(20),
  user_password CHAR(255),
  salt          CHAR(255),
  last_login    DATETIME
);

CREATE TABLE Content (
  content_id   INT PRIMARY KEY,
  user_id      INT,
  publish_time DATETIME,
  content      TEXT,
  FOREIGN KEY (user_id) REFERENCES UserInfo(user_id)
);

GRANT ALL PRIVILEGES ON timeline.* TO 'timeline'@'%' IDENTIFIED BY 'commonProject';
