CREATE TABLE `group3` (
  `g_id` int NOT NULL AUTO_INCREMENT,
  `group_name` varchar(45) NOT NULL,
  PRIMARY KEY (`g_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

SELECT * FROM testgroup.group3;

/* 초기 데이터 생성 */
insert into group3 (g_id, group_name) values (1, 'smilegate');

insert into group3 (g_id, group_name) values (2, 'stove');
insert into group3 (g_id, group_name) values (3, 'Ent.');

insert into group3 (g_id, group_name) values (4, '플랫폼개발본부');
insert into group3 (g_id, group_name) values (5, 'CF개발본부');

insert into group3 (g_id, group_name) values (6, 'DevCamp');
insert into group3 (g_id, group_name) values (7, 'TF개발실');
insert into group3 (g_id, group_name) values (8, 'CF스튜디오');
insert into group3 (g_id, group_name) values (9, 'CF스튜디오모바일');

insert into group3 (g_id, group_name) values (10, 'JobJava');
insert into group3 (g_id, group_name) values (11, 'BigSmile');
insert into group3 (g_id, group_name) values (12, 'SoulMates');
insert into group3 (g_id, group_name) values (13, 'TF-FE');
insert into group3 (g_id, group_name) values (14, 'TF-BE');

insert into group3 (g_id, group_name) values (15, '개발실');
insert into group3 (g_id, group_name) values (16, '기획실');

insert into group1 (g_id, group_name) values (17, '엔진팀');
insert into group1 (g_id, group_name) values (18, '서버팀');


