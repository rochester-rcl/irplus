-- ---------------------------------------------
-- DROP the fedora information
-- ---------------------------------------------

DROP TABLE fedora_file_system.file;
DROP TABLE fedora_file_system.alternate_id;
DROP TABLE fedora_file_system.datastream_info;
DROP TABLE fedora_file_system.file_database;
DROP TABLE fedora_file_system.file_server;

DROP SEQUENCE fedora_file_system.file_seq;
DROP SEQUENCE fedora_file_system.alternate_id_seq;
DROP SEQUENCE fedora_file_system.datastream_info_seq;
DROP SEQUENCE fedora_file_system.file_database_seq;
DROP SEQUENCE fedora_file_system.file_server_seq;
DROP SEQUENCE fedora_file_system.file_system_name_seq;

DROP SCHEMA fedora_file_system;

-- create the institutional collection index folder
ALTER TABLE ir_repository.repository ADD COLUMN institutional_collection_index_folder TEXT;

-- create the institutional collection index folder
ALTER TABLE ir_repository.repository ADD COLUMN user_group_index_folder TEXT;


-- add invite info created date
ALTER TABLE ir_user.invite_info ADD COLUMN created_date TIMESTAMP WITH TIME ZONE;
UPDATE  ir_user.invite_info set created_date = date(now());

ALTER TABLE ir_user.invite_info ALTER COLUMN created_date SET NOT NULL;

-- add message column
ALTER TABLE ir_invite.invite_token ADD COLUMN invite_message TEXT;

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


-- -----------------------------------------------
-- Create a lower case email for email addresses
-- -----------------------------------------------

ALTER TABLE ir_user.user_email ADD COLUMN lower_case_email TEXT;

UPDATE ir_user.user_email set lower_case_email = trim(both ' ' from lower(email));

ALTER TABLE ir_user.user_email ALTER COLUMN lower_case_email SET NOT NULL;

ALTER TABLE ir_user.user_email  ADD CONSTRAINT user_email_lower_case_email_key UNIQUE(lower_case_email);


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
-- Group workspace user Information
-- ---------------------------------------------
CREATE TABLE ir_group_workspace.group_workspace_user
(
    group_workspace_user_id BIGINT PRIMARY KEY,
    group_workspace_id BIGINT NOT NULL,
    is_owner boolean NOT NULL,
    user_id BIGINT NOT NULL,
    version INTEGER,
    FOREIGN KEY (group_workspace_id) REFERENCES ir_group_workspace.group_workspace (group_workspace_id),
    FOREIGN KEY (user_id) REFERENCES ir_user.ir_user (user_id)
);
ALTER TABLE ir_group_workspace.group_workspace_user OWNER TO ir_plus;

-- The group file sequence
CREATE SEQUENCE ir_group_workspace.group_workspace_user_seq;
ALTER TABLE ir_group_workspace.group_workspace_user_seq OWNER TO ir_plus;






-- ---------------------------------------------
-- Create a re-index service
-- ---------------------------------------------

insert into 
ir_index.index_processing_type ( index_processing_type_id, version, name, description) 
values (nextval('ir_index.index_processing_type_seq'), 0, 'DELETE_INDEX', 'the index needs to be deleted and rebuilt');


-- ---------------------------------------------
-- Create unique constraints on the delete tables
-- ---------------------------------------------
ALTER TABLE ir_repository.deleted_institutional_item
  ADD CONSTRAINT deleted_institutional_item_institutional_item_id_key UNIQUE(institutional_item_id);

  
 ALTER TABLE ir_repository.deleted_institutional_item_version
  ADD CONSTRAINT deleted_institutional_item_ve_institutional_item_version_id_key UNIQUE(institutional_item_version_id);
  
  
-- ---------------------------------------------
-- Security class types for permissions
-- ---------------------------------------------
insert into ir_security.class_type(class_type_id, name , description , version) 
values (nextval('ir_security.class_type_seq'), 'edu.ur.ir.groupspace.GroupWorkspaceFile', 
'Group Workspace File',1);
  
insert into ir_security.class_type(class_type_id, name , description , version) 
values (nextval('ir_security.class_type_seq'), 'edu.ur.ir.groupspace.GroupWorkspaceFolder', 
'Group Workspace Folder',1);

insert into ir_security.class_type(class_type_id, name , description , version) 
values (nextval('ir_security.class_type_seq'), 'edu.ur.ir.groupspace.GroupWorkspace', 
'Group Workspace',1);


-- ---------------------------------------------
-- New permission types for workspace files
-- ---------------------------------------------
insert into ir_security.class_type_permission select 
nextval('ir_security.class_type_permission_seq'),
  ir_security.class_type.class_type_id, 'GROUP_WORKSPACE_FILE_READ','The user can read the file',0
  from ir_security.class_type where ir_security.class_type.name = 
'edu.ur.ir.groupspace.GroupWorkspaceFile';

insert into ir_security.class_type_permission select 
nextval('ir_security.class_type_permission_seq'),
  ir_security.class_type.class_type_id, 'GROUP_WORKSPACE_FILE_EDIT','The user can read and add new versions of the file',0
  from ir_security.class_type where ir_security.class_type.name = 
'edu.ur.ir.groupspace.GroupWorkspaceFile';

insert into ir_security.class_type_permission select 
nextval('ir_security.class_type_permission_seq'),
  ir_security.class_type.class_type_id, 'GROUP_WORKSPACE_FILE_MANAGE','The user can delete, read and add new versions of the file',0
  from ir_security.class_type where ir_security.class_type.name = 
'edu.ur.ir.groupspace.GroupWorkspaceFile';
-- ---------------------------------------------
-- New permission types for workspace folders
-- ---------------------------------------------

insert into ir_security.class_type_permission select 
nextval('ir_security.class_type_permission_seq'),
  ir_security.class_type.class_type_id, 'GROUP_WORKSPACE_FOLDER_EDIT','The user can add and delete any files and folders from the specified folder including child files and folders',0
  from ir_security.class_type where ir_security.class_type.name = 
'edu.ur.ir.groupspace.GroupWorkspaceFolder';

insert into ir_security.class_type_permission select 
nextval('ir_security.class_type_permission_seq'),
  ir_security.class_type.class_type_id, 'GROUP_WORKSPACE_FOLDER_ADD_FILE','The user can add files to the specified folder and only delete files they own',0
  from ir_security.class_type where ir_security.class_type.name = 
'edu.ur.ir.groupspace.GroupWorkspaceFolder';

insert into ir_security.class_type_permission select 
nextval('ir_security.class_type_permission_seq'),
  ir_security.class_type.class_type_id, 'GROUP_WORKSPACE_FOLDER_READ','The user can view the folder and read all of the files and folders within the folder',0
  from ir_security.class_type where ir_security.class_type.name = 
'edu.ur.ir.groupspace.GroupWorkspaceFolder';

-- ---------------------------------------------
-- New permission types for workspace
-- ---------------------------------------------


insert into ir_security.class_type_permission select 
nextval('ir_security.class_type_permission_seq'),
  ir_security.class_type.class_type_id, 'GROUP_WORKSPACE_EDIT','The user(s) can read, add and delete any files and folders from the specified group',0
  from ir_security.class_type where ir_security.class_type.name = 
'edu.ur.ir.groupspace.GroupWorkspace';

insert into ir_security.class_type_permission select 
nextval('ir_security.class_type_permission_seq'),
  ir_security.class_type.class_type_id, 'GROUP_WORKSPACE_ADD_FILE','The user(s) can read all files in the group, add files to the group and only delete files they own',0
  from ir_security.class_type where ir_security.class_type.name = 
'edu.ur.ir.groupspace.GroupWorkspace';

insert into ir_security.class_type_permission select 
nextval('ir_security.class_type_permission_seq'),
  ir_security.class_type.class_type_id, 'GROUP_WORKSPACE_READ','The user(s) can read and open all files and folders within the group',0
  from ir_security.class_type where ir_security.class_type.name = 
'edu.ur.ir.groupspace.GroupWorkspace';



-- ---------------------------------------------
-- Add a new column most recent login date
-- last login date will hold the last time the 
-- user logged in
-- ---------------------------------------------
ALTER TABLE ir_user.ir_user ADD COLUMN most_recent_login_date TIMESTAMP WITH TIME ZONE;

UPDATE ir_user.ir_user set most_recent_login_date = last_login_date;


-- ---------------------------------------------
-- Add a constraint on the invite info table
-- ---------------------------------------------
 ALTER TABLE ir_user.invite_info
  ADD CONSTRAINT invite_info_token_key UNIQUE (token);


-- ---------------------------------------------
-- Create a schema to hold all file system
-- information.
-- ---------------------------------------------

CREATE SCHEMA ir_invite AUTHORIZATION ir_plus;

CREATE TABLE ir_invite.invite_token
(
  invite_token_id BIGINT PRIMARY KEY,
  version INTEGER,
  token TEXT NOT NULL,
  email TEXT NOT NULL,
  invite_message TEXT,
  inviting_user_id BIGINT NOT NULL,
  created_date TIMESTAMP WITH TIME ZONE NOT NULL,
  expiration_date TIMESTAMP WITH TIME ZONE,
  FOREIGN KEY (inviting_user_id) REFERENCES ir_user.ir_user (user_id), 
  UNIQUE(token)
);
ALTER TABLE ir_invite.invite_token OWNER TO ir_plus;

-- The  invite token sequence
CREATE SEQUENCE ir_invite.invite_token_seq;
ALTER TABLE ir_invite.invite_token_seq OWNER TO ir_plus;


-- ---------------------------------------------
-- Move invite info over
-- ---------------------------------------------



INSERT INTO ir_invite.invite_token SELECT
nextval('ir_invite.invite_token_seq'),0, token, email, user_id, created_date, null
FROM ir_user.invite_info;

-- Add new column
ALTER TABLE ir_user.invite_info ADD COLUMN invite_token_id BIGINT;

-- create reference
UPDATE ir_user.invite_info SET invite_token_id = (SELECT invite_token_id
FROM ir_invite.invite_token WHERE invite_info.token = invite_token.token);

-- set not null
ALTER TABLE ir_user.invite_info ALTER COLUMN invite_token_id SET NOT NULL;

-- create foreign key link
ALTER TABLE ir_user.invite_info
  ADD CONSTRAINT invite_info_invite_token_id_fkey FOREIGN KEY (invite_token_id) REFERENCES ir_invite.invite_token (invite_token_id);

-- drop moved columns
ALTER TABLE ir_user.invite_info DROP COLUMN token;
ALTER TABLE ir_user.invite_info DROP COLUMN email;
ALTER TABLE ir_user.invite_info DROP COLUMN user_id;
ALTER TABLE ir_user.invite_info DROP COLUMN created_date;
-- ---------------------------------------------
-- Update all users who have a verified email
-- where the token is not null
-- ---------------------------------------------
UPDATE ir_user.user_email set token = null
where ir_user.user_email.isverified = true
and token is not null;


-- create an index on the handle info local name
CREATE INDEX handle_info_local_name_idx ON handle.handle_info(local_name);


-- ---------------------------------------------
-- Group workspace email invite Information
-- ---------------------------------------------
CREATE TABLE ir_group_workspace.group_workspace_email_invite
(
    group_workspace_email_invite_id BIGINT PRIMARY KEY,
    version INTEGER,
    invite_token_id BIGINT NOT NULL,
    set_as_owner BOOLEAN NOT NULL,
    group_workspace_id BIGINT NOT NULL,
    FOREIGN KEY (group_workspace_id) REFERENCES ir_group_workspace.group_workspace (group_workspace_id),
    FOREIGN KEY (invite_token_id) REFERENCES ir_invite.invite_token(invite_token_id),
    UNIQUE(invite_token_id)
);
ALTER TABLE ir_group_workspace.group_workspace_email_invite OWNER TO ir_plus;

-- The group workspace group sequence
CREATE SEQUENCE ir_group_workspace.group_workspace_email_invite_seq;
ALTER TABLE ir_group_workspace.group_workspace_email_invite_seq OWNER TO ir_plus;

-- ---------------------------------------------
-- Group workspace user invite Information
-- ---------------------------------------------
CREATE TABLE ir_group_workspace.group_workspace_user_invite
(
    group_workspace_user_invite_id BIGINT PRIMARY KEY,
    version INTEGER,
    inviting_user_id BIGINT NOT NULL,
    invited_user_id BIGINT NOT NULL,
    set_as_owner BOOLEAN NOT NULL,
    email TEXT NOT NULL,
    invite_message TEXT,
    group_workspace_id BIGINT NOT NULL,
    created_date TIMESTAMP WITH TIME ZONE NOT NULL,
    FOREIGN KEY (group_workspace_id) REFERENCES ir_group_workspace.group_workspace (group_workspace_id),
    FOREIGN KEY (inviting_user_id) REFERENCES ir_user.ir_user (user_id),
    FOREIGN KEY (invited_user_id) REFERENCES ir_user.ir_user (user_id)
);
ALTER TABLE ir_group_workspace.group_workspace_user_invite OWNER TO ir_plus;

-- The group workspace group sequence
CREATE SEQUENCE ir_group_workspace.group_workspace_user_invite_seq;
ALTER TABLE ir_group_workspace.group_workspace_user_invite_seq OWNER TO ir_plus;


-- ---------------------------------------------
-- Invite permission
-- ---------------------------------------------

CREATE TABLE  ir_group_workspace.group_workspace_email_invite_permissions
(
    group_workspace_email_invite_id BIGINT NOT NULL, 
    class_type_permission_id BIGINT NOT NULL,
    PRIMARY KEY (group_workspace_email_invite_id, class_type_permission_id),
    FOREIGN KEY (group_workspace_email_invite_id) REFERENCES ir_group_workspace.group_workspace_email_invite(group_workspace_email_invite_id),
    FOREIGN KEY (class_type_permission_id) REFERENCES ir_security.class_type_permission(class_type_permission_id)
);
ALTER TABLE ir_group_workspace.group_workspace_email_invite_permissions OWNER TO ir_plus;

-- ---------------------------------------------
-- group workspace file delete record
-- ---------------------------------------------

CREATE TABLE ir_group_workspace.group_workspace_file_delete_record
(
  group_workspace_file_delete_record_id BIGINT PRIMARY KEY,
  group_workspace_file_id BIGINT NOT NULL,
  group_workspace_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  date_deleted TIMESTAMP WITH TIME ZONE NOT NULL,
  full_path TEXT NOT NULL,
  group_workspace_name TEXT NOT NULL,
  description TEXT,
  delete_reason TEXT,
  UNIQUE(group_workspace_file_id)
);
ALTER TABLE ir_group_workspace.group_workspace_file_delete_record OWNER TO ir_plus;

-- The group workspace file delete record sequence
CREATE SEQUENCE ir_group_workspace.group_workspace_file_delete_record_seq;
ALTER TABLE ir_group_workspace.group_workspace_file_delete_record_seq OWNER TO ir_plus;

