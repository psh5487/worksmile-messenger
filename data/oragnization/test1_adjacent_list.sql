CREATE TABLE `group1` (
  `group_id` int NOT NULL AUTO_INCREMENT,
  `parent_id` int DEFAULT NULL,
  `group_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 ;

SELECT * FROM testgroup.group1;

/* 초기 데이터 생성 */
insert into group1 (parent_id, group_name) values (null, 'smilegate');

insert into group1 (parent_id, group_name) values (1, 'stove');
insert into group1 (parent_id, group_name) values (1, 'Ent.');

insert into group1 (parent_id, group_name) values (2, '플랫폼개발본부');
insert into group1 (parent_idgroup1, group_name) values (3, 'CF개발본부');

insert into group1 (parent_id, group_name) values (4, 'DevCamp');
insert into group1 (parent_id, group_name) values (4, 'TF개발실');
insert into group1 (parent_id, group_name) values (5, 'CF스튜디오');
insert into group1 (parent_id, group_name) values (5, 'CF스튜디오모바일');

insert into group1 (parent_id, group_name) values (6, 'JobJava');
insert into group1 (parent_id, group_name) values (6, 'BigSmile');
insert into group1 (parent_id, group_name) values (6, 'SoulMates');
insert into group1 (parent_id, group_name) values (7, 'TF-FE');
insert into group1 (parent_id, group_name) values (7, 'TF-BE');

insert into group1 (parent_id, group_name) values (8, '개발실');
insert into group1 (parent_id, group_name) values (8, '기획실');

insert into group1 (parent_id, group_name) values (15, '엔진팀');
insert into group1 (parent_id, group_name) values (15, '서버팀');

/* group_id가 # 하위의 모든 데이터 조희 */
/* 그냥 depth만큼 싹다 JOIN하면 됨 ; 효율 극강으로 안좋음 */

/* group_id가 #의 다음 depth 데이터 조회 */
use testgroup;
select g1.*
from group1 g1 JOIN group1 g2 
on g1.parent_id = g2.group_id
where g2.group_id = 2;

/* 데이터 추가 */
insert into group1 (parent_id, group_name) values (2, '플랫폼운영사업부');

/*
	단점 : 모든 하위 노드 조회시, depth가 내려갈수록 기하급수적으로 복잡해짐
    장점 : 데이터 추가시, 기존 데이터의 수정이 없다. 관리하기는 편할 듯
    
    --> 하위 모든 데이터를 가져와야하는 경우도 생길 것 같으므로 부적합
*/