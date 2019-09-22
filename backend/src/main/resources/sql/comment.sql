create table comment
(
	id int auto_increment,
	content text,
	userid int,
	recordid int,
	time long,
	constraint comment_RECORD_INFO_ID_fk
		foreign key (recordid) references RECORD_INFO,
	constraint comment_USER_INFO__id
		foreign key (userid) references USER_INFO
);

create unique index comment_id_uindex
	on comment (id);

alter table comment
	add constraint comment_pk
		primary key (id);

