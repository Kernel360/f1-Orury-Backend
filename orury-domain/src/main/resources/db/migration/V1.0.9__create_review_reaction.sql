CREATE TABLE `review_reaction` (
                                   `review_id`	bigint(32)	NOT NULL	COMMENT '리뷰 ID',
                                   `user_id`	bigint(32)	NOT NULL	COMMENT '반응 남긴 유저 Id',
                                   `reaction_type`	int	NOT NULL	COMMENT '1.interest/2.like/3.help/4.thumb/5.angry'
);

ALTER TABLE `review_reaction` ADD CONSTRAINT `FK_review_TO_review_reaction_1` FOREIGN KEY (
                                                                                           `review_id`
    )
    REFERENCES `review` (
                         `id`
        )
    ON DELETE CASCADE;

ALTER TABLE `review_reaction` ADD CONSTRAINT `FK_user_TO_review_reaction_1` FOREIGN KEY (
                                                                                         `user_id`
    )
    REFERENCES `user` (
                       `id`
        )
    ON DELETE CASCADE;