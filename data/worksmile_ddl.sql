use worksmile;

-- companys DDL
CREATE TABLE companys
(
    `cid`    INT(5)         NOT NULL    AUTO_INCREMENT,
    `cname`  VARCHAR(50)    NOT NULL    DEFAULT '', 
    `is_subroot`	CHAR(1)	NOT NULL	DEFAULT 'N',
    PRIMARY KEY (cid)
);

-- positions DDL
CREATE TABLE positions
(
    `pid`    INT(3)         NOT NULL    AUTO_INCREMENT, 
    `pname`  VARCHAR(50)    NOT NULL    DEFAULT '', 
    PRIMARY KEY (pid)
);

-- worksmile_users DDL
CREATE TABLE worksmile_users
(
    `uuid`             CHAR(36)        NOT NULL, 
    `uid`              VARCHAR(45)     NOT NULL    DEFAULT '', 
    `pwd`              CHAR(60)        NOT NULL    DEFAULT '', 
    `salt`             VARCHAR(45)     NULL        DEFAULT '',
    `uname`            VARCHAR(20)     NOT NULL    DEFAULT '', 
    `profile`          JSON            NULL, 
    `role`             VARCHAR(50)     NOT NULL    DEFAULT 'ROLE_NOT_PERMITTED', 
    `cid`              INT(5)          NOT NULL    DEFAULT 0,
    `subroot_cid`      INT(5)          NOT NULL    DEFAULT 0, 
    `root_cid`         INT(5)          NOT NULL    DEFAULT 0, 
    `pid`              INT(3)          NOT NULL    DEFAULT 0, 
    `email`            VARCHAR(100)    NOT NULL    DEFAULT '', 
    `phone`            CHAR(11)        NOT NULL    DEFAULT '01000000000', 
    `register_at`      TIMESTAMP       NOT NULL    DEFAULT now(),
    `login_at`         TIMESTAMP       NULL, 
    `all_push_notice`  CHAR(3)         NOT NULL    DEFAULT 'on', 
    PRIMARY KEY (uuid)
);

-- worksmile_users uid index
CREATE INDEX uid_idx on worksmile_users (uid);

-- worksmile_users foreign key
ALTER TABLE worksmile_users
    ADD CONSTRAINT FK_worksmile_users_pid_positions_pid FOREIGN KEY (pid)
        REFERENCES positions (pid) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE worksmile_users
    ADD CONSTRAINT FK_worksmile_users_cid_companys_cid FOREIGN KEY (cid)
        REFERENCES companys (cid) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE worksmile_users
    ADD CONSTRAINT FK_worksmile_users_root_cid_companys_cid FOREIGN KEY (root_cid)
        REFERENCES companys (cid) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE worksmile_users
    ADD CONSTRAINT FK_worksmile_users_subroot_cid_companys_cid FOREIGN KEY (subroot_cid)
        REFERENCES companys (cid) ON DELETE RESTRICT ON UPDATE RESTRICT;


-- rooms DDL
CREATE TABLE rooms
(
    `ruuid`        CHAR(36)       NOT NULL, 
    `memcnt`       INT(11)        NOT NULL    DEFAULT 0, 
    `register_at`  TIMESTAMP      NOT NULL    DEFAULT now(), 
    `update_at`    TIMESTAMP      NOT NULL    DEFAULT now(), 
    `room_type`    VARCHAR(30)    NOT NULL    DEFAULT '', 
    `leader_id`    VARCHAR(45)    NOT NULL    DEFAULT '', 
    `init_name`    VARCHAR(50)    NOT NULL    DEFAULT '', 
    `root_cid`     INT(5)         NOT NULL    DEFAULT 0, 
    PRIMARY KEY (ruuid)
);

-- rooms foreign key
ALTER TABLE rooms
    ADD CONSTRAINT FK_rooms_leader_id_worksmile_users_uid FOREIGN KEY (leader_id)
        REFERENCES worksmile_users (uid) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE rooms
    ADD CONSTRAINT FK_rooms_root_cid_companys_cid FOREIGN KEY (root_cid)
        REFERENCES companys (cid) ON DELETE RESTRICT ON UPDATE RESTRICT;


-- room_user DDL
CREATE TABLE room_user
(
    `uuid`           CHAR(36)           NOT NULL, 
    `ruuid`          CHAR(36)           NOT NULL, 
    `rname`          VARCHAR(50)        NOT NULL    DEFAULT '', 
    `favorite_type`  CHAR(3)            NOT NULL    DEFAULT 'off', 
    `push_notice`    CHAR(3)            NOT NULL    DEFAULT 'on',
    `last_read_idx`  BIGINT UNSIGNED    NOT NULL    DEFAULT 0, 
    PRIMARY KEY (uuid, ruuid)
);

ALTER TABLE room_user
    ADD CONSTRAINT FK_room_user_ruuid_rooms_ruuid FOREIGN KEY (ruuid)
        REFERENCES rooms (ruuid) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE room_user
    ADD CONSTRAINT FK_room_user_uuid_worksmile_users_uuid FOREIGN KEY (uuid)
        REFERENCES worksmile_users (uuid) ON DELETE RESTRICT ON UPDATE RESTRICT;


-- company_relations DDL
CREATE TABLE company_relations
(
    `parent_id`  INT(5)    NOT NULL, 
    `child_id`   INT(5)    NOT NULL, 
    `depth`      INT(5)    NOT NULL    DEFAULT 0, 
    PRIMARY KEY (parent_id, child_id)
);

ALTER TABLE company_relations
    ADD CONSTRAINT FK_company_relations_parent_id_companys_cid FOREIGN KEY (parent_id)
        REFERENCES companys (cid) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE company_relations
    ADD CONSTRAINT FK_company_relations_child_id_companys_cid FOREIGN KEY (child_id)
        REFERENCES companys (cid) ON DELETE RESTRICT ON UPDATE RESTRICT;


-- forbidden_words DDL
CREATE TABLE forbidden_words
(
    `wid`   BIGINT          NOT NULL    AUTO_INCREMENT, 
    `word`  VARCHAR(100)    NOT NULL    DEFAULT '', 
    PRIMARY KEY (wid)
);


