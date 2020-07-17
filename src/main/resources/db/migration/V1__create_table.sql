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
    status INT(11) NULL DEFAULT (0),
    status_name VARCHAR(64),
    function_status INT(11) NULL DEFAULT (0),
    insert_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    insert_user_id int(11) NOT NULL,
    update_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_user_id int(11) NOT NULL,
    delete_date timestamp NULL DEFAULT NULL,
    delete_user_id int(11) DEFAULT NULL,
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