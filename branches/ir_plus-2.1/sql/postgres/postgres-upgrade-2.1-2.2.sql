-- Fix issue where user can have the same personal file listed twice in two different folders
ALTER TABLE ir_user.personal_file DROP CONSTRAINT personal_file_user_id_key;
ALTER TABLE ir_user.personal_file ADD CONSTRAINT personal_file_user_id_versioned_file_id_key UNIQUE (user_id, versioned_file_id);

-- -----------------------------------------------
-- Create a lower case email for email addresses
-- -----------------------------------------------

ALTER TABLE ir_user.user_email ADD COLUMN lower_case_email TEXT;

UPDATE ir_user.user_email set lower_case_email = trim(both ' ' from lower(email));

ALTER TABLE ir_user.user_email ALTER COLUMN lower_case_email SET NOT NULL;

ALTER TABLE ir_user.user_email  ADD CONSTRAINT user_email_lower_case_email_key UNIQUE(lower_case_email);


-- ---------------------------------------------
-- Add a constraint on the invite info table
-- ---------------------------------------------
 ALTER TABLE ir_user.invite_info
  ADD CONSTRAINT invite_info_token_key UNIQUE (token);
  
 
-- ---------------------------------------------
-- Create a schema to hold all system
-- invite information.
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

INSERT INTO ir_invite.invite_token(invite_token_id, version, token, email, inviting_user_id, created_date,expiration_date)  SELECT
nextval('ir_invite.invite_token_seq'),0, token, lower(email), user_id, now(), null
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
-- ---------------------------------------------
-- Update all users who have a verified email
-- where the token is not null
-- ---------------------------------------------
UPDATE ir_user.user_email set token = null
where ir_user.user_email.isverified = true
and token is not null;

-- -------------------------------------------------------
-- create an index on the handle info local name
-- -------------------------------------------------------

CREATE INDEX handle_info_local_name_idx ON handle.handle_info(local_name);

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


