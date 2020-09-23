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

 CREATE TABLE `user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `name` VARCHAR(200) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`, `username`));
  
CREATE TABLE `role` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));
  
CREATE TABLE `user_role` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `role_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_user_role_user_id_idx` (`user_id` ASC),
  INDEX `fk_user_role_role_id_idx` (`role_id` ASC),
  CONSTRAINT `fk_user_role_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_role_role_id`
    FOREIGN KEY (`role_id`)
    REFERENCES `role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
  