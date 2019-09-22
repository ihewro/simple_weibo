-- 保存sql语句
create table user_info
(
	id          int           auto_increment,
	name      varchar(63)   not null,
	password     varchar(1023) not null,
	time        long          not null,
	constraint user_info
		primary key (id)
);