drop table if exists USERS;
create table Users (
                       USER_NO bigint not null,
                       AUTH_TYPE VARCHAR(20) DEFAULT 'USER',
                       DEL_YN VARCHAR(1) DEFAULT 'N',
                       EMAIL varchar(30) not null,
                       EMAIL_CONFIRM VARCHAR(1) DEFAULT 'N',
                       LOCK_YN VARCHAR(1) DEFAULT 'N',
                       LOGIN_FAIL_CNT integer,
                       NICKNAME varchar(20) not null,
                       PASSWD varchar(30) not null,
                       POINT integer,
                       REG_DATE timestamp not null,
                       REPORT_CNT integer,
                       TEL varchar(11),
                       USER_ID varchar(20) not null,
                       FILE_NO bigint,
                       primary key (USER_NO)
);
alter table Users
    add constraint UK_USER_ID unique (USER_ID);
alter table Users
    add constraint UK_EMAIL unique (EMAIL);
alter table Users
    add constraint UK_NICKNAME unique (NICKNAME);
alter table Users
    add constraint FK_USERS_FILE_NO
        foreign key (FILE_NO)
            references File;