CREATE TABLE `refresh_token`
(
    `user_id`         bigint   NOT NULL,
    `value`           varchar(255) NOT NULL,
    `created_at`      DATETIME     NOT NULL,
    `updated_at`      DATETIME     NULL,
    PRIMARY KEY (`user_id`)
);

ALTER TABLE `refresh_token`
    ADD CONSTRAINT `FK_user_TO_refresh_token_1`
        FOREIGN KEY (`user_id`)
            REFERENCES `user` (`id`);