CREATE TABLE `gym` (
	`id`	bigint	NOT NULL AUTO_INCREMENT,
	`name`	varchar(50)	NOT NULL	COMMENT '암장 이름',
	`road_address`	varchar(100)	NOT NULL	COMMENT '도로명주소',
	`address`	varchar(100)	NOT NULL	COMMENT '지번 주소',
	`score_average`	float	NULL	COMMENT '암장 평점',
	`images`	varchar(255)	NULL	COMMENT '암장 이미지',
	`latitude`	varchar(30)	NOT NULL	COMMENT '프론트로 보낼때는 소수점 아래 6자리까지만',
	`longitude`	varchar(30)	NOT NULL	COMMENT '프론트로 보낼때는 소수점 아래 6자리까지만',
	`open_time`	varchar(10)	NULL	COMMENT '최소 0000',
	`close_time`	varchar(10)	NULL	COMMENT '최대 2400',
	`brand`	varchar(20)	NULL,
	`phone_number`	varchar(20)	NULL	COMMENT '암장 연락처',
	`instagram_link`	varchar(50)	NULL,
	`setting_day`	varchar(10)	NULL	COMMENT '세팅요일 영어로',
	`created_at`    DATETIME     NOT NULL COMMENT '@CreatedDate',
    `updated_at`    DATETIME     NULL,
	PRIMARY KEY (`id`)
);