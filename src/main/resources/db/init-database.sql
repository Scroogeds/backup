/*DROP TABLE IF EXISTS backup_config;*/
CREATE TABLE IF NOT EXISTS backup_config (
	row_id CHAR(32),
	code CHAR(32)  PRIMARY KEY,
    describe TEXT,
    cron_expr VARCHAR(32),
    cron_type CHAR(1),
	create_date CHAR(19),
	update_date CHAR(19)
);

/*DROP TABLE IF EXISTS backup_server;*/
CREATE TABLE IF NOT EXISTS backup_server (
	row_id CHAR(32) PRIMARY KEY,
	config_code CHAR(32),
    sys_type CHAR(1),
    address VARCHAR(32),
    username VARCHAR(32),
    password VARCHAR(32),
    connect_type CHAR(1),
    port VARCHAR(5),
    server_type CHAR(1),
    filepath TEXT,
    save_day_num INT,
	create_date CHAR(19),
	update_date CHAR(19)
);