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
       
-- IR metadata schema  

-- **********************************************
-- ----------------------------------------------

CREATE SCHEMA ir_metadata_dublin_core AUTHORIZATION ir_plus;

CREATE TABLE ir_metadata_dublin_core.contributor_type_dc_mapping
(
    contributor_type_dc_mapping_id BIGINT PRIMARY KEY,
    contributor_type_id BIGINT NOT NULL,
    dublin_core_element_id BIGINT NOT NULL,
    version INTEGER,
    UNIQUE(contributor_type_id),
    FOREIGN KEY (contributor_type_id) REFERENCES person.contributor_type(contributor_type_id),
    FOREIGN KEY (dublin_core_element_id) REFERENCES metadata.dublin_core_element(dublin_core_element_id)
);

ALTER TABLE ir_metadata_dublin_core.contributor_type_dc_mapping OWNER TO ir_plus;

-- The contributor type dc mapping sequence
CREATE SEQUENCE ir_metadata_dublin_core.contributor_type_dc_mapping_seq ;
ALTER TABLE ir_metadata_dublin_core.contributor_type_dc_mapping_seq OWNER TO ir_plus;