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