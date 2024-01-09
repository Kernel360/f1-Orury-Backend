CREATE TABLE `review`
(
    `id`             bigint(32)   NOT NULL AUTO_INCREMENT,
    `content`        varchar(255) NULL COMMENT '리뷰 내용',
    `images`         varchar(255) NULL COMMENT '리뷰에 첨부된 이미지들',
    `score`          float        NOT NULL COMMENT '별점',
    `interest_count` int          NOT NULL COMMENT '반응: 놀람 누른 수',
    `like_count`     int          NOT NULL COMMENT '반응: 하트 누른 수',
    `help_count`     int          NOT NULL COMMENT '반응: 도움 누른 수',
    `thumb_count`    int          NOT NULL COMMENT '반응: 따봉 누른 수',
    `angry_count`    int          NOT NULL COMMENT '반응: 화남 누른 수',
    `user_id`        bigint(32)   NOT NULL,
    `gym_id`         bigint       NOT NULL COMMENT '암장 Id',
    `created_at`     DATETIME     NOT NULL COMMENT '@CreatedDate',
    `updated_at`     DATETIME     NULL,
    PRIMARY KEY (`id`)
);