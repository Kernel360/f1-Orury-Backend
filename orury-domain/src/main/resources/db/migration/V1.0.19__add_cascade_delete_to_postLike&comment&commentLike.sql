ALTER TABLE `post_like`
DROP CONSTRAINT `FK_post_TO_post_like_1`,
    ADD CONSTRAINT `FK_post_TO_post_like_cascade`
        FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
            ON DELETE CASCADE;

ALTER TABLE `comment`
DROP CONSTRAINT `FKs1slvnkuemjsq2kj4h3vhx7i1`,
    ADD CONSTRAINT `FK_post_TO_comment_cascade`
        FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
            ON DELETE CASCADE;

ALTER TABLE `comment_like`
DROP CONSTRAINT `FK_comment_TO_comment_like_1`,
    ADD CONSTRAINT `FK_comment_TO_comment_like_cascade`
        FOREIGN KEY (`comment_id`) REFERENCES `comment` (`id`)
            ON DELETE CASCADE;

