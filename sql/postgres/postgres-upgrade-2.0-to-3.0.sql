-- create the institutional collection index folder
ALTER TABLE ir_repository.repository ADD COLUMN institutional_collection_index_folder TEXT;

-- create the institutional collection index folder
ALTER TABLE ir_repository.repository ADD COLUMN user_group_index_folder TEXT;


-- create the institutional collection index folder
ALTER TABLE ir_user.invite_info ADD COLUMN created_date TIMESTAMP WITH TIME ZONE;
UPDATE  ir_user.invite_info set created_date = date(now());

ALTER TABLE ir_user.invite_info ALTER COLUMN created_date SET NOT NULL;



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


-- ---------------------------------------------
-- Create a re-index service
-- ---------------------------------------------

insert into 
ir_index.index_processing_type ( index_processing_type_id, version, name, description) 
values (nextval('ir_index.index_processing_type_seq'), 0, 'DELETE_INDEX', 'the index needs to be deleted and rebuilt');
