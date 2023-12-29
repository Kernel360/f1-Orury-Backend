ALTER TABLE `comment`
    ADD COLUMN `like_count` INT NOT NULL DEFAULT 0 COMMENT '좋아요 수' AFTER `parent_id`;
