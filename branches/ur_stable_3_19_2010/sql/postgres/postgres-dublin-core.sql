-- ----------------------------------------------
-- **********************************************
       
-- Dublin core tables  

-- **********************************************


-- ----------------------------------------------
-- ---------------------------------------------
-- Create a schema to hold security information
-- ---------------------------------------------

CREATE SCHEMA ir_dublin_core AUTHORIZATION ir_plus;

-- ---------------------------------------------
-- Dublin core element table
-- ---------------------------------------------
CREATE TABLE ir_dublin_core.dc_element
(
    dc_element_id BIGINT PRIMARY KEY,
    version INTEGER,
    name TEXT NOT NULL,
    description TEXT,
    UNIQUE(name)
);
ALTER TABLE ir_dublin_core.dc_element OWNER TO ir_plus;

-- The external account type sequence
CREATE SEQUENCE ir_dublin_core.dc_element_seq;
ALTER TABLE ir_dublin_core.dc_element_seq OWNER TO ir_plus;


-- ---------------------------------------------
-- Insert the 15 elements into the dublin core
-- table
-- ---------------------------------------------

insert into                                                         
ir_dublin_core.dc_element ( dc_element_id, version, name, description) 
values (nextval('ir_dublin_core.dc_element_seq'), 0, 'contributor', 'An entity responsible for making contributions to the resource');

insert into                                                         
ir_dublin_core.dc_element ( dc_element_id, version, name, description) 
values (nextval('ir_dublin_core.dc_element_seq'), 0, 'coverage', 'The spatial or temporal topic of the resource, the spatial applicability of the resource, or the jurisdiction under which the resource is relevant.');

insert into                                                         
ir_dublin_core.dc_element ( dc_element_id, version, name, description) 
values (nextval('ir_dublin_core.dc_element_seq'), 0, 'creator', 'An entity primarily responsible for making the resource.');

insert into                                                         
ir_dublin_core.dc_element ( dc_element_id, version, name, description) 
values (nextval('ir_dublin_core.dc_element_seq'), 0, 'date', 'A point or period of time associated with an event in the lifecycle of the resource.');

insert into                                                         
ir_dublin_core.dc_element ( dc_element_id, version, name, description) 
values (nextval('ir_dublin_core.dc_element_seq'), 0, 'description', 'An account of the resource');

insert into                                                         
ir_dublin_core.dc_element ( dc_element_id, version, name, description) 
values (nextval('ir_dublin_core.dc_element_seq'), 0, 'format', 'The file format, physical medium, or dimensions of the resource.');

insert into                                                         
ir_dublin_core.dc_element ( dc_element_id, version, name, description) 
values (nextval('ir_dublin_core.dc_element_seq'), 0, 'identifier', 'An unambiguous reference to the resource within a given context.');

insert into                                                         
ir_dublin_core.dc_element ( dc_element_id, version, name, description) 
values (nextval('ir_dublin_core.dc_element_seq'), 0, 'language', 'A language of the resource.');

insert into                                                         
ir_dublin_core.dc_element ( dc_element_id, version, name, description) 
values (nextval('ir_dublin_core.dc_element_seq'), 0, 'publisher', 'An entity responsible for making the resource available.');

insert into                                                         
ir_dublin_core.dc_element ( dc_element_id, version, name, description) 
values (nextval('ir_dublin_core.dc_element_seq'), 0, 'relation', 'A related resource.');

insert into                                                         
ir_dublin_core.dc_element ( dc_element_id, version, name, description) 
values (nextval('ir_dublin_core.dc_element_seq'), 0, 'rights', 'Information about rights held in and over the resource.');

insert into                                                         
ir_dublin_core.dc_element ( dc_element_id, version, name, description) 
values (nextval('ir_dublin_core.dc_element_seq'), 0, 'source', 'A related resource from which the described resource is derived.');

insert into                                                         
ir_dublin_core.dc_element ( dc_element_id, version, name, description) 
values (nextval('ir_dublin_core.dc_element_seq'), 0, 'subject', 'The topic of the resource.');

insert into                                                         
ir_dublin_core.dc_element ( dc_element_id, version, name, description) 
values (nextval('ir_dublin_core.dc_element_seq'), 0, 'title', 'A name given to the resource.');

insert into                                                         
ir_dublin_core.dc_element ( dc_element_id, version, name, description) 
values (nextval('ir_dublin_core.dc_element_seq'), 0, 'type', 'The nature or genre of the resource.');

