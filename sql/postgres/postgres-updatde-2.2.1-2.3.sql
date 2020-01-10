-- ---------------------------------------------
-- Person authority identifier type
-- ---------------------------------------------
CREATE TABLE person.person_name_authority_identifier_type
(
    person_name_authority_identifier_type_id BIGINT PRIMARY KEY,
    version INTEGER,
    name TEXT NOT NULL,
    unique_system_code TEXT,
    description TEXT,
    CONSTRAINT identifier_type_name_key UNIQUE(name) 
);
ALTER TABLE person.person_name_authority_identifier_type OWNER TO ir_plus;

-- The person authority sequence
CREATE SEQUENCE person.person_name_authority_identifier_type_seq ;
ALTER TABLE person.person_name_authority_identifier_type_seq OWNER TO ir_plus;


