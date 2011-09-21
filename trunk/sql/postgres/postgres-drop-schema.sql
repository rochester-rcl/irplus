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

DROP TABLE ir_metadata_marc.extent_type_sub_field_mapper;
DROP SEQUENCE ir_metadata_marc.extent_type_sub_field_mapper_seq;

DROP TABLE ir_metadata_marc.identifier_type_sub_field_mapper;
DROP SEQUENCE ir_metadata_marc.identifier_type_sub_field_mapper_seq;

DROP TABLE ir_metadata_marc.content_type_field_mapping;
DROP SEQUENCE ir_metadata_marc.content_type_field_mapping_seq;

DROP TABLE ir_metadata_marc.contributor_type_relator_code;
DROP SEQUENCE ir_metadata_marc.contributor_type_relator_code_seq;

DROP TABLE ir_metadata_marc.data_field_mapper;
DROP SEQUENCE ir_metadata_marc.data_field_mapper_seq;

DROP SCHEMA ir_metadata_marc;

-- ---------------------------------------------
-- Drop Institutional Repository Metadata tables/schema
-- ---------------------------------------------

DROP TABLE ir_metadata_dublin_core.contributor_type_dc_mapping;
DROP SEQUENCE ir_metadata_dublin_core.contributor_type_dc_mapping_seq;

DROP TABLE ir_metadata_dublin_core.identifier_type_dc_mapping;
DROP SEQUENCE ir_metadata_dublin_core.identifier_type_dc_mapping_seq;

DROP SCHEMA ir_metadata_dublin_core;


-- ---------------------------------------------
-- Drop security tables
-- ---------------------------------------------
 


DROP TABLE ir_security.role_control_entry_permission;
DROP TABLE ir_security.role_control_entry;
DROP TABLE ir_security.user_control_entry_permission;
DROP TABLE ir_security.user_control_entry;
DROP TABLE ir_security.user_group_control_entry_permission;
DROP TABLE ir_security.user_group_control_entry;
DROP TABLE ir_security.acl;
ALTER TABLE ir_user.invite_permissions DROP CONSTRAINT invite_permissions_class_type_permission_id_fkey;
ALTER TABLE ir_user.folder_invite_permissions DROP CONSTRAINT folder_invite_permissions_class_type_permission_id_fkey;
ALTER TABLE ir_user.folder_auto_share_permissions DROP CONSTRAINT folder_auto_share_permissions_class_type_permission_id_fkey;

DROP TABLE ir_security.class_type_permission;
DROP TABLE ir_security.class_type;

DROP SEQUENCE ir_security.role_control_entry_seq;
DROP SEQUENCE ir_security.class_type_permission_seq;
DROP SEQUENCE ir_security.user_control_entry_seq;
DROP SEQUENCE ir_security.user_group_control_entry_seq;
DROP SEQUENCE ir_security.acl_seq;
DROP SEQUENCE ir_security.class_type_seq;

DROP SCHEMA ir_security;

-- ---------------------------------------------
-- Drop Group spaces tables/schema
-- ---------------------------------------------
DROP TABLE ir_group_workspace.group_workspace_user_invite;
DROP TABLE ir_group_workspace.group_workspace_email_invite;
DROP TABLE ir_group_workspace.group_workspace_user;
DROP TABLE ir_group_workspace.group_workspace_file;
DROP TABLE ir_group_workspace.group_workspace_folder;
DROP TABLE ir_group_workspace.group_workspace;
DROP TABLE ir_group_workspace.group_workspace_file_delete_record;

DROP SEQUENCE ir_group_workspace.group_workspace_user_seq;
DROP SEQUENCE ir_group_workspace.group_workspace_email_invite_seq;
DROP SEQUENCE ir_group_workspace.group_workspace_user_invite_seq;
DROP SEQUENCE ir_group_workspace.group_workspace_file_seq;
DROP SEQUENCE ir_group_workspace.group_workspace_seq;
DROP SEQUENCE ir_group_workspace.group_workspace_folder_seq;
DROP SEQUENCE ir_group_workspace.group_workspace_file_delete_record_seq;

DROP SCHEMA ir_group_workspace;





-- ---------------------------------------------
-- Drop ir_repository information
-- ---------------------------------------------

ALTER TABLE ir_repository.institutional_item_version DROP CONSTRAINT institutional_item_version_withdrawn_token_id_fkey;
ALTER TABLE ir_researcher.researcher_institutional_item DROP CONSTRAINT researcher_institutional_item_institutional_item_id_fkey;

DROP TABLE ir_repository.retired_repository_license;
DROP TABLE ir_repository.user_repository_license;
DROP TABLE ir_repository.reviewable_item;
DROP TABLE ir_repository.repository_picture;
DROP TABLE ir_repository.withdrawn_token;
DROP TABLE ir_repository.reinstate_token;
DROP TABLE ir_repository.institutional_item_repository_license;
DROP TABLE ir_repository.institutional_item_version;
DROP TABLE ir_repository.institutional_item;
DROP TABLE ir_repository.versioned_institutional_item;
DROP TABLE ir_repository.deleted_institutional_item_version;
DROP TABLE ir_repository.deleted_institutional_item;
DROP TABLE ir_repository.institutional_collection_link;
DROP TABLE ir_repository.institutional_collection_subscription;
DROP TABLE ir_repository.institutional_collection_picture;
DROP TABLE ir_repository.institutional_collection;
DROP TABLE ir_repository.repository;
DROP TABLE ir_repository.license_version;
DROP TABLE ir_repository.versioned_license;
DROP TABLE ir_repository.license;
DROP TABLE ir_repository.institutional_item_index_processing_record;

DROP SEQUENCE ir_repository.institutional_item_index_processing_record_seq;
DROP SEQUENCE ir_repository.institutional_item_repository_license_seq;
DROP SEQUENCE ir_repository.retired_repository_license_seq;
DROP SEQUENCE ir_repository.user_repository_license_seq;
DROP SEQUENCE ir_repository.institutional_collection_subscription_seq;
DROP SEQUENCE ir_repository.institutional_collection_link_seq;
DROP SEQUENCE ir_repository.reinstate_token_seq;
DROP SEQUENCE ir_repository.withdrawn_token_seq;
DROP SEQUENCE ir_repository.versioned_institutional_item_seq;
DROP SEQUENCE ir_repository.reviewable_item_seq;
DROP SEQUENCE ir_repository.repository_picture_seq;
DROP SEQUENCE ir_repository.institutional_item_version_seq;
DROP SEQUENCE ir_repository.institutional_item_seq;
DROP SEQUENCE ir_repository.deleted_institutional_item_seq;
DROP SEQUENCE ir_repository.deleted_institutional_item_version_seq;
DROP SEQUENCE ir_repository.institutional_collection_seq;
DROP SEQUENCE ir_repository.repository_seq;
DROP SEQUENCE ir_repository.license_seq;
DROP SEQUENCE ir_repository.license_version_seq;
DROP SEQUENCE ir_repository.versioned_license_seq;

DROP SCHEMA ir_repository;



-- ---------------------------------------------
-- Drop researcher information
-- ---------------------------------------------
DROP TABLE ir_researcher.researcher_field;
DROP TABLE ir_researcher.researcher_personal_link;
DROP TABLE ir_researcher.researcher_picture;
DROP TABLE ir_researcher.researcher_file;
DROP TABLE ir_researcher.researcher_publication;
DROP TABLE ir_researcher.researcher_institutional_item;
DROP TABLE ir_researcher.researcher_link;
DROP TABLE ir_researcher.researcher_folder;
DROP TABLE ir_researcher.researcher;
DROP TABLE ir_researcher.field;

DROP SEQUENCE ir_researcher.researcher_personal_link_seq;
DROP SEQUENCE ir_researcher.researcher_file_seq;
DROP SEQUENCE ir_researcher.researcher_publication_seq;
DROP SEQUENCE ir_researcher.researcher_institutional_item_seq;
DROP SEQUENCE ir_researcher.researcher_link_seq;
DROP SEQUENCE ir_researcher.researcher_folder_seq;
DROP SEQUENCE ir_researcher.researcher_seq;
DROP SEQUENCE ir_researcher.field_seq;

DROP SCHEMA ir_researcher;


-- ---------------------------------------------
-- Drop user information
-- ---------------------------------------------
DROP TABLE ir_user.personal_file_delete_record;
DROP TABLE ir_user.personal_item_delete_record;
DROP TABLE ir_user.user_workspace_index_processing_record;
DROP TABLE ir_user.user_department;
DROP TABLE ir_user.invite_permissions;
DROP TABLE ir_user.folder_invite_permissions;
DROP TABLE ir_user.folder_auto_share_permissions;
DROP TABLE ir_user.folder_auto_share_info;
DROP TABLE ir_user.invite_files;
DROP TABLE ir_user.personal_item;
DROP TABLE ir_user.personal_collection;
DROP TABLE ir_user.personal_file;
DROP TABLE ir_user.folder_invite_info;
DROP TABLE ir_user.personal_folder;
DROP TABLE ir_user.user_role;

DROP TABLE ir_user.role;
DROP TABLE ir_user.user_group_users;
DROP TABLE ir_user.user_group_admins;
DROP TABLE ir_user.user_group;

ALTER TABLE ir_user.ir_user DROP CONSTRAINT  user_default_email_id_fkey;
DROP TABLE ir_user.user_email;
DROP TABLE ir_user.invite_info;
ALTER TABLE ir_user.ir_user DROP CONSTRAINT  ir_user_affiliation_id_fkey;
DROP TABLE ir_user.affiliation;
ALTER TABLE ir_user.ir_user DROP CONSTRAINT  ir_user_department_id_fkey;
DROP TABLE ir_user.department;

DROP SEQUENCE ir_user.personal_file_delete_record_seq;
DROP SEQUENCE ir_user.personal_item_delete_record_seq;
DROP SEQUENCE ir_user.user_workspace_index_processing_record_seq;
DROP SEQUENCE ir_user.personal_item_seq;
DROP SEQUENCE ir_user.personal_collection_seq;
DROP SEQUENCE ir_user.personal_file_seq;
DROP SEQUENCE ir_user.personal_folder_seq;
DROP SEQUENCE ir_user.user_group_seq;
DROP SEQUENCE ir_user.ir_user_seq;
DROP SEQUENCE ir_user.role_seq;
DROP SEQUENCE ir_user.affiliation_seq;
DROP SEQUENCE ir_user.department_seq;
DROP SEQUENCE ir_user.user_email_seq;
DROP SEQUENCE ir_user.invite_info_seq;
DROP SEQUENCE ir_user.folder_auto_share_info_seq;
DROP SEQUENCE ir_user.folder_invite_info_seq;
DROP SEQUENCE ir_user.shared_inbox_file_seq;


-- ---------------------------------------------
-- Drop ir_invite information
-- ---------------------------------------------
DROP TABLE ir_invite.invite_token;
DROP SEQUENCE ir_invite.invite_token_seq;


DROP SCHEMA ir_invite;

-- ---------------------------------------------
-- Drop ir_item information
-- ---------------------------------------------
ALTER TABLE ir_item.item DROP CONSTRAINT item_primay_image_item_file_id_fkey;
ALTER TABLE ir_item.first_available_date DROP CONSTRAINT item_first_available_date_id_fkey;
ALTER TABLE ir_item.original_item_creation_date DROP CONSTRAINT item_original_item_creation_date_id_fkey;
ALTER TABLE ir_item.published_date DROP CONSTRAINT external_published_item_published_date_id_fkey;
DROP TABLE ir_item.item_content_type;
DROP TABLE ir_item.item_contributor;
DROP TABLE ir_item.item_series_report_number;
DROP TABLE ir_item.series;
DROP TABLE ir_item.item_sponsor;
DROP TABLE ir_item.item_title;
DROP TABLE ir_item.item_link;
DROP TABLE ir_item.item_version;
DROP TABLE ir_item.versioned_item;
DROP TABLE ir_item.item_file;
DROP TABLE ir_item.item_identifier;
DROP TABLE ir_item.item_extent;
DROP TABLE ir_item.item;
DROP TABLE ir_item.external_published_item;
DROP TABLE ir_item.place_of_publication;
DROP TABLE ir_item.publisher;
DROP TABLE ir_item.sponsor;
DROP TABLE ir_item.identifier_type;
DROP TABLE ir_item.extent_type;
DROP TABLE ir_item.language_type;
DROP TABLE ir_item.content_type;
DROP TABLE ir_item.copyright_statement;

DROP TABLE ir_item.published_date;
DROP TABLE ir_item.original_item_creation_date;
DROP TABLE ir_item.first_available_date;

DROP SEQUENCE ir_item.place_of_publication_seq;
DROP SEQUENCE ir_item.copyright_statement_seq;
DROP SEQUENCE ir_item.item_sponsor_seq;
DROP SEQUENCE ir_item.item_contributor_seq;
DROP SEQUENCE ir_item.item_seq;
DROP SEQUENCE ir_item.published_date_seq;
DROP SEQUENCE ir_item.original_item_creation_date_seq;
DROP SEQUENCE ir_item.first_available_date_seq;
DROP SEQUENCE ir_item.language_type_seq;
DROP SEQUENCE ir_item.identifier_type_seq;
DROP SEQUENCE ir_item.content_type_seq;
DROP SEQUENCE ir_item.item_identifier_seq;
DROP SEQUENCE ir_item.item_title_seq;
DROP SEQUENCE ir_item.item_link_seq;
DROP SEQUENCE ir_item.publisher_seq;
DROP SEQUENCE ir_item.item_version_seq;
DROP SEQUENCE ir_item.versioned_item_seq;
DROP SEQUENCE ir_item.item_file_seq;
DROP SEQUENCE ir_item.external_published_item_seq;
DROP SEQUENCE ir_item.item_series_report_number_seq;
DROP SEQUENCE ir_item.series_seq;
DROP SEQUENCE ir_item.sponsor_seq;
DROP SEQUENCE ir_item.extent_type_seq;
DROP SEQUENCE ir_item.item_extent_seq;
DROP SEQUENCE ir_item.item_content_type_seq;

DROP SCHEMA ir_item;

-- ---------------------------------------------
-- DROP the news information
-- ---------------------------------------------

DROP TABLE ir_news.news_item_picture;
DROP TABLE ir_news.news_item;
DROP SEQUENCE ir_news.news_item_seq;

DROP SCHEMA ir_news;


-- ---------------------------------------------
-- Drop ir_file information
-- ---------------------------------------------

DROP TABLE ir_file.ir_file_transformation_failure_record;
DROP TABLE ir_file.ir_file_indexing_failure_record;
DROP TABLE ir_file.transformed_file;
DROP TABLE ir_file.transformed_file_type;
DROP TABLE ir_file.file_version;

DROP TABLE ir_file.ir_file;
DROP TABLE ir_file.file_collaborator;
DROP TABLE ir_file.versioned_file;

DROP SEQUENCE ir_file.ir_file_transformation_failure_record_seq;
DROP SEQUENCE ir_file.ir_file_indexing_failure_record_seq;
DROP SEQUENCE ir_file.transformed_file_seq;
DROP SEQUENCE ir_file.transformed_file_type_seq;
DROP SEQUENCE ir_file.file_version_seq;
DROP SEQUENCE ir_file.ir_file_seq;
DROP SEQUENCE ir_file.file_collaborator_seq;
DROP SEQUENCE ir_file.versioned_file_seq;

DROP SCHEMA ir_file;


-- ---------------------------------------------
-- Drop the rest of the user information
-- ---------------------------------------------
DROP TABLE ir_user.shared_inbox_file;
DROP TABLE ir_user.external_user_account;
DROP SEQUENCE ir_user.external_user_account_seq;
DROP TABLE ir_user.ir_user;
DROP TABLE ir_user.external_account_type;
DROP SEQUENCE ir_user.external_account_type_seq;
DROP SCHEMA ir_user;

-- ---------------------------------------------
-- Drop person information
-- ---------------------------------------------
ALTER TABLE person.person_name_authority DROP CONSTRAINT person_authoritiative_name_id_fkey;
ALTER TABLE person.birth_date DROP CONSTRAINT person_birth_date_id_fkey;
ALTER TABLE person.death_date DROP CONSTRAINT person_death_date_id_fkey;
DROP TABLE person.contributor;
DROP TABLE person.person_name_title;
DROP TABLE person.person_name;
DROP TABLE person.person_name_authority;
DROP TABLE person.contributor_type;
DROP TABLE person.birth_date;
DROP TABLE person.death_date;


DROP SEQUENCE person.contributor_seq;
DROP SEQUENCE person.birth_date_seq;
DROP SEQUENCE person.death_date_seq;
DROP SEQUENCE person.person_name_authority_seq;
DROP SEQUENCE person.person_name_seq;
DROP SEQUENCE person.contributor_type_seq;
DROP SEQUENCE person.person_name_title_seq;

DROP SCHEMA person;

-- ---------------------------------------------
-- Drop file system information
-- ---------------------------------------------

DROP TABLE file_system.file_checksum;
DROP TABLE file_system.file;
DROP TABLE file_system.folder;
DROP TABLE file_system.file_database;
DROP TABLE file_system.file_server;

DROP SEQUENCE file_system.file_checksum_seq;
DROP SEQUENCE file_system.file_database_seq;
DROP SEQUENCE file_system.file_server_seq;
DROP SEQUENCE file_system.folder_seq;
DROP SEQUENCE file_system.file_system_name_seq;
DROP SEQUENCE file_system.file_seq;

DROP SCHEMA file_system;

-- ---------------------------------------------
-- Drop mime tables
-- ---------------------------------------------

DROP TABLE mime.sub_type_extension;
DROP TABLE mime.sub_type;
DROP TABLE mime.top_media_type;

DROP SEQUENCE mime.top_media_type_seq;
DROP SEQUENCE mime.sub_type_seq;
DROP SEQUENCE mime.sub_type_extension_seq;

DROP SCHEMA mime;



-- ---------------------------------------------
-- Drop statistics tables
-- ---------------------------------------------
DROP TABLE ir_statistics.file_download_info;
DROP TABLE ir_statistics.ip_address_ignore;
DROP TABLE ir_statistics.ir_file_roll_up_processing_record;
DROP TABLE ir_statistics.ip_ignore_file_download_info;

DROP SEQUENCE ir_statistics.file_download_info_seq;
DROP SEQUENCE ir_statistics.ip_address_ignore_seq;
DROP SEQUENCE ir_statistics.ir_file_roll_up_processing_record_seq;
DROP SEQUENCE ir_statistics.ip_ignore_file_download_info_seq;


DROP SCHEMA ir_statistics;

-- ---------------------------------------------
-- Drop handle tables
-- ---------------------------------------------
DROP TABLE handle.handle_info;
DROP TABLE handle.handle_name_authority;

DROP SEQUENCE handle.unique_handle_name_seq;
DROP SEQUENCE handle.handle_name_authority_seq;
DROP SEQUENCE handle.handle_info_seq;

DROP SCHEMA handle;


-- ---------------------------------------------
-- Drop index processing tables
-- ---------------------------------------------
DROP TABLE ir_index.index_processing_type;

DROP SEQUENCE ir_index.index_processing_type_seq;

DROP SCHEMA ir_index;


-- ---------------------------------------------
-- Drop Metadata tables/schema
-- ---------------------------------------------

DROP TABLE metadata.metadata_type;
DROP TABLE metadata.dublin_core_term;
DROP TABLE metadata.dublin_core_encoding_scheme;
DROP TABLE metadata.marc_data_field;
DROP TABLE metadata.marc_sub_field;
DROP TABLE metadata.marc_relator_code;
DROP TABLE metadata.marc_type_of_record;


DROP SEQUENCE metadata.metadata_type_seq;
DROP SEQUENCE metadata.dublin_core_term_seq;
DROP SEQUENCE metadata.dublin_core_encoding_scheme_seq;
DROP SEQUENCE metadata.marc_data_field_seq;
DROP SEQUENCE metadata.marc_sub_field_seq;
DROP SEQUENCE metadata.marc_relator_code_seq;
DROP SEQUENCE metadata.marc_type_of_record_seq;


DROP SCHEMA metadata;




