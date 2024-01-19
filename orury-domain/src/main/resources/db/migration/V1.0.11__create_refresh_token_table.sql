CREATE TABLE `refresh_token`
(
    `user_id`         bigint   NOT NULL,
    `value`           varchar(255) NOT NULL,
    `created_at`      DATETIME     NOT NULL,
    `updated_at`      DATETIME     NULL,
    PRIMARY KEY (`user_id`)
);