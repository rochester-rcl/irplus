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


insert into 
person.person_name_authority_identifier_type ( person_name_authority_identifier_type_id, version, name, description, unique_system_code) 
values (nextval('person.person_name_authority_identifier_type_seq'), 0, 'LCNAF', 'Library of Congress Name Authority File (LCNAF) - https://id.loc.gov/authorities/names.html', 'LCNAF');

insert into 
person.person_name_authority_identifier_type ( person_name_authority_identifier_type_id, version, name, description, unique_system_code) 
values (nextval('person.person_name_authority_identifier_type_seq'), 0, 'ORCID', 'Open Researcher and Contributor ID (ORCID) -  https://orcid.org/', 'ORCID');

insert into 
person.person_name_authority_identifier_type ( person_name_authority_identifier_type_id, version, name, description, unique_system_code) 
values (nextval('person.person_name_authority_identifier_type_seq'), 0, 'VIAF', 'Virtual International Authority File (VIAF) -  http://viaf.org/', 'VIAF');

-- ---------------------------------------------
-- Person name authority Identifier table
-- ---------------------------------------------
CREATE TABLE person.person_name_authority_identifier
(
    person_name_authority_identifier_id BIGINT PRIMARY KEY,
    person_name_authority_id BIGINT NOT NULL,
    person_name_authority_identifier_type_id BIGINT NOT NULL,
    version INTEGER,
    value TEXT,
    UNIQUE(person_name_authority_identifier_type_id, value),
    FOREIGN KEY (person_name_authority_id) REFERENCES person.person_name_authority(person_name_authority_id),
    FOREIGN KEY (person_name_authority_identifier_type_id) REFERENCES person.person_name_authority_identifier_type (person_name_authority_identifier_type_id) 
);
ALTER TABLE person.person_name_authority_identifier OWNER TO ir_plus;

-- The item identifier sequence
CREATE SEQUENCE person.person_name_authority_identifier_seq ;
ALTER TABLE person.person_name_authority_identifier_seq OWNER TO ir_plus;