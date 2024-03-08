-- 기존의 외래키 제약 조건 삭제
ALTER TABLE `crew_member` DROP FOREIGN KEY `FK_crew_TO_crew_member_1`;
ALTER TABLE `crew_meeting` DROP FOREIGN KEY `FK_crew_TO_crew_meeting_1`;
ALTER TABLE `crew_application` DROP FOREIGN KEY `FK_crew_TO_crew_application_1`;
ALTER TABLE `crew_meeting_member` DROP FOREIGN KEY `FK_crew_meeting_TO_crew_meeting_member_1`;

-- crew 테이블 auto_increment 설정
ALTER TABLE `crew`
    DROP PRIMARY KEY,
    MODIFY COLUMN `id` bigint AUTO_INCREMENT,
    ADD PRIMARY KEY (`id`);

-- crew_meeting 테이블 auto_increment 설정
ALTER TABLE `crew_meeting`
    DROP PRIMARY KEY,
    MODIFY COLUMN `id` bigint AUTO_INCREMENT,
    ADD PRIMARY KEY (`id`);

-- 외래키 제약 조건 다시 설정
ALTER TABLE `crew_member`
    ADD CONSTRAINT `FK_crew_TO_crew_member_1` FOREIGN KEY (`crew_id`)
        REFERENCES `crew` (`id`)
        ON DELETE CASCADE;

ALTER TABLE `crew_meeting`
    ADD CONSTRAINT `FK_crew_TO_crew_meeting_1` FOREIGN KEY (`crew_id`)
        REFERENCES `crew` (`id`)
        ON DELETE CASCADE;

ALTER TABLE `crew_application`
    ADD CONSTRAINT `FK_crew_TO_crew_application_1` FOREIGN KEY (`crew_id`)
        REFERENCES `crew` (`id`)
        ON DELETE CASCADE;

ALTER TABLE `crew_meeting_member`
    ADD CONSTRAINT `FK_crew_meeting_TO_crew_meeting_member_1` FOREIGN KEY (`meeting_id`)
        REFERENCES `crew_meeting` (`id`)
        ON DELETE CASCADE;