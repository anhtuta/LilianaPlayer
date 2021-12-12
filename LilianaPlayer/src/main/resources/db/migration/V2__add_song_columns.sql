ALTER TABLE `liliana_player`.`song`
ADD COLUMN `image_url` VARCHAR(500) NULL AFTER `file_name`,
ADD COLUMN `created_date` DATETIME NOT NULL DEFAULT now() AFTER `image_url`;

ALTER TABLE `liliana_player`.`song` 
ADD COLUMN `image_name` VARCHAR(100) NULL AFTER `file_name`;
