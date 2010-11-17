-- create the institutional collection index folder
ALTER TABLE ir_repository.repository ADD COLUMN institutional_collection_index_folder TEXT;

-- create the institutional collection index folder
ALTER TABLE ir_repository.repository ADD COLUMN user_group_index_folder TEXT;


-- create the institutional collection index folder
ALTER TABLE ir_user.invite_info ADD COLUMN created_date TIMESTAMP WITH TIME ZONE;
UPDATE  ir_user.invite_info set created_date = date(now());

ALTER TABLE ir_user.invite_info ALTER COLUMN created_date SET NOT NULL;