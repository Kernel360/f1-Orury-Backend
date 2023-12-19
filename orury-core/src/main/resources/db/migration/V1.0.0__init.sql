CREATE TABLE `post` (
                        `id`	bigint(32)	NOT NULL AUTO_INCREMENT,
                        `title`	varchar(50)	NOT NULL,
                        `content`	text	NOT NULL,
                        `view_count`	int	NOT NULL,
                        `images`	varchar(255)	NULL	COMMENT 'List<String>',
                        `category`	int	NOT NULL	COMMENT '1 : 자유게시판 2 : Q&A',
                        `user_id`	bigint(32)	NOT NULL,
                        `created_at`	DATETIME	NOT NULL	COMMENT '@CreatedDate',
                        `updated_at`	DATETIME	NULL,
                        PRIMARY KEY(`id`)
);

CREATE TABLE `post_like` (
                             `user_id`	bigint(32)	NOT NULL,
                             `post_id`	bigint(32)	NOT NULL	COMMENT '게시글 아이디',
                             `created_at`	DATETIME	NOT NULL	COMMENT '@CreatedDate',
                             `updated_at`	DATETIME	NULL
);

CREATE TABLE `user` (
                        `id`	bigint(32)	NOT NULL AUTO_INCREMENT,
                        `email`	varchar(50)	NOT NULL,
                        `nickname`	varchar(8)	NOT NULL,
                        `password`	varchar(100)	NOT NULL,
                        `sign_up_type`	int	NOT NULL	COMMENT '1 : 카카오 로그인 2 : 구글 로그인',
                        `gender`	int	NOT NULL	COMMENT '1 : 남성 2: 여성',
                        `birthday`	date	NOT NULL,
                        `profile_image`	varchar(50)	NULL,
                        `created_at`	DATETIME	NOT NULL	COMMENT '@CreatedDate',
                        `updated_at`	DATETIME	NULL,
                        PRIMARY KEY(`id`)
);

CREATE TABLE `comment_like` (
                                `user_id`	bigint(32)	NOT NULL,
                                `comment_id`	bigint(32)	NOT NULL,
                                `created_at`	DATETIME	NOT NULL	COMMENT '@CreatedDate',
                                `updated_at`	DATETIME	NULL
);

CREATE TABLE `comment` (
                           `id`	BIGINT(32)	NOT NULL AUTO_INCREMENT,
                           `content`	VARCHAR(1000)	NOT NULL,
                           `parent_id`	BIGINT(32)	NOT NULL	DEFAULT 0	COMMENT '부모 댓글이 없으면 일반 댓글, 있으면 대댓',
                           `post_id`	BIGINT(32)	NOT NULL	COMMENT '게시글 아이디',
                           `user_id`	BIGINT(32)	NOT NULL,
                           `created_at`	DATETIME	NOT NULL	COMMENT '@CreatedDate',
                           `updated_at`	DATETIME	NULL,
                           PRIMARY KEY(`id`)
);

CREATE TABLE `refresh_token` (
                                 `id`	bigint(32)	NOT NULL AUTO_INCREMENT,
                                 `user_id`	bigint(32)	NOT NULL,
                                 `value`	varchar(255)	NOT NULL,
                                 `expiration_time`	datetime	NOT NULL,
                                 `created_at`	DATETIME	NOT NULL	COMMENT '@CreatedDate',
                                 `updated_at`	DATETIME	NULL,
                                 PRIMARY KEY(`id`)
);

CREATE TABLE `Notice` (
                          `id`	BIGINT(32)	NOT NULL AUTO_INCREMENT,
                          `title`	VARCHAR(50)	NOT NULL,
                          `content`	VARCHAR(50)	NOT NULL,
                          `admin_id`	BIGINT(32)	NOT NULL,
                          `created_at`	DATETIME	NOT NULL	COMMENT '@CreatedDate',
                          `updated_at`	DATETIME	NULL,
                          PRIMARY KEY(`id`)
);

CREATE TABLE `Admin` (
                         `id`	BIGINT(32)	NOT NULL AUTO_INCREMENT,
                         `name`	VARCHAR(50)	NOT NULL,
                         `email`	VARCHAR(50)	NOT NULL,
                         `password`	VARCHAR(50)	NOT NULL,
                         `role` VARCHAR(50) NOT NULL,
                         `created_at`	DATETIME	NOT NULL	COMMENT '@CreatedDate',
                         `updated_at`	DATETIME	NULL,
                         PRIMARY KEY(`id`)
);

CREATE TABLE `user_withdrawal` (
                                   `id`	BIGINT(32)	NOT NULL AUTO_INCREMENT,
                                   `email`	VARCHAR(50)	NOT NULL,
                                   `nickname`	VARCHAR(8)	NOT NULL,
                                   `password`	VARCHAR(100)	NOT NULL,
                                   `sign_up_type`	int	NOT NULL	COMMENT '1 : 카카오 로그인 2 : 구글 로그인',
                                   `gender`	int	NOT NULL	COMMENT '1 : 남성 2: 여성',
                                   `birthday`	date	NOT NULL,
                                   `created_at`	DATETIME	NOT NULL	COMMENT '@CreatedDate',
                                   `updated_at`	DATETIME	NULL,
                                   PRIMARY KEY(`id`)
);

ALTER TABLE `post_like` ADD CONSTRAINT `PK_POST_LIKE` PRIMARY KEY (
                                                                   `user_id`,
                                                                   `post_id`
    );

ALTER TABLE `comment_like` ADD CONSTRAINT `PK_COMMENT_LIKE` PRIMARY KEY (
                                                                         `user_id`,
                                                                         `comment_id`
    );

ALTER TABLE `post_like` ADD CONSTRAINT `FK_user_TO_post_like_1` FOREIGN KEY (
                                                                             `user_id`
    )
    REFERENCES `user` (
                       `id`
        );

ALTER TABLE `post_like` ADD CONSTRAINT `FK_post_TO_post_like_1` FOREIGN KEY (
                                                                             `post_id`
    )
    REFERENCES `post` (
                       `id`
        );

ALTER TABLE `comment_like` ADD CONSTRAINT `FK_user_TO_comment_like_1` FOREIGN KEY (
                                                                                   `user_id`
    )
    REFERENCES `user` (
                       `id`
        );

ALTER TABLE `comment_like` ADD CONSTRAINT `FK_comment_TO_comment_like_1` FOREIGN KEY (
                                                                                      `comment_id`
    )
    REFERENCES `comment` (
                          `id`
        );

ALTER TABLE `refresh_token` ADD CONSTRAINT `FK_user_TO_refresh_token_1` FOREIGN KEY (
                                                                                     `user_id`
    )
    REFERENCES `user` (
                       `id`
        );

