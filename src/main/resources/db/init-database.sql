/*DROP TABLE IF EXISTS backup_config;*/
CREATE TABLE IF NOT EXISTS backup_config (
	id CHAR(15) PRIMARY KEY,
	sys_type CHAR(1) NOT NULL,
    address VARCHAR(15),
    username VARCHAR(32),
    password VARCHAR(32),
    connect_type CHAR(1)
);

/*DROP TABLE IF EXISTS backup_config_data;*/
CREATE TABLE IF NOT EXISTS backup_config_data (
	id CHAR(15) PRIMARY KEY,
	backup_config_id CHAR(15),
    shell_commands TEXT,
    backup_paths TEXT,
    cron_expr VARCHAR(128),
    filename VARCHAR(64)
);