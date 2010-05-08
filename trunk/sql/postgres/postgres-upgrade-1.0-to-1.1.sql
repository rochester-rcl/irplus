-- ---------------------------------------------
-- This represents an external account type
-- in particular for user accounts that are in
-- other external ssytems that can be used to
-- validate against
-- ---------------------------------------------

CREATE TABLE ir_user.external_account_type
(
    external_account_type_id BIGINT PRIMARY KEY,
    version INTEGER,
    name TEXT NOT NULL,
    description TEXT,
    UNIQUE(name)
);
ALTER TABLE ir_user.external_account_type OWNER TO ir_plus;

-- The external account type sequence
CREATE SEQUENCE ir_user.external_account_type_seq;
ALTER TABLE ir_user.external_account_type_seq OWNER TO ir_plus;


---  Create the default account type
insert into ir_user.external_account_type (external_account_type_id, version, name, description)
values (nextval('ir_user.external_account_type_seq'), 0, 'default_ldap_account', 'The default ldap account');

-- ---------------------------------------------
-- This binds an external account type to 
-- a particular user
-- ---------------------------------------------

CREATE TABLE ir_user.external_user_account
(
    external_user_account_id BIGINT PRIMARY KEY,
    external_account_type_id BIGINT NOT NULL,
    external_user_account_name TEXT NOT NULL,
    user_id BIGINT NOT NULL,
    UNIQUE(external_account_type_id, external_user_account_name),
    FOREIGN KEY (user_id) REFERENCES ir_user.ir_user(user_id),
    FOREIGN KEY (external_account_type_id) REFERENCES ir_user.external_account_type(external_account_type_id)
);
ALTER TABLE ir_user.external_user_account OWNER TO ir_plus;

-- The external account type sequence
CREATE SEQUENCE ir_user.external_user_account_seq;
ALTER TABLE ir_user.external_user_account_seq OWNER TO ir_plus;


-- Index on the external user name
CREATE INDEX external_user_name_idx ON ir_user.external_user_account (external_user_account_name);

-- Index on the external user name
CREATE INDEX external_user_name_type_idx ON ir_user.external_user_account (external_user_account_name, external_account_type_id);

ALTER TABLE ir_user.external_user_account ADD CONSTRAINT external_user_account_user_id_key UNIQUE (user_id);



-- update all accounts to the default ldap account type
insert into ir_user.external_user_account select
  nextval('ir_user.external_user_account_seq'), 
  external_account_type.external_account_type_id, 
  ir_user.ldap_user_name,
  ir_user.user_id
  from ir_user.external_account_type,
       ir_user.ir_user
  where ir_user.external_account_type.name = 'default_ldap_account'
  and ir_user.ldap_user_name is not null
  and trim(both ' ' from ir_user.ldap_user_name) <> '';



-- ----------------------------------------------
-- **********************************************
       
-- add the sponsor name first character column

-- **********************************************
-- ----------------------------------------------
ALTER TABLE ir_item.sponsor
ADD COLUMN sponsor_name_first_char CHAR;

UPDATE ir_item.sponsor as spon
set sponsor_name_first_char = 
( select lower(substring(sponsor.name from 1 for 1))
  from ir_item.sponsor
  where spon.sponsor_id = sponsor.sponsor_id);
  
ALTER TABLE ir_item.sponsor ALTER COLUMN sponsor_name_first_char SET NOT NULL;
CREATE INDEX sponsor_name_first_char_idx ON ir_item.sponsor(sponsor_name_first_char);



-- ----------------------------------------------
-- **********************************************
       
-- add the ir file download count column
-- and drop ir_statistics.ir_file_roll_up

-- **********************************************
-- ----------------------------------------------
ALTER TABLE ir_file.ir_file
ADD COLUMN download_count BIGINT;

UPDATE ir_file.ir_file as file
SET download_count = 
(SELECT download_count from
ir_statistics.ir_file_roll_up
where ir_file_roll_up.ir_file_id = file.ir_file_id);

-- update all null values
UPDATE ir_file.ir_file
SET download_count = 0 
where ir_file.download_count is null;

DROP TABLE ir_statistics.ir_file_roll_up;




-- ----------------------------------------------
-- ---------------------------------------------


-- This is for the metadata and dublin core changes
-- 


-- ---------------------------------------------
-- ---------------------------------------------







-- ----------------------------------------------
-- ---------------------------------------------
-- Create a schema to basic metadata information
-- ---------------------------------------------

CREATE SCHEMA metadata AUTHORIZATION ir_plus;

-- ---------------------------------------------
-- Dublin core element table
-- ---------------------------------------------
CREATE TABLE metadata.metadata_type
(
    metadata_type_id BIGINT PRIMARY KEY,
    version INTEGER,
    name TEXT NOT NULL,
    description TEXT,
    UNIQUE(name)
);
ALTER TABLE metadata.metadata_type OWNER TO ir_plus;

-- The external account type sequence
CREATE SEQUENCE metadata.metadata_type_seq;
ALTER TABLE metadata.metadata_type_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Dublin core term table
-- ---------------------------------------------
CREATE TABLE metadata.dublin_core_term
(
    dublin_core_term_id BIGINT PRIMARY KEY,
    is_simple_dublin_core_element BOOLEAN NOT NULL,
    version INTEGER,
    name TEXT NOT NULL,
    description TEXT,
    UNIQUE(name)
);
ALTER TABLE metadata.dublin_core_term OWNER TO ir_plus;

-- The external account type sequence
CREATE SEQUENCE metadata.dublin_core_term_seq;
ALTER TABLE metadata.dublin_core_term_seq OWNER TO ir_plus;


-- ---------------------------------------------
-- Insert the  elements into the dublin core terms 
-- table
-- ---------------------------------------------


insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description ) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'contributor', true, 'An entity responsible for making contributions to the resource');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'coverage', true, 'The spatial or temporal topic of the resource, the spatial applicability of the resource, or the jurisdiction under which the resource is relevant.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'creator', true, 'An entity primarily responsible for making the resource.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'date', true, 'A point or period of time associated with an event in the lifecycle of the resource.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'description', true, 'An account of the resource');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'format', true, 'The file format, physical medium, or dimensions of the resource.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'identifier', true, 'An unambiguous reference to the resource within a given context.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'language', true, 'A language of the resource.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'publisher', true, 'An entity responsible for making the resource available.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'relation', true, 'A related resource.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'rights', true, 'Information about rights held in and over the resource.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'source', true, 'A related resource from which the described resource is derived.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'subject', true, 'The topic of the resource.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'title', true, 'A name given to the resource.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'type', true, 'The nature or genre of the resource.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'abstract', false, 'A summary of the resource.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'accessRights', false, 'Information about who can access the resource or an indication of its security status.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'accrualMethod', false, 'The method by which items are added to a collection.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'accrualPeriodicity', false, 'The frequency with which items are added to a collection.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'accrualPolicy', false, 'The policy governing the addition of items to a collection.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'alternative', false, 'An alternative name for the resource.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'audience', false, 'A class of entity for whom the resource is intended or useful.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'available', false, 'Date (often a range) that the resource became or will become available.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'bibliographicCitation', false, 'Recommended practice is to include sufficient bibliographic detail to identify the resource as unambiguously as possible.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'conformsTo', false, 'An established standard to which the described resource conforms.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'created', false, 'Date of creation of the resource.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'dateAccepted', false, 'Date of acceptance of the resource.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'dateCopyrighted', false, 'Date of copyright.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'dateSubmitted', false, 'Date of submission of the resource.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'educationLevel', false, 'A class of entity, defined in terms of progression through an educational or training context, for which the described resource is intended.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'hasFormat', false, 'A related resource that is substantially the same as the pre-existing described resource, but in another format.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'hasPart', false, 'A related resource that is included either physically or logically in the described resource.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'hasVersion', false, 'A related resource that is a version, edition, or adaptation of the described resource.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'instructionalMethod', false, 'A process, used to engender knowledge, attitudes and skills, that the described resource is designed to support.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'isFormatOf', false, 'A related resource that is substantially the same as the described resource, but in another format.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'isPartOf', false, 'A related resource in which the described resource is physically or logically included.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'isReferencedBy', false, 'A related resource that references, cites, or otherwise points to the described resource.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'isReplacedBy', false, 'A related resource that supplants, displaces, or supersedes the described resource.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'isRequiredBy', false, 'A related resource that requires the described resource to support its function, delivery, or coherence.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'issued', false, 'Date of formal issuance (e.g., publication) of the resource.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'isVersionOf', false, 'A related resource of which the described resource is a version, edition, or adaptation.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'license', false, 'A legal document giving official permission to do something with the resource.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'mediator', false, 'An entity that mediates access to the resource and for whom the resource is intended or useful.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'medium', false, 'The material or physical carrier of the resource.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'modified', false, 'Date on which the resource was changed.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'provenance', false, 'A statement of any changes in ownership and custody of the resource since its creation that are significant for its authenticity, integrity, and interpretation.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'references', false, 'A related resource that is referenced, cited, or otherwise pointed to by the described resource.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'replaces', false, 'A related resource that is supplanted, displaced, or superseded by the described resource.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'requires', false, 'A related resource that is required by the described resource to support its function, delivery, or coherence.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'rightsHolder', false, 'A person or organization owning or managing rights over the resource.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'spatial', false, 'Spatial characteristics of the resource.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'tableOfContents', false, 'A list of subunits of the resource.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'temporal', false, 'Temporal characteristics of the resource.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'valid', false, 'Date (often a range) of validity of a resource.');

insert into                                                         
metadata.dublin_core_term ( dublin_core_term_id, version, name, is_simple_dublin_core_element, description) 
values (nextval('metadata.dublin_core_term_seq'), 0, 'extent', false, 'The size or duration of the resource.');

-- ---------------------------------------------
-- Insert the  elements into the dublin core 
-- encoding schemes table
-- ---------------------------------------------

-- ---------------------------------------------
-- Dublin core term type table
-- ---------------------------------------------
CREATE TABLE metadata.dublin_core_encoding_scheme
(
    dublin_core_encoding_scheme_id BIGINT PRIMARY KEY,
    version INTEGER,
    name TEXT NOT NULL,
    description TEXT,
    UNIQUE(name)
);
ALTER TABLE metadata.dublin_core_encoding_scheme OWNER TO ir_plus;

-- The external account type sequence
CREATE SEQUENCE metadata.dublin_core_encoding_scheme_seq;
ALTER TABLE metadata.dublin_core_encoding_scheme_seq OWNER TO ir_plus;

-- -----------------------------------
--  Insert the encoding schemes 
-- -----------------------------------
insert into                                                         
metadata.dublin_core_encoding_scheme ( dublin_core_encoding_scheme_id, version, name, description) 
values (nextval('metadata.dublin_core_encoding_scheme_seq'), 0, 'DCMIType',  'The set of classes specified by the DCMI Type Vocabulary, used to categorize the nature or genre of the resource.');

insert into                                                         
metadata.dublin_core_encoding_scheme ( dublin_core_encoding_scheme_id, version, name, description) 
values (nextval('metadata.dublin_core_encoding_scheme_seq'), 0, 'DDC',  'The set of conceptual resources specified by the Dewey Decimal Classification.');

insert into                                                         
metadata.dublin_core_encoding_scheme ( dublin_core_encoding_scheme_id, version, name, description) 
values (nextval('metadata.dublin_core_encoding_scheme_seq'), 0, 'IMT',  'The set of media types specified by the Internet Assigned Numbers Authority.');

insert into                                                         
metadata.dublin_core_encoding_scheme ( dublin_core_encoding_scheme_id, version, name, description) 
values (nextval('metadata.dublin_core_encoding_scheme_seq'), 0, 'LCC',  'The set of conceptual resources specified by the Library of Congress Classification.');

insert into                                                         
metadata.dublin_core_encoding_scheme ( dublin_core_encoding_scheme_id, version, name, description) 
values (nextval('metadata.dublin_core_encoding_scheme_seq'), 0, 'LCSH',  'The set of labeled concepts specified by the Library of Congress Subject Headings.');

insert into                                                         
metadata.dublin_core_encoding_scheme ( dublin_core_encoding_scheme_id, version, name, description) 
values (nextval('metadata.dublin_core_encoding_scheme_seq'), 0, 'MESH',  'The set of labeled concepts specified by the Medical Subject Headings.');

insert into                                                         
metadata.dublin_core_encoding_scheme ( dublin_core_encoding_scheme_id, version, name, description) 
values (nextval('metadata.dublin_core_encoding_scheme_seq'), 0, 'NLM',  'The set of conceptual resources specified by the National Library of Medicine Classification.');

insert into                                                         
metadata.dublin_core_encoding_scheme ( dublin_core_encoding_scheme_id, version, name, description) 
values (nextval('metadata.dublin_core_encoding_scheme_seq'), 0, 'TGN',  'The set of places specified by the Getty Thesaurus of Geographic Names.');

insert into                                                         
metadata.dublin_core_encoding_scheme ( dublin_core_encoding_scheme_id, version, name, description) 
values (nextval('metadata.dublin_core_encoding_scheme_seq'), 0, 'UDC',  'The set of conceptual resources specified by the Universal Decimal Classification.');

insert into                                                         
metadata.dublin_core_encoding_scheme ( dublin_core_encoding_scheme_id, version, name, description) 
values (nextval('metadata.dublin_core_encoding_scheme_seq'), 0, 'Box',  'The set of regions in space defined by their geographic coordinates according to the DCMI Box Encoding Scheme.');

insert into                                                         
metadata.dublin_core_encoding_scheme ( dublin_core_encoding_scheme_id, version, name, description) 
values (nextval('metadata.dublin_core_encoding_scheme_seq'), 0, 'ISO3166',  'The set of codes listed in ISO 3166-1 for the representation of names of countries.');

insert into                                                         
metadata.dublin_core_encoding_scheme ( dublin_core_encoding_scheme_id, version, name, description) 
values (nextval('metadata.dublin_core_encoding_scheme_seq'), 0, 'ISO639-2',  'The three-letter alphabetic codes listed in ISO639-2 for the representation of names of languages.');

insert into                                                         
metadata.dublin_core_encoding_scheme ( dublin_core_encoding_scheme_id, version, name, description) 
values (nextval('metadata.dublin_core_encoding_scheme_seq'), 0, 'ISO639-3',  'The set of three-letter codes listed in ISO 639-3 for the representation of names of languages.');

insert into                                                         
metadata.dublin_core_encoding_scheme ( dublin_core_encoding_scheme_id, version, name, description) 
values (nextval('metadata.dublin_core_encoding_scheme_seq'), 0, 'Period',  'The set of time intervals defined by their limits according to the DCMI Period Encoding Scheme.');

insert into                                                         
metadata.dublin_core_encoding_scheme ( dublin_core_encoding_scheme_id, version, name, description) 
values (nextval('metadata.dublin_core_encoding_scheme_seq'), 0, 'Point',  'The set of points in space defined by their geographic coordinates according to the DCMI Point Encoding Scheme.');

insert into                                                         
metadata.dublin_core_encoding_scheme ( dublin_core_encoding_scheme_id, version, name, description) 
values (nextval('metadata.dublin_core_encoding_scheme_seq'), 0, 'RFC1766',  'The set of tags, constructed according to RFC 1766, for the identification of languages.');

insert into                                                         
metadata.dublin_core_encoding_scheme ( dublin_core_encoding_scheme_id, version, name, description) 
values (nextval('metadata.dublin_core_encoding_scheme_seq'), 0, 'RFC3066',  'The set of tags constructed according to RFC 3066 for the identification of languages.');

insert into                                                         
metadata.dublin_core_encoding_scheme ( dublin_core_encoding_scheme_id, version, name, description) 
values (nextval('metadata.dublin_core_encoding_scheme_seq'), 0, 'RFC4646',  'The set of tags constructed according to RFC 4646 for the identification of languages.');

insert into                                                         
metadata.dublin_core_encoding_scheme ( dublin_core_encoding_scheme_id, version, name, description) 
values (nextval('metadata.dublin_core_encoding_scheme_seq'), 0, 'URI',  'The set of identifiers constructed according to the generic syntax for Uniform Resource Identifiers as specified by the Internet Engineering Task Force.');

insert into                                                         
metadata.dublin_core_encoding_scheme ( dublin_core_encoding_scheme_id, version, name, description) 
values (nextval('metadata.dublin_core_encoding_scheme_seq'), 0, 'W3CDTF',  'The set of dates and times constructed according to the W3C Date and Time Formats Specification.');


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
    dublin_core_term_id BIGINT NOT NULL,
    version INTEGER,
    UNIQUE(contributor_type_id),
    FOREIGN KEY (contributor_type_id) REFERENCES person.contributor_type(contributor_type_id),
    FOREIGN KEY (dublin_core_term_id) REFERENCES metadata.dublin_core_term(dublin_core_term_id)
);

ALTER TABLE ir_metadata_dublin_core.contributor_type_dc_mapping OWNER TO ir_plus;

-- The contributor type dc mapping sequence
CREATE SEQUENCE ir_metadata_dublin_core.contributor_type_dc_mapping_seq ;
ALTER TABLE ir_metadata_dublin_core.contributor_type_dc_mapping_seq OWNER TO ir_plus;


-- ----------------------------------------------
-- **********************************************
       
-- Changes to deleted institutional item data

-- **********************************************
-- ----------------------------------------------
ALTER TABLE ir_repository.deleted_institutional_item ALTER COLUMN institutional_item_id SET NOT NULL;

-- ---------------------------------------------
-- Deleted Institutional Item Version
-- ---------------------------------------------
CREATE TABLE ir_repository.deleted_institutional_item_version
(
	deleted_institutional_item_version_id BIGINT PRIMARY KEY,
	deleted_institutional_item_id BIGINT NOT NULL,
    institutional_item_version_id BIGINT NOT NULL,
    handle_info_id BIGINT,
    versionNumber INTEGER NOT NULL,
    version INTEGER,
    FOREIGN KEY (deleted_institutional_item_id) REFERENCES ir_repository.deleted_institutional_item(deleted_institutional_item_id)
);
ALTER TABLE ir_repository.deleted_institutional_item_version OWNER TO ir_plus;

-- The deleted institutional item seq
CREATE SEQUENCE ir_repository.deleted_institutional_item_version_seq;
ALTER TABLE ir_repository.deleted_institutional_item_version_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- New columns for institutional item version
-- ---------------------------------------------

ALTER TABLE ir_repository.institutional_item_version
ADD COLUMN date_modified TIMESTAMP WITH TIME ZONE;

UPDATE ir_repository.institutional_item_version AS item_version 
SET date_modified = ( SELECT date_of_deposit FROM ir_repository.institutional_item_version 
where institutional_item_version.institutional_item_version_id = item_version.institutional_item_version_id);

ALTER TABLE ir_repository.institutional_item_version ALTER COLUMN date_modified SET NOT NULL;

ALTER TABLE ir_repository.institutional_item_version
ADD COLUMN  modified_by_user_id BIGINT;

ALTER TABLE ir_repository.institutional_item_version ADD CONSTRAINT modbyuserfk FOREIGN KEY ( modified_by_user_id) REFERENCES ir_user.ir_user(user_id) ;

ALTER TABLE ir_repository.institutional_item_version
ADD COLUMN modification_note TEXT;


