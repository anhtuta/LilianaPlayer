CREATE TABLE `liliana_player`.`song` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(100) NULL,
    `artist` VARCHAR(100) NULL,
    `listens` INT NULL DEFAULT 0,
    `file_path` VARCHAR(500) NULL,
PRIMARY KEY (`id`));

ALTER TABLE `liliana_player`.`song`
ADD COLUMN `album` VARCHAR(100) NULL AFTER `artist`;

ALTER TABLE `liliana_player`.`song`
ADD COLUMN `type` VARCHAR(100) NULL AFTER `album`;

ALTER TABLE `liliana_player`.`song`
CHANGE COLUMN `file_path` `file_name` VARCHAR(500) NULL DEFAULT NULL;