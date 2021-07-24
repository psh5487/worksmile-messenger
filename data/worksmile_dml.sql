-- companys DML
SELECT * FROM companys;
TRUNCATE companys;

INSERT INTO companys VALUES (0, '미할당', 'N');
INSERT INTO companys VALUES (1, 'A', 'N');
INSERT INTO companys VALUES (2, 'B', 'Y');
INSERT INTO companys VALUES (3, 'AA', 'Y');
INSERT INTO companys VALUES (4, 'AB', 'Y');
INSERT INTO companys VALUES (5, 'BA', 'N');
INSERT INTO companys VALUES (6, 'AAA', 'N');
INSERT INTO companys VALUES (7, 'AAB', 'N');
INSERT INTO companys VALUES (8, 'AAC', 'N');
INSERT INTO companys VALUES (9, 'ABA', 'N');
INSERT INTO companys VALUES (10, 'BAA', 'N');
INSERT INTO companys VALUES (11, 'BAB', 'N');
INSERT INTO companys VALUES (12, 'ABAA', 'N');
INSERT INTO companys VALUES (13, 'ABAB', 'N');
INSERT INTO companys VALUES (14, 'BABA', 'N');
INSERT INTO companys VALUES (15, 'BABB', 'N');
INSERT INTO companys VALUES (16, 'BAAA', 'N');

-- 회원 가입 시, 회사 리스트 요청
SELECT c.*
FROM companys c
WHERE c.is_subroot = 'Y';

-- 가입 완료 API 요청 시(cid가 넘어옴), 해당 subroot로 root 찾기
-- 1. 해당 cid(3)를 통해 depth를 찾고
SELECT cr.depth
FROM company_relations cr
WHERE cr.parent_id = cr.child_id
AND cr.parent_id = 3;

-- 2. depth가 1인지 아닌지에 따라 다른 쿼리문 실행
-- depth가 1이면 root_cid = subroot_cid

-- depth가 1이 아니면 depth가 1인 root_cid 찾음
SELECT cr2.*
FROM(
	SELECT *
	FROM company_relations
	WHERE child_id = 3 AND parent_id != 3) cr, company_relations cr2
WHERE cr.parent_id = cr2.child_id AND cr2.depth = 1;

-----------------------------------------------------------------------------------------------------------------------------------------------------------------

-- positions DML
SELECT * FROM positions;

INSERT INTO positions VALUES (0, '미할당');
INSERT INTO positions VALUES (1, '인턴');
INSERT INTO positions VALUES (2, '사원');
INSERT INTO positions VALUES (3, '주임');
INSERT INTO positions VALUES (4, '대리');
INSERT INTO positions VALUES (5, '팀장');
INSERT INTO positions VALUES (6, '과장');
INSERT INTO positions VALUES (7, '차장');
INSERT INTO positions VALUES (8, '본부장');

-----------------------------------------------------------------------------------------------------------------------------------------------------------------

-- worksmile_users DML
SELECT * FROM worksmile_users;

INSERT INTO worksmile_users values ('uuid01','uid01','pwd01','','uname01',null,'ROLE_SUPER_ADMIN',6,3,1,8,'email01@gmail.com','01011111111',now(),null,'on');
INSERT INTO worksmile_users values ('uuid02','uid02','pwd02','','uname02',null,'ROLE_ROOT_COMPANY_ADMIN',12,4,1,7,'email02@gmail.com','01012121212',now(),null,'on');
INSERT INTO worksmile_users values ('uuid03','uid03','pwd03','','uname03',null,'ROLE_ROOT_COMPANY_ADMIN',10,2,2,7,'email03@gmail.com','01023232323',now(),null,'on');
INSERT INTO worksmile_users values ('uuid04','uid04','pwd04','','uname04',null,'ROLE_COMPANY_ADMIN',14,2,2,6,'email04@gmail.com','01034343434',now(),null,'on');
INSERT INTO worksmile_users values ('uuid05','uid05','pwd05','','uname05',null,'ROLE_COMPANY_ADMIN',7,3,1,6,'email05@gmail.com','01045454545',now(),null,'on');
INSERT INTO worksmile_users values ('uuid06','uid06','pwd06','','uname06',null,'ROLE_COMPANY_ADMIN',8,3,1,5,'email06@gmail.com','01056565656',now(),null,'on');
INSERT INTO worksmile_users values ('uuid07','uid07','pwd07','','uname07',null,'ROLE_USER',12,4,1,3,'email07@gmail.com','01067676767',now(),null,'on');
INSERT INTO worksmile_users values ('uuid08','uid08','pwd08','','uname08',null,'ROLE_USER',13,4,1,4,'email08@gmail.com','01078787878',now(),null,'on');
INSERT INTO worksmile_users values ('uuid09','uid09','pwd09','','uname09',null,'ROLE_USER',15,2,2,4,'email09@gmail.com','01089898989',now(),null,'on');
INSERT INTO worksmile_users values ('uuid10','uid10','pwd10','','uname10',null,'ROLE_USER',9,4,1,5,'email10@gmail.com','01090909090',now(),null,'on');
INSERT INTO worksmile_users values ('uuid11','uid11','pwd11','','uname11',null,'ROLE_USER',10,2,2,2,'email11@gmail.com','01079797979',now(),null,'on');
INSERT INTO worksmile_users values ('uuid12','uid12','pwd12','','uname12',null,'ROLE_USER',7,3,1,1,'email12@gmail.com','01046464646',now(),null,'on');
INSERT INTO worksmile_users values ('uuid13','uid13','pwd13','','uname13',null,'ROLE_USER',8,3,1,1,'email13@gmail.com','01035353535',now(),null,'on');
INSERT INTO worksmile_users values ('uuid14','uid14','pwd14','','uname14',null,'ROLE_NOT_PERMITTED',6,3,1,1,'email14@gmail.com','01015151515',now(),null,'on');

-----------------------------------------------------------------------------------------------------------------------------------------------------------------

-- rooms DML
SELECT * FROM ROOMS;

INSERT INTO rooms VALUES ('ruuid01', 2, now(), now(), '', 'uid12', 'rname01', 2);
INSERT INTO rooms VALUES ('ruuid02', 10, now(), now(), 'private', 'uid12', 'rname02', 3);
INSERT INTO rooms VALUES ('ruuid03', 200, now(), now(), 'private', 'uid13', 'rname03', 3);
INSERT INTO rooms VALUES ('ruuid04', 1, now(), now(), 'private', 'uid03', 'rname04', 4);
INSERT INTO rooms VALUES ('ruuid05', 100, now(), now(), 'private', 'uid04', 'rname05', 4);
INSERT INTO rooms VALUES ('ruuid06', 2, now(), now(), '', 'uid05', 'rname06', 2);
INSERT INTO rooms VALUES ('ruuid07', 200, now(), now(), 'public', 'uid05', 'rname07', 2);
INSERT INTO rooms VALUES ('ruuid08', 50, now(), now(), 'public', 'uid06', 'rname08', 4);
INSERT INTO rooms VALUES ('ruuid09', 2, now(), now(), '', 'uid06', 'rname09', 4);
INSERT INTO rooms VALUES ('ruuid10', 5, now(), now(), 'public', 'uid01', 'rname10', 3);

-----------------------------------------------------------------------------------------------------------------------------------------------------------------

-- room_user DML
SELECT * FROM room_user;

INSERT INTO room_user (uuid, ruuid, rname) VALUES ('uuid12', 'ruuid01', 'uid04님과의 대화방');
INSERT INTO room_user (uuid, ruuid, rname) VALUES ('uuid04', 'ruuid01', 'uid12님과의 대화방');

INSERT INTO room_user (uuid, ruuid, rname) VALUES ('uuid12', 'ruuid02', '10명 있는 비공개대화방');
INSERT INTO room_user (uuid, ruuid, rname) VALUES ('uuid13', 'ruuid02', '10명 있는 비공개대화방');
INSERT INTO room_user (uuid, ruuid, rname) VALUES ('uuid11', 'ruuid02', '10명 있는 비공개대화방');

INSERT INTO room_user (uuid, ruuid, rname) VALUES ('uuid13', 'ruuid03', '200명 있는 비공개대화방');
INSERT INTO room_user (uuid, ruuid, rname) VALUES ('uuid02', 'ruuid03', '200명 있는 비공개대화방');
INSERT INTO room_user (uuid, ruuid, rname) VALUES ('uuid04', 'ruuid03', '200명 있는 비공개대화방');
INSERT INTO room_user (uuid, ruuid, rname) VALUES ('uuid05', 'ruuid03', '200명 있는 비공개대화방');

INSERT INTO room_user (uuid, ruuid, rname) VALUES ('uuid03', 'ruuid04', '나혼자 있는 대화방');

INSERT INTO room_user (uuid, ruuid, rname) VALUES ('uuid04', 'ruuid05', '100명 있는 비공개대화방');
INSERT INTO room_user (uuid, ruuid, rname) VALUES ('uuid05', 'ruuid05', '100명 있는 비공개대화방');

INSERT INTO room_user (uuid, ruuid, rname) VALUES ('uuid05', 'ruuid06', 'uid01님과의 대화방');
INSERT INTO room_user (uuid, ruuid, rname) VALUES ('uuid01', 'ruuid06', 'uid05님과의 대화방');

INSERT INTO room_user (uuid, ruuid, rname) VALUES ('uuid05', 'ruuid07', '200명 있는 공개대화방');
INSERT INTO room_user (uuid, ruuid, rname) VALUES ('uuid09', 'ruuid07', '200명 있는 공개대화방');
INSERT INTO room_user (uuid, ruuid, rname) VALUES ('uuid08', 'ruuid07', '200명 있는 공개대화방');

INSERT INTO room_user (uuid, ruuid, rname) VALUES ('uuid06', 'ruuid08', '50명 있는 공개대화방');

INSERT INTO room_user (uuid, ruuid, rname) VALUES ('uuid06', 'ruuid09', 'uuid03님과의 대화방');
INSERT INTO room_user (uuid, ruuid, rname) VALUES ('uuid03', 'ruuid09', 'uuid06님과의 대화방');

INSERT INTO room_user (uuid, ruuid, rname) VALUES ('uuid01', 'ruuid10', '5명 있는 공개대화방');
INSERT INTO room_user (uuid, ruuid, rname) VALUES ('uuid02', 'ruuid10', '5명 있는 공개대화방');
INSERT INTO room_user (uuid, ruuid, rname) VALUES ('uuid11', 'ruuid10', '5명 있는 공개대화방');

-----------------------------------------------------------------------------------------------------------------------------------------------------------------

-- company_relations DML
SELECT * FROM company_relations;
SELECT * FROM companys;

INSERT INTO company_relations VALUES (1,1,1);
INSERT INTO company_relations VALUES (1,3,2);
INSERT INTO company_relations VALUES (1,4,2);
INSERT INTO company_relations VALUES (1,6,3);
INSERT INTO company_relations VALUES (1,7,3);
INSERT INTO company_relations VALUES (1,8,3);
INSERT INTO company_relations VALUES (1,9,3);
INSERT INTO company_relations VALUES (1,12,4);
INSERT INTO company_relations VALUES (1,13,4);

INSERT INTO company_relations VALUES (2,2,1);
INSERT INTO company_relations VALUES (2,5,2);
INSERT INTO company_relations VALUES (2,10,3);
INSERT INTO company_relations VALUES (2,11,3);
INSERT INTO company_relations VALUES (2,14,4);
INSERT INTO company_relations VALUES (2,15,4);

INSERT INTO company_relations VALUES (3,3,2);
INSERT INTO company_relations VALUES (3,6,3);
INSERT INTO company_relations VALUES (3,7,3);
INSERT INTO company_relations VALUES (3,8,3);

INSERT INTO company_relations VALUES (4,4,2);
INSERT INTO company_relations VALUES (4,9,3);
INSERT INTO company_relations VALUES (4,12,4);
INSERT INTO company_relations VALUES (4,13,4);

INSERT INTO company_relations VALUES (5,5,2);
INSERT INTO company_relations VALUES (5,10,3);
INSERT INTO company_relations VALUES (5,11,3);
INSERT INTO company_relations VALUES (5,14,4);
INSERT INTO company_relations VALUES (5,15,4);

INSERT INTO company_relations VALUES (6,6,3);

INSERT INTO company_relations VALUES (7,7,3);

INSERT INTO company_relations VALUES (8,8,3);

INSERT INTO company_relations VALUES (9,9,3);
INSERT INTO company_relations VALUES (9,12,4);
INSERT INTO company_relations VALUES (9,13,4);

INSERT INTO company_relations VALUES (10,10,3);

INSERT INTO company_relations VALUES (11,11,3);
INSERT INTO company_relations VALUES (11,14,4);
INSERT INTO company_relations VALUES (11,15,4);

INSERT INTO company_relations VALUES (12,12,4);

INSERT INTO company_relations VALUES (13,13,4);

INSERT INTO company_relations VALUES (14,14,4);

INSERT INTO company_relations VALUES (15,15,4);

/* cid가 5(BA)인 데이터의 하위 모든 노드 조회 */
select c.*
from company_relations r JOIN companys c
on r.child_id = c.cid
where r.parent_id = 5
AND r.child_id != 5;

/* cid가 10인 노드(depth = 3)의 바로 다음 하위 노드(depth = 4)만 조회 */
-- explain
select c.*
from company_relations r JOIN companys c
on r.child_id = c.cid
where r.parent_id = 10
-- AND r.depth = (
-- 				select r.depth 
-- 				from company_relations r 
-- 				where r.parent_id = 4 
--                 AND r.child_id = 4) + 1;
AND r.depth = 4;

/* 
	데이터 추가 
    depth와 추가할 노드의 바로 상위 부모 cid는 웹 프론트에서 받아 와야할 듯
*/
SELECT * FROM companys;
SELECT * FROM company_relations;

insert into companys (cname) values ('BAAA');

insert into company_relations (parent_id, child_id, depth)
select LAST_INSERT_ID(), LAST_INSERT_ID(), 4
union all
select parent_id, LAST_INSERT_ID(), 4
from company_relations
where child_id = 10;

-----------------------------------------------------------------------------------------------------------------------------------------------------------------

-- forbidden_words DML
SELECT * FROM forbidden_words;

INSERT INTO forbidden_words VALUES (1, 'ㅅㅂ');
INSERT INTO forbidden_words VALUES (2, 'ㅈㄹ');
INSERT INTO forbidden_words VALUES (3, 'ㅈㄴ');