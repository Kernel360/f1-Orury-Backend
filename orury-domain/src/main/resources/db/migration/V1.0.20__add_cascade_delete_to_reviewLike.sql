ALTER TABLE `review_reaction`
DROP CONSTRAINT `FK_review_TO_review_reaction_1`,
    ADD CONSTRAINT `FK_review_TO_review_reaction_cascade`
        FOREIGN KEY (`review_id`) REFERENCES `review` (`id`)
            ON DELETE CASCADE;