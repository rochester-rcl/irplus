-- ----------------------------------------------
-- **********************************************
       
--   Copyright 2008 University of Rochester
--
--   Licensed under the Apache License, Version 2.0 (the "License");
--   you may not use this file except in compliance with the License.
--   You may obtain a copy of the License at

--       http://www.apache.org/licenses/LICENSE-2.0

--   Unless required by applicable law or agreed to in writing, software
--   distributed under the License is distributed on an "AS IS" BASIS,
--   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
--   See the License for the specific language governing permissions and
--   limitations under the License.

-- **********************************************
-- ----------------------------------------------



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