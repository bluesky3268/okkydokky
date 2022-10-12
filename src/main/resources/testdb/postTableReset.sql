drop table if exists POST;
create table Post (
                      POST_NO bigint not null,
                      CONT varchar(255) not null,
                      PASSWD varchar(255) not null,
                      POST_TYPE integer not null,
                      recommend integer not null,
                      REG_DATE timestamp not null,
                      TITLE varchar(255) not null,
                      UPD_DATE timestamp,
                      views bigint not null,
                      USER_NO bigint not null,
                      primary key (POST_NO)
)
alter table Post
    add constraint FK_POST_USER_NO
        foreign key (USER_NO)
            references Users