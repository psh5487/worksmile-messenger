CREATE TABLE `id_relation` (
  `parent_id` int NOT NULL,
  `child_id` int NOT NULL,
  `level` int DEFAULT NULL,
  PRIMARY KEY (`parent_id`,`child_id`),
  KEY `child_id_idx` (`child_id`),
  CONSTRAINT `child_id` FOREIGN KEY (`child_id`) REFERENCES `group3` (`g_id`),
  CONSTRAINT `parent_id` FOREIGN KEY (`parent_id`) REFERENCES `group3` (`g_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SELECT * FROM testgroup.id_relation order by parent_id asc;

/* 초기 데이터 생성 */
insert into id_relation values (1, 1);
insert into id_relation values (1, 2);
insert into id_relation values (1, 3);
insert into id_relation values (1, 4);
insert into id_relation values (1, 5);
insert into id_relation values (1, 6);
insert into id_relation values (1, 7);
insert into id_relation values (1, 8);
insert into id_relation values (1, 9);
insert into id_relation values (1, 10);
insert into id_relation values (1, 11);
insert into id_relation values (1, 12);
insert into id_relation values (1, 13);
insert into id_relation values (1, 14);
insert into id_relation values (1, 15);
insert into id_relation values (1, 16);
insert into id_relation values (1, 17);
insert into id_relation values (1, 18);

insert into id_relation values (2, 2);
insert into id_relation values (2, 4);
insert into id_relation values (2, 6);
insert into id_relation values (2, 7);
insert into id_relation values (2, 10);
insert into id_relation values (2, 11);
insert into id_relation values (2, 12);
insert into id_relation values (2, 13);
insert into id_relation values (2, 14);

insert into id_relation values (3, 3);
insert into id_relation values (3, 5);
insert into id_relation values (3, 8);
insert into id_relation values (3, 9);
insert into id_relation values (3, 15);
insert into id_relation values (3, 16);
insert into id_relation values (3, 17);
insert into id_relation values (3, 18);

insert into id_relation values (4, 4);
insert into id_relation values (4, 6);
insert into id_relation values (4, 7);
insert into id_relation values (4, 10);
insert into id_relation values (4, 11);
insert into id_relation values (4, 12);
insert into id_relation values (4, 13);
insert into id_relation values (4, 14);

insert into id_relation values (5, 5);
insert into id_relation values (5, 8);
insert into id_relation values (5, 9);
insert into id_relation values (5, 15);
insert into id_relation values (5, 16);
insert into id_relation values (5, 17);
insert into id_relation values (5, 18);

insert into id_relation values (6, 6);
insert into id_relation values (6, 10);
insert into id_relation values (6, 11);
insert into id_relation values (6, 12);

insert into id_relation values (7, 7);
insert into id_relation values (7, 13);
insert into id_relation values (7, 14);

insert into id_relation values (8, 8);
insert into id_relation values (8, 15);
insert into id_relation values (8, 16);
insert into id_relation values (8, 17);
insert into id_relation values (8, 18);

insert into id_relation values (9, 9);

insert into id_relation values (10, 10);

insert into id_relation values (11, 11);

insert into id_relation values (12, 12);

insert into id_relation values (13, 13);

insert into id_relation values (14, 14);

insert into id_relation values (15, 15);
insert into id_relation values (15, 17);
insert into id_relation values (15, 18);

insert into id_relation values (16, 16);

insert into id_relation values (17, 17);

insert into id_relation values (18, 18);

/* group_id가 #인 데이터의 하위 모든 노드 조회 */
select r.*, g3.*
from id_relation r JOIN group3 g3
on r.parent_id = g3.g_id
where r.parent_id = 4
AND r.child_id != 4;

select g3.*
from id_relation r JOIN group3 g3
on r.child_id = g3.g_id
where r.parent_id = 4
AND r.child_id != 4;

/* group_id가 #인 노드의 바로 다음 하위 노드만 조회 */
explain
select g3.*
from id_relation r JOIN group3 g3
on r.child_id = g3.g_id
where r.parent_id = 4
-- AND r.level = (
-- 				select r.level 
-- 				from id_relation r 
-- 				where r.parent_id = 4 
--                 AND r.child_id = 4) + 1;
AND r.level = 4;

/* 
	데이터 추가 
    level과 추가할 노드의 바로 상위 부모 id는 웹 프론트에서 받아 와야할 듯
*/
insert into group3 values (19, '플랫폼운영사업부');
insert into id_relation (parent_id, child_id, level)
select LAST_INSERT_ID(), LAST_INSERT_ID(), 3
union all
select parent_id, LAST_INSERT_ID(), 3
from id_relation
where child_id = 2;

/*
	장점 : 데이터 무결성을 지키기 좋다
		: depth가 내려가도 하위 노드를 조회할 때, 성능이 그대로
    단점 : 데이터의 양이 O(N^2)개라 많다.
*/