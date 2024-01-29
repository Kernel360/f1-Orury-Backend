ALTER TABLE `gym`
    ADD COLUMN `total_score` float NULL COMMENT '암장 총 평점',
    ADD COLUMN `review_count` int NULL COMMENT '암장 리뷰 수';
