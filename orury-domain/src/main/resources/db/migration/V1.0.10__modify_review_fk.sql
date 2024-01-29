ALTER TABLE `review`
    ADD CONSTRAINT `FK_user_TO_review` FOREIGN KEY (
                                                    `user_id`
        )
        REFERENCES `user` (
                           `id`
            )
        ON DELETE CASCADE;

ALTER TABLE `review`
    ADD CONSTRAINT `FK_gym_TO_review` FOREIGN KEY (
                                                   `gym_id`
        )
        REFERENCES `gym` (
                          `id`
            )
        ON DELETE CASCADE;