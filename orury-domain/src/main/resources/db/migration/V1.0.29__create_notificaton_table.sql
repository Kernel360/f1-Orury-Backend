CREATE TABLE IF NOT EXISTS `notification` (
    `id`	            bigint      	NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id`	        bigint(32)	    NOT NULL,
    `url`	        varchar(100)	NOT NULL,
    `is_read`	    tinyint	        NOT NULL	DEFAULT 0,
    `created_at`      DATETIME        NOT NULL,
    `updated_at`      DATETIME        NULL
    );

ALTER TABLE `notification`
    ADD CONSTRAINT `FK_user_TO_notification` FOREIGN KEY (
                                                    `user_id`
        )
        REFERENCES `user` (
                           `id`
            )
        ON DELETE CASCADE;


