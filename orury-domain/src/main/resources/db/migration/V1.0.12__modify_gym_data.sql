-- open_time, close_time 컬럼 삭제
ALTER TABLE `gym`
DROP
COLUMN `open_time`,
DROP
COLUMN `close_time`;

-- service_mon, service_tue, service_wed, service_thu, service_fri, service_sat, service_sun 컬럼 추가
ALTER TABLE `gym`
    ADD COLUMN `service_mon` varchar(20) NULL,
    ADD COLUMN `service_tue` varchar(20) NULL,
    ADD COLUMN `service_wed` varchar(20) NULL,
    ADD COLUMN `service_thu` varchar(20) NULL,
    ADD COLUMN `service_fri` varchar(20) NULL,
    ADD COLUMN `service_sat` varchar(20) NULL,
    ADD COLUMN `service_sun` varchar(20) NULL;

-- 'hompage_link' 컬럼 추가
ALTER TABLE `gym`
    ADD COLUMN `homepage_link` varchar(200) NULL;

-- 'remark' 컬럼 추가
ALTER TABLE `gym`
    ADD COLUMN `remark` varchar(100) NULL;

-- 'instagram_link' 컬럼 타입 수정
ALTER TABLE `gym`
    MODIFY COLUMN `instagram_link` varchar (200) NULL;
