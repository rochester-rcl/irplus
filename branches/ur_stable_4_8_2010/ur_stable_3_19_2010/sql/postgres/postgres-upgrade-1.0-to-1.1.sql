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
       
-- Update to create a link from Versioned Institutional 
-- item to Institutional Item     

-- **********************************************
-- ----------------------------------------------

ALTER TABLE ir_repository.versioned_institutional_item
ADD COLUMN institutional_item_id BIGINT;


-- update all with the item id
UPDATE ir_repository.versioned_institutional_item as version
SET institutional_item_id = 
(SELECT institutional_item_id from
ir_repository.institutional_item
where ir_repository.institutional_item.versioned_institutional_item_id = version.versioned_institutional_item_id);


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
