USE mysql;
-- GRANT ALL ON *.* TO 'root'@'%' WITH GRANT OPTION ;
-- GRANT ALL ON *.* TO 'root'@'localhost' WITH GRANT OPTION;
UPDATE user SET password=PASSWORD('chen0469') WHERE user='root';
FLUSH PRIVILEGES;
CREATE DATABASE IF NOT EXISTS spring_boot_slingshot CHARACTER SET utf8 COLLATE utf8_unicode_ci;
DROP DATABASE IF EXISTS test ;
