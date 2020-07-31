CREATE TABLE mst_user(
    id INT(11) AUTO_INCREMENT NOT NULL,
    user_name VARCHAR(64) NOT NULL,
    password varchar(64),
    status INT(11) NOT NULL,
    branch_code varchar(64) DEFAULT NULL,
    position_code varchar(64) DEFAULT NULL,
    business_code varchar(64) DEFAULT NULL,
    insert_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    insert_user_id int(11) NOT NULL,
    update_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_user_id int(11) NOT NULL,
    delete_date timestamp DEFAULT NULL,
    delete_user_id int(11) DEFAULT NULL,
    PRIMARY KEY (id)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE mst_auth(
    id INT(11) AUTO_INCREMENT NOT NULL,
    status VARCHAR(64),
    status_name VARCHAR(64),
    insert_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    insert_user_id int(11) NOT NULL,
    update_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_user_id int(11) NOT NULL,
    delete_date timestamp NULL DEFAULT NULL,
    delete_user_id int(11) DEFAULT NULL,
    auth_code VARCHAR(64),
    name VARCHAR(64),
    PRIMARY KEY (id)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE account(
    id INT(11) AUTO_INCREMENT NOT NULL,
    account_number INT(11) NULL DEFAULT 0,
    client_id INT(11) NULL DEFAULT 0,
    branch_code varchar(64) NULL DEFAULT 0,
    insert_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    insert_user_id int(11) NOT NULL,
    update_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_user_id int(11) NOT NULL,
    delete_date timestamp NULL DEFAULT NULL,
    delete_user_id int(11) DEFAULT NULL,
    PRIMARY KEY (id)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE client(
    id INT(11) AUTO_INCREMENT NOT NULL,
    client_name varchar(64) NULL DEFAULT 0,
    client_name_kana varchar(64) NULL DEFAULT 0,
    tell varchar(64) NULL DEFAULT 0,
    mail_address varchar(64) NULL DEFAULT 0,
    password varchar(64) NULL DEFAULT 0,
    insert_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    insert_user_id int(11) NOT NULL,
    update_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_user_id int(11) NOT NULL,
    delete_date timestamp NULL DEFAULT NULL,
    delete_user_id int(11) DEFAULT NULL,
    PRIMARY KEY (id)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE task(
    id INT(11) AUTO_INCREMENT NOT NULL,
    account_number INT(11) NULL DEFAULT 0,
    pay_account_number INT(11) NULL DEFAULT 0,
    type INT(11) NULL DEFAULT 0,
    pool_flag INT(11) NULL DEFAULT 0,
    amount INT(11) NULL DEFAULT 0,
    fee_id INT(11) NULL DEFAULT 0,
    balance INT(11) NULL DEFAULT 0,
    trading_date timestamp NULL DEFAULT NULL,
    insert_date timestamp NULL DEFAULT NULL,
    insert_user_id int(11) DEFAULT NULL,
    update_date timestamp NULL DEFAULT NULL,
    update_user_id int(11) DEFAULT NULL,
    PRIMARY KEY (id)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE transaction(
    id INT(11) AUTO_INCREMENT NOT NULL,
    account_number INT(11) NULL DEFAULT 0,
    pay_account_number INT(11) NULL DEFAULT 0,
    type INT(11) NULL DEFAULT 0,
    pool_flag INT(11) NULL DEFAULT 0,
    amount INT(11) NULL DEFAULT 0,
    fee_id INT(11) NULL DEFAULT 0,
    balance INT(11) NULL DEFAULT 0,
    trading_date timestamp NULL DEFAULT NULL,
    insert_date timestamp NULL DEFAULT NULL,
    insert_user_id int(11) DEFAULT NULL,
    update_date timestamp NULL DEFAULT NULL,
    update_user_id int(11) DEFAULT NULL,
    PRIMARY KEY (id)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE mst_fee(
    id INT(11) AUTO_INCREMENT NOT NULL,
    fee_code varchar(64) DEFAULT NULL,
    fee_price INT(11) NULL DEFAULT 0,
    branch_code varchar(64) DEFAULT NULL,
    business_frag varchar(64) DEFAULT NULL,
    start_day varchar(64) DEFAULT NULL,
    end_day varchar(64) DEFAULT NULL,
    insert_date timestamp NULL DEFAULT NULL,
    insert_user_id int(11) DEFAULT NULL,
    update_date timestamp NULL DEFAULT NULL,
    update_user_id int(11) DEFAULT NULL,
    PRIMARY KEY (id)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `user_auth` (
  `user_id` int NOT NULL,
  `auth_id` int NOT NULL,
  PRIMARY KEY(`user_id`, `auth_id`),
  foreign key `fk_user` (`user_id`) references `mst_user`(`id`),
  foreign key `fk_auth` (`auth_id`) references `mst_auth`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO mst_auth VALUES(1, '社員管理者', null, NOW(), 1, NOW(), 1, null, null, 'USER', 'USER');
INSERT INTO mst_auth VALUES(2, '取引管理者', null, NOW(), 1, NOW(), 1, null, null, 'TRANSACTION', 'TRANSACTION');
INSERT INTO mst_auth VALUES(3, '口座管理者', null, NOW(), 1, NOW(), 1, null, null, 'ACCOUNT', 'ACCOUNT');
INSERT INTO mst_auth VALUES(4, '顧客管理者', null, NOW(), 1, NOW(), 1, null, null, 'CLIENT', 'CLIENT');
INSERT INTO mst_auth VALUES(5, '権限管理者', null, NOW(), 1, NOW(), 1, null, null, 'AUTH', 'AUTH');
INSERT INTO mst_auth VALUES(6, '手数料管理者', null, NOW(), 1, NOW(), 1, null, null, 'FEE', 'FEE');
INSERT INTO mst_auth VALUES(7, 'システム管理者', null, NOW(), 1, NOW(), 1, null, null, 'ADMIN', 'ADMIN');

INSERT INTO mst_user VALUES(1, 'user', '$2a$10$hejdr6e.rtPRd7YjU3dhbudNE8sPjWEvPLTGaYXiN16Hn4iiUgS1a', 1, '', '', '', NOW(), 1, NOW(), 1, null, null);
INSERT INTO mst_user VALUES(2, 'transaction', '$2a$10$hejdr6e.rtPRd7YjU3dhbudNE8sPjWEvPLTGaYXiN16Hn4iiUgS1a', 1, '', '', '', NOW(), 1, NOW(), 1, null, null);
INSERT INTO mst_user VALUES(3, 'account', '$2a$10$hejdr6e.rtPRd7YjU3dhbudNE8sPjWEvPLTGaYXiN16Hn4iiUgS1a', 1, '', '', '', NOW(), 1, NOW(), 1, null, null);
INSERT INTO mst_user VALUES(4, 'client', '$2a$10$hejdr6e.rtPRd7YjU3dhbudNE8sPjWEvPLTGaYXiN16Hn4iiUgS1a', 1, '', '', '', NOW(), 1, NOW(), 1, null, null);
INSERT INTO mst_user VALUES(5, 'auth', '$2a$10$hejdr6e.rtPRd7YjU3dhbudNE8sPjWEvPLTGaYXiN16Hn4iiUgS1a', 1, '', '', '', NOW(), 1, NOW(), 1, null, null);
INSERT INTO mst_user VALUES(6, 'fee', '$2a$10$hejdr6e.rtPRd7YjU3dhbudNE8sPjWEvPLTGaYXiN16Hn4iiUgS1a', 1, '', '', '', NOW(), 1, NOW(), 1, null, null);
INSERT INTO mst_user VALUES(7, 'admin', '$2a$10$hejdr6e.rtPRd7YjU3dhbudNE8sPjWEvPLTGaYXiN16Hn4iiUgS1a', 1, '', '', '', NOW(), 1, NOW(), 1, null, null);

INSERT INTO user_auth VALUES(1, 1);
INSERT INTO user_auth VALUES(2, 2);
INSERT INTO user_auth VALUES(3, 3);
INSERT INTO user_auth VALUES(4, 4);
INSERT INTO user_auth VALUES(5, 5);
INSERT INTO user_auth VALUES(6, 6);
INSERT INTO user_auth VALUES(7, 7);