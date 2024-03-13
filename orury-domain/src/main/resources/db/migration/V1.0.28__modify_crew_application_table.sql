ALTER TABLE `crew_application`
    DROP COLUMN is_declined,
    DROP COLUMN description,
    ADD COLUMN answer TEXT NULL AFTER crew_id;