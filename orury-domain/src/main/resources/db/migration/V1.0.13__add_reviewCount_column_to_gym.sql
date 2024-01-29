ALTER TABLE `gym`
    ADD COLUMN `like_count` INT NOT NULL DEFAULT 0 AFTER `score_average`;