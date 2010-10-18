-- ---------------------------------------------
-- Insert the 15 elements into the dublin core
-- table
-- ---------------------------------------------

insert into                                                         
metadata.dublin_core_element ( dublin_core_element_id, version, name, description) 
values (nextval('metadata.dublin_core_element_seq'), 0, 'contributor', 'An entity responsible for making contributions to the resource');

insert into                                                         
metadata.dublin_core_element ( dublin_core_element_id, version, name, description) 
values (nextval('metadata.dublin_core_element_seq'), 0, 'coverage', 'The spatial or temporal topic of the resource, the spatial applicability of the resource, or the jurisdiction under which the resource is relevant.');

insert into                                                         
metadata.dublin_core_element ( dublin_core_element_id, version, name, description) 
values (nextval('metadata.dublin_core_element_seq'), 0, 'creator', 'An entity primarily responsible for making the resource.');

insert into                                                         
metadata.dublin_core_element ( dublin_core_element_id, version, name, description) 
values (nextval('metadata.dublin_core_element_seq'), 0, 'date', 'A point or period of time associated with an event in the lifecycle of the resource.');

insert into                                                         
metadata.dublin_core_element ( dublin_core_element_id, version, name, description) 
values (nextval('metadata.dublin_core_element_seq'), 0, 'description', 'An account of the resource');

insert into                                                         
metadata.dublin_core_element ( dublin_core_element_id, version, name, description) 
values (nextval('metadata.dublin_core_element_seq'), 0, 'format', 'The file format, physical medium, or dimensions of the resource.');

insert into                                                         
metadata.dublin_core_element ( dublin_core_element_id, version, name, description) 
values (nextval('metadata.dublin_core_element_seq'), 0, 'identifier', 'An unambiguous reference to the resource within a given context.');

insert into                                                         
metadata.dublin_core_element ( dublin_core_element_id, version, name, description) 
values (nextval('metadata.dublin_core_element_seq'), 0, 'language', 'A language of the resource.');

insert into                                                         
metadata.dublin_core_element ( dublin_core_element_id, version, name, description) 
values (nextval('metadata.dublin_core_element_seq'), 0, 'publisher', 'An entity responsible for making the resource available.');

insert into                                                         
metadata.dublin_core_element ( dublin_core_element_id, version, name, description) 
values (nextval('metadata.dublin_core_element_seq'), 0, 'relation', 'A related resource.');

insert into                                                         
metadata.dublin_core_element ( dublin_core_element_id, version, name, description) 
values (nextval('metadata.dublin_core_element_seq'), 0, 'rights', 'Information about rights held in and over the resource.');

insert into                                                         
metadata.dublin_core_element ( dublin_core_element_id, version, name, description) 
values (nextval('metadata.dublin_core_element_seq'), 0, 'source', 'A related resource from which the described resource is derived.');

insert into                                                         
metadata.dublin_core_element ( dublin_core_element_id, version, name, description) 
values (nextval('metadata.dublin_core_element_seq'), 0, 'subject', 'The topic of the resource.');

insert into                                                         
metadata.dublin_core_element ( dublin_core_element_id, version, name, description) 
values (nextval('metadata.dublin_core_element_seq'), 0, 'title', 'A name given to the resource.');

insert into                                                         
metadata.dublin_core_element ( dublin_core_element_id, version, name, description) 
values (nextval('metadata.dublin_core_element_seq'), 0, 'type', 'The nature or genre of the resource.');

