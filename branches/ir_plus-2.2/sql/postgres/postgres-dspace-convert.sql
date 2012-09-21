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

-- ----------------------------------------------
-- **********************************************
       
-- DSpace conversion tables  

-- **********************************************
-- ----------------------------------------------

-- ---------------------------------------------
-- Create a schema to hold all dspace community
-- information
-- ---------------------------------------------

CREATE SCHEMA dspace_convert AUTHORIZATION ir_plus;

-- Create a new table to hold link between old dspace community
-- and new ir_plus collection
CREATE TABLE dspace_convert.community
(
  dspace_community_id BIGINT PRIMARY KEY,
  ur_research_collection_id BIGINT
);
ALTER TABLE dspace_convert.community OWNER TO ir_plus;


-- Create a new table to hold link between old dspace collection
-- and new ir_plus collection
CREATE TABLE dspace_convert.collection
(
  dspace_collection_id BIGINT PRIMARY KEY,
  ur_research_collection_id BIGINT
);
ALTER TABLE dspace_convert.collection OWNER TO ir_plus;

-- Create a new table to hold link between old dspace users
-- and new ir_plus user
CREATE TABLE dspace_convert.ir_user
(
  dspace_eperson_id BIGINT PRIMARY KEY,
  ur_research_user_id BIGINT
);
ALTER TABLE dspace_convert.ir_user OWNER TO ir_plus;


-- Create a new table to hold link between old dspace users
-- and new urresearch user
CREATE TABLE dspace_convert.researcher
(
  dspace_researcher_id BIGINT PRIMARY KEY,
  ur_research_researcher_id BIGINT
);
ALTER TABLE dspace_convert.researcher OWNER TO ir_plus;

-- Create a new table to hold link between old dspace users
-- and new ir_plus user
CREATE TABLE dspace_convert.item
(
  dspace_item_id BIGINT,
  ur_research_institutional_item_id BIGINT,
  handle_local_name BIGINT,
  PRIMARY KEY(dspace_item_id, ur_research_institutional_item_id)
);
ALTER TABLE dspace_convert.item OWNER TO ir_plus;


-- Create a new table to hold link between old dspace users
-- and new ir_plus user
CREATE TABLE dspace_convert.item_file
(
  dspace_bitstream_id BIGINT,
  ur_research_institutional_item_file_id BIGINT,
  PRIMARY KEY(dspace_bitstream_id, ur_research_institutional_item_file_id)
);
ALTER TABLE dspace_convert.item_file OWNER TO ir_plus;

-- Create a new table to hold link between old dspace groups
-- and new ir_plus groups
CREATE TABLE dspace_convert.ir_group
(
  dspace_group_id BIGINT PRIMARY KEY,
  ur_research_group_id BIGINT
);
ALTER TABLE dspace_convert.ir_group OWNER TO ir_plus;


