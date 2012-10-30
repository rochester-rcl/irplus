
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

-- ---------------------------------------------
-- Checksum Information
-- ---------------------------------------------

-- Create a new table to hold file checksum information in the system
CREATE TABLE file_system.file_checksum_reset_history
(
  file_checksum_reset_history_id BIGINT NOT NULL PRIMARY KEY,
  file_checksum_id BIGINT NOT NULL,
  original_checksum TEXT NOT NULL,
  new_checksum TEXT NOT NULL,
  algorithm_type TEXT NOT NULL,
  user_id BIGINT NOT NULL,
  notes TEXT,
  date_reset TIMESTAMP WITH TIME ZONE NOT NULL ,
  version INTEGER,
  FOREIGN KEY (file_checksum_id) REFERENCES file_system.file_checksum (file_checksum_id)
);
ALTER TABLE file_system.file_checksum_reset_history OWNER TO ir_plus;

-- The checksum sequence
CREATE SEQUENCE file_system.file_checksum_reset_history_seq;
ALTER TABLE file_system.file_checksum_reset_history_seq OWNER TO ir_plus;