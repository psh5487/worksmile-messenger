CREATE TABLE `group2` (
  `group_id` int NOT NULL AUTO_INCREMENT,
  `group_path` varchar(45) DEFAULT NULL,
  `group_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

SELECT * FROM testgroup.group2;

/* 초기 데이터 생성 */
insert into group2 (group_path, group_name) values ('1/', 'smilegate');

insert into group2 (group_path, group_name) values ('1/2/', 'stove');
insert into group2 (group_path, group_name) values ('1/3/', 'Ent.');

insert into group2 (group_path, group_name) values ('1/2/4/', '플랫폼개발본부');
insert into group2 (group_path, group_name) values ('1/3/5/', 'CF개발본부');

insert into group2 (group_path, group_name) values ('1/2/4/6/', 'DevCamp');
insert into group2 (group_path, group_name) values ('1/2/4/7/', 'TF개발실');
insert into group2 (group_path, group_name) values ('1/3/5/8/', 'CF스튜디오');
insert into group2 (group_path, group_name) values ('1/3/5/9/', 'CF스튜디오모바일');

insert into group2 (group_path, group_name) values ('1/2/4/6/10/', 'JobJava');
insert into group2 (group_path, group_name) values ('1/2/4/6/11/', 'BigSmile');
insert into group2 (group_path, group_name) values ('1/2/4/6/12/', 'SoulMates');
insert into group2 (group_path, group_name) values ('1/2/4/7/13/', 'TF-FE');
insert into group2 (group_path, group_name) values ('1/2/4/7/14/', 'TF-BE');

insert into group2 (group_path, group_name) values ('1/3/5/8/15/', '개발실');
insert into group2 (group_path, group_name) values ('1/3/5/8/16/', '기획실');

insert into group2 (group_path, group_name) values ('1/3/5/8/15/17/', '엔진팀');
insert into group2 (group_path, group_name) values ('1/3/5/8/15/18/', '서버팀');

/* group_id가 # 하위의 모든 데이터 조회 ; 상위 노드 path는 '1/2/' */
select g2.*
from group2 g2
where g2.group_path like '1/2/%';

/* group_id가 #인 상위의 모든 데이터 조회 */
select g2.*
from group2 g2
where '1/2/4/6/' like concat(g2.group_path, '%');

/* group_id가 # 바로 하위의 데이터만 조회 ; 상위 노드 path는 '1/2/' */
select g2.*
from group2 g2
where g2.group_path like '1/2/4/_/';
-- 1/2/4/ path를 가진 노드의 바로 하위의 데이터 노드의 자릿수가 2자린지 3자린지 모름

/* 
	데이터 추가 ; parent의 group_path를 받고, 이후에 본인 group_id를 뒤에 붙힌 형태로 update
    즉, 2번의 쿼리 필요
*/
insert into group2 (group_path, group_name) values ('1/2/', '플랫폼운영사업부');
update group2 
set group_path = CONCAT(group_path, group_id, '/')
where group_id = LAST_INSERT_ID();

/*
	단점 : 데이터 추가시, 2번의 쿼리가 필요
		: 바로 하위의 데이터 조회가 힘듬 ; 하위 노드의 id가 몇 자리냐에 따라 처리를 따로 해주어야 함
    장점 : adjacent table에 비해 하위 전체의 노드를 가지고 오는 연산이 편함
*/