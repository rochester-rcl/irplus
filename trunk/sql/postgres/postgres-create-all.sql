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
       
-- Metadata tables  

-- **********************************************


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
-- Dublin core encoding scheme table
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

-- **********************************************
       
-- Index SCHEMA     

-- **********************************************
-- ----------------------------------------------

CREATE SCHEMA ir_index AUTHORIZATION ir_plus;

-- ---------------------------------------------
-- index processing type table
-- ---------------------------------------------
CREATE TABLE ir_index.index_processing_type
(
    index_processing_type_id BIGINT PRIMARY KEY,
    version INTEGER,
    name TEXT NOT NULL,
    description TEXT,
    unique_system_code TEXT,
    UNIQUE(name)
);
ALTER TABLE ir_index.index_processing_type OWNER TO ir_plus;

-- The content type sequence
CREATE SEQUENCE ir_index.index_processing_type_seq;
ALTER TABLE ir_index.index_processing_type_seq OWNER TO ir_plus;


-- **********************************************
       
-- Handle SCHEMA     

-- **********************************************
-- ----------------------------------------------


CREATE SCHEMA handle AUTHORIZATION ir_plus;

-- ---------------------------------------------
-- Sequence for handle values
-- ---------------------------------------------

CREATE SEQUENCE handle.unique_handle_name_seq; 
ALTER TABLE handle.unique_handle_name_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- handle nameing authority information
-- ---------------------------------------------
CREATE TABLE handle.handle_name_authority
(
  handle_name_authority_id BIGINT PRIMARY KEY,
  handle_name_authority TEXT NOT NULL,
  base_url TEXT,
  description TEXT,
  version INTEGER,
  UNIQUE(handle_name_authority)
);
ALTER TABLE handle.handle_name_authority OWNER TO ir_plus;
CREATE INDEX handle_name_authority_idx ON handle.handle_name_authority(handle_name_authority);


CREATE SEQUENCE handle.handle_name_authority_seq;
ALTER TABLE handle.handle_name_authority_seq OWNER TO ir_plus;
-- ---------------------------------------------
-- handle information
-- ---------------------------------------------
CREATE TABLE handle.handle_info
(
    handle_id BIGINT NOT NULL PRIMARY KEY,
    handle_name_authority_id BIGINT NOT NULL,
    handle_idx BIGINT NOT NULL,
    local_name text NOT NULL, 
    data_type TEXT,
    data TEXT,
    time_to_live_type INT,
    time_to_live INT,
    time_stamp INT,
    refs TEXT,
    version INTEGER,
    admin_read BOOLEAN,
    admin_write BOOLEAN,
    public_read BOOLEAN,
    public_write BOOLEAN,
    UNIQUE(handle_name_authority_id, local_name, handle_idx),
    FOREIGN KEY (handle_name_authority_id) REFERENCES handle.handle_name_authority (handle_name_authority_id)
);
ALTER TABLE handle.handle_info OWNER TO ir_plus;

CREATE SEQUENCE handle.handle_info_seq;
ALTER TABLE handle.handle_info_seq OWNER TO ir_plus;





-- ----------------------------------------------
-- **********************************************
       
-- FILE SYSTEM SCHEMA     

-- **********************************************
-- ----------------------------------------------

-- ---------------------------------------------
-- Create a schema to hold all file system
-- information.
-- ---------------------------------------------

CREATE SCHEMA file_system AUTHORIZATION ir_plus;

-- ---------------------------------------------
-- Sequence for naming files and folders on the file
-- system
-- ---------------------------------------------

CREATE SEQUENCE file_system.file_system_name_seq; 
ALTER TABLE file_system.file_system_name_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- File Server information
-- ---------------------------------------------

-- Create a new table to hold folder information in the system
CREATE TABLE file_system.file_server
(
  file_server_id BIGINT PRIMARY KEY,
  name TEXT UNIQUE NOT NULL,
  description TEXT,
  version INTEGER
);
ALTER TABLE file_system.file_server OWNER TO ir_plus;

-- The folder name sequence
CREATE SEQUENCE file_system.file_server_seq;
ALTER TABLE file_system.file_server_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- File Database Information
-- ---------------------------------------------

-- Create a new table to hold database information in the system
CREATE TABLE file_system.file_database
(
  file_database_id BIGINT PRIMARY KEY,
  file_server_id BIGINT,
  current_folder_id BIGINT,
  prefix VARCHAR(5) NOT NULL,
  path TEXT NOT NULL,
  name TEXT NOT NULL,
  display_name TEXT,
  description TEXT,
  version INTEGER,
  FOREIGN KEY(file_server_id) REFERENCES file_system.file_server (file_server_id),
  UNIQUE (name, file_server_id), 
  UNIQUE (name, path)
);
ALTER TABLE file_system.file_database OWNER TO ir_plus;

-- The folder name sequence
CREATE SEQUENCE file_system.file_database_seq;
ALTER TABLE file_system.file_database_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Folder Information
-- ---------------------------------------------

-- Create a new table to hold folder information in the system
CREATE TABLE file_system.folder
(
  folder_id BIGINT PRIMARY KEY,
  root_folder_id BIGINT NOT NULL,
  parent_id BIGINT,
  file_database_id BIGINT,
  left_value BIGINT,
  right_value BIGINT,
  exists BOOL NOT NULL,
  display_name TEXT,
  folder_name TEXT NOT NULL,
  path TEXT,
  description TEXT,
  version INTEGER,
  FOREIGN KEY (parent_id) REFERENCES file_system.folder (folder_id),
  FOREIGN KEY (root_folder_id) REFERENCES file_system.folder (folder_id),
  FOREIGN KEY (file_database_id) REFERENCES file_system.file_database 

 (file_database_id),
  UNIQUE (parent_id, folder_name)
);
ALTER TABLE file_system.folder OWNER TO ir_plus;

CREATE INDEX folder_display_name_idx ON file_system.folder USING btree 

(display_name);

-- The folder sequence
CREATE SEQUENCE file_system.folder_seq;
ALTER TABLE file_system.folder_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- File Information
-- ---------------------------------------------

-- Create a new table to hold file information in the system
CREATE TABLE file_system.file
(
  file_id BIGINT NOT NULL PRIMARY KEY,
  folder_id BIGINT NOT NULL,
  size BIGINT,
  created_date TIMESTAMP WITH TIME ZONE,
  extension VARCHAR(10),
  modified_date TIMESTAMP WITH TIME ZONE,
  exists bool NOT NULL,
  display_name TEXT,
  file_name TEXT NOT NULL,
  description TEXT,
  version INTEGER,
  FOREIGN KEY (folder_id) REFERENCES file_system.folder (folder_id),
  UNIQUE (folder_id, file_name)
);
ALTER TABLE file_system.file OWNER TO ir_plus;

-- Index on the file Name
CREATE INDEX file_display_name_idx ON file_system.file USING btree (display_name);

-- The file sequence
CREATE SEQUENCE file_system.file_seq;
ALTER TABLE file_system.file_seq OWNER TO ir_plus;



-- ---------------------------------------------
-- Checksum Information
-- ---------------------------------------------

-- Create a new table to hold file information in the system
CREATE TABLE file_system.file_checksum
(
  file_checksum_id BIGINT NOT NULL PRIMARY KEY,
  file_id BIGINT NOT NULL,
  checksum TEXT NOT NULL,
  algorithm_type TEXT NOT NULL,
  date_calculated TIMESTAMP WITH TIME ZONE NOT NULL ,
  version INTEGER,
  FOREIGN KEY (file_id) REFERENCES file_system.file (file_id)
);
ALTER TABLE file_system.file_checksum OWNER TO ir_plus;

-- The checksum sequence
CREATE SEQUENCE file_system.file_checksum_seq;
ALTER TABLE file_system.file_checksum_seq OWNER TO ir_plus;





-- ----------------------------------------------
-- **********************************************
       
-- MIME SCHEMA     

-- **********************************************
-- ----------------------------------------------



-- ---------------------------------------------
-- Mime Information
-- ---------------------------------------------

-- ---------------------------------------------
-- Create a schema to hold all mime
-- information.
-- ---------------------------------------------

CREATE SCHEMA mime AUTHORIZATION ir_plus;

-- ---------------------------------------------
-- Create the mime top media type table
-- ---------------------------------------------
-- the top level mime type
CREATE TABLE mime.top_media_type
(
  top_media_type_id BIGINT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  description TEXT,
  version INTEGER,
  UNIQUE (name)
);
ALTER TABLE mime.top_media_type OWNER TO ir_plus;

-- sequence for top level media mime types
CREATE SEQUENCE mime.top_media_type_seq; 
ALTER TABLE mime.top_media_type_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Create the mime sub type table
-- ---------------------------------------------
-- squences for mime sub types
CREATE SEQUENCE mime.sub_type_seq;
ALTER TABLE mime.sub_type_seq OWNER TO ir_plus;

-- the sub level mime type
CREATE TABLE mime.sub_type
(
  sub_type_id BIGINT PRIMARY KEY,
  top_media_type_id BIGINT NOT NULL,
  name VARCHAR(100) NOT NULL,
  short_description VARCHAR(200),
  description TEXT,
  version INTEGER,
  FOREIGN KEY (top_media_type_id) REFERENCES mime.top_media_type 

(top_media_type_id),
  UNIQUE (top_media_type_id, name)
);
ALTER TABLE mime.sub_type OWNER TO ir_plus;

-- ---------------------------------------------
-- Create the mime sub type extensions table
-- ---------------------------------------------
-- create a sequence for the mime type extensions
CREATE SEQUENCE mime.sub_type_extension_seq;
ALTER TABLE mime.sub_type_extension_seq OWNER TO ir_plus;

-- create a table for the extensions
CREATE TABLE mime.sub_type_extension
(
  sub_type_extension_id BIGINT PRIMARY KEY,
  sub_type_id BIGINT NOT NULL,
  mime_type TEXT NOT NULL,
  extension VARCHAR(20) NOT NULL,
  description TEXT,
  version INTEGER,
  FOREIGN KEY (sub_type_id)  REFERENCES mime.sub_type (sub_type_id),
  UNIQUE (sub_type_id, extension)
);
ALTER TABLE mime.sub_type_extension OWNER TO ir_plus;


CREATE INDEX sub_type_extension_ext_idx ON mime.sub_type_extension(extension);


-- ----------------------------------------------
-- **********************************************
       
-- PERSON SCHEMA     

-- **********************************************
-- ----------------------------------------------






-- ---------------------------------------------
-- Person Schema
-- ---------------------------------------------

CREATE SCHEMA person AUTHORIZATION ir_plus;


-- ---------------------------------------------
-- Person's birth Date 
-- ---------------------------------------------

CREATE TABLE person.birth_date (
  birth_date_id bigint NOT NULL,
  version integer,
  year integer,
  person_name_authority_id BIGINT,
  PRIMARY KEY (birth_date_id)
  
) ;
ALTER TABLE person.birth_date OWNER TO ir_plus;

-- The birth date sequence
CREATE SEQUENCE person.birth_date_seq ;
ALTER TABLE person.birth_date_seq OWNER TO ir_plus;


-- ---------------------------------------------
-- Person's death Date 
-- ---------------------------------------------

CREATE TABLE person.death_date (
  death_date_id bigint NOT NULL,
  version integer,
  year integer,
  person_name_authority_id BIGINT,
  PRIMARY KEY (death_date_id)
  
  
) ;
ALTER TABLE person.death_date OWNER TO ir_plus;

-- The death date sequence
CREATE SEQUENCE person.death_date_seq ;
ALTER TABLE person.death_date_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Contributor Type table
-- ---------------------------------------------
CREATE TABLE person.contributor_type
(
    contributor_type_id BIGINT PRIMARY KEY,
    version INTEGER,
    name TEXT NOT NULL,
    unique_system_code TEXT,
    description TEXT,
    UNIQUE(name)
);
ALTER TABLE person.contributor_type OWNER TO ir_plus;

-- The contributory type sequence
CREATE SEQUENCE person.contributor_type_seq;
ALTER TABLE person.contributor_type_seq OWNER TO ir_plus;


-- ---------------------------------------------
-- Person authority 
-- ---------------------------------------------
CREATE TABLE person.person_name_authority
(
    person_name_authority_id BIGINT PRIMARY KEY,
    version INTEGER,
    birth_date_id BIGINT,
    death_date_id BIGINT,
    authoritative_name_id BIGINT,
	FOREIGN KEY (birth_date_id) REFERENCES person.birth_date(birth_date_id),
	FOREIGN KEY (death_date_id) REFERENCES person.death_date(death_date_id)    
);
ALTER TABLE person.person_name_authority OWNER TO ir_plus;

-- The person authority sequence
CREATE SEQUENCE person.person_name_authority_seq ;
ALTER TABLE person.person_name_authority_seq OWNER TO ir_plus;


-- ---------------------------------------------
-- Constraint for birth date 
-- ---------------------------------------------
ALTER TABLE person.birth_date ADD CONSTRAINT person_birth_date_id_fkey FOREIGN 
KEY (person_name_authority_id)
      REFERENCES person.person_name_authority(person_name_authority_id);;
      
-- ---------------------------------------------
-- Constraint for death date 
-- ---------------------------------------------
ALTER TABLE person.death_date ADD CONSTRAINT person_death_date_id_fkey FOREIGN 
KEY (person_name_authority_id)
      REFERENCES person.person_name_authority(person_name_authority_id);      
-- ---------------------------------------------
-- Person Name 
-- ---------------------------------------------
CREATE TABLE person.person_name
(
    person_name_id BIGINT PRIMARY KEY,
    person_name_authority_id BIGINT,
    version INTEGER,
    forename TEXT,
    lower_case_forename TEXT,
    middle_name TEXT,
    lower_case_middle_name TEXT,
	surname TEXT,
	lower_case_surname TEXT,
    surname_first_char char,
	family_name TEXT,
	lower_case_family_name TEXT,
	initials TEXT,
	lower_case_initials TEXT,
    numeration TEXT,
    lower_case_numeration TEXT,
    titles_other_words TEXT,
    UNIQUE (person_name_authority_id, person_name_id),
    FOREIGN KEY (person_name_authority_id) REFERENCES person.person_name_authority(person_name_authority_id)
);
ALTER TABLE person.person_name OWNER TO ir_plus;

CREATE INDEX person_name_first_char_idx ON person.person_name(surname_first_char);

CREATE INDEX person_full_name_sort_idx ON person.person_name (lower_case_surname, lower_case_forename, lower_case_family_name, lower_case_middle_name);

-- The person name sequence
CREATE SEQUENCE person.person_name_seq ;
ALTER TABLE person.person_name_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Person Name Title 
-- ---------------------------------------------
CREATE TABLE person.person_name_title
(
    person_name_title_id BIGINT PRIMARY KEY,
    person_name_id BIGINT NOT NULL,
    version INTEGER,
    value TEXT,
    FOREIGN KEY (person_name_id) REFERENCES person.person_name(person_name_id)
);
ALTER TABLE person.person_name_title OWNER TO ir_plus;

-- The person name sequence
CREATE SEQUENCE person.person_name_title_seq ;
ALTER TABLE person.person_name_title_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Constraint for authoritative name
-- it can now link to the person.person_name
-- table.
-- ---------------------------------------------
ALTER TABLE person.person_name_authority ADD CONSTRAINT person_authoritiative_name_id_fkey FOREIGN 

KEY (authoritative_name_id)
      REFERENCES person.person_name(person_name_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
-- ---------------------------------------------
-- Contributor
-- ---------------------------------------------
CREATE TABLE person.contributor
(
    contributor_id BIGINT PRIMARY KEY,
    contributor_type_id BIGINT NOT NULL,
    person_name_id BIGINT NOT NULL,
    version INTEGER,
    UNIQUE (contributor_type_id, person_name_id),
    FOREIGN KEY (person_name_id) REFERENCES person.person_name(person_name_id),
    FOREIGN KEY (contributor_type_id) REFERENCES person.contributor_type(contributor_type_id)
);
ALTER TABLE person.contributor OWNER TO ir_plus;

-- The contributory sequence
CREATE SEQUENCE person.contributor_seq ;
ALTER TABLE person.contributor_seq OWNER TO ir_plus;



-- ----------------------------------------------
-- **********************************************
       
-- USER SCHEMA     

-- **********************************************
-- ----------------------------------------------





-- ---------------------------------------------
-- Create a schema to hold security information
-- ---------------------------------------------

CREATE SCHEMA ir_user AUTHORIZATION ir_plus;

-- ---------------------------------------------
-- External account type table
-- ---------------------------------------------
CREATE TABLE ir_user.external_account_type
(
    external_account_type_id BIGINT PRIMARY KEY,
    version INTEGER,
    user_name_case_sensitive BOOLEAN NOT NULL,
    name TEXT NOT NULL,
    description TEXT,
    UNIQUE(name)
);
ALTER TABLE ir_user.external_account_type OWNER TO ir_plus;

-- The external account type sequence
CREATE SEQUENCE ir_user.external_account_type_seq;
ALTER TABLE ir_user.external_account_type_seq OWNER TO ir_plus;






-- ---------------------------------------------
-- user workspace index process record Information
-- ---------------------------------------------

CREATE TABLE ir_user.user_workspace_index_processing_record
(
  user_workspace_index_processing_record_id BIGINT PRIMARY KEY,
  workspace_item_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  workspace_type TEXT NOT NULL, 
  created_date TIMESTAMP WITH TIME ZONE NOT NULL,
  index_processing_type_id BIGINT NOT NULL,
  skip_record BOOLEAN NOT NULL,
  skip_reason TEXT,
  UNIQUE(workspace_item_id, user_id, workspace_type, index_processing_type_id),
  FOREIGN KEY (index_processing_type_id)
      REFERENCES ir_index.index_processing_type (index_processing_type_id)
);
ALTER TABLE ir_user.user_workspace_index_processing_record OWNER TO ir_plus;

-- The repository license sequence
CREATE SEQUENCE ir_user.user_workspace_index_processing_record_seq;
ALTER TABLE ir_user.user_workspace_index_processing_record_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Affiliation table
-- ---------------------------------------------


-- ---------------------------------------------
-- user delete personal file record
-- ---------------------------------------------

CREATE TABLE ir_user.personal_file_delete_record
(
  personal_file_delete_record_id BIGINT PRIMARY KEY,
  personal_file_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  date_deleted TIMESTAMP WITH TIME ZONE NOT NULL,
  full_path TEXT NOT NULL,
  description TEXT,
  delete_reason TEXT,
  UNIQUE(personal_file_id)
);
ALTER TABLE ir_user.personal_file_delete_record OWNER TO ir_plus;

-- The repository license sequence
CREATE SEQUENCE ir_user.personal_file_delete_record_seq;
ALTER TABLE ir_user.personal_file_delete_record_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- user delete personal file record
-- ---------------------------------------------

CREATE TABLE ir_user.personal_item_delete_record
(
  personal_item_delete_record_id BIGINT PRIMARY KEY,
  personal_item_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  date_deleted TIMESTAMP WITH TIME ZONE NOT NULL,
  full_path TEXT NOT NULL,
  description TEXT,
  delete_reason TEXT,
  UNIQUE(personal_item_id)
);
ALTER TABLE ir_user.personal_item_delete_record OWNER TO ir_plus;

-- The repository license sequence
CREATE SEQUENCE ir_user.personal_item_delete_record_seq;
ALTER TABLE ir_user.personal_item_delete_record_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Affiliation table
-- ---------------------------------------------


CREATE TABLE ir_user.affiliation
(
  affiliation_id bigint NOT NULL,
  version integer,
  name text NOT NULL,
  description text,
  is_author BOOLEAN NOT NULL,
  is_researcher BOOLEAN NOT NULL,
  needs_approval BOOLEAN NOT NULL,
  CONSTRAINT affiliation_pkey PRIMARY KEY (affiliation_id),
  CONSTRAINT affiliation_name_key UNIQUE (name)
); 

ALTER TABLE ir_user.affiliation OWNER TO ir_plus;

-- The affiliation sequence
CREATE SEQUENCE ir_user.affiliation_seq ;
ALTER TABLE ir_user.affiliation_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Department table
-- ---------------------------------------------

CREATE TABLE ir_user.department
(
  department_id bigint PRIMARY KEY,
  version integer,
  name text NOT NULL,
  description text,
  UNIQUE (name)
); 

ALTER TABLE ir_user.department OWNER TO ir_plus;

-- The department sequence
CREATE SEQUENCE ir_user.department_seq ;
ALTER TABLE ir_user.department_seq OWNER TO ir_plus;


-- ---------------------------------------------
-- Users table
-- ---------------------------------------------
CREATE TABLE ir_user.ir_user
(
  user_id bigint PRIMARY KEY,
  user_password text NOT NULL,
  password_encoding VARCHAR(50),
  default_email_id BIGINT,
  username TEXT NOT NULL,
  first_name TEXT,
  lower_case_first_name TEXT,
  last_name TEXT,
  lower_case_last_name TEXT,
  middle_name TEXT,
  lower_case_middle_name TEXT,
  created_date TIMESTAMP WITH TIME ZONE,
  last_login_date TIMESTAMP WITH TIME ZONE,
  self_registered BOOLEAN,
  phone_number TEXT,
  account_expired BOOLEAN NOT NULL,
  account_locked BOOLEAN NOT NULL,
  credentials_expired BOOLEAN NOT NULL,
  password_token TEXT,
  force_change_password BOOLEAN,
  person_name_authority_id BIGINT,
  version INTEGER,
  personal_index_folder TEXT,
  affiliation_id BIGINT,
  department_id BIGINT,
  affiliation_approved BOOLEAN,
  re_build_user_workspace_index BOOLEAN NOT NULL,
  FOREIGN KEY (affiliation_id) REFERENCES ir_user.affiliation (affiliation_id),
  FOREIGN KEY (department_id) REFERENCES ir_user.department (department_id),
  FOREIGN KEY (person_name_authority_id) REFERENCES person.person_name_authority (person_name_authority_id),
  UNIQUE (username)
);
ALTER TABLE ir_user.ir_user OWNER TO ir_plus;



-- Index on the user for sorting
CREATE INDEX user_last_name_idx ON ir_user.ir_user (lower_case_last_name, lower_case_first_name, lower_case_middle_name);


-- The user sequence
CREATE SEQUENCE ir_user.ir_user_seq ;
ALTER TABLE ir_user.ir_user_seq OWNER TO ir_plus;

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

-- create a unique constraint on user id
ALTER TABLE ir_user.external_user_account ADD CONSTRAINT external_user_account_user_id_key UNIQUE (user_id);

-- Index on the external user name
CREATE INDEX external_user_name_type_idx ON ir_user.external_user_account (external_user_account_name, external_account_type_id);

-- ---------------------------------------------
-- Users department table
-- ---------------------------------------------

CREATE TABLE ir_user.user_department
(
    user_id BIGINT NOT NULL, 
    department_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, department_id),
    FOREIGN KEY (user_id) REFERENCES ir_user.ir_user(user_id),
    FOREIGN KEY (department_id) REFERENCES ir_user.department(department_id)
);
ALTER TABLE ir_user.user_department OWNER TO ir_plus;


-- ---------------------------------------------
-- Role table
-- ---------------------------------------------

CREATE TABLE ir_user.role
(
    role_id BIGINT PRIMARY KEY,
    version INTEGER,
    name TEXT NOT NULL,
    description TEXT,
    UNIQUE(name)
);
ALTER TABLE ir_user.role OWNER TO ir_plus;

-- The role sequence
CREATE SEQUENCE ir_user.role_seq ;
ALTER TABLE ir_user.role_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Group table for grouping users
-- ---------------------------------------------

CREATE TABLE ir_user.user_group
(
    group_id BIGINT NOT NULL PRIMARY KEY,
    version INTEGER,
    name TEXT NOT NULL,
    description TEXT,
    UNIQUE(name)
);
ALTER TABLE ir_user.user_group OWNER TO ir_plus;

-- The group sequence
CREATE SEQUENCE ir_user.user_group_seq ;
ALTER TABLE ir_user.user_group_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Link between a group and a set of users
-- ---------------------------------------------

CREATE TABLE ir_user.user_group_users
(
    group_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY(group_id, user_id),
    FOREIGN KEY (user_id) REFERENCES ir_user.ir_user(user_id),
    FOREIGN KEY (group_id) REFERENCES ir_user.user_group(group_id)
);
ALTER TABLE ir_user.user_group_users OWNER TO ir_plus;

-- ---------------------------------------------
-- Link between a group and the admins of that group
-- ---------------------------------------------

CREATE TABLE ir_user.user_group_admins
(
    group_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY(group_id, user_id),
    FOREIGN KEY (user_id) REFERENCES ir_user.ir_user(user_id),
    FOREIGN KEY (group_id) REFERENCES ir_user.user_group(group_id)
);
ALTER TABLE ir_user.user_group_admins OWNER TO ir_plus;

-- ---------------------------------------------
-- User Role table
-- ---------------------------------------------

CREATE TABLE ir_user.user_role
(
    user_id BIGINT NOT NULL, 
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES ir_user.ir_user(user_id),
    FOREIGN KEY (role_id) REFERENCES ir_user.role(role_id)
);
ALTER TABLE ir_user.user_role OWNER TO ir_plus;





-- ----------------------------------------------
-- **********************************************
       
-- IR File SCHEMA     

-- **********************************************
-- ----------------------------------------------


-- ---------------------------------------------
-- Create a schema to hold all file system
-- information.
-- ---------------------------------------------

CREATE SCHEMA ir_file AUTHORIZATION ir_plus;

-- ---------------------------------------------
-- IR File Indexing failure record
-- ---------------------------------------------
CREATE TABLE ir_file.ir_file_indexing_failure_record
(
    ir_file_indexing_failure_record_id BIGINT PRIMARY KEY,
    ir_file_id BIGINT NOT NULL,
    date_created TIMESTAMP WITH TIME ZONE NOT NULL,
    failure_reason TEXT
);
ALTER TABLE ir_file.ir_file_indexing_failure_record OWNER TO ir_plus;

-- The ir file sequence
CREATE SEQUENCE ir_file.ir_file_indexing_failure_record_seq ;
ALTER TABLE ir_file.ir_file_indexing_failure_record_seq OWNER TO ir_plus;

-- Index on the item Name and lower case name
CREATE INDEX ir_file_indexing_failure_record_file_id_idx ON 
    ir_file.ir_file_indexing_failure_record (ir_file_id);
    
    
-- ---------------------------------------------
-- IR File Transformation failure record
-- ---------------------------------------------
CREATE TABLE ir_file.ir_file_transformation_failure_record
(
    ir_file_transformation_failure_record_id BIGINT PRIMARY KEY,
    ir_file_id BIGINT NOT NULL,
    date_created TIMESTAMP WITH TIME ZONE NOT NULL,
    failure_reason TEXT
);
ALTER TABLE ir_file.ir_file_transformation_failure_record OWNER TO ir_plus;

-- The ir file sequence
CREATE SEQUENCE ir_file.ir_file_transformation_failure_record_seq;
ALTER TABLE ir_file.ir_file_transformation_failure_record_seq OWNER TO ir_plus;

-- Index on the item Name and lower case name
CREATE INDEX ir_file_transformation_failure_record_file_id_idx ON 
    ir_file.ir_file_transformation_failure_record (ir_file_id);

-- ---------------------------------------------
-- IR File Information
-- ---------------------------------------------
CREATE TABLE ir_file.ir_file
(
    ir_file_id BIGINT PRIMARY KEY,
    file_id BIGINT NOT NULL,
    public_viewable BOOLEAN NOT NULL,
    version INTEGER,
    user_id BIGINT,
    download_count BIGINT,
    FOREIGN KEY (file_id) REFERENCES file_system.file (file_id),
    FOREIGN KEY (user_id) REFERENCES ir_user.ir_user(user_id)
);
ALTER TABLE ir_file.ir_file OWNER TO ir_plus;

-- The ir file sequence
CREATE SEQUENCE ir_file.ir_file_seq ;
ALTER TABLE ir_file.ir_file_seq OWNER TO ir_plus;


-- ---------------------------------------------
-- Transformed File type Information
-- ---------------------------------------------
CREATE TABLE ir_file.transformed_file_type
(
    transformed_file_type_id BIGINT PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT,
    system_code TEXT,
    version INTEGER,
    UNIQUE(system_code)
);
ALTER TABLE ir_file.transformed_file_type OWNER TO ir_plus;

-- The transformed file type seq
CREATE SEQUENCE ir_file.transformed_file_type_seq ;
ALTER TABLE ir_file.transformed_file_type_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Transformed File Information
-- ---------------------------------------------
CREATE TABLE ir_file.transformed_file
(
    transformed_file_id BIGINT PRIMARY KEY,
    version INTEGER,
    actual_file_id BIGINT,
    public_viewable BOOLEAN NOT NULL,
    transformed_id BIGINT,
    transformed_file_type_id BIGINT,
    FOREIGN KEY (actual_file_id) REFERENCES ir_file.ir_file (ir_file_id),
    FOREIGN KEY (transformed_id) REFERENCES file_system.file(file_id),
    FOREIGN KEY (transformed_file_type_id) REFERENCES 
        ir_file.transformed_file_type (transformed_file_type_id)
);
ALTER TABLE ir_file.transformed_file OWNER TO ir_plus;

-- The transformed file type seq
CREATE SEQUENCE ir_file.transformed_file_seq ;
ALTER TABLE ir_file.transformed_file_seq OWNER TO ir_plus;


-- ---------------------------------------------
-- Versioned IR File Information
-- the current_file_version_id references
-- the file versions.  It can be null but
-- this link should be maintained in the software
-- ---------------------------------------------
CREATE TABLE ir_file.versioned_file
(
    versioned_file_id BIGINT PRIMARY KEY,
    largest_file_version_id INTEGER,
    current_file_version_id BIGINT,
    extension TEXT,
    name TEXT NOT NULL,
    description TEXT,
    user_id BIGINT,
    locked_user_id BIGINT,
    current_file_size_bytes BIGINT,
    total_file_size_all_files_bytes BIGINT,
    version INTEGER,
    FOREIGN KEY (user_id) REFERENCES ir_user.ir_user(user_id),
    FOREIGN KEY (locked_user_id) REFERENCES ir_user.ir_user(user_id)
);
ALTER TABLE ir_file.versioned_file OWNER TO ir_plus;

-- The versioned ir file sequence
CREATE SEQUENCE ir_file.versioned_file_seq;
ALTER TABLE ir_file.versioned_file_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- File Collaborator
-- ---------------------------------------------
CREATE TABLE ir_file.file_collaborator
(
    file_collaborator_id BIGINT PRIMARY KEY,
    versioned_file_id BIGINT NOT NULL,
    version INTEGER,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (versioned_file_id) REFERENCES 

ir_file.versioned_file(versioned_file_id),
    FOREIGN KEY (user_id) REFERENCES ir_user.ir_user(user_id),
    CONSTRAINT file_collaborator_user_versioned_file_key UNIQUE (user_id, 

versioned_file_id)
);
ALTER TABLE ir_file.file_collaborator OWNER TO ir_plus;

-- The person name sequence
CREATE SEQUENCE ir_file.file_collaborator_seq ;
ALTER TABLE ir_file.file_collaborator_seq OWNER TO ir_plus;


-- ---------------------------------------------
-- Version Information  
-- Links an ir file and a versioned ir file to create
-- a particular instance of a versioned file
-- ---------------------------------------------
CREATE TABLE ir_file.file_version
(
  file_version_id BIGINT PRIMARY KEY,
  ir_file_id BIGINT NOT NULL,
  versioned_file_id BIGINT NOT NULL,
  version_number INTEGER NOT NULL,
  version INTEGER,
  user_id BIGINT,
  UNIQUE(versioned_file_id, version_number),
  FOREIGN KEY (ir_file_id) REFERENCES ir_file.ir_file (ir_file_id),
  FOREIGN KEY (versioned_file_id) REFERENCES ir_file.versioned_file 

(versioned_file_id) ,
  FOREIGN KEY (user_id) REFERENCES ir_user.ir_user(user_id)
);
ALTER TABLE ir_file.file_version OWNER TO ir_plus;

-- The version sequence
CREATE SEQUENCE ir_file.file_version_seq;
ALTER TABLE ir_file.file_version_seq OWNER TO ir_plus;







-- ----------------------------------------------
-- **********************************************
       
-- IR Item SCHEMA     

-- **********************************************
-- ----------------------------------------------



-- ---------------------------------------------
-- Create a schema to hold all file system
-- information.
-- ---------------------------------------------

CREATE SCHEMA ir_item AUTHORIZATION ir_plus;

-- ---------------------------------------------
-- Content Type table
-- ---------------------------------------------
CREATE TABLE ir_item.content_type
(
    content_type_id BIGINT PRIMARY KEY,
    version INTEGER,
    name TEXT NOT NULL,
    description TEXT,
    unique_system_code TEXT,
    UNIQUE(name)
);
ALTER TABLE ir_item.content_type OWNER TO ir_plus;

-- The content type sequence
CREATE SEQUENCE ir_item.content_type_seq;
ALTER TABLE ir_item.content_type_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Content Type table
-- ---------------------------------------------
CREATE TABLE ir_item.copyright_statement
(
    copyright_statement_id BIGINT PRIMARY KEY,
    version INTEGER,
    name TEXT NOT NULL,
    description TEXT,
    copyright_text TEXT,
    UNIQUE(name)
);
ALTER TABLE ir_item.copyright_statement OWNER TO ir_plus;

-- The content type sequence
CREATE SEQUENCE ir_item.copyright_statement_seq;
ALTER TABLE ir_item.copyright_statement_seq OWNER TO ir_plus;


-- ---------------------------------------------
-- Extent Type table
-- ---------------------------------------------
CREATE TABLE ir_item.extent_type
(
    extent_type_id BIGINT PRIMARY KEY,
    version INTEGER,
    name TEXT,
    description TEXT,
    UNIQUE(name)
);
ALTER TABLE ir_item.extent_type OWNER TO ir_plus;

-- The extent type sequence
CREATE SEQUENCE ir_item.extent_type_seq;
ALTER TABLE ir_item.extent_type_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Publisher table
-- ---------------------------------------------
CREATE TABLE ir_item.publisher
(
    publisher_id BIGINT PRIMARY KEY,
    version INTEGER,
    name TEXT,
    description TEXT
);
ALTER TABLE ir_item.publisher OWNER TO ir_plus;

-- The publisher sequence
CREATE SEQUENCE ir_item.publisher_seq;
ALTER TABLE ir_item.publisher_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Language type table
-- ---------------------------------------------
CREATE TABLE ir_item.language_type
(
    language_type_id BIGINT PRIMARY KEY,
    version INTEGER,
    name TEXT,
    unique_system_code TEXT,
    iso639_1 VARCHAR(2),
    iso639_2 VARCHAR(3),
    description TEXT,
    UNIQUE(name)
);
ALTER TABLE ir_item.language_type OWNER TO ir_plus;

-- The language type sequence
CREATE SEQUENCE ir_item.language_type_seq ;
ALTER TABLE ir_item.language_type_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Identifier type table
-- ---------------------------------------------
CREATE TABLE ir_item.identifier_type
(
    identifier_type_id BIGINT PRIMARY KEY,
    version INTEGER,
    name TEXT NOT NULL,
    unique_system_code TEXT,
    description TEXT,
    CONSTRAINT identifier_type_name_key UNIQUE(name)
) ;
ALTER TABLE ir_item.identifier_type OWNER TO ir_plus;

-- The identifier type sequence
CREATE SEQUENCE ir_item.identifier_type_seq;
ALTER TABLE ir_item.identifier_type_seq OWNER TO ir_plus;



-- ---------------------------------------------
-- Sponsor
-- ---------------------------------------------
CREATE TABLE ir_item.sponsor(
  sponsor_id bigint NOT NULL,
  version INTEGER,
  sponsor_name_first_char CHAR NOT NULL,
  name TEXT,
  description TEXT,
  PRIMARY KEY (sponsor_id),
  UNIQUE (name)
  
) ;
ALTER TABLE ir_item.sponsor OWNER TO ir_plus;

-- The sponser sequence
CREATE SEQUENCE ir_item.sponsor_seq ;
ALTER TABLE ir_item.sponsor_seq OWNER TO ir_plus;
CREATE INDEX sponsor_name_first_char_idx ON ir_item.sponsor(sponsor_name_first_char);


-- ---------------------------------------------
-- Item's published Date 
-- ---------------------------------------------

CREATE TABLE ir_item.published_date (
  published_date_id BIGINT NOT NULL,
  version INTEGER,
  day INTEGER,
  month INTEGER,
  year INTEGER,
  hours INTEGER,
  minutes INTEGER,
  seconds INTEGER,
  fraction_seconds INTEGER,
  external_published_item_id BIGINT,
  PRIMARY KEY (published_date_id)
  
) ;
ALTER TABLE ir_item.published_date OWNER TO ir_plus;

-- The published date sequence
CREATE SEQUENCE ir_item.published_date_seq ;
ALTER TABLE ir_item.published_date_seq OWNER TO ir_plus;


-- ---------------------------------------------
-- External published item
-- ---------------------------------------------

CREATE TABLE ir_item.external_published_item(
  external_published_item_id bigint NOT NULL,
  version INTEGER,
  publisher_id BIGINT,
  published_date_id BIGINT,
  citation TEXT,
  PRIMARY KEY (external_published_item_id),
  FOREIGN KEY (publisher_id)
      REFERENCES ir_item.publisher (publisher_id) ,
  FOREIGN KEY (published_date_id) REFERENCES ir_item.published_date 
	(published_date_id)   
  
) ;
ALTER TABLE ir_item.external_published_item OWNER TO ir_plus;

-- The external published item sequence
CREATE SEQUENCE ir_item.external_published_item_seq ;
ALTER TABLE ir_item.external_published_item_seq OWNER TO ir_plus;

      
-- ---------------------------------------------
-- Constraint for published date 
-- ---------------------------------------------
ALTER TABLE ir_item.published_date ADD CONSTRAINT external_published_item_published_date_id_fkey FOREIGN 

KEY (external_published_item_id)
      REFERENCES ir_item.external_published_item(external_published_item_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

-- ---------------------------------------------
-- Item's  First available Date 
-- ---------------------------------------------

CREATE TABLE ir_item.first_available_date (
  first_available_date_id bigint NOT NULL,
  version integer,
  day integer,
  month integer,
  year integer,
  hours integer,
  minutes integer,
  seconds integer,
  fraction_seconds integer,
  item_id BIGINT,
  PRIMARY KEY (first_available_date_id)
  
) ;
ALTER TABLE ir_item.first_available_date OWNER TO ir_plus;

-- The published date sequence
CREATE SEQUENCE ir_item.first_available_date_seq ;
ALTER TABLE ir_item.first_available_date_seq OWNER TO ir_plus;


-- ---------------------------------------------
-- Item's release Date 
-- ---------------------------------------------

CREATE TABLE ir_item.original_item_creation_date (
  original_item_creation_date_id bigint NOT NULL,
  version integer,
  day integer,
  month integer,
  year integer,
  hours integer,
  minutes integer,
  seconds integer,
  fraction_seconds integer,
  item_id BIGINT,
  PRIMARY KEY (original_item_creation_date_id)
  
  
) ;
ALTER TABLE ir_item.original_item_creation_date OWNER TO ir_plus;

-- The published date sequence
CREATE SEQUENCE ir_item.original_item_creation_date_seq ;
ALTER TABLE ir_item.original_item_creation_date_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Item Information
-- ---------------------------------------------
-- Create a new table to hold item information for the system
CREATE TABLE ir_item.item
(
  item_id BIGINT PRIMARY KEY,
  language_type_id BIGINT,
  content_type_id BIGINT,
  external_published_item_id BIGINT,
  original_item_creation_date_id BIGINT,
  copyright_statement_id BIGINT,
  user_id BIGINT,
  withdrawn BOOLEAN,
  release_date DATE,
  first_available_date_id BIGINT,
  name TEXT NOT NULL,
  name_first_char CHAR NOT NULL,
  lower_case_name TEXT NOT NULL,
  leading_name_articles TEXT,
  description TEXT,
  abstract TEXT,
  keywords TEXT,
  version INTEGER,
  published BOOLEAN NOT NULL,
  locked_for_review BOOLEAN,
  publicly_viewable BOOLEAN, 
  primary_image_item_file_id BIGINT,
  FOREIGN KEY (language_type_id) REFERENCES ir_item.language_type 
	(language_type_id),
  FOREIGN KEY (copyright_statement_id) REFERENCES ir_item.copyright_statement
	(copyright_statement_id),
  FOREIGN KEY (original_item_creation_date_id) REFERENCES ir_item.original_item_creation_date 
	(original_item_creation_date_id),
  FOREIGN KEY (first_available_date_id) REFERENCES ir_item.first_available_date 
	(first_available_date_id),	
  FOREIGN KEY (content_type_id) REFERENCES ir_item.content_type (content_type_id),
  FOREIGN KEY (user_id) REFERENCES ir_user.ir_user(user_id),
  FOREIGN KEY (external_published_item_id) REFERENCES ir_item.external_published_item (external_published_item_id)
) ;
ALTER TABLE ir_item.item OWNER TO ir_plus;

-- Index on the item Name and lower case name
CREATE INDEX item_lower_case_name_idx ON ir_item.item USING btree (lower_case_name);
CREATE INDEX item_name_idx ON ir_item.item USING btree (name);

-- Index on the item Name first character
CREATE INDEX item_name_first_char_idx ON ir_item.item(name_first_char);


-- The item sequence
CREATE SEQUENCE ir_item.item_seq;
ALTER TABLE ir_item.item_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Constraint for first available date 
-- ---------------------------------------------
ALTER TABLE ir_item.first_available_date ADD CONSTRAINT item_first_available_date_id_fkey FOREIGN 
KEY (item_id)  REFERENCES ir_item.item(item_id) ;
      
-- ---------------------------------------------
-- Constraint for release date 
-- ---------------------------------------------
ALTER TABLE ir_item.original_item_creation_date ADD CONSTRAINT item_original_item_creation_date_id_fkey FOREIGN 
KEY (item_id) REFERENCES ir_item.item(item_id);

-- ---------------------------------------------
-- Item type table
-- ---------------------------------------------
CREATE TABLE ir_item.item_content_type
(
    item_content_type_id BIGINT NOT NULL PRIMARY KEY,
    item_id BIGINT NOT NULL,
    is_primary BOOLEAN NOT NULL,
    content_type_id BIGINT NOT NULL,
    UNIQUE(item_id, content_type_id ),
    FOREIGN KEY (content_type_id) REFERENCES ir_item.content_type (content_type_id),
    FOREIGN KEY (item_id) REFERENCES ir_item.item (item_id) 
);
ALTER TABLE ir_item.item_content_type OWNER TO ir_plus;
            
CREATE SEQUENCE ir_item.item_content_type_seq ;
ALTER TABLE ir_item.item_content_type_seq OWNER TO ir_plus;
-- ---------------------------------------------
-- Item Sponsor table
-- ---------------------------------------------

CREATE TABLE ir_item.item_sponsor
(
	item_sponsor_id BIGINT PRIMARY KEY,
    item_id BIGINT NOT NULL, 
    sponsor_id BIGINT NOT NULL,
    description text,
    version INTEGER,
    FOREIGN KEY (item_id) REFERENCES ir_item.item(item_id),
    FOREIGN KEY (sponsor_id) REFERENCES ir_item.sponsor(sponsor_id)
);
ALTER TABLE ir_item.item_sponsor OWNER TO ir_plus;

-- The sponsor sequence
CREATE SEQUENCE ir_item.item_sponsor_seq ;
ALTER TABLE ir_item.item_sponsor_seq OWNER TO ir_plus;
-- ---------------------------------------------
-- Item Contributor table
-- ---------------------------------------------     

CREATE TABLE ir_item.item_contributor
(
    item_contributor_id BIGINT PRIMARY KEY,
    item_id BIGINT NOT NULL,
    contributor_id BIGINT NOT NULL,
    contributor_order INTEGER,
    UNIQUE (contributor_id, item_id),
    FOREIGN KEY (item_id) REFERENCES ir_item.item(item_id),
    FOREIGN KEY (contributor_id) REFERENCES person.contributor(contributor_id)
);
ALTER TABLE ir_item.item_contributor OWNER TO ir_plus;

-- The contributory sequence
CREATE SEQUENCE ir_item.item_contributor_seq ;
ALTER TABLE ir_item.item_contributor_seq OWNER TO ir_plus;
-- ---------------------------------------------
-- Versioned IR Item Information
-- ---------------------------------------------
CREATE TABLE ir_item.versioned_item
(
    versioned_item_id BIGINT PRIMARY KEY,
    largest_item_version_id INTEGER NOT NULL,
    max_allowed_versions INTEGER NOT NULL,
    current_item_version_id bigint,
    version INTEGER,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES ir_user.ir_user(user_id)
);
ALTER TABLE ir_item.versioned_item OWNER TO ir_plus;

-- The versioned ir file sequence
CREATE SEQUENCE ir_item.versioned_item_seq;
ALTER TABLE ir_item.versioned_item_seq OWNER TO ir_plus;


-- ---------------------------------------------
-- Version Information
-- Links an item and a versioned item to create
-- a particular instance of a versioned item
-- ---------------------------------------------
CREATE TABLE ir_item.item_version
(
  item_version_id BIGINT PRIMARY KEY,
  item_id BIGINT NOT NULL,
  versioned_item_id BIGINT NOT NULL,
  version_number INTEGER NOT NULL,
  version INTEGER,
  UNIQUE(versioned_item_id, version_number),
  FOREIGN KEY (item_id) REFERENCES ir_item.item (item_id),
  FOREIGN KEY (versioned_item_id) REFERENCES ir_item.versioned_item 

(versioned_item_id) 
);
ALTER TABLE ir_item.item_version OWNER TO ir_plus;

-- The version sequence
CREATE SEQUENCE ir_item.item_version_seq;
ALTER TABLE ir_item.item_version_seq OWNER TO ir_plus;


-- ---------------------------------------------
-- Item File Information
-- Link between items and ir files
-- ---------------------------------------------
CREATE TABLE ir_item.item_file
(
  item_file_id BIGINT PRIMARY KEY,
  ir_file_id BIGINT NOT NULL,
  item_id BIGINT NOT NULL,
  file_order INTEGER NULL,
  version_number INTEGER NULL,
  description TEXT NULL,
  is_public Boolean,
  FOREIGN KEY (item_id) REFERENCES ir_item.item (item_id),
  FOREIGN KEY (ir_file_id) REFERENCES ir_file.ir_file (ir_file_id),
  UNIQUE (ir_file_id, item_id)
); 
ALTER TABLE ir_item.item_file OWNER TO ir_plus;

-- The item sequence
CREATE SEQUENCE ir_item.item_file_seq;
ALTER TABLE ir_item.item_file_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Constraint for link to item file primary image
-- ---------------------------------------------
-- ---------------------------------------------
-- Constraint for release date 
-- ---------------------------------------------
ALTER TABLE ir_item.item ADD CONSTRAINT item_primay_image_item_file_id_fkey FOREIGN 
KEY (primary_image_item_file_id)  REFERENCES ir_item.item_file(item_file_id);

-- ---------------------------------------------
-- Item Links table
-- ---------------------------------------------

CREATE TABLE ir_item.item_link
(
    item_link_id BIGINT NOT NULL PRIMARY KEY,
    name TEXT NOT NULL,
    item_id BIGINT NOT NULL,
    url_value TEXT NOT NULL,
    description TEXT, 
    version INTEGER,
    link_order INTEGER,
    UNIQUE(item_id, name),
    FOREIGN KEY (item_id) REFERENCES ir_item.item (item_id)
);
ALTER TABLE ir_item.item_link OWNER TO ir_plus;

-- The item link sequence
CREATE SEQUENCE ir_item.item_link_seq ;
ALTER TABLE ir_item.item_link_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Item title table
-- this is used for other titles 
-- the main title is currently stored in
-- the item
-- ---------------------------------------------

CREATE TABLE ir_item.item_title
(
    item_title_id BIGINT NOT NULL PRIMARY KEY,
    title TEXT NOT NULL,
    lower_case_title TEXT NOT NULL, 
    title_first_char CHAR NOT NULL,
    leading_articles TEXT,
    version INTEGER,
    item_id BIGINT NOT NULL,
    FOREIGN KEY (item_id) REFERENCES ir_item.item (item_id)
);
ALTER TABLE ir_item.item_title OWNER TO ir_plus;

-- The item link sequence
CREATE SEQUENCE ir_item.item_title_seq ;
ALTER TABLE ir_item.item_title_seq OWNER TO ir_plus;

-- Index on the item Name and lower case name
CREATE INDEX title_lower_case_name_idx ON ir_item.item_title USING btree (lower_case_title);
CREATE INDEX title_name_idx ON ir_item.item_title USING btree (title);

-- Index on the item Name first character
CREATE INDEX item_title_first_char_idx ON ir_item.item_title(title_first_char);


-- ---------------------------------------------
-- Item Identifier table
-- ---------------------------------------------
CREATE TABLE ir_item.item_identifier
(
    item_identifier_id BIGINT PRIMARY KEY,
    item_id BIGINT NOT NULL,
    identifier_type_id BIGINT NOT NULL,
    version INTEGER,
    value TEXT,
    UNIQUE(identifier_type_id, value, item_id ),
    FOREIGN KEY (item_id) REFERENCES ir_item.item (item_id),
    FOREIGN KEY (identifier_type_id) REFERENCES ir_item.identifier_type 

(identifier_type_id) 
);
ALTER TABLE ir_item.item_identifier OWNER TO ir_plus;

-- The item identifier sequence
CREATE SEQUENCE ir_item.item_identifier_seq ;
ALTER TABLE ir_item.item_identifier_seq OWNER TO ir_plus;


-- ---------------------------------------------
-- Item extent table
-- ---------------------------------------------
CREATE TABLE ir_item.item_extent
(
    item_extent_id BIGINT PRIMARY KEY,
    item_id BIGINT NOT NULL,
    extent_type_id BIGINT NOT NULL,
    version INTEGER,
    value TEXT,
    UNIQUE(extent_type_id, value, item_id ),
    FOREIGN KEY (item_id) REFERENCES ir_item.item (item_id),
    FOREIGN KEY (extent_type_id) REFERENCES ir_item.extent_type (extent_type_id) 
);
ALTER TABLE ir_item.item_extent OWNER TO ir_plus;

-- The item extent sequence
CREATE SEQUENCE ir_item.item_extent_seq ;
ALTER TABLE ir_item.item_extent_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Series
-- ---------------------------------------------
CREATE TABLE ir_item.series(
  series_id bigint NOT NULL,
  version integer,
  name text NOT NULL,
  number text,
  description text,
  PRIMARY KEY (series_id),
  UNIQUE (name, number)
) ;
ALTER TABLE ir_item.series OWNER TO ir_plus;

-- The item series sequence
CREATE SEQUENCE ir_item.series_seq ;
ALTER TABLE ir_item.series_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Item series Report number
-- ---------------------------------------------
CREATE TABLE ir_item.item_series_report_number(
  item_series_report_number_id bigint NOT NULL,
  version integer,
  series_id BIGINT NOT null,
  item_id BIGINT NOT NULL,
  report_number text,
  PRIMARY KEY (item_series_report_number_id),
  FOREIGN KEY (series_id)
      REFERENCES ir_item.series (series_id) ,
  FOREIGN KEY (item_id)
      REFERENCES ir_item.item (item_id)
) ;
ALTER TABLE ir_item.item_series_report_number OWNER TO ir_plus;

-- The report sequence
CREATE SEQUENCE ir_item.item_series_report_number_seq ;
ALTER TABLE ir_item.item_series_report_number_seq OWNER TO ir_plus;


-- ----------------------------------------------
-- **********************************************
       
-- IR Reposiotry SCHEMA     

-- **********************************************
-- ----------------------------------------------



-- ---------------------------------------------
-- Create a schema to hold all file system
-- information.
-- ---------------------------------------------

CREATE SCHEMA ir_repository AUTHORIZATION ir_plus;

-- ---------------------------------------------
-- License Information
-- ---------------------------------------------

-- Create a new table to hold repository license information 
CREATE TABLE ir_repository.license
(
  license_id BIGINT PRIMARY KEY,
  name TEXT NOT NULL,
  license_text TEXT,
  description TEXT,
  version INTEGER,
  created_date TIMESTAMP WITH TIME ZONE,
  user_id BIGINT,
  FOREIGN KEY (user_id) REFERENCES ir_user.ir_user(user_id)
);
ALTER TABLE ir_repository.license OWNER TO ir_plus;

-- The repository license sequence
CREATE SEQUENCE ir_repository.license_seq;
ALTER TABLE ir_repository.license_seq OWNER TO ir_plus;



-- ---------------------------------------------
-- item index process record Information
-- ---------------------------------------------

CREATE TABLE ir_repository.institutional_item_index_processing_record
(
  institutional_item_index_processing_record_id BIGINT PRIMARY KEY,
  institutional_item_id BIGINT NOT NULL,
  updated_date TIMESTAMP WITH TIME ZONE NOT NULL,
  index_processing_type_id BIGINT NOT NULL,
  UNIQUE(institutional_item_id, index_processing_type_id),
  FOREIGN KEY (index_processing_type_id)
      REFERENCES ir_index.index_processing_type (index_processing_type_id)
);
ALTER TABLE ir_repository.institutional_item_index_processing_record OWNER TO ir_plus;

-- The repository license sequence
CREATE SEQUENCE ir_repository.institutional_item_index_processing_record_seq;
ALTER TABLE ir_repository.institutional_item_index_processing_record_seq OWNER TO ir_plus;


CREATE INDEX index_processing_record_item_idx ON ir_repository.institutional_item_index_processing_record(institutional_item_id);
-- ---------------------------------------------
-- Versioned license
-- ---------------------------------------------
CREATE TABLE ir_repository.versioned_license
(
    versioned_license_id BIGINT PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT,
    largest_license_version_id INTEGER NOT NULL,
    current_version_id BIGINT,
    version INTEGER
);
ALTER TABLE ir_repository.versioned_license OWNER TO ir_plus;

-- The versioned license sequence
CREATE SEQUENCE ir_repository.versioned_license_seq;
ALTER TABLE ir_repository.versioned_license_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- License Version
-- ---------------------------------------------

-- Create a table to hold repository license version information 
CREATE TABLE ir_repository.license_version
(
  license_version_id BIGINT PRIMARY KEY,
  version_number INT NOT NULL,
  versioned_license_id BIGINT NOT NULL,
  license_id BIGINT NOT NULL,
  version INTEGER,
  FOREIGN KEY (license_id) REFERENCES ir_repository.license(license_id),
  FOREIGN KEY (versioned_license_id) REFERENCES ir_repository.versioned_license(versioned_license_id)
);
ALTER TABLE ir_repository.license_version OWNER TO ir_plus;

-- The repository license sequence
CREATE SEQUENCE ir_repository.license_version_seq;
ALTER TABLE ir_repository.license_version_seq OWNER TO ir_plus;



-- Create a new table to hold repository information in the system
CREATE TABLE ir_repository.repository
(
  repository_id BIGINT PRIMARY KEY,
  name TEXT NOT NULL,
  suspend_subscription_emails BOOLEAN,
  institution_name TEXT,
  file_database_id BIGINT,
  default_license_version_id BIGINT,
  description TEXT,
  version INTEGER,
  name_index_folder TEXT,
  user_index_folder TEXT,
  institutional_item_index_folder TEXT,
  researcher_index_folder TEXT,
  user_workspace_index_folder TEXT,
  institutional_collection_index_folder TEXT,
  user_group_index_folder TEXT,
  default_handle_authority_id BIGINT,
  last_email_subscriber_process_sent_date TIMESTAMP WITH TIME ZONE,
  UNIQUE (name),
  FOREIGN KEY (file_database_id) REFERENCES file_system.file_database (file_database_id),
  FOREIGN KEY (default_license_version_id) REFERENCES ir_repository.license_version (license_version_id)
);
ALTER TABLE ir_repository.repository OWNER TO ir_plus;

-- The repository sequence
CREATE SEQUENCE ir_repository.repository_seq;
ALTER TABLE ir_repository.repository_seq OWNER TO ir_plus;


-- ---------------------------------------------
-- Repository pictures
-- ---------------------------------------------
-- Create a new table to hold repository pictures in the system
CREATE TABLE ir_repository.repository_picture
(
  repository_id BIGINT NOT NULL,
  ir_file_id BIGINT NOT NULL,
  PRIMARY KEY (repository_id, ir_file_id),
  FOREIGN KEY (repository_id) REFERENCES ir_repository.repository (repository_id),
  FOREIGN KEY (ir_file_id) REFERENCES ir_file.ir_file (ir_file_id)

);
ALTER TABLE ir_repository.repository_picture OWNER TO ir_plus;

-- The repository sequence
CREATE SEQUENCE ir_repository.repository_picture_seq;
ALTER TABLE ir_repository.repository_picture_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- this is for users who have accepted the
-- license.
-- ---------------------------------------------
CREATE TABLE ir_repository.user_repository_license
(
    user_repository_license_id BIGINT NOT NULL PRIMARY KEY,
    date_accepted TIMESTAMP WITH TIME ZONE NOT NULL,
    license_version_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    version INTEGER,
    UNIQUE(license_version_id, user_id),
    FOREIGN KEY (user_id) REFERENCES ir_user.ir_user(user_id),
    FOREIGN KEY (license_version_id) REFERENCES ir_repository.license_version(license_version_id)
);

ALTER TABLE ir_repository.user_repository_license OWNER TO ir_plus;

CREATE SEQUENCE ir_repository.user_repository_license_seq;
ALTER TABLE ir_repository.user_repository_license_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- this is for retired repository licenses
-- ---------------------------------------------
CREATE TABLE ir_repository.retired_repository_license
(
    retired_repository_license_id BIGINT NOT NULL PRIMARY KEY,
    date_retired TIMESTAMP WITH TIME ZONE NOT NULL,
    repository_id BIGINT NOT NULL,
    retired_by_user_id BIGINT NOT NULL,
    license_version_id BIGINT NOT NULL,
    FOREIGN KEY (repository_id) REFERENCES ir_repository.repository(repository_id),
    FOREIGN KEY (retired_by_user_id) REFERENCES ir_user.ir_user(user_id),
    FOREIGN KEY (license_version_id) REFERENCES ir_repository.license_version(license_version_id)
);

ALTER TABLE ir_repository.retired_repository_license OWNER TO ir_plus;

CREATE SEQUENCE ir_repository.retired_repository_license_seq;
ALTER TABLE ir_repository.retired_repository_license_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Institutional Collection Information
-- ---------------------------------------------

-- Create a new table to hold collection information in the system
CREATE TABLE ir_repository.institutional_collection
(
  institutional_collection_id BIGINT PRIMARY KEY,
  root_collection_id BIGINT NOT NULL,
  parent_id BIGINT,
  repository_id BIGINT,
  left_value BIGINT,
  right_value BIGINT,
  name TEXT NOT NULL,
  path TEXT NOT NULL,
  description TEXT,
  copyright TEXT,
  primary_picture_id BIGINT,
  publicly_viewable BOOLEAN,
  version INTEGER,
  FOREIGN KEY (primary_picture_id) REFERENCES ir_file.ir_file (ir_file_id),
  FOREIGN KEY (parent_id) 
     REFERENCES ir_repository.institutional_collection (institutional_collection_id),
  FOREIGN KEY (root_collection_id) 
     REFERENCES  ir_repository.institutional_collection (institutional_collection_id),
  FOREIGN KEY (repository_id) REFERENCES ir_repository.repository (repository_id),
  UNIQUE (parent_id, name),
  UNIQUE (repository_id, path, name)
);

ALTER TABLE ir_repository.institutional_collection OWNER TO ir_plus;
CREATE INDEX collection_name_idx ON ir_repository.institutional_collection USING 
btree (name);

CREATE INDEX collection_left_value_idx ON ir_repository.institutional_collection(left_value);
CREATE INDEX collection_right_value_idx ON ir_repository.institutional_collection(right_value);
CREATE INDEX collection_tree_root_idx ON ir_repository.institutional_collection(root_collection_id);

-- The collection sequence
CREATE SEQUENCE ir_repository.institutional_collection_seq ;
ALTER TABLE ir_repository.institutional_collection_seq OWNER TO ir_plus;


-- ---------------------------------------------
-- Institutional Collection Links
-- ---------------------------------------------
CREATE TABLE ir_repository.institutional_collection_link
(
  institutional_collection_link_id BIGINT PRIMARY KEY,
  institutional_collection_id BIGINT NOT NULL,
  name TEXT NOT NULL,
  url TEXT NOT NULL,
  order_value INTEGER NOT NULL,
  description TEXT,
  version INTEGER,
  FOREIGN KEY (institutional_collection_id) REFERENCES 
     ir_repository.institutional_collection (institutional_collection_id)
);
ALTER TABLE ir_repository.institutional_collection_link OWNER TO ir_plus;


-- The collection link sequence
CREATE SEQUENCE ir_repository.institutional_collection_link_seq ;
ALTER TABLE ir_repository.institutional_collection_link_seq OWNER TO ir_plus;


-- ---------------------------------------------
-- Institutional Collection Picture Information
-- ---------------------------------------------
-- Create a new table to hold institutional 
-- collection pictures in the system
CREATE TABLE ir_repository.institutional_collection_picture
(
  institutional_collection_id BIGINT NOT NULL,
  ir_file_id BIGINT NOT NULL,
  PRIMARY KEY (institutional_collection_id, ir_file_id),
  FOREIGN KEY (institutional_collection_id) REFERENCES 
     ir_repository.institutional_collection (institutional_collection_id),
  FOREIGN KEY (ir_file_id) REFERENCES ir_file.ir_file (ir_file_id)
);
ALTER TABLE ir_repository.institutional_collection_picture OWNER TO ir_plus;



-- ---------------------------------------------
-- Institutional Collection subscription
-- ---------------------------------------------
-- Create a new table to hold institutional 
-- collection subscribers
CREATE TABLE ir_repository.institutional_collection_subscription
(
  institutional_collection_subscription_id BIGINT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  subscription_start_date DATE NOT NULL,
  institutional_collection_id BIGINT NOT NULL,
  version INTEGER,
  UNIQUE(institutional_collection_id, user_id),
  FOREIGN KEY (institutional_collection_id) REFERENCES 
     ir_repository.institutional_collection (institutional_collection_id),
  FOREIGN KEY (user_id) REFERENCES ir_user.ir_user (user_id)
);
ALTER TABLE ir_repository.institutional_collection_subscription OWNER TO ir_plus;

-- The item sequence
CREATE SEQUENCE ir_repository.institutional_collection_subscription_seq;
ALTER TABLE ir_repository.institutional_collection_subscription_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Versioned institutional Item Information
-- ---------------------------------------------
CREATE TABLE ir_repository.versioned_institutional_item
(
    versioned_institutional_item_id BIGINT PRIMARY KEY,
    largest_item_version_id INTEGER NOT NULL,
    current_institutional_item_version_id BIGINT,
    version INTEGER
);
ALTER TABLE ir_repository.versioned_institutional_item OWNER TO ir_plus;

-- The versioned ir file sequence
CREATE SEQUENCE ir_repository.versioned_institutional_item_seq;
ALTER TABLE ir_repository.versioned_institutional_item_seq OWNER TO ir_plus;


-- ---------------------------------------------
-- Institutional Items
-- ---------------------------------------------
CREATE TABLE ir_repository.institutional_item
(
    institutional_item_id BIGINT PRIMARY KEY,
    institutional_collection_id BIGINT NOT NULL,
    versioned_institutional_item_id BIGINT NOT NULL,
    version INTEGER,
    user_id BIGINT,
    FOREIGN KEY (institutional_collection_id) 
       REFERENCES ir_repository.institutional_collection(institutional_collection_id),
    FOREIGN KEY (versioned_institutional_item_id) 
       REFERENCES ir_repository.versioned_institutional_item(versioned_institutional_item_id),
    UNIQUE(institutional_collection_id, versioned_institutional_item_id),
    FOREIGN KEY (user_id) REFERENCES ir_user.ir_user(user_id)
);
ALTER TABLE ir_repository.institutional_item OWNER TO ir_plus;

-- The ir file sequence
CREATE SEQUENCE ir_repository.institutional_item_seq;
ALTER TABLE ir_repository.institutional_item_seq OWNER TO ir_plus;


-- ---------------------------------------------
-- Deleted Institutional Item
-- ---------------------------------------------
CREATE TABLE ir_repository.deleted_institutional_item
(
	deleted_institutional_item_id BIGINT PRIMARY KEY,
    institutional_item_id BIGINT NOT NULL,
    institutional_collection_name TEXT,
    institutional_collection_id BIGINT NOT NULL,
    institutional_item_name TEXT,
    user_id BIGINT NOT NULL,
    deleted_date TIMESTAMP WITH TIME ZONE NOT NULL ,
    version INTEGER,
    FOREIGN KEY (user_id) REFERENCES ir_user.ir_user(user_id)
);
ALTER TABLE ir_repository.deleted_institutional_item OWNER TO ir_plus;

-- The deleted institutional item seq
CREATE SEQUENCE ir_repository.deleted_institutional_item_seq;
ALTER TABLE ir_repository.deleted_institutional_item_seq OWNER TO ir_plus;

-- Index on the deleted date
CREATE INDEX institutional_item_deleted_date_idx ON ir_repository.deleted_institutional_item(deleted_date);

-- Index on the collection id
CREATE INDEX institutional_item_deleted_collection_id_idx ON ir_repository.deleted_institutional_item(institutional_collection_id);

-- ---------------------------------------------
-- Deleted Institutional Item Version
-- ---------------------------------------------
CREATE TABLE ir_repository.deleted_institutional_item_version
(
	deleted_institutional_item_version_id BIGINT PRIMARY KEY,
	deleted_institutional_item_id BIGINT NOT NULL,
    institutional_item_version_id BIGINT NOT NULL,
    handle_info_id BIGINT,
    version_number INTEGER NOT NULL,
    version INTEGER,
    FOREIGN KEY (deleted_institutional_item_id) REFERENCES ir_repository.deleted_institutional_item(deleted_institutional_item_id)
);
ALTER TABLE ir_repository.deleted_institutional_item_version OWNER TO ir_plus;

-- The deleted institutional item seq
CREATE SEQUENCE ir_repository.deleted_institutional_item_version_seq;
ALTER TABLE ir_repository.deleted_institutional_item_version_seq OWNER TO ir_plus;



-- ---------------------------------------------
-- Institutional item version
-- ---------------------------------------------
CREATE TABLE ir_repository.institutional_item_version
(
    institutional_item_version_id BIGINT PRIMARY KEY,
    item_id BIGINT NOT NULL,
    versioned_institutional_item_id BIGINT NOT NULL,
    institutional_item_repository_license_id BIGINT,
    withdrawn_token_id BIGINT,
    version_number int,
    handle_info_id BIGINT,
    date_of_deposit TIMESTAMP WITH TIME ZONE NOT NULL,
    date_modified TIMESTAMP WITH TIME ZONE NOT NULL,
    modified_by_user_id BIGINT,
    modification_note TEXT,
    version INTEGER,
    FOREIGN KEY (versioned_institutional_item_id) 
       REFERENCES ir_repository.versioned_institutional_item (versioned_institutional_item_id),
    FOREIGN KEY (item_id) REFERENCES ir_item.item(item_id),
    FOREIGN KEY (handle_info_id) REFERENCES handle.handle_info(handle_id),
    FOREIGN KEY (modified_by_user_id) REFERENCES ir_user.ir_user(user_id)
);
ALTER TABLE ir_repository.institutional_item_version OWNER TO ir_plus;

-- The ir file sequence
CREATE SEQUENCE ir_repository.institutional_item_version_seq;
ALTER TABLE ir_repository.institutional_item_version_seq OWNER TO ir_plus;

-- Index on the file date of deposit
CREATE INDEX institutional_item_version_deposit_date_idx ON ir_repository.institutional_item_version USING btree 
(date_of_deposit);

-- Index on the file date modified
CREATE INDEX institutional_item_version_date_modified_idx ON ir_repository.institutional_item_version(date_modified);

-- ---------------------------------------------
-- Institutional Item repository License
-- ---------------------------------------------
CREATE TABLE ir_repository.institutional_item_repository_license
(
    institutional_item_repository_license_id BIGINT PRIMARY KEY,
    institutional_item_version_id BIGINT NOT NULL,
    license_version_id BIGINT NOT NULL,
    date_granted TIMESTAMP WITH TIME ZONE NOT NULL,
    granted_by_user_id BIGINT NOT NULL,
    version INTEGER,
    FOREIGN KEY (granted_by_user_id) REFERENCES ir_user.ir_user (user_id),
    FOREIGN KEY (license_version_id) REFERENCES ir_repository.license_version(license_version_id),
    FOREIGN KEY (institutional_item_version_id) 
       REFERENCES ir_repository.institutional_item_version (institutional_item_version_id)
);

ALTER TABLE ir_repository.institutional_item_repository_license OWNER TO ir_plus;

-- The institutional item repository license sequence
CREATE SEQUENCE ir_repository.institutional_item_repository_license_seq;
ALTER TABLE ir_repository.institutional_item_repository_license_seq OWNER TO ir_plus;
-- ---------------------------------------------
-- Withdrawn token
-- ---------------------------------------------
CREATE TABLE ir_repository.withdrawn_token
(
    withdrawn_token_id BIGINT PRIMARY KEY,
    institutional_item_version_id BIGINT NOT NULL,
    date_withdrawn DATE,
    show_metadata BOOLEAN,
    withdrawn_reason text,
    user_id BIGINT NOT NULL,
    version INTEGER,
    FOREIGN KEY (user_id) REFERENCES ir_user.ir_user (user_id),
    FOREIGN KEY (institutional_item_version_id) 
       REFERENCES ir_repository.institutional_item_version (institutional_item_version_id)
);
ALTER TABLE ir_repository.withdrawn_token OWNER TO ir_plus;


CREATE SEQUENCE ir_repository.withdrawn_token_seq;
ALTER TABLE ir_repository.withdrawn_token_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Re-Instate token
-- ---------------------------------------------
CREATE TABLE ir_repository.reinstate_token
(
    reinstate_token_id BIGINT PRIMARY KEY,
    institutional_item_version_id BIGINT NOT NULL,
    date_reinstated DATE,
    reinstate_reason text,
    user_id BIGINT NOT NULL,
    version INTEGER,
    FOREIGN KEY (user_id) REFERENCES ir_user.ir_user (user_id),
    FOREIGN KEY (institutional_item_version_id) 
       REFERENCES ir_repository.institutional_item_version (institutional_item_version_id)
);
ALTER TABLE ir_repository.reinstate_token OWNER TO ir_plus;


CREATE SEQUENCE ir_repository.reinstate_token_seq;
ALTER TABLE ir_repository.reinstate_token_seq OWNER TO ir_plus;



-- ---------------------------------------------
-- Add a contstraint back on the institutional_item_version
-- table
-- ---------------------------------------------
ALTER TABLE ir_repository.institutional_item_version ADD CONSTRAINT institutional_item_version_withdrawn_token_id_fkey FOREIGN KEY 
(withdrawn_token_id)  REFERENCES ir_repository.withdrawn_token(withdrawn_token_id);


-- ---------------------------------------------
-- Reviewable Items
-- ---------------------------------------------
CREATE TABLE ir_repository.reviewable_item
(
    reviewable_item_id BIGINT PRIMARY KEY,
    institutional_collection_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    version INTEGER,
    date_reviewed TIMESTAMP WITH TIME ZONE,
    rejection_reason text,
    user_id BIGINT,
    review_status text,
    FOREIGN KEY (institutional_collection_id) 
       REFERENCES ir_repository.institutional_collection(institutional_collection_id) ,
    FOREIGN KEY (item_id) REFERENCES ir_item.item(item_id),
    FOREIGN KEY (user_id) REFERENCES ir_user.ir_user(user_id)
);
ALTER TABLE ir_repository.reviewable_item OWNER TO ir_plus;

-- The reviewable item sequence
CREATE SEQUENCE ir_repository.reviewable_item_seq;
ALTER TABLE ir_repository.reviewable_item_seq OWNER TO ir_plus;




-- ----------------------------------------------
-- **********************************************
       
-- FEDORA SCHEMA     

-- **********************************************
-- ----------------------------------------------






-- ---------------------------------------------
-- Create a schema to hold all fedora file system
-- information.
-- ---------------------------------------------

CREATE SCHEMA fedora_file_system AUTHORIZATION ir_plus;

-- ---------------------------------------------
-- Sequence for naming files and folders on the file
-- system
-- ---------------------------------------------

CREATE SEQUENCE fedora_file_system.file_system_name_seq; 
ALTER TABLE fedora_file_system.file_system_name_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- File Server information
-- ---------------------------------------------

-- Create a new table to hold folder information in the system
CREATE TABLE fedora_file_system.file_server
(
  file_server_id BIGINT PRIMARY KEY,
  name TEXT UNIQUE NOT NULL,
  description TEXT,
  version INTEGER
);
ALTER TABLE fedora_file_system.file_server OWNER TO ir_plus;

-- The folder name sequence
CREATE SEQUENCE fedora_file_system.file_server_seq;
ALTER TABLE fedora_file_system.file_server_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Fedora File Database Information
-- ---------------------------------------------

-- Create a new table to hold database information in the system
CREATE TABLE fedora_file_system.file_database
(
  file_database_id BIGINT PRIMARY KEY,
  file_server_id BIGINT NOT NULL,
  name TEXT NOT NULL,
  display_name TEXT,
  description TEXT,
  base_url TEXT,
  upload_url TEXT, 		
  admin_user_name TEXT,			
  admin_password TEXT,
  uri_prefix TEXT,
  default_log_message TEXT,
  version INTEGER,
  FOREIGN KEY(file_server_id) REFERENCES fedora_file_system.file_server 

(file_server_id),
  UNIQUE (name, file_server_id)
);
ALTER TABLE fedora_file_system.file_database OWNER TO ir_plus;

-- The folder name sequence
CREATE SEQUENCE fedora_file_system.file_database_seq;
ALTER TABLE fedora_file_system.file_database_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Datastream Information
-- ---------------------------------------------

-- Create a new table to hold datastream information in the system
CREATE TABLE fedora_file_system.datastream_info
(
  datastream_info_id BIGINT NOT NULL PRIMARY KEY,
  fedora_state TEXT,
  pid TEXT NOT NULL,
  external_datastream_id TEXT,
  datastream_label TEXT,
  upload_url TEXT,
  versionable BOOLEAN,
  mime_type TEXT,
  format_uri TEXT,
  datastream_location TEXT,
  fedora_control_group TEXT,
  checksum_type TEXT,
  checksum TEXT,
  log_message TEXT,
  version INTEGER,
  UNIQUE (pid)
);
ALTER TABLE fedora_file_system.datastream_info OWNER TO ir_plus;

-- The file sequence
CREATE SEQUENCE fedora_file_system.datastream_info_seq;
ALTER TABLE fedora_file_system.datastream_info_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Alternate Id 
-- ---------------------------------------------

-- Create a new table to hold alternate ids for fedora datastreams
CREATE TABLE fedora_file_system.alternate_id
(
  alternate_id BIGINT NOT NULL PRIMARY KEY,
  datastream_info_id BIGINT NOT NULL,
  id_value TEXT NOT NULL,
  version INTEGER,
  FOREIGN KEY (datastream_info_id) REFERENCES fedora_file_system.datastream_info 

(datastream_info_id)
);
ALTER TABLE fedora_file_system.alternate_id OWNER TO ir_plus;

-- The file sequence
CREATE SEQUENCE fedora_file_system.alternate_id_seq;
ALTER TABLE fedora_file_system.alternate_id_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- File Information
-- ---------------------------------------------

-- Create a new table to hold file information in the system
CREATE TABLE fedora_file_system.file
(
  file_id BIGINT NOT NULL PRIMARY KEY,
  datastream_info_id BIGINT,
  file_database_id BIGINT NOT NULL,
  file_name TEXT NOT NULL,
  path TEXT not NULL,
  size BIGINT,
  created_date TIMESTAMP WITH TIME ZONE,
  extension VARCHAR(10),
  modified_date TIMESTAMP WITH TIME ZONE,
  display_name TEXT,
  description TEXT,
  version INTEGER,
  FOREIGN KEY (file_database_id) REFERENCES fedora_file_system.file_database 

(file_database_id),
  FOREIGN KEY (datastream_info_id) REFERENCES fedora_file_system.datastream_info 

(datastream_info_id),
  UNIQUE (file_name)
);
ALTER TABLE fedora_file_system.file OWNER TO ir_plus;

-- Index on the file Name
CREATE INDEX fedora_file_display_name_idx ON fedora_file_system.file USING btree 

(display_name);

-- The file sequence
CREATE SEQUENCE fedora_file_system.file_seq;
ALTER TABLE fedora_file_system.file_seq OWNER TO ir_plus;










-- ----------------------------------------------
-- **********************************************
       
-- Complete the USER SCHEMA     

-- **********************************************
-- ----------------------------------------------






-- ---------------------------------------------
-- Personal Folder Information
-- ---------------------------------------------

-- Create a new table to hold personal folder information in the system
CREATE TABLE ir_user.personal_folder
(
  personal_folder_id BIGINT PRIMARY KEY,
  root_personal_folder_id BIGINT NOT NULL,
  parent_id BIGINT,
  user_id BIGINT NOT NULL,
  left_value BIGINT,
  right_value BIGINT,
  name TEXT NOT NULL,
  path TEXT NOT NULL,
  description TEXT,
  version INTEGER,
  FOREIGN KEY (parent_id) REFERENCES ir_user.personal_folder (personal_folder_id),
  FOREIGN KEY (root_personal_folder_id) REFERENCES ir_user.personal_folder 

(personal_folder_id),
  FOREIGN KEY (user_id) REFERENCES ir_user.ir_user (user_id),
  UNIQUE (parent_id, name),
  UNIQUE (user_id, path, name)
);
ALTER TABLE ir_user.personal_folder OWNER TO ir_plus;

CREATE INDEX personal_folder_idx ON ir_user.personal_folder USING btree (name);

-- The collection sequence
CREATE SEQUENCE ir_user.personal_folder_seq ;
ALTER TABLE ir_user.personal_folder_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Personal file Information
-- ---------------------------------------------
CREATE TABLE ir_user.personal_file
(
    personal_file_id BIGINT PRIMARY KEY,
    personal_folder_id BIGINT,
    user_id BIGINT NOT NULL,
    versioned_file_id BIGINT NOT NULL,
    version INTEGER,
    FOREIGN KEY (personal_folder_id) REFERENCES ir_user.personal_folder (personal_folder_id),
    FOREIGN KEY (versioned_file_id) REFERENCES ir_file.versioned_file (versioned_file_id),
    FOREIGN KEY (user_id) REFERENCES ir_user.ir_user (user_id),
    UNIQUE(user_id, personal_folder_id, versioned_file_id)
);
ALTER TABLE ir_user.personal_file OWNER TO ir_plus;

-- The ir file sequence
CREATE SEQUENCE ir_user.personal_file_seq;
ALTER TABLE ir_user.personal_file_seq OWNER TO ir_plus;


-- ---------------------------------------------
-- Personal Collection Information
-- ---------------------------------------------

-- Create a new table to hold personal collection information in the system
CREATE TABLE ir_user.personal_collection
(
  personal_collection_id BIGINT PRIMARY KEY,
  root_personal_collection_id BIGINT NOT NULL,
  parent_id BIGINT,
  user_id BIGINT NOT NULL,
  left_value BIGINT,
  right_value BIGINT,
  name TEXT NOT NULL,
  path TEXT NOT NULL,
  description TEXT,
  version INTEGER,
  FOREIGN KEY (parent_id) REFERENCES ir_user.personal_collection (personal_collection_id),
  FOREIGN KEY (root_personal_collection_id) REFERENCES ir_user.personal_collection (personal_collection_id),
  FOREIGN KEY (user_id) REFERENCES ir_user.ir_user (user_id),
  UNIQUE (parent_id, name),
  UNIQUE (user_id, path, name)
);
ALTER TABLE ir_user.personal_collection OWNER TO ir_plus;

CREATE INDEX personal_collection_idx ON ir_user.personal_collection USING btree (name);

-- The collection sequence
CREATE SEQUENCE ir_user.personal_collection_seq ;
ALTER TABLE ir_user.personal_collection_seq OWNER TO ir_plus;


-- ---------------------------------------------
-- Personal Information
-- ---------------------------------------------
CREATE TABLE ir_user.personal_item
(
    personal_item_id BIGINT PRIMARY KEY,
    personal_collection_id BIGINT,
    user_id BIGINT NOT NULL,
    versioned_item_id BIGINT NOT NULL,
    version INTEGER,
    FOREIGN KEY (personal_collection_id) REFERENCES ir_user.personal_collection (personal_collection_id),
    FOREIGN KEY (versioned_item_id) REFERENCES ir_item.versioned_item (versioned_item_id),
    FOREIGN KEY (user_id) REFERENCES ir_user.ir_user (user_id),
    UNIQUE(user_id, personal_collection_id, versioned_item_id)
);
ALTER TABLE ir_user.personal_item OWNER TO ir_plus;

-- The ir file sequence
CREATE SEQUENCE ir_user.personal_item_seq;
ALTER TABLE ir_user.personal_item_seq OWNER TO ir_plus;



-- ---------------------------------------------
-- User Email Information
-- ---------------------------------------------

CREATE TABLE ir_user.user_email
(
  user_email_id BIGINT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  version INTEGER,
  email TEXT NOT NULL,
  lower_case_email TEXT NOT NULL,
  isVerified BOOLEAN NOT NULL,
  token TEXT,
  FOREIGN KEY (user_id) REFERENCES ir_user.ir_user (user_id),
  UNIQUE (user_id, user_email_id),
  UNIQUE (email),
  UNIQUE (lower_case_email)
) ; 
ALTER TABLE ir_user.user_email OWNER TO ir_plus;


-- The ir file sequence
CREATE SEQUENCE ir_user.user_email_seq;
ALTER TABLE ir_user.user_email_seq OWNER TO ir_plus;



-- ---------------------------------------------
-- Add constraint to ir_user.user for user_email
-- ---------------------------------------------
ALTER TABLE ir_user.ir_user ADD CONSTRAINT user_default_email_id_fkey FOREIGN KEY 
(default_email_id)  REFERENCES ir_user.user_email(user_email_id);

-- ---------------------------------------------
-- Insert data for ir_user.ir_user administrator
-- the password will be admin
-- ---------------------------------------------
insert into 
ir_user.ir_user ( user_id, user_password, password_encoding, default_email_id, username, 
first_name, lower_case_first_name, last_name, lower_case_last_name, 
version, account_expired, account_locked, credentials_expired, 
force_change_password, affiliation_approved, self_registered, created_date, re_build_user_workspace_index)  
values (nextval('ir_user.ir_user_seq'), 
      'd033e22ae348aeb5660fc2140aec35850c4da997', 'SHA-1', null, 'admin', 'System', 
      'system', 'Admin', 'admin', 0, false, 

false, false, false, true, false, date(now()), false);

insert into ir_user.user_email(user_email_id, version, email, lower_case_email, user_id, isVerified) values 

(nextval('ir_user.user_email_seq'), 1, 'test@abc.com', 'test@abc.com',

currval('ir_user.ir_user_seq'), true);

update ir_user.ir_user set default_email_id = currval('ir_user.user_email_seq') where 

user_id = currval('ir_user.ir_user_seq');



-- ---------------------------------------------
-- Invite Information
-- ---------------------------------------------



CREATE TABLE ir_user.invite_info
(
  invite_info_id BIGINT PRIMARY KEY,
  version INTEGER,
  token TEXT NOT NULL,
  email TEXT NOT NULL,
  user_id BIGINT NOT NULL,
  created_date TIMESTAMP WITH TIME ZONE NOT NULL,
  FOREIGN KEY (user_id) REFERENCES ir_user.ir_user (user_id) 
);
ALTER TABLE ir_user.invite_info OWNER TO ir_plus;

-- The ir file sequence
CREATE SEQUENCE ir_user.invite_info_seq;
ALTER TABLE ir_user.invite_info_seq OWNER TO ir_plus;


-- ---------------------------------------------
-- Invite files
-- ---------------------------------------------

CREATE TABLE ir_user.invite_files
(
    invite_info_id BIGINT NOT NULL, 
    versioned_file_id BIGINT NOT NULL,
    PRIMARY KEY (invite_info_id, versioned_file_id),
    FOREIGN KEY (invite_info_id) REFERENCES ir_user.invite_info(invite_info_id),
    FOREIGN KEY (versioned_file_id) REFERENCES ir_file.versioned_file(versioned_file_id)
);
ALTER TABLE ir_user.invite_files OWNER TO ir_plus;

-- ---------------------------------------------
-- Shared inbox table
-- represents a file shared with another user
-- ---------------------------------------------

CREATE TABLE ir_user.shared_inbox_file
(
  shared_inbox_file_id BIGINT PRIMARY KEY,
  shared_with_user_id BIGINT NOT NULL,
  sharing_user_id BIGINT NOT NULL,
  versioned_file_id BIGINT NOT NULL,
  version INTEGER,
  UNIQUE(shared_with_user_id, versioned_file_id)
); 

ALTER TABLE ir_user.shared_inbox_file OWNER TO ir_plus;

-- The share file sequence
CREATE SEQUENCE ir_user.shared_inbox_file_seq ;
ALTER TABLE ir_user.shared_inbox_file_seq OWNER TO ir_plus;


-- ---------------------------------------------
-- Invite info for folder data
-- ---------------------------------------------

CREATE TABLE ir_user.folder_invite_info
(
  folder_invite_info_id BIGINT PRIMARY KEY,
  version INTEGER,
  email TEXT NOT NULL,
  personal_folder_id BIGINT NOT NULL,
  created_date TIMESTAMP WITH TIME ZONE NOT NULL,
  FOREIGN KEY (personal_folder_id) REFERENCES ir_user.personal_folder (personal_folder_id) 
);
ALTER TABLE ir_user.folder_invite_info OWNER TO ir_plus;

-- The folder invite info sequence
CREATE SEQUENCE ir_user.folder_invite_info_seq;
ALTER TABLE ir_user.folder_invite_info_seq OWNER TO ir_plus;



-- ----------------------------------------------
-- **********************************************
       
-- SECURITY SCHEMA     

-- **********************************************
-- ----------------------------------------------





-- ---------------------------------------------
-- Create a schema to hold security information
-- ---------------------------------------------

CREATE SCHEMA ir_security AUTHORIZATION ir_plus;

-- ---------------------------------------------
-- Class type table
-- ---------------------------------------------

CREATE TABLE ir_security.class_type
(
    class_type_id BIGINT PRIMARY KEY, 
    name TEXT,
    description TEXT,
    version INTEGER,
    UNIQUE (name)
) ;
ALTER TABLE ir_security.class_type OWNER TO ir_plus;

-- The class type sequence
CREATE SEQUENCE ir_security.class_type_seq ;
ALTER TABLE ir_security.class_type_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Security information 
-- ---------------------------------------------

insert into ir_security.class_type(class_type_id, name , description , version) 
values (nextval('ir_security.class_type_seq'), 'edu.ur.ir.file.VersionedFile', 
'Versioned file',1);

insert into ir_security.class_type(class_type_id, name , description , version) 
values (nextval('ir_security.class_type_seq'), 'edu.ur.ir.institution.InstitutionalCollection', 
'Institutional Collection',1);

insert into ir_security.class_type(class_type_id, name , description , version) 
values (nextval('ir_security.class_type_seq'), 'edu.ur.ir.item.GenericItem', 
'Generic Item',1);

insert into ir_security.class_type(class_type_id, name , description , version) 
values (nextval('ir_security.class_type_seq'), 'edu.ur.ir.item.ItemFile', 
'Item File',1);

-- ---------------------------------------------
-- Class type permission
-- ---------------------------------------------
CREATE TABLE ir_security.class_type_permission
(
    class_type_permission_id BIGINT PRIMARY KEY,
    class_type_id BIGINT NOT NULL, 
    name TEXT,
    description TEXT,
    version INTEGER,
    UNIQUE (class_type_id, name),
    FOREIGN KEY (class_type_id) REFERENCES ir_security.class_type(class_type_id)
) ;
ALTER TABLE ir_security.class_type_permission OWNER TO ir_plus;

-- The class type sequence
CREATE SEQUENCE ir_security.class_type_permission_seq ;
ALTER TABLE ir_security.class_type_permission_seq OWNER TO ir_plus;


-- ---------------------------------------------
-- Invite permission
-- ---------------------------------------------

CREATE TABLE ir_user.invite_permissions
(
    invite_info_id BIGINT NOT NULL, 
    class_type_permission_id BIGINT NOT NULL,
    PRIMARY KEY (invite_info_id, class_type_permission_id),
    FOREIGN KEY (invite_info_id) REFERENCES ir_user.invite_info(invite_info_id),
    FOREIGN KEY (class_type_permission_id) REFERENCES ir_security.class_type_permission(class_type_permission_id)
);
ALTER TABLE ir_user.invite_permissions OWNER TO ir_plus;


-- ---------------------------------------------
-- Folder invite permission
-- ---------------------------------------------

CREATE TABLE ir_user.folder_invite_permissions
(
    folder_invite_info_id BIGINT NOT NULL, 
    class_type_permission_id BIGINT NOT NULL,
    PRIMARY KEY (folder_invite_info_id, class_type_permission_id),
    FOREIGN KEY (folder_invite_info_id) REFERENCES ir_user.folder_invite_info(folder_invite_info_id),
    FOREIGN KEY (class_type_permission_id) REFERENCES ir_security.class_type_permission(class_type_permission_id)
);
ALTER TABLE ir_user.folder_invite_permissions OWNER TO ir_plus;

-- ---------------------------------------------
-- Auto share information
-- ---------------------------------------------
CREATE TABLE ir_user.folder_auto_share_info
(
    folder_auto_share_info_id BIGINT PRIMARY KEY,
    personal_folder_id BIGINT NOT NULL REFERENCES ir_user.personal_folder(personal_folder_id),
    user_id BIGINT NOT NULL REFERENCES ir_user.ir_user(user_id),
    created_date TIMESTAMP WITH TIME ZONE NOT NULL,
    version INTEGER,
    UNIQUE(personal_folder_id, user_id)
);
ALTER TABLE ir_user.folder_auto_share_info OWNER TO ir_plus;

-- The auto share sequence
CREATE SEQUENCE ir_user.folder_auto_share_info_seq ;
ALTER TABLE ir_user.folder_auto_share_info_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Auto share folder permissions 
-- ---------------------------------------------

CREATE TABLE ir_user.folder_auto_share_permissions
(
    folder_auto_share_info_id BIGINT NOT NULL REFERENCES ir_user.folder_auto_share_info(folder_auto_share_info_id), 
    class_type_permission_id BIGINT NOT NULL REFERENCES ir_security.class_type_permission(class_type_permission_id),
    PRIMARY KEY (folder_auto_share_info_id, class_type_permission_id)
);
ALTER TABLE ir_user.folder_auto_share_permissions OWNER TO ir_plus;


-- ---------------------------------------------
-- Insert values for Class type permission
-- ---------------------------------------------

-- versioned file permissions
insert into ir_security.class_type_permission select 
nextval('ir_security.class_type_permission_seq'), 
  ir_security.class_type.class_type_id, 'VIEW','The user can download the file',0
  from ir_security.class_type where ir_security.class_type.name = 
'edu.ur.ir.file.VersionedFile';

insert into ir_security.class_type_permission select 
nextval('ir_security.class_type_permission_seq'), 
  ir_security.class_type.class_type_id, 'EDIT','The user can download, edit and upload new 
versions',0
  from ir_security.class_type where ir_security.class_type.name = 
'edu.ur.ir.file.VersionedFile';

insert into ir_security.class_type_permission select 
nextval('ir_security.class_type_permission_seq'),
  ir_security.class_type.class_type_id, 'SHARE','The user can download, edit and upload new 
versions as well as share/unshare the file with other users and give those users permissions',0
  from ir_security.class_type where ir_security.class_type.name = 
'edu.ur.ir.file.VersionedFile';

-- ------------------------------------
-- institutional colleciton permissions
-- ------------------------------------

insert into ir_security.class_type_permission select 
nextval('ir_security.class_type_permission_seq'), 
  ir_security.class_type.class_type_id, 
   'ADMINISTRATION',
  'The user can administer the institutional collection',
  0
  from ir_security.class_type where ir_security.class_type.name = 
  'edu.ur.ir.institution.InstitutionalCollection';

insert into ir_security.class_type_permission select 
nextval('ir_security.class_type_permission_seq'), 
  ir_security.class_type.class_type_id, 
   'DIRECT_SUBMIT',
  'The user can submit directly to the institutional collection',
  0
  from ir_security.class_type where ir_security.class_type.name = 
'edu.ur.ir.institution.InstitutionalCollection';

insert into ir_security.class_type_permission select 
   nextval('ir_security.class_type_permission_seq'),
  ir_security.class_type.class_type_id, 
  'REVIEW_SUBMIT',
  'The user can submit to a collection but the publication must be reviewed first',
  0
  from ir_security.class_type where ir_security.class_type.name = 
  'edu.ur.ir.institution.InstitutionalCollection';

insert into ir_security.class_type_permission select 
   nextval('ir_security.class_type_permission_seq'),
  ir_security.class_type.class_type_id, 
  'VIEW',
  'The user can view a collection - this is only needed for private collections',
  0
  from ir_security.class_type where ir_security.class_type.name = 
  'edu.ur.ir.institution.InstitutionalCollection';

insert into ir_security.class_type_permission select 
   nextval('ir_security.class_type_permission_seq'),
  ir_security.class_type.class_type_id, 
  'REVIEWER',
  'The user can review submitted publications',
  0
  from ir_security.class_type where ir_security.class_type.name = 
  'edu.ur.ir.institution.InstitutionalCollection';


-- ------------------------------------
-- institutional colleciton permissions
-- ------------------------------------

insert into ir_security.class_type_permission select 
nextval('ir_security.class_type_permission_seq'), 
  ir_security.class_type.class_type_id, 
   'ITEM_METADATA_READ',
  'The user can view the item metadata',
  0
  from ir_security.class_type where ir_security.class_type.name = 
  'edu.ur.ir.item.GenericItem';

insert into ir_security.class_type_permission select 
nextval('ir_security.class_type_permission_seq'), 
  ir_security.class_type.class_type_id, 
   'ITEM_METADATA_EDIT',
  'The user can edit the item metadata',
  0
  from ir_security.class_type where ir_security.class_type.name = 
'edu.ur.ir.item.GenericItem';

insert into ir_security.class_type_permission select 
nextval('ir_security.class_type_permission_seq'), 
  ir_security.class_type.class_type_id, 
   'ITEM_FILE_EDIT',
  'The user can add, edit, remove files from the item',
  0
  from ir_security.class_type where ir_security.class_type.name = 
'edu.ur.ir.item.GenericItem';

insert into ir_security.class_type_permission select 
nextval('ir_security.class_type_permission_seq'), 
  ir_security.class_type.class_type_id, 
   'ITEM_FILE_READ',
  'The user can view the file in the item',
  0
  from ir_security.class_type where ir_security.class_type.name = 
  'edu.ur.ir.item.ItemFile';

-- ---------------------------------------------
-- Acess control list table.
-- ---------------------------------------------
CREATE TABLE ir_security.acl
(
    acl_id BIGINT PRIMARY KEY,
    parent_id BIGINT, 
    entries_inheriting boolean,
   class_type_id BIGINT NOT NULL,
   object_id BIGINT NOT NULL,
    version INTEGER,
    UNIQUE (object_id, class_type_id),
    FOREIGN KEY (parent_id) REFERENCES ir_security.acl(acl_id),
   FOREIGN KEY (class_type_id) REFERENCES ir_security.class_type(class_type_id)

);
ALTER TABLE ir_security.acl OWNER TO ir_plus;

-- The object identity sequence
CREATE SEQUENCE ir_security.acl_seq ;
ALTER TABLE ir_security.acl_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- User Acess control list table.
-- ---------------------------------------------
CREATE TABLE ir_security.user_control_entry
(
    user_control_entry_id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    acl_id BIGINT NOT NULL,
    version INTEGER,
    UNIQUE (user_id , acl_id),
    FOREIGN KEY (user_id) REFERENCES ir_user.ir_user(user_id),
    FOREIGN KEY (acl_id) REFERENCES ir_security.acl(acl_id)
);
ALTER TABLE ir_security.user_control_entry OWNER TO ir_plus;

-- The object identity sequence
CREATE SEQUENCE ir_security.user_control_entry_seq ;
ALTER TABLE ir_security.user_control_entry_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Permissions for the user control entries
-- ---------------------------------------------
CREATE TABLE ir_security.user_control_entry_permission
(
    user_control_entry_id BIGINT NOT NULL,
    class_type_permission_id BIGINT NOT NULL,
    PRIMARY KEY (user_control_entry_id, class_type_permission_id),
    FOREIGN KEY (user_control_entry_id) REFERENCES 

ir_security.user_control_entry(user_control_entry_id),
    FOREIGN KEY (class_type_permission_id) 
        REFERENCES ir_security.class_type_permission(class_type_permission_id)
);
ALTER TABLE ir_security.user_control_entry_permission OWNER TO ir_plus;

-- ---------------------------------------------
-- Role Acess control list table.
-- ---------------------------------------------
CREATE TABLE ir_security.role_control_entry
(
    role_control_entry_id BIGINT PRIMARY KEY,
    role_id BIGINT NOT NULL,
    acl_id BIGINT NOT NULL,
    version INTEGER,
    FOREIGN KEY (role_id) REFERENCES ir_user.role(role_id),
    FOREIGN KEY (acl_id) REFERENCES ir_security.acl(acl_id)
);
ALTER TABLE ir_security.role_control_entry OWNER TO ir_plus;

-- The object identity sequence
CREATE SEQUENCE ir_security.role_control_entry_seq ;
ALTER TABLE ir_security.role_control_entry_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Permissions for the role control entries
-- ---------------------------------------------
CREATE TABLE ir_security.role_control_entry_permission
(
    role_control_entry_id BIGINT NOT NULL,
    class_type_permission_id BIGINT NOT NULL,
    PRIMARY KEY (role_control_entry_id, class_type_permission_id),
    FOREIGN KEY (role_control_entry_id) REFERENCES 

ir_security.role_control_entry(role_control_entry_id),
    FOREIGN KEY (class_type_permission_id) 
        REFERENCES ir_security.class_type_permission(class_type_permission_id)
);
ALTER TABLE ir_security.role_control_entry_permission OWNER TO ir_plus;


-- ---------------------------------------------
-- User Group Acess control list table.
-- ---------------------------------------------
CREATE TABLE ir_security.user_group_control_entry
(
    user_group_control_entry_id BIGINT PRIMARY KEY,
    user_group_id BIGINT NOT NULL,
    acl_id BIGINT NOT NULL,
    version INTEGER,
    UNIQUE (user_group_id , acl_id),
    FOREIGN KEY (user_group_id) REFERENCES ir_user.user_group(group_id),
    FOREIGN KEY (acl_id) REFERENCES ir_security.acl(acl_id)
);
ALTER TABLE ir_security.user_group_control_entry OWNER TO ir_plus;

-- The object identity sequence
CREATE SEQUENCE ir_security.user_group_control_entry_seq ;
ALTER TABLE ir_security.user_group_control_entry_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Permissions for the user group control entries
-- ---------------------------------------------
CREATE TABLE ir_security.user_group_control_entry_permission
(
    user_group_control_entry_id BIGINT NOT NULL,
    class_type_permission_id BIGINT NOT NULL,
    PRIMARY KEY (user_group_control_entry_id, class_type_permission_id),
    FOREIGN KEY (user_group_control_entry_id) REFERENCES 
       ir_security.user_group_control_entry(user_group_control_entry_id),
    FOREIGN KEY (class_type_permission_id) 
        REFERENCES ir_security.class_type_permission(class_type_permission_id)
);
ALTER TABLE ir_security.user_group_control_entry_permission OWNER TO ir_plus;





-- ----------------------------------------------
-- **********************************************
       
--  the ir_news SCHEMA     

-- **********************************************
-- ----------------------------------------------



CREATE SCHEMA ir_news AUTHORIZATION ir_plus;


-- ---------------------------------------------
-- News table
-- ---------------------------------------------


CREATE TABLE ir_news.news_item
(
  news_item_id BIGINT NOT NULL,
  name TEXT NOT NULL,
  article_file_id BIGINT,
  description TEXT,
  article TEXT,
  dateavailable DATE,
  dateremoved DATE,
  primary_picture_id BIGINT,
  version INTEGER,
  FOREIGN KEY (primary_picture_id) REFERENCES ir_file.ir_file(ir_file_id),
  FOREIGN KEY (article_file_id) REFERENCES file_system.file(file_id),
  CONSTRAINT news_pkey PRIMARY KEY (news_item_id),
  CONSTRAINT news_name_key UNIQUE (name)
); 

ALTER TABLE ir_news.news_item OWNER TO ir_plus;

-- The news sequence

CREATE SEQUENCE ir_news.news_item_seq;
ALTER TABLE ir_news.news_item_seq OWNER TO ir_plus;

-- Create a new table to hold news pictures in the system
CREATE TABLE ir_news.news_item_picture
(
  news_item_id BIGINT NOT NULL,
  ir_file_id BIGINT NOT NULL,
  PRIMARY KEY (news_item_id, ir_file_id),
  FOREIGN KEY (news_item_id) REFERENCES ir_news.news_item (news_item_id),
  FOREIGN KEY (ir_file_id) REFERENCES ir_file.ir_file (ir_file_id)
);
ALTER TABLE ir_news.news_item_picture OWNER TO ir_plus;



-- ----------------------------------------------
-- **********************************************
       
-- RESEARCHER SCHEMA     

-- **********************************************
-- ----------------------------------------------


-- ---------------------------------------------
-- Create a schema to hold researcher information
-- ---------------------------------------------

CREATE SCHEMA ir_researcher AUTHORIZATION ir_plus;


-- ---------------------------------------------
-- Field Information
-- ---------------------------------------------
CREATE TABLE ir_researcher.field
(
    field_id BIGINT PRIMARY KEY,
    name text ,
    version INTEGER,
    description TEXT
);
ALTER TABLE ir_researcher.field OWNER TO ir_plus;

-- The field sequence
CREATE SEQUENCE ir_researcher.field_seq ;
ALTER TABLE ir_researcher.field_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Researcher
-- ---------------------------------------------
CREATE TABLE ir_researcher.researcher
(
    researcher_id BIGINT PRIMARY KEY,
    title text ,
    research_interest TEXT,
    teaching_interest TEXT,
    keywords TEXT,
    campus_location TEXT,
    phone_number TEXT,
    email TEXT,
    fax TEXT,
    primary_picture_id BIGINT,
    version INTEGER,
    user_id BIGINT NOT NULL,
    is_public BOOLEAN,
    FOREIGN KEY (user_id) REFERENCES ir_user.ir_user (user_id),
    FOREIGN KEY (primary_picture_id) REFERENCES ir_file.ir_file (ir_file_id)
);
ALTER TABLE ir_researcher.researcher OWNER TO ir_plus;

-- The researcher sequence
CREATE SEQUENCE ir_researcher.researcher_seq ;
ALTER TABLE ir_researcher.researcher_seq OWNER TO ir_plus;



-- ---------------------------------------------
-- Researcher field table
-- ---------------------------------------------

CREATE TABLE ir_researcher.researcher_field
(
    researcher_id BIGINT NOT NULL, 
    field_id BIGINT NOT NULL,
    PRIMARY KEY (researcher_id, field_id),
    FOREIGN KEY (researcher_id) REFERENCES ir_researcher.researcher(researcher_id),
    FOREIGN KEY (field_id) REFERENCES ir_researcher.field(field_id)
);
ALTER TABLE ir_researcher.researcher_field OWNER TO ir_plus;

-- ---------------------------------------------
-- Researcher personal Links
-- ---------------------------------------------
CREATE TABLE ir_researcher.researcher_personal_link
(
  researcher_personal_link_id BIGINT PRIMARY KEY,
  researcher_id BIGINT NOT NULL,
  name TEXT NOT NULL,
  url TEXT NOT NULL,
  order_value INTEGER NOT NULL,
  description TEXT,
  version INTEGER,
  FOREIGN KEY (researcher_id) REFERENCES 
     ir_researcher.researcher (researcher_id)
);
ALTER TABLE ir_researcher.researcher_personal_link OWNER TO ir_plus;


-- The collection link sequence
CREATE SEQUENCE ir_researcher.researcher_personal_link_seq ;
ALTER TABLE ir_researcher.researcher_personal_link_seq OWNER TO ir_plus;


-- ---------------------------------------------
-- Researcher Picture Information
-- ---------------------------------------------
-- Create a new table to hold Researcher 
-- pictures in the system
CREATE TABLE ir_researcher.researcher_picture
(
  researcher_id BIGINT NOT NULL,
  ir_file_id BIGINT NOT NULL,
  PRIMARY KEY (researcher_id, ir_file_id),
  FOREIGN KEY (researcher_id) REFERENCES ir_researcher.researcher (researcher_id),
  FOREIGN KEY (ir_file_id) REFERENCES ir_file.ir_file (ir_file_id)
);
ALTER TABLE ir_researcher.researcher_picture OWNER TO ir_plus;

-- ---------------------------------------------
-- Researcher Folder Information
-- ---------------------------------------------

-- Create a new table to hold researcher folder information in the system
CREATE TABLE ir_researcher.researcher_folder
(
  researcher_folder_id BIGINT PRIMARY KEY,
  root_researcher_folder_id BIGINT NOT NULL,
  parent_id BIGINT,
  researcher_id BIGINT NOT NULL,
  left_value BIGINT,
  right_value BIGINT,
  name TEXT NOT NULL,
  path TEXT NOT NULL,
  description TEXT,
  version INTEGER,
  FOREIGN KEY (parent_id) REFERENCES ir_researcher.researcher_folder (researcher_folder_id),
  FOREIGN KEY (root_researcher_folder_id) REFERENCES ir_researcher.researcher_folder 

(researcher_folder_id),
  FOREIGN KEY (researcher_id) REFERENCES ir_researcher.researcher (researcher_id),
  UNIQUE (parent_id, name),
  UNIQUE (researcher_id, path, name)
);
ALTER TABLE ir_researcher.researcher_folder OWNER TO ir_plus;

CREATE INDEX researcher_folder_idx ON ir_researcher.researcher_folder USING btree (name);

-- The researcher folder  sequence
CREATE SEQUENCE ir_researcher.researcher_folder_seq ;
ALTER TABLE ir_researcher.researcher_folder_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Researcher file Information
-- ---------------------------------------------
CREATE TABLE ir_researcher.researcher_file
(
    researcher_file_id BIGINT PRIMARY KEY,
    researcher_folder_id BIGINT,
    researcher_id BIGINT NOT NULL,
    ir_file_id BIGINT NOT NULL,
    version_number INTEGER NOT NULL,
    version INTEGER,
    FOREIGN KEY (researcher_folder_id) REFERENCES ir_researcher.researcher_folder (researcher_folder_id),
    FOREIGN KEY (ir_file_id) REFERENCES ir_file.ir_file (ir_file_id),
    FOREIGN KEY (researcher_id) REFERENCES ir_researcher.researcher (researcher_id),
    UNIQUE(researcher_id, researcher_folder_id, ir_file_id)
);
ALTER TABLE ir_researcher.researcher_file OWNER TO ir_plus;

-- The researcher file sequence
CREATE SEQUENCE ir_researcher.researcher_file_seq;
ALTER TABLE ir_researcher.researcher_file_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Researcher publication Information
-- ---------------------------------------------
CREATE TABLE ir_researcher.researcher_publication
(
    researcher_publication_id BIGINT PRIMARY KEY,
    researcher_folder_id BIGINT,
    researcher_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    version_number INTEGER NOT NULL,
    version INTEGER,
    FOREIGN KEY (researcher_folder_id) REFERENCES ir_researcher.researcher_folder (researcher_folder_id),
    FOREIGN KEY (item_id) REFERENCES ir_item.item (item_id),
    FOREIGN KEY (researcher_id) REFERENCES ir_researcher.researcher (researcher_id),
    UNIQUE(researcher_id, researcher_folder_id, item_id)
);
ALTER TABLE ir_researcher.researcher_publication OWNER TO ir_plus;

-- The researcher publication sequence
CREATE SEQUENCE ir_researcher.researcher_publication_seq;
ALTER TABLE ir_researcher.researcher_publication_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Researcher institutional item Information
-- ---------------------------------------------
CREATE TABLE ir_researcher.researcher_institutional_item
(
    researcher_institutional_item_id BIGINT PRIMARY KEY,
    researcher_folder_id BIGINT,
    researcher_id BIGINT NOT NULL,
    institutional_item_id BIGINT NOT NULL,
    description TEXT,
    version INTEGER,
    FOREIGN KEY (researcher_folder_id) REFERENCES ir_researcher.researcher_folder (researcher_folder_id),
    FOREIGN KEY (institutional_item_id) REFERENCES ir_repository.institutional_item (institutional_item_id),
    FOREIGN KEY (researcher_id) REFERENCES ir_researcher.researcher (researcher_id),
    UNIQUE(researcher_id, researcher_folder_id, institutional_item_id)
);
ALTER TABLE ir_researcher.researcher_institutional_item OWNER TO ir_plus;

-- The researcher institutional item sequence
CREATE SEQUENCE ir_researcher.researcher_institutional_item_seq;
ALTER TABLE ir_researcher.researcher_institutional_item_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Researcher link Information
-- ---------------------------------------------
CREATE TABLE ir_researcher.researcher_link
(
    researcher_link_id BIGINT PRIMARY KEY,
    researcher_folder_id BIGINT,
    researcher_id BIGINT NOT NULL,
    url TEXT,
    name TEXT,
    description TEXT,
    version INTEGER,
    FOREIGN KEY (researcher_folder_id) REFERENCES ir_researcher.researcher_folder (researcher_folder_id),
    FOREIGN KEY (researcher_id) REFERENCES ir_researcher.researcher (researcher_id),
    UNIQUE(researcher_id, researcher_folder_id, name)
);
ALTER TABLE ir_researcher.researcher_link OWNER TO ir_plus;

-- The researcher link sequence
CREATE SEQUENCE ir_researcher.researcher_link_seq;
ALTER TABLE ir_researcher.researcher_link_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Insert values for admin researcher 
-- ---------------------------------------------
-- Insert Researcher
insert into ir_researcher.researcher (researcher_id, version, user_id, is_public,title ,
  research_interest ,
  teaching_interest ,
  campus_location ,
  phone_number ,
  email,
  fax ) 
select nextval('ir_researcher.researcher_seq'), 0, ir_user.ir_user.user_id, false, null,
null, null, null, null, null, null from ir_user.ir_user where ir_user.ir_user.username = 'admin';


-- **********************************************
       
-- Statistics SCHEMA     

-- **********************************************
-- ----------------------------------------------


-- ---------------------------------------------
-- Create a schema to hold statistics information
-- ---------------------------------------------

CREATE SCHEMA ir_statistics AUTHORIZATION ir_plus;


-- ---------------------------------------------
-- File download Information
-- ---------------------------------------------
CREATE TABLE ir_statistics.file_download_info
(
    file_download_info_id BIGINT PRIMARY KEY,
    ip_address text ,
    ip_address_part1 INTEGER,
    ip_address_part2 INTEGER,
    ip_address_part3 INTEGER,
    ip_address_part4 INTEGER,
    version INTEGER,
    ir_file_id BIGINT NOT NULL,
    download_date DATE NOT NULL,
    count INTEGER,
    UNIQUE(ip_address, ir_file_id, download_date)
);
ALTER TABLE ir_statistics.file_download_info OWNER TO ir_plus;

-- The field sequence
CREATE SEQUENCE ir_statistics.file_download_info_seq ;
ALTER TABLE ir_statistics.file_download_info_seq OWNER TO ir_plus;

CREATE INDEX file_download_info_ip_part_idx
  ON ir_statistics.file_download_info(ip_address_part1, ip_address_part2, ip_address_part3, ip_address_part4);
  
CREATE INDEX ir_file_id_idx
  ON ir_statistics.file_download_info(ir_file_id);
  
CREATE INDEX file_download_info_ip_address_idx
  ON ir_statistics.file_download_info(ip_address);

-- ---------------------------------------------
-- IP address table
-- ---------------------------------------------
CREATE TABLE ir_statistics.ip_address_ignore
(
    ip_address_ignore_id BIGINT PRIMARY KEY,
    name TEXT,
    store_counts BOOLEAN NOT NULL,
    description TEXT,
    from_ip_address_part1 INTEGER,
    from_ip_address_part2 INTEGER,
    from_ip_address_part3 INTEGER,
    from_ip_address_part4 INTEGER,
    to_ip_address_part4 INTEGER,
    version INTEGER
    
);
ALTER TABLE ir_statistics.ip_address_ignore OWNER TO ir_plus;

CREATE INDEX ir_address_ignore_ip_part_idx
  ON ir_statistics.ip_address_ignore(from_ip_address_part1, 
      from_ip_address_part2, 
      from_ip_address_part3, 
      from_ip_address_part4,
      to_ip_address_part4);

-- The field sequence
CREATE SEQUENCE ir_statistics.ip_address_ignore_seq ;
ALTER TABLE ir_statistics.ip_address_ignore_seq OWNER TO ir_plus;


-- ---------------------------------------------
-- roll up processing record table for ir files
-- ---------------------------------------------
CREATE TABLE ir_statistics.ir_file_roll_up_processing_record
(
    ir_file_roll_up_processing_record_id BIGINT PRIMARY KEY,
    created_date TIMESTAMP WITH TIME ZONE,
    ir_file_id BIGINT NOT NULL
);
ALTER TABLE ir_statistics.ir_file_roll_up_processing_record OWNER TO ir_plus;

-- The field sequence
CREATE SEQUENCE ir_statistics.ir_file_roll_up_processing_record_seq;
ALTER TABLE ir_statistics.ir_file_roll_up_processing_record_seq OWNER TO ir_plus;

CREATE INDEX file_roll_up_processing_record_ir_file_idx
  ON ir_statistics.file_download_info(ir_file_id);


-- ---------------------------------------------
-- Ip Ignore File download Information
-- ---------------------------------------------
CREATE TABLE ir_statistics.ip_ignore_file_download_info
(
    ip_ignore_file_download_info_id BIGINT PRIMARY KEY,
    ip_address text ,
    ip_address_part1 INTEGER,
    ip_address_part2 INTEGER,
    ip_address_part3 INTEGER,
    ip_address_part4 INTEGER,
    version INTEGER,
    ir_file_id BIGINT NOT NULL,
    download_date DATE NOT NULL,
    download_count INTEGER,
    UNIQUE(ip_address, ir_file_id, download_date)
);
ALTER TABLE ir_statistics.ip_ignore_file_download_info OWNER TO ir_plus;

-- The field sequence
CREATE SEQUENCE ir_statistics.ip_ignore_file_download_info_seq ;
ALTER TABLE ir_statistics.ip_ignore_file_download_info_seq OWNER TO ir_plus;

CREATE INDEX ip_ignore_file_download_info_ip_part_idx
  ON ir_statistics.ip_ignore_file_download_info(ip_address_part1, ip_address_part2, ip_address_part3, ip_address_part4);
  
CREATE INDEX ip_ignore_ir_file_id_idx
  ON ir_statistics.file_download_info(ir_file_id);
  
CREATE INDEX ip_ignore_ip_address_idx
  ON ir_statistics.file_download_info(ip_address);  

-- ----------------------------------------------
-- **********************************************
       
-- IR metadata schema  

-- **********************************************
-- ----------------------------------------------

CREATE SCHEMA ir_metadata_dublin_core AUTHORIZATION ir_plus;

-- ---------------------------------------------
-- contributor type dublin core mapping
-- ---------------------------------------------

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

-- ---------------------------------------------
-- metadata identifier type dublin core mapping
-- ---------------------------------------------
CREATE TABLE ir_metadata_dublin_core.identifier_type_dc_mapping
(
    identifier_type_dc_mapping_id BIGINT PRIMARY KEY,
    identifier_type_id BIGINT NOT NULL,
    dublin_core_term_id BIGINT NOT NULL,
    dublin_core_encoding_scheme_id BIGINT,
    version INTEGER,
    UNIQUE(identifier_type_id),
    FOREIGN KEY (identifier_type_id) REFERENCES ir_item.identifier_type(identifier_type_id),
    FOREIGN KEY (dublin_core_term_id) REFERENCES metadata.dublin_core_term(dublin_core_term_id),
    FOREIGN KEY (dublin_core_encoding_scheme_id) REFERENCES metadata.dublin_core_encoding_scheme(dublin_core_encoding_scheme_id)
);

ALTER TABLE ir_metadata_dublin_core.identifier_type_dc_mapping OWNER TO ir_plus;

-- The contributor type dc mapping sequence
CREATE SEQUENCE ir_metadata_dublin_core.identifier_type_dc_mapping_seq ;
ALTER TABLE ir_metadata_dublin_core.identifier_type_dc_mapping_seq OWNER TO ir_plus;



-- ----------------------------------------------
-- **********************************************
       
-- Group space schema  

-- **********************************************
-- ----------------------------------------------



CREATE SCHEMA ir_group_workspace AUTHORIZATION ir_plus;


-- ---------------------------------------------
-- group space information
-- ---------------------------------------------
CREATE TABLE ir_group_workspace.group_workspace
(
  group_workspace_id BIGINT PRIMARY KEY,
  name TEXT NOT NULL,
  lower_case_name TEXT NOT NULL,
  description TEXT,
  date_created DATE,
  version INTEGER,
  UNIQUE (lower_case_name)
);
ALTER TABLE ir_group_workspace.group_workspace OWNER TO ir_plus;

-- The group space sequence
CREATE SEQUENCE ir_group_workspace.group_workspace_seq ;
ALTER TABLE ir_group_workspace.group_workspace_seq OWNER TO ir_plus;


-- ---------------------------------------------
-- group space folder information
-- ---------------------------------------------

CREATE TABLE ir_group_workspace.group_workspace_folder
(
  group_workspace_folder_id BIGINT PRIMARY KEY,
  root_group_workspace_folder_id BIGINT NOT NULL,
  parent_id BIGINT,
  group_workspace_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  left_value BIGINT NOT NULL,
  right_value BIGINT NOT NULL,
  name TEXT NOT NULL,
  path TEXT NOT NULL,
  description TEXT,
  version INTEGER,
  FOREIGN KEY (parent_id) REFERENCES ir_group_workspace.group_workspace_folder (group_workspace_folder_id),
  FOREIGN KEY (root_group_workspace_folder_id) REFERENCES ir_group_workspace.group_workspace_folder (group_workspace_folder_id),
  FOREIGN KEY (user_id) REFERENCES ir_user.ir_user (user_id),
  UNIQUE (parent_id, name),
  UNIQUE (group_workspace_id, path, name)
);
ALTER TABLE ir_group_workspace.group_workspace_folder OWNER TO ir_plus;

-- The group folder sequence
CREATE SEQUENCE ir_group_workspace.group_workspace_folder_seq ;
ALTER TABLE ir_group_workspace.group_workspace_folder_seq OWNER TO ir_plus;


-- ---------------------------------------------
-- Group file Information
-- ---------------------------------------------
CREATE TABLE ir_group_workspace.group_workspace_file
(
    group_workspace_file_id BIGINT PRIMARY KEY,
    group_workspace_folder_id BIGINT,
    group_workspace_id BIGINT NOT NULL,
    versioned_file_id BIGINT NOT NULL,
    version INTEGER,
    FOREIGN KEY (group_workspace_folder_id) REFERENCES ir_group_workspace.group_workspace_folder (group_workspace_folder_id),
    FOREIGN KEY (versioned_file_id) REFERENCES ir_file.versioned_file (versioned_file_id),
    FOREIGN KEY (group_workspace_id) REFERENCES ir_group_workspace.group_workspace (group_workspace_id),
    UNIQUE(group_workspace_id, group_workspace_folder_id, versioned_file_id)
);
ALTER TABLE ir_group_workspace.group_workspace_file OWNER TO ir_plus;

-- The group file sequence
CREATE SEQUENCE ir_group_workspace.group_workspace_file_seq;
ALTER TABLE ir_group_workspace.group_workspace_file_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Group space owner Information
-- ---------------------------------------------
CREATE TABLE ir_group_workspace.group_workspace_owner
(
    group_workspace_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (group_workspace_id) REFERENCES ir_group_workspace.group_workspace (group_workspace_id),
    FOREIGN KEY (user_id) REFERENCES ir_user.ir_user (user_id),
    UNIQUE(group_workspace_id, user_id)
);
ALTER TABLE ir_group_workspace.group_workspace_owner OWNER TO ir_plus;

-- ---------------------------------------------
-- Group space group Information
-- ---------------------------------------------
CREATE TABLE ir_group_workspace.group_workspace_group
(
    group_workspace_group_id BIGINT PRIMARY KEY,
    group_workspace_id BIGINT NOT NULL,
    name TEXT NOT NULL,
    lower_case_name TEXT NOT NULL,
    description TEXT,
    FOREIGN KEY (group_workspace_id) REFERENCES ir_group_workspace.group_workspace (group_workspace_id)
);
ALTER TABLE ir_group_workspace.group_workspace_group OWNER TO ir_plus;

-- The group file sequence
CREATE SEQUENCE ir_group_workspace.group_workspace_group_seq;
ALTER TABLE ir_group_workspace.group_workspace_group_seq OWNER TO ir_plus;


-- ---------------------------------------------
-- Group space group membership Information
-- ---------------------------------------------
CREATE TABLE ir_group_workspace.group_workspace_group_members
(
    group_workspace_group_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (group_workspace_group_id) REFERENCES ir_group_workspace.group_workspace_group (group_workspace_group_id),
    FOREIGN KEY (user_id) REFERENCES ir_user.ir_user (user_id),
    PRIMARY KEY(group_workspace_group_id, user_id)
);
ALTER TABLE ir_group_workspace.group_workspace_group_members OWNER TO ir_plus;
