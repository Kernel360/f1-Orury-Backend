CREATE TABLE IF NOT EXISTS `crew_tag` (
                                              `id`	        BIGINT   	       NOT NULL    AUTO_INCREMENT,
                                              `crew_id`	    BIGINT             NOT NULL,
                                              `tag`         VARCHAR(20)        NOT NULL,
    PRIMARY KEY (`id`)
);

ALTER TABLE `crew_tag` ADD CONSTRAINT `FK_crew_TO_crew_tag_1` FOREIGN KEY (`crew_id`)
    REFERENCES `crew` (`id`)
    ON DELETE CASCADE;

ALTER TABLE `crew` ADD COLUMN min_age TINYINT NOT NULL;
ALTER TABLE `crew` ADD COLUMN max_age TINYINT NOT NULL;
ALTER TABLE `crew` ADD COLUMN gender varchar(1) NOT NULL default 'A';
ALTER TABLE `crew` ADD COLUMN permission_required TINYINT(1) NOT NULL;
ALTER TABLE `crew` ADD COLUMN question TEXT NULL;
ALTER TABLE `crew` ADD COLUMN answer_required TINYINT(1) NOT NULL;
ALTER TABLE `crew` MODIFY COLUMN `region` VARCHAR(20) NOT NULL;

