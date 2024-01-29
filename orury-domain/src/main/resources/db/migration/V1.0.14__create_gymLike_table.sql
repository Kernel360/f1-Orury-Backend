CREATE TABLE `gym_like`
(
    `user_id`    bigint(32) NOT NULL,
    `gym_id` bigint(32) NOT NULL
);

ALTER TABLE `gym_like`
    ADD CONSTRAINT `PK_GYM_LIKE` PRIMARY KEY (
        `user_id`,
        `gym_id`
    );

ALTER TABLE `gym_like`
    ADD CONSTRAINT `FK_user_TO_gym_like_1` FOREIGN KEY (
        `user_id`
    )
        REFERENCES `user` (
           `id`
        );

ALTER TABLE `gym_like`
    ADD CONSTRAINT `FK_gym_TO_gym_like_1` FOREIGN KEY (
        `gym_id`
    )
        REFERENCES `gym` (
            `id`
        );