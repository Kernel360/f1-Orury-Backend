ALTER TABLE `crew` DROP COLUMN is_deleted;
ALTER TABLE `crew` ADD COLUMN status VARCHAR(1) NOT NULL;