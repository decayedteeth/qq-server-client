drop database qqdb;
create database qqdb default character set utf8;
use qqdb;
CREATE TABLE MEMBER (
     ID  INTEGER not null primary key,
    NAME  VARCHAR(32),
   PASSWORD  VARCHAR(256),
    EMAIL  VARCHAR(64),
    HEADIMAGE  VARCHAR(128),
     TIME  TIMESTAMP
);
insert into member values(10000,'张三','123456','zhangsan@163.com','i9000.jpg',current_timestamp);
insert into member values(20000,'李四','123456','lisi@163.com','i9001.jpg',current_timestamp);
insert into member values(30000,'王五','123456','wangwu@163.com','i9002.jpg',current_timestamp);

