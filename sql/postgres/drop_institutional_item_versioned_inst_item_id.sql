ALTER TABLE ir_repository.versioned_institutional_item
DROP COLUMN institutional_item_id;


-- ---------------------------------------------
-- Allow external account types to indicate if they
-- are case sensitive
-- ---------------------------------------------

ALTER TABLE ir_user.external_account_type
ADD COLUMN user_name_case_sensitive BOOLEAN;

UPDATE ir_user.external_account_type
SET user_name_case_sensitive = false;

ALTER TABLE ir_user.external_account_type ALTER COLUMN user_name_case_sensitive SET NOT NULL;

UPDATE ir_user.external_user_account 
SET external_user_account_name = lower(external_user_account_name);