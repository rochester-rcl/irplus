-- Fix issue where user can have the same personal file listed twice in two different folders
ALTER TABLE ir_user.personal_file DROP CONSTRAINT personal_file_user_id_key;
ALTER TABLE ir_user.personal_file ADD CONSTRAINT personal_file_user_id_versioned_file_id_key UNIQUE (user_id, versioned_file_id);

-- -----------------------------------------------
-- Create a lower case email for email addresses
-- -----------------------------------------------

ALTER TABLE ir_user.user_email ADD COLUMN lower_case_email TEXT;

UPDATE ir_user.user_email set lower_case_email = trim(both ' ' from lower(email));

ALTER TABLE ir_user.user_email ALTER COLUMN lower_case_email SET NOT NULL;

ALTER TABLE ir_user.user_email  ADD CONSTRAINT user_email_lower_case_email_key UNIQUE(lower_case_email);