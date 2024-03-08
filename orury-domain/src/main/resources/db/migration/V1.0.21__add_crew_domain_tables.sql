CREATE TABLE IF NOT EXISTS `crew_member` (
                                              `user_id`	      bigint	   NOT NULL,
                                              `crew_id`	      bigint	   NOT NULL,
                                              `created_at`    DATETIME     NOT NULL,
                                              `updated_at`    DATETIME     NULL
     );

CREATE TABLE IF NOT EXISTS `crew` (
                                      `id`	            bigint      	NOT NULL,
                                      `name`	        varchar(15)	    NOT NULL,
                                      `member_count`	int	            NOT NULL    DEFAULT 1,
                                      `capacity`	    int	            NOT NULL,
                                      `region`	        varchar(100)	NOT NULL,
                                      `description`	    text	        NULL,
                                      `icon`	        varchar(20)	    NULL,
                                      `is_deleted`	    tinyint	        NOT NULL	DEFAULT 0,
                                      `user_id`	        bigint(32)	    NOT NULL,
                                      `created_at`      DATETIME        NOT NULL,
                                      `updated_at`      DATETIME        NULL
    );

CREATE TABLE IF NOT EXISTS `crew_meeting` (
                                              `id`	            bigint	    NOT NULL,
                                              `start_time`	    datetime	NOT NULL,
                                              `end_time`	    datetime	NOT NULL,
                                              `member_count`	int	        NOT NULL	DEFAULT 0,
                                              `capacity`	    int	        NOT NULL,
                                              `user_id`	        bigint	    NOT NULL,
                                              `gym_id`	        bigint	    NOT NULL,
                                              `crew_id`	        bigint	    NOT NULL,
                                              `created_at`      DATETIME    NOT NULL,
                                              `updated_at`      DATETIME    NULL
    );

CREATE TABLE IF NOT EXISTS `crew_application` (
                                                  `user_id`     	bigint      	NOT NULL,
                                                  `crew_id`     	bigint      	NOT NULL,
                                                  `description`	    varchar(100)	NULL,
                                                  `is_declined`	    tinyint     	NOT NULL	DEFAULT 0,
                                                  `created_at`      DATETIME        NOT NULL,
                                                  `updated_at`      DATETIME        NULL
    );

CREATE TABLE IF NOT EXISTS `crew_meeting_member` (
                                                     `meeting_id`	bigint  	NOT NULL,
                                                     `user_id`  	bigint  	NOT NULL
);

ALTER TABLE `crew_member` ADD CONSTRAINT `PK_CREW_MEMBER` PRIMARY KEY (
                                                                         `user_id`,
                                                                         `crew_id`
    );

ALTER TABLE `crew` ADD CONSTRAINT `PK_CREW` PRIMARY KEY (
                                                         `id`
    );

ALTER TABLE `crew_meeting` ADD CONSTRAINT `PK_CREW_MEETING` PRIMARY KEY (
                                                                         `id`
    );

ALTER TABLE `crew_application` ADD CONSTRAINT `PK_CREW_APPLICATION` PRIMARY KEY (
                                                                                 `user_id`,
                                                                                 `crew_id`
    );

ALTER TABLE `crew_meeting_member` ADD CONSTRAINT `PK_CREW_MEETING_MEMBER` PRIMARY KEY (
                                                                                       `meeting_id`,
                                                                                       `user_id`
    );

ALTER TABLE `crew_member` ADD CONSTRAINT `FK_user_TO_crew_member_1` FOREIGN KEY (
                                                                                   `user_id`
    )
    REFERENCES `user` (
                       `id`
        );

ALTER TABLE `crew_member` ADD CONSTRAINT `FK_crew_TO_crew_member_1` FOREIGN KEY (
                                                                                   `crew_id`
    )
    REFERENCES `crew` (
                       `id`
        )
    ON DELETE CASCADE;

ALTER TABLE `crew` ADD CONSTRAINT `FK_user_TO_crew_1` FOREIGN KEY (
                                                                   `user_id`
    )
    REFERENCES `user` (
                       `id`
        );

ALTER TABLE `crew_meeting` ADD CONSTRAINT `FK_user_TO_crew_meeting_1` FOREIGN KEY (
                                                                                   `user_id`
    )
    REFERENCES `user` (
                       `id`
        );

ALTER TABLE `crew_meeting` ADD CONSTRAINT `FK_gym_TO_crew_meeting_1` FOREIGN KEY (
                                                                                  `gym_id`
    )
    REFERENCES `gym` (
                      `id`
        );

ALTER TABLE `crew_meeting` ADD CONSTRAINT `FK_crew_TO_crew_meeting_1` FOREIGN KEY (
                                                                                   `crew_id`
    )
    REFERENCES `crew` (
                       `id`
        )
    ON DELETE CASCADE;;

ALTER TABLE `crew_application` ADD CONSTRAINT `FK_user_TO_crew_application_1` FOREIGN KEY (
                                                                                           `user_id`
    )
    REFERENCES `user` (
                       `id`
        );

ALTER TABLE `crew_application` ADD CONSTRAINT `FK_crew_TO_crew_application_1` FOREIGN KEY (
                                                                                           `crew_id`
    )
    REFERENCES `crew` (
                       `id`
        )
    ON DELETE CASCADE;;

ALTER TABLE `crew_meeting_member` ADD CONSTRAINT `FK_crew_meeting_TO_crew_meeting_member_1` FOREIGN KEY (
                                                                                                         `meeting_id`
    )
    REFERENCES `crew_meeting` (
                               `id`
        )
    ON DELETE CASCADE;;

ALTER TABLE `crew_meeting_member` ADD CONSTRAINT `FK_user_TO_crew_meeting_member_1` FOREIGN KEY (
                                                                                                 `user_id`
    )
    REFERENCES `user` (
                       `id`
        );