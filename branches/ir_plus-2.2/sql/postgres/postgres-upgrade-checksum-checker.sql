
-- Add new columns
ALTER TABLE file_system.file_checksum ADD COLUMN date_re_calculated TIMESTAMP WITH TIME ZONE;
ALTER TABLE file_system.file_checksum ADD COLUMN date_last_check_passed TIMESTAMP WITH TIME ZONE;
ALTER TABLE file_system.file_checksum ADD COLUMN re_calculated_passed BOOLEAN NOT NULL DEFAULT TRUE;
ALTER TABLE file_system.file_checksum ADD COLUMN re_calculated_value TEXT;
ALTER TABLE file_system.file_checksum ADD COLUMN re_calculate_checksum BOOLEAN NOT NULL DEFAULT TRUE;

UPDATE file_system.file_checksum set date_re_calculated = date_calculated;
UPDATE file_system.file_checksum set re_calculated_value = checksum;

ALTER TABLE file_system.file_checksum ALTER COLUMN date_re_calculated SET NOT NULL;
ALTER TABLE file_system.file_checksum ALTER COLUMN re_calculated_value SET NOT NULL;

-- make file id unique on the ir_file table
ALTER TABLE ir_file.ir_file ADD CONSTRAINT ir_file_file_id_unique UNIQUE (file_id);