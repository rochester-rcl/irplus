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

-- ---------------------------------------------
-- Insert data into the tables
-- ---------------------------------------------
 

-- ---------------------------------------------
-- Default content types
-- ---------------------------------------------

insert into                                                         
ir_item.content_type ( content_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.content_type_seq'), 0, 'Book', null, 'BOOK');

insert into 
ir_item.content_type ( content_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.content_type_seq'), 0, 'Video', null, 'VIDEO');

insert into 
ir_item.content_type ( content_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.content_type_seq'), 0, 'Image', null, 'IMAGE');

insert into 
ir_item.content_type ( content_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.content_type_seq'), 0, 'Musical Score', null, 'MUSICAL_SCORE');

insert into 
ir_item.content_type ( content_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.content_type_seq'), 0, 'Working Paper', null, 'WORKING_PAPER');

insert into 
ir_item.content_type ( content_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.content_type_seq'), 0, 'Technical Report', null, 'TECHNICAL_REPORT');

insert into 
ir_item.content_type ( content_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.content_type_seq'), 0, 'Recording, Oral', null, 'RECORDING');

insert into 
ir_item.content_type ( content_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.content_type_seq'), 0, 'Learning Object', null, 'LEARNING_OBJECT');

insert into 
ir_item.content_type ( content_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.content_type_seq'), 0, 'Article', null, 'ARTICLE');

insert into 
ir_item.content_type ( content_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.content_type_seq'), 0, 'Book Chapter', null, 'BOOK_CHAPTER');

insert into 
ir_item.content_type ( content_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.content_type_seq'), 0, 'Preprint', null, 'PREPRINT');

insert into 
ir_item.content_type ( content_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.content_type_seq'), 0, 'Plan', null, 'PLAN');

insert into 
ir_item.content_type ( content_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.content_type_seq'), 0, 'Blueprint', null, 'BLUEPRINT');

insert into 
ir_item.content_type ( content_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.content_type_seq'), 0, 'Recording, Musical', null, 'RECORDING_MUSICAL');

insert into 
ir_item.content_type ( content_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.content_type_seq'), 0, 'Dataset', null, 'DATASET');

insert into 
ir_item.content_type ( content_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.content_type_seq'), 0, 'Image, 3-D', null, 'IMAGE_3_D');

insert into 
ir_item.content_type ( content_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.content_type_seq'), 0, 'Recording, Acoustical', null, 'RECORDING_ACOUSTICAL');

insert into 
ir_item.content_type ( content_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.content_type_seq'), 0, 'Software', null, 'SOFTWARE');

insert into 
ir_item.content_type ( content_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.content_type_seq'), 0, 'Presentation', null, 'PRESENATATION');

insert into 
ir_item.content_type ( content_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.content_type_seq'), 0, 'Animation', null, 'ANIMATION');

insert into 
ir_item.content_type ( content_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.content_type_seq'), 0, 'Form', null, 'FORM');

-- ---------------------------------------------
-- Default Contributor types
-- ---------------------------------------------
insert into 
person.contributor_type ( contributor_type_id, version, name, unique_system_code, description) 
values (nextval('person.contributor_type_seq'), 0, 'Author', 'AUTHOR', null);

insert into 
person.contributor_type ( contributor_type_id, version, name, unique_system_code, description) 
values (nextval('person.contributor_type_seq'), 0, 'Thesis Advisor', 'THESIS_ADVISOR', null);

insert into 
person.contributor_type ( contributor_type_id, version, name, unique_system_code, description) 
values (nextval('person.contributor_type_seq'), 0, 'Illustrator', 'ILLUSTRATOR', null);

insert into 
person.contributor_type ( contributor_type_id, version, name, unique_system_code, description) 
values (nextval('person.contributor_type_seq'), 0, 'Composer', 'COMPOSER', null);

insert into 
person.contributor_type ( contributor_type_id, version, name, unique_system_code, description) 
values (nextval('person.contributor_type_seq'), 0, 'Artist', 'ARTIST', null);

insert into 
person.contributor_type ( contributor_type_id, version, name, unique_system_code, description) 
values (nextval('person.contributor_type_seq'), 0, 'Editor', 'EDITOR', null);

insert into 
person.contributor_type ( contributor_type_id, version, name, unique_system_code, description) 
values (nextval('person.contributor_type_seq'), 0, 'Donor', 'DONOR',  null);

insert into 
person.contributor_type ( contributor_type_id, version, name, unique_system_code, description) 
values (nextval('person.contributor_type_seq'), 0, 'Photographer', 'PHOTOGRAPHER', null);

-- ---------------------------------------------
-- Default Languages
-- ---------------------------------------------

insert into 
ir_item.language_type ( language_type_id, version, name, description, iso639_1, 
iso639_2, unique_system_code ) 
values (nextval('ir_item.language_type_seq'), 0, 'English', null, 'en', 'eng', 'ENG');

insert into 
ir_item.language_type ( language_type_id, version, name, description, iso639_1, 
iso639_2, unique_system_code ) 
values (nextval('ir_item.language_type_seq'), 0, 'French', null, 'fr', 'fre', 'FRE');

insert into 
ir_item.language_type ( language_type_id, version, name, description, iso639_1, 
iso639_2, unique_system_code  ) 
values (nextval('ir_item.language_type_seq'), 0, 'German', null, 'de', 'ger', 'GER');

insert into 
ir_item.language_type ( language_type_id, version, name, description, iso639_1, 
iso639_2, unique_system_code ) 
values (nextval('ir_item.language_type_seq'), 0, 'Japanese', null, 'ja', 'jpn', 'JPN');

insert into 
ir_item.language_type ( language_type_id, version, name, description, iso639_1, 
iso639_2, unique_system_code ) 
values (nextval('ir_item.language_type_seq'), 0, 'Spanish; Castilian', null, 'es', 
'spa', 'SPA');

insert into 
ir_item.language_type ( language_type_id, version, name, description, iso639_1, 
iso639_2, unique_system_code ) 
values (nextval('ir_item.language_type_seq'), 0, 'Chinese', null, 'zh', 'chi', 'CHI');

insert into 
ir_item.language_type ( language_type_id, version, name, description, iso639_1, 
iso639_2, unique_system_code ) 
values (nextval('ir_item.language_type_seq'), 0, 'Italian', null, 'it', 'ita', 'ITA');

-- ---------------------------------------------
-- Default Identifier type
-- ---------------------------------------------

insert into 
ir_item.identifier_type ( identifier_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.identifier_type_seq'), 0, 'ISSN', null, 'ISSN');

insert into 
ir_item.identifier_type ( identifier_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.identifier_type_seq'), 0, 'ISBN', null, 'ISBN');

insert into 
ir_item.identifier_type ( identifier_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.identifier_type_seq'), 0, 'ISMN', null, 'ISMN');

insert into 
ir_item.identifier_type ( identifier_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.identifier_type_seq'), 0, 'URI', null, 'URI');

insert into 
ir_item.identifier_type ( identifier_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.identifier_type_seq'), 0, 'Gov Doc#', null, 'GOVT_DOC');

insert into 
ir_item.identifier_type ( identifier_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.identifier_type_seq'), 0, 'LCSH', null, 'LCSH');

insert into 
ir_item.identifier_type ( identifier_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.identifier_type_seq'), 0, 'MeSH', null, 'MESH');

insert into 
ir_item.identifier_type ( identifier_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.identifier_type_seq'), 0, 'is part of', null, 'IS_PART_OF');

insert into 
ir_item.identifier_type ( identifier_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.identifier_type_seq'), 0, 'Unknown', null, 'UNKNOWN_IDENTIFIER');

insert into 
ir_item.identifier_type ( identifier_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.identifier_type_seq'), 0, 'Handle', null, 'HANDLE');

insert into 
ir_item.identifier_type ( identifier_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.identifier_type_seq'), 0, 'LC Call No.', null, 'LC_CALL_NO');

insert into 
ir_item.identifier_type ( identifier_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.identifier_type_seq'), 0, 'SML Accession No.', null, 'SML_ACCESSION_NO');

insert into 
ir_item.identifier_type ( identifier_type_id, version, name, description, unique_system_code) 
values (nextval('ir_item.identifier_type_seq'), 0, 'Publisher No.', null, 'PUBLISHER_NO');



-- ---------------------------------------------
-- Mime Application Types 
-- ---------------------------------------------

insert into
mime.top_media_type ( top_media_type_id, version, name, description) 
values (nextval('mime.top_media_type_seq'), 0, 'text', 'The subtype "plain" in 

particular indicates plain text containing no formatting commands or directives of 

any sort. Plain text is intended to be displayed "as-is". No special software is 

required to get the full meaning of the text, aside from support for the indicated 

character set. Other subtypes are to be used for enriched text in forms where 

application software may enhance the appearance of the text, but such software must 

not be required in order to get the general idea of the content.  Possible subtypes 

of "text" thus include any word processor format that can be read without resorting 

to software that understands the format. In particular, formats that employ 

embeddded binary formatting information are not considered directly readable. A 

very simple and portable subtype, "richtext", was defined in RFC 1341, with a 

further revision in RFC 1896 under the name "enriched".');

insert into
mime.top_media_type ( top_media_type_id, version, name, description) 
values (nextval('mime.top_media_type_seq'), 0, 'image', 'requires a display device 

(such as a graphical display, a graphics printer, or a FAX machine) to view the 

information. An initial subtype is defined for the widely-used image format JPEG. 

subtypes are defined for two widely-used image formats, jpeg and gif');

insert into
mime.top_media_type ( top_media_type_id, version, name, description) 
values (nextval('mime.top_media_type_seq'), 0, 'audio', 'requires an audio output 

device (such as a speaker or a telephone) to "display" the contents.  An initial 

subtype "basic" is defined in this document.');

insert into
mime.top_media_type ( top_media_type_id, version, name, description) 
values (nextval('mime.top_media_type_seq'), 0, 'video', 'requires an audio output 

device (such as a speaker or a telephone) to "display" the contents.  An initial 

subtype "basic" is defined in this document.');

insert into
mime.top_media_type ( top_media_type_id, version, name, description) 
values (nextval('mime.top_media_type_seq'), 0, 'application', 'some other kind of 

data, typically either uninterpreted binary data or information to be processed by 

an application.  The subtype "octet-stream" is to be used in the case of 

uninterpreted  binary data, in which case the simplest recommended action is to 

offer to write the information into a file for the user.  The "PostScript" subtype 

is also defined for the transport of PostScript material.  Other expected uses for 

"application" include spreadsheets, data for mail-based scheduling systems, and 

languages for "active" (computational) messaging, and word processing formats that 

are not directly readable. Note that security considerations may exist for some 

types of application data, most notably  "application/PostScript" and any form of 

active  messaging.  These issues are discussed later in this document.');

-- ---------------------------------------------
-- Application Types 
-- ---------------------------------------------

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'envoy', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'fractals', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'futuresplash', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'mac-binhex40', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'msword', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'octet-stream', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'oda', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'olescript', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'pdf', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'pics-rules', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'pkcs10', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'pkix-crl', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'postscript', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 

'set-payment-initiation', '' from mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 

'set-registration-initiation', '' from mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.ms-excel', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.ms-pkicertstore', 

'' from mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.ms-pkiseccat', '' 

from mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.ms-pkistl', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.ms-powerpoint', '' 

from mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.ms-project', '' 

from mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.ms-works', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'winhlp', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-bcpio', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-cdf', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-compress', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-compressed', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-csh', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-director', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-dvi', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-gtar', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-gzip', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-hdf', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-internet-signup', '' 

from mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-iphone', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-javascript', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-latex', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-msaccess', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-mscardfile', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-msclip', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-msdownload', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-msmediaview', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-msmetafile', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-msmoney', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-mspublisher', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-msschedule', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-msterminal', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-mswrite', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-netcdf', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-perfmon', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-pkcs12', '' from 

mime.top_media_type
where name = 'application';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-pkcs7-certificates', 

'' from mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-pkcs7-certreqresp', 

'' from mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-pkcs7-mime', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-pkcs7-signature', '' 

from mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-sh', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-shar', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-stuffit', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-sv4cpio', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-tar', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-tcl', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-tex', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-texinfo', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-troff', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-troff-man', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-troff-me', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-troff-ms', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-ustar', '' from 

mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'x-x509-ca-cert', '' 

from mime.top_media_type
where name = 'application';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'ynd.ms-pkipko', '' from 

mime.top_media_type
where name = 'application';  


insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'zip', '' from 

mime.top_media_type
where name = 'application';  

-- ---------------------------------------------
-- Image Types 
-- ---------------------------------------------

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'cgm', '' from 

mime.top_media_type
where name = 'image';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'example', '' from 

mime.top_media_type
where name = 'image';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'fits', '' from 

mime.top_media_type
where name = 'image';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'g3fax', '' from 

mime.top_media_type
where name = 'image';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'gif', '' from 

mime.top_media_type
where name = 'image';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'ief', '' from 

mime.top_media_type
where name = 'image';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'jp2', '' from 

mime.top_media_type
where name = 'image';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'jpeg', '' from 

mime.top_media_type
where name = 'image'; 

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'jpm', '' from 

mime.top_media_type
where name = 'image';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'jpx', '' from 

mime.top_media_type
where name = 'image';  

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'naplps', '' from 

mime.top_media_type
where name = 'image';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'png', '' from 

mime.top_media_type
where name = 'image';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'prs.btif', '' from 

mime.top_media_type
where name = 'image';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'prs.pti', '' from 

mime.top_media_type
where name = 'image';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 't38', '' from 

mime.top_media_type
where name = 'image';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'tiff', '' from 

mime.top_media_type
where name = 'image';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'tiff-fx', '' from 

mime.top_media_type
where name = 'image';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.adobe.photoshop', 

'' from mime.top_media_type
where name = 'image';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.cns.inf2', '' from 

mime.top_media_type
where name = 'image';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.djvu', '' from 

mime.top_media_type
where name = 'image';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.dwg', '' from 

mime.top_media_type
where name = 'image';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.dxf', '' from 

mime.top_media_type
where name = 'image';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.fastbidsheet', '' 

from mime.top_media_type
where name = 'image';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.fpx', '' from 

mime.top_media_type
where name = 'image';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.fst', '' from 

mime.top_media_type
where name = 'image';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 

'vnd.fujixerox.edmics-mmr', '' from mime.top_media_type
where name = 'image';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 

'vnd.fujixerox.edmics-rlc', '' from mime.top_media_type
where name = 'image';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 

'vnd.globalgraphics.pgb', '' from mime.top_media_type
where name = 'image';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.microsoft.icon', '' 

from mime.top_media_type
where name = 'image';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.mix', '' from 

mime.top_media_type
where name = 'image';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.ms-modi', '' from 

mime.top_media_type
where name = 'image';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.net-fpx', '' from 

mime.top_media_type
where name = 'image';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.sealed.png', '' 

from mime.top_media_type
where name = 'image';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 

'vnd.sealedmedia.softseal.gif', '' from mime.top_media_type
where name = 'image';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 

'vnd.sealedmedia.softseal.jpg', '' from mime.top_media_type
where name = 'image';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.svf', '' from 

mime.top_media_type
where name = 'image';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.wap.wbmp', '' from 

mime.top_media_type
where name = 'image';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.xiff', '' from 

mime.top_media_type
where name = 'image';



-- ---------------------------------------------
-- Audio Types 
-- ---------------------------------------------

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, '32kadpcm', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, '3gpp', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, '3gpp2', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'ac3', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'AMR', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'AMR-WB', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'amr-wb+', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'asc', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'basic', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'BV16', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'BV32', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'clearmode', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'CN', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'DAT12', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'dls', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'dsr-es201108', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'dsr-es202050', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'dsr-es202211', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'dsr-es202212', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'eac3', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'DVI4', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'EVRC', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'EVRC0', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'EVRC-QCP', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'G722', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'G7221', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'G723', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'G726-16', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'G726-24', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'G726-32', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'G726-40', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'G728', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'G729', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'G729D', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'G729E', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'GSM', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'GSM-EFR', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'iLBC', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'L8', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'L16', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'L20', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'L24', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'LPC', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'MPA', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'mp4', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'MP4A-LATM', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'mpa-robust', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'mpeg', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'mpeg4-generic', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'parityfec', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'PCMA', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'PCMU', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'prs.sid', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'QCELP', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'RED', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'rtp-midi', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'rtx', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'SMV', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'SMV0', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'SMV-QCP', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 't140c', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 't38', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'telephone-event', '' 

from mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'tone', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'VDVI', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'VMR-WB', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.3gpp.iufp', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.4SB', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.audiokoz', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.CELP', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.cisco.nse', '' from 

mime.top_media_type
where name = 'audio'; 

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 

'vnd.cmles.radio-events', '' from mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.cns.anp1', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.cns.inf1', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.digital-winds', '' 

from mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.dlna.adts', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.everad.plj', '' 

from mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.hns.audio', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.lucent.voice', '' 

from mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.nokia.mobile-xmf', 

'' from mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.nortel.vbk', '' 

from mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.nuera.ecelp4800', 

'' from mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.nuera.ecelp7470', 

'' from mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.nuera.ecelp9600', 

'' from mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.octel.sbc', '' from 

mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.rhetorex.32kadpcm', 

'' from mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 

'vnd.sealedmedia.softseal.mpeg', '' from mime.top_media_type
where name = 'audio';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.vmx.cvsd', '' from 

mime.top_media_type
where name = 'audio';

-- ---------------------------------------------
-- Text Types 
-- ---------------------------------------------


insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'calendar', '' from 

mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'css', '' from 

mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'csv', '' from 

mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'directory', '' from 

mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'dns', '' from 

mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'enriched', '' from 

mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'example', '' from 

mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'html', '' from 

mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'parityfec', '' from 

mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'plain', '' from 

mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'prs.fallenstein.rst', 

'' from mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'prs.lines.tag', '' from 

mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'RED', '' from 

mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'rfc822-headers', '' 

from mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'richtext', '' from 

mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'rtf', '' from 

mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'rtx', '' from 

mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'sgml', '' from 

mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 't140', '' from 

mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'tab-separated-values', 

'' from mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'troff', '' from 

mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'uri-list', '' from 

mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.abc', '' from 

mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.curl', '' from 

mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.DMClientScript', '' 

from mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 

'vnd.esmertec.theme-descriptor', '' from mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.fly', '' from 

mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.fmi.flexstor', '' 

from mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.in3d.3dml', '' from 

mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.in3d.spot', '' from 

mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.IPTC.NewsML', '' 

from mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.IPTC.NITF', '' from 

mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.latex-z', '' from 

mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.motorola.reflex', 

'' from mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.ms-mediapackage', 

'' from mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 

'vnd.net2phone.commcenter.command', '' from mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 

'vnd.sun.j2me.app-descriptor', '' from mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.wap.si', '' from 

mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.wap.sl', '' from 

mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.wap.wml', '' from 

mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.wap.wmlscript', '' 

from mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'xml', '' from 

mime.top_media_type
where name = 'text';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 

'xml-external-parsed-entity', '' from mime.top_media_type
where name = 'text';


-- ---------------------------------------------
-- Video Types 
-- ---------------------------------------------


insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, '3gpp', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, '3gpp2', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, '3gpp-tt', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'BMPEG', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'BT656', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'CelB', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'DV', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'example', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'H261', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'H263', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'H263-1998', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'H263-2000', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'H264', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'JPEG', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'MJ2', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'MP1S', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'MP2P', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'MP2T', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'mp4', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'MP4V-ES', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'MPV', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'mpeg', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'mpeg4-generic', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'nv', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'parityfec', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'pointer', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'quicktime', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'raw', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'rtx', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'SMPTE292M', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vc1', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.dlna.mpeg-tts', '' 

from mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.fvt', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.hns.video', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.motorola.video', '' 

from mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.motorola.videop', 

'' from mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.mpegurl', '' from 

mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 

'vnd.nokia.interleaved-multimedia', '' from mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.objectvideo', '' 

from mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.sealed.mpeg1', '' 

from mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.sealed.mpeg4', '' 

from mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.sealed.swf', '' 

from mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 

'vnd.sealedmedia.softseal.mov', '' from mime.top_media_type
where name = 'video';

insert into mime.sub_type ( sub_type_id, top_media_type_id, version, name, 

description) 
select nextval('mime.sub_type_seq'), top_media_type_id, 0, 'vnd.vivo', '' from 

mime.top_media_type
where name = 'video';


-- ---------------------------------------------
-- Mime Extensions
-- ---------------------------------------------


insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'mpa', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'mpeg'
and mime.top_media_type.name = 'video'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'mpe', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from  
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'mpeg'
and mime.top_media_type.name = 'video'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'mpeg', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'mpeg'
and mime.top_media_type.name = 'video'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'mpg', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'mpeg'
and mime.top_media_type.name = 'video'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'mpv2', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from  
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'mpeg'
and mime.top_media_type.name = 'video'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'mp3', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'mpeg'
and mime.top_media_type.name = 'audio'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'mp2', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'mpeg'
and mime.top_media_type.name = 'video'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'evy', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'envoy'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'fif', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'fractals'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'spl', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'futuresplash'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;


insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'hta', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'hta'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'acx', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'internet-property-stream'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'hqx', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'mac-binhex40'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'doc', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'msword'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'dot', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'msword'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, '*', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'octet-stream'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'bin', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'octet-stream'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'class', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'octet-stream'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'dms', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'octet-stream'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'exe', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'octet-stream'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'lha', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'octet-stream'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'lzh', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'octet-stream'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'oda', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'oda'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'axs', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'olescript'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'pdf', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'pdf'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'prf', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'pics-rules'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'p10', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'pkcs10'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'crl', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'pkix-crl'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'ai', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'postscript'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'eps', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'postscript'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'ps', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'postscript'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'rtf', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'rtf'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'setpay', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'set-payment-initiation'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'setreg', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'set-registration-initiation'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'xla', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'vnd.ms-excel'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'xlc', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'vnd.ms-excel'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'xlm', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'vnd.ms-excel'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'xls', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'vnd.ms-excel'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'xlt', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'vnd.ms-excel'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'xlw', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'vnd.ms-excel'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'sst', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'vnd.ms-pkicertstore'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'cat', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'vnd.ms-pkiseccat'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'stl', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'vnd.ms-pkistl'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'pot', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'vnd.ms-powerpoint'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'pps', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'vnd.ms-powerpoint'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'ppt', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'vnd.ms-powerpoint'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'mpp', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'vnd.ms-project'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'wcm', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'vnd.ms-works'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'wdb', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'vnd.ms-works'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'wks', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'vnd.ms-works'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'wps', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'vnd.ms-works'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'hlp', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'winhlp'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'bcpio', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-bcpio'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'cdf', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-cdf'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'z', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-compress'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'tgz', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-compressed'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'cpio', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-cpio'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'csh', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-csh'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'dcr', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-director'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'dir', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-director'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'dxr', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-director'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'dvi', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-dvi'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'gtar', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-gtar'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'gz', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-gzip'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'hdf', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-hdf'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'ins', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-internet-signup'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'iii', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-iphone'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'js', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-javascript'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'latex', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-latex'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'mdb', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-msaccess'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'crd', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-mscardfile'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'clp', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-msclip'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'dll', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-msdownload'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'm13', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-msmediaview'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'm14', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-msmediaview'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'mvb', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-msmediaview'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'wmf', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-msmetafile'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'mny', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-msmoney'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'pub', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-mspublisher'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'scd', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-msschedule'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'trm', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-msterminal'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'wri', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-mswrite'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'cdf', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-netcdf'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'nc', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-netcdf'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'pma', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-perfmon'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'pmc', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-perfmon'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'pml', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-perfmon'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'pmr', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-perfmon'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'pmw', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-perfmon'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'p12', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-pkc12'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'pfx', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-pkc12'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'p7b', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-pkcs7-certificates'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'spc', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-pkcs7-certificates'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'p7r', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-pkcs7-certreqresp'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'p7c', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-pkcs7-mime'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'p7m', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-pkcs7-mime'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'p7s', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-pkcs7-signature'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'sh', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-sh'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'shar', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-shar'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'sit', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-stuffit'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'sv4cpio', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-sv4cpio'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'sv4crc', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-sv4crc'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'tar', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-tar'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'tcl', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-tcl'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'tex', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-tex'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'texi', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-texinfo'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'texinfo', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-texinfo'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'roff', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-troff'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 't', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-troff'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'tr', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-troff'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'man', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-troff-man'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'me', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-troff-me'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'ms', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-troff-ms'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'ustar', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-ustar'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'src', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-wais-source'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'cer', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-x509-ca-cert'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'crt', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-x509-ca-cert'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'der', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-x509-ca-cert'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'pko', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'ynd.ms-pkipko'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'zip', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'zip'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'au', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'basic'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'snd', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'basic'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'mid', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'mid'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'rmi', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'mid'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'aif', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-aiff'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'aifc', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-aiff'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'aiff', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-aiff'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'm3u', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-mpegurl'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'ra', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-pn-realaudio'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'ram', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-pn-realaudio'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'wav', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-wav'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'bmp', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'bmp'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'cod', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'cis-cod'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'gif', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'gif'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'ief', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'ief'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'jpe', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'jpeg'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'jpeg', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'jpeg'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'jpg', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'jpeg'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'jfif', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'pipeg'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'tif', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'tiff'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'tiff', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'tiff'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'ras', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-cmu-raster'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'cmx', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-cmx'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'ico', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-icon'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'pnm', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-portable-anymap'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'pbm', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-portable-bitmap'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'pgm', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-portable-graymap'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type)
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'ppm', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-portable-pixmap'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'rgb', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-rgb'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'xbm', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-xbitmap'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'xpm', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-pixmap'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'xwd', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-xwindowdump'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'css', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'css'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, '323', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'h323'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'htm', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'html'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'html', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'html'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'stm', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'html'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'uls', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'iuls'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'bas', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'plain'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'c', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'plain'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'h', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'plain'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'txt', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'plain'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'rtx', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'richtext'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'sct', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'scriptlet'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'tsv', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'tab-separated-values'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'htt', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'webviewhtml'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'htc', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-component'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'etx', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-setext'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'vcf', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-vcard'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'mov', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'quicktime'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'qt', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'quicktime'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'lsf', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-la-asf'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'lsx', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-la-asf'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'asf', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-ms-asf'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'asr', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-ms-asf'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'asx', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-ms-asf'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'avi', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-msvideo'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

insert into mime.sub_type_extension ( sub_type_extension_id, sub_type_id, version, 

extension, mime_type) 
select nextval('mime.sub_type_extension_seq'), sub_type_id, 0, 'movie', 

mime.top_media_type.name || 
'/' || mime.sub_type.name from 
mime.sub_type, mime.top_media_type
where mime.sub_type.name = 'x-sgi-movie'
and mime.sub_type.top_media_type_id = mime.top_media_type.top_media_type_id;

-- ---------------------------------------------
-- Security class types
-- ---------------------------------------------

insert into ir_security.class_type ( class_type_id, name, description, version )
select nextval('ir_security.class_type_seq'), 'edu.ur.ir.repository.UrPublished', 

'The UrPublished class type', 0;


-- ---------------------------------------------
-- class type permissions.
-- ---------------------------------------------
insert into ir_security.class_type_permission ( class_type_permission_id,
    class_type_id, name, description, version )
select nextval('ir_security.class_type_permission_seq'), 

ir_security.class_type.class_type_id,
'READ', 'allow users to read the 

ur published object', 0
from ir_security.class_type
where ir_security.class_type.name = 'edu.ur.ir.repository.UrPublished'; 

-- ---------------------------------------------
-- Insert into the acl table the ur object
-- ---------------------------------------------

insert into ir_security.acl (acl_id, parent_id, entries_inheriting, 

object_id, class_type_id)
 values (nextval('ir_security.acl_seq'), null, false,  

1, currval('ir_security.class_type_seq'));

-- ---------------------------------------------
-- Insert the roles into the database
-- ---------------------------------------------
insert into ir_user.role (role_id, version, name, description)
select nextval('ir_user.role_seq'), 0, 'ROLE_ADMIN', 'Indicates the user is an 

administrator';

insert into ir_user.role (role_id, version, name, description)
select nextval('ir_user.role_seq'), 0, 'ROLE_COLLABORATOR', 'Indicates the user is a
collaborator';

insert into ir_user.role (role_id, version, name, description)
select nextval('ir_user.role_seq'), 0, 'ROLE_RESEARCHER', 'Indicates the user is a 
researcher';

insert into ir_user.role (role_id, version, name, description)
select nextval('ir_user.role_seq'), 0, 'ROLE_AUTHOR', 'Indicates the user is a 
Author';

insert into ir_user.role (role_id, version, name, description)
select nextval('ir_user.role_seq'), 0, 'ROLE_IMPORTER', 
'Indicates the user can import files';

insert into ir_user.role (role_id, version, name, description)
select nextval('ir_user.role_seq'), 0, 'ROLE_COLLECTION_ADMIN', 'Indicates the user is a 
Collection Administrator';

insert into ir_user.role (role_id, version, name, description)
select nextval('ir_user.role_seq'), 0, 'ROLE_AFFLIATION_APPROVER', 
'Indicates the user can approve user affilations';

insert into ir_user.role (role_id, version, name, description)
select nextval('ir_user.role_seq'), 0, 'ROLE_GROUP_WORKSPACE_CREATOR', 
'Indicates the user can create group workspaces';

-- ---------------------------------------------
-- Insert the admin role into the database
-- ---------------------------------------------

insert into ir_user.user_role(user_id, role_id)
select ir_user.ir_user.user_id, ir_user.role.role_id from
ir_user.ir_user, ir_user.role
where ir_user.ir_user.username = 'admin'
and ir_user.role.name ='ROLE_ADMIN';

insert into ir_user.user_role(user_id, role_id)
select ir_user.ir_user.user_id, ir_user.role.role_id from
ir_user.ir_user, ir_user.role
where ir_user.ir_user.username = 'admin'
and ir_user.role.name ='ROLE_RESEARCHER';

insert into ir_user.user_role(user_id, role_id)
select ir_user.ir_user.user_id, ir_user.role.role_id from
ir_user.ir_user, ir_user.role
where ir_user.ir_user.username = 'admin'
and ir_user.role.name ='ROLE_USER';

insert into ir_user.user_role(user_id, role_id)
select ir_user.ir_user.user_id, ir_user.role.role_id from
ir_user.ir_user, ir_user.role
where ir_user.ir_user.username = 'admin'
and ir_user.role.name ='ROLE_AUTHOR';

insert into ir_user.user_role(user_id, role_id)
select ir_user.ir_user.user_id, ir_user.role.role_id from
ir_user.ir_user, ir_user.role
where ir_user.ir_user.username = 'admin'
and ir_user.role.name ='COLLECTION_AUTHOR';

insert into ir_user.user_role(user_id, role_id)
select ir_user.ir_user.user_id, ir_user.role.role_id from
ir_user.ir_user, ir_user.role
where ir_user.ir_user.username = 'admin'
and ir_user.role.name ='ROLE_IMPORTER';

insert into ir_user.user_role(user_id, role_id)
select ir_user.ir_user.user_id, ir_user.role.role_id from
ir_user.ir_user, ir_user.role
where ir_user.ir_user.username = 'admin'
and ir_user.role.name ='ROLE_AFFLIATION_APPROVER';

insert into ir_user.user_role(user_id, role_id)
select ir_user.ir_user.user_id, ir_user.role.role_id from
ir_user.ir_user, ir_user.role
where ir_user.ir_user.username = 'admin'
and ir_user.role.name ='ROLE_GROUP_WORKSPACE_CREATOR';


-- ---------------------------------------------
-- Insert the user role into the database
-- ---------------------------------------------
insert into ir_user.role (role_id, version, name, description)
select nextval('ir_user.role_seq'), 0, 'ROLE_USER', 'Indicates this is a registered 

user in the system';


insert into ir_user.user_role(user_id, role_id)
select ir_user.ir_user.user_id, ir_user.role.role_id from
ir_user.ir_user, ir_user.role
where ir_user.ir_user.username = 'admin'
and ir_user.role.name ='ROLE_USER';

-- ---------------------------------------------
-- Insert type of thumbnail we are creating
-- ---------------------------------------------

insert into ir_file.transformed_file_type ( transformed_file_type_id, 
name, description, system_code, version)
select nextval('ir_file.transformed_file_type_seq'), 'primary thumbnail', 
'primary thumbnail to be shown', 'PRIMARY_THUMBNAIL',
0;


-- ---------------------------------------------
-- Insert Affiliations
-- ---------------------------------------------

insert into ir_user.affiliation( affiliation_id, 
name, description, version, is_author, is_researcher, needs_approval)
select nextval('ir_user.affiliation_seq'), 'Not Affiliated', 
'User is not affiliated with the university', 0, false, false, false;


insert into ir_user.affiliation( affiliation_id, 
name, description, version, is_author, is_researcher, needs_approval)
select nextval('ir_user.affiliation_seq'), 'Staff', 
'User is a staff in the university', 0,false, false, false;


insert into ir_user.affiliation( affiliation_id, 
name, description, version, is_author, is_researcher, needs_approval)
select nextval('ir_user.affiliation_seq'), 'Undergraduate student', 
'User is an undergraduate student at the university', 0,false, false, false;

insert into ir_user.affiliation( affiliation_id, 
name, description, version, is_author, is_researcher, needs_approval)
select nextval('ir_user.affiliation_seq'), 'Graduate student', 
'User is a graduate student at the university', 0,true, true, true;


insert into ir_user.affiliation( affiliation_id, 
name, description, version, is_author, is_researcher, needs_approval)
select nextval('ir_user.affiliation_seq'), 'Alumni', 
'User is an alumni of the university', 0,false, false, false;


insert into ir_user.affiliation( affiliation_id, 
name, description, version, is_author, is_researcher, needs_approval)
select nextval('ir_user.affiliation_seq'), 'Faculty', 
'User is a faculty member of the university', 0, true,true, true;

-- ---------------------------------------------
-- Insert Affiliations
-- ---------------------------------------------

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Anthropology', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Art & Art History', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Biology', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Brain & Cognitive Sciences', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Chemistry', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Clinical & Social Sciences in Psychology', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Computer Science', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Earth & Environmental Sciences', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Economics', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'English', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'History', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Linguistics', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Mathematics', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Modern Languages & Cultures', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Music', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Philosophy', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Physics & Astronomy', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Political Science', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Religion & Classics', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Biomedical Engineering', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Chemical Engineering', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Electrical and Computer Engineering', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Mechanical Engineering', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Institute of Optics', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Chamber Music', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Composition', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Conducting & Ensembles', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Humanities', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Jazz Studies & Contemporary Media', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Music Education', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Musicology', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Organ & Historical Keyboards', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Piano', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Strings, Harp, & Guitar', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Music Theory', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Voice & Opera', 
'', 0;

insert into ir_user.department( department_id, 
name, description, version)
select nextval('ir_user.department_seq'), 'Woodwinds, Brass, & Percussion', 
'', 0;

-- ---------------------------------------------
-- Default Extent types
-- ---------------------------------------------

insert into 
ir_item.extent_type ( extent_type_id, version, name, description) 
values (nextval('ir_item.extent_type_seq'), 0, 'Volumes', null);

insert into 
ir_item.extent_type ( extent_type_id, version, name, description) 
values (nextval('ir_item.extent_type_seq'), 0, 'Dimensions', null);

insert into 
ir_item.extent_type ( extent_type_id, version, name, description) 
values (nextval('ir_item.extent_type_seq'), 0, 'Number of Pages', null);

insert into 
ir_item.extent_type ( extent_type_id, version, name, description) 
values (nextval('ir_item.extent_type_seq'), 0, 'Lenght in Time', null);

-- ---------------------------------------------
-- Default processing types
-- ---------------------------------------------

insert into 
ir_index.index_processing_type ( index_processing_type_id, version, name, description) 
values (nextval('ir_index.index_processing_type_seq'), 0, 'INSERT', 'A record is to be inserted into the index');

insert into 
ir_index.index_processing_type ( index_processing_type_id, version, name, description) 
values (nextval('ir_index.index_processing_type_seq'), 0, 'UPDATE', 'A record is to be updated in the index');


insert into 
ir_index.index_processing_type ( index_processing_type_id, version, name, description) 
values (nextval('ir_index.index_processing_type_seq'), 0, 'UPDATE_NO_FILE_CHANGE', 'A record is to be updated in the 
index but the files have not changed in the record');


insert into 
ir_index.index_processing_type ( index_processing_type_id, version, name, description) 
values (nextval('ir_index.index_processing_type_seq'), 0, 'DELETE', 'A record is to be deleted from the index');

insert into 
ir_index.index_processing_type ( index_processing_type_id, version, name, description) 
values (nextval('ir_index.index_processing_type_seq'), 0, 'DELETE_INDEX', 'the index needs to be deleted and rebuilt');


-- -----------------------------------
--  Insert the dublin core 
--  encoding schemes 
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
-- Inserts the marc sub fields
-- ---------------------------------------------

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, 'a', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, 'b', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, 'c', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, 'd', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, 'e', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, 'f', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, 'g', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, 'h', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, 'i', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, 'j', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, 'k', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, 'l', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, 'm', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, 'n', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, 'o', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, 'p', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, 'q', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, 'r', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, 's', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, 't', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, 'u', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, 'v', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, 'w', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, 'x', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, 'y', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, 'z', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, '1', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, '2', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, '3', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, '4', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, '5', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, '6', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, '7', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, '8', '');

insert into metadata.marc_sub_field(marc_sub_field_id, version, name, description)
values(nextval('metadata.marc_sub_field_seq'), 0, '9', '');

-- ---------------------------------------------
-- Inserts the marc data fields
-- ---------------------------------------------

 
insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '010', 'Library of Congress Control Number', false, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '013', 'Patent Control Information', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '015', 'National Bibliography Number', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '016', 'National Bibliographic Agency Control Number', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '017', 'Copyright or Legal Deposit Number', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '018', 'Copyright Article-Fee Code', false, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '020', 'International Standard Book Number', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '022', 'International Standard Serial Number', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '024', 'Other Standard Identifier', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '025', 'Overseas Acquisition Number', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '026', 'Fingerprint Identifier', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '027', 'Standard Technical Report Number', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '028', 'Publisher Number', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '030', 'CODEN Designation', true, '');



insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '031', 'Musical Incipits Information', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '032', 'Postal Registration Number', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '033', 'Date/Time and Place of an Event', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '034', 'Coded Cartographic Mathematical Data', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '035', 'System Control Number', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '036', 'Original Study Number for Computer Data Files', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '037', 'Source of Acquisition', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '038', 'Record Content Licensor', false, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '040', 'Cataloging Source', false, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '041', 'Language Code', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '042', 'Authentication Code', false, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '043', 'Geographic Area Code', false, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '044', 'Country of Publishing/Producing Entity Code', false, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '045', 'Time Period of Content', false, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '046', 'Special Coded Dates', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '047', 'Form of Musical Composition Code', false, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '048', 'Number of Musical Instruments or Voices Codes', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '050', 'Library of Congress Call Number', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '051', 'Library of Congress Copy, Issue, Offprint Statement', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '052', 'Geographic Classification', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '055', 'Classification Numbers Assigned in Canada', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '060', 'National Library of Medicine Call Number', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '061', 'National Library of Medicine Copy Statement', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '066', 'Character Sets Present', false, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '070', 'National Agricultural Library Call Number', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '071', 'National Agricultural Library Copy Statement', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '072', 'Subject Category Code', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '074', 'GPO Item Number', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '080', 'Universal Decimal Classification Number', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '082', 'Dewey Decimal Classification Number', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '083', 'Additional Dewey Decimal Classification Number', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '084', 'Other Classification Number', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '085', 'Synthesized Classification Number Components', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '086', 'Government Document Classification Number', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '088', 'Report Number', true, '');



insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '090', 'Local Call Number - 090', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '091', 'Local Call Number - 091', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '092', 'Local Call Number - 092', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '093', 'Local Call Number - 093', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '094', 'Local Call Number - 094', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '095', 'Local Call Number - 095', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '096', 'Local Call Number - 096', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '097', 'Local Call Number - 097', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '098', 'Local Call Number - 098', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '099', 'Local Call Number - 099', true, '');




insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '100', 'Main Entry - Personal Name', false, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '110', 'Main Entry - Corporate Name', false, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '111', 'Main Entry - Meeting Name', false, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '130', 'Main Entry - Uniform Title', false, '');


insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '210', 'Abbreviated Title', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '222', 'Key Title', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '240', 'Uniform Title', false, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '242', 'Translation of Title by Cataloging Agency', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '243', 'Collective Uniform Title', false, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '245', 'Title Statement', false, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '246', 'Varying Form of Title', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '247', 'Former Title', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '250', 'Edition Statement', false, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '254', 'Musical Presentation Statement', false, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '255', 'Cartographic Mathematical Data', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '256', 'Computer File Characteristics', false, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '257', 'Country of Producing Entity', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '258', 'Philatelic Issue Data', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '260', 'Publication, Distribution, etc. (Imprint)', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '263', 'Projected Publication Date', false, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '270', 'Address', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '300', 'Physical Description', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '306', 'Playing Time', false, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '307', 'Hours, etc.', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '310', 'Current Publication Frequency', false, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '321', 'Former Publication Frequency', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '336', 'Content Type', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '337', 'Media Type', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '338', 'Carrier Type', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '340', 'Physical Medium', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '342', 'Geospatial Reference Data', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '343', 'Planar Coordinate Data', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '351', 'Organization and Arrangement of Materials', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '352', 'Digital Graphic Representation', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '355', 'Security Classification Control', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '357', 'Originator Dissemination Control', false, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '362', 'Dates of Publication and/or Sequential Designation', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '363', 'Normalized Date and Sequential Designation', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '365', 'Trade Price', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '366', 'Trade Availability Information', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '380', 'Form of Work', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '381', 'Other Distinguishing Characteristics of Work or Expression', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '382', 'Medium of Performance', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '383', 'Numeric Designation of Musical Work', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '384', 'Key', false, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '490', 'Series Statement', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '500', 'General Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '501', 'With Note ', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '502', 'Dissertation Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '504', 'Bibliography, etc. Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '505', 'Formatted Contents Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '506', 'Restrictions on Access Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '507', 'Scale Note for Graphic Material', false, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '508', 'Creation/Production Credits Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '510', 'Citation/References Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '511', 'Participant or Performer Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '513', 'Type of Report and Period Covered Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '514', 'Data Quality Note', false, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '515', 'Numbering Peculiarities Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '516', 'Type of Computer File or Data Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '518', 'Date/Time and Place of an Event Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '520', 'Summary, etc.', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '521', 'Target Audience Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '522', 'Geographic Coverage Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '524', 'Preferred Citation of Described Materials Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '525', 'Supplement Note ', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '526', 'Study Program Information Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '530', 'Additional Physical Form available Note ', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '533', 'Reproduction Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '534', 'Original Version Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '535', 'Location of Originals/Duplicates Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '536', 'Funding Information Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '538', 'System Details Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '540', 'Terms Governing Use and Reproduction Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '541', 'Immediate Source of Acquisition Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '542', 'Information Relating to Copyright Status', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '544', 'Location of Other Archival Materials Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '545', 'Biographical or Historical Data', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '546', 'Language Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '547', 'Former Title Complexity Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '550', 'Issuing Body Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '552', 'Entity and Attribute Information Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '555', 'Cumulative Index/Finding Aids Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '556', 'Information About Documentation Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '561', 'Ownership and Custodial History', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '562', 'Copy and Version Identification Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '563', 'Binding Information', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '565', 'Case File Characteristics Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '567', 'Methodology Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '580', 'Linking Entry Complexity Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '581', 'Publications About Described Materials Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '583', 'Action Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '584', 'Accumulation and Frequency of Use Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '585', 'Exhibitions Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '586', 'Awards Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '588', 'Source of Description Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '600', 'Subject Added Entry - Personal Name', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '610', 'Subject Added Entry - Corporate Name', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '611', 'Subject Added Entry - Meeting Name', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '630', 'Subject Added Entry - Uniform Title', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '648', 'Subject Added Entry - Chronological Term', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '650', 'Subject Added Entry - Topical Term', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '651', 'Subject Added Entry - Geographic Name ', true, '');


insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '653', 'Index Term - Uncontrolled', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '654', 'Subject Added Entry - Faceted Topical Terms', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '655', 'Index Term - Genre/Form', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '656', 'Index Term - Occupation', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '657', 'Index Term - Function', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '658', 'Index Term - Curriculum Objective', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '662', 'Subject Added Entry - Hierarchical Place Name', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '700', 'Added Entry - Personal Name', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '710', 'Added Entry - Corporate Name', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '711', 'Added Entry - Meeting Name', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '720', 'Added Entry - Uncontrolled Name', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '730', 'Added Entry - Uniform Title', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '740', 'Added Entry - Uncontrolled Related/Analytical Title', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '751', 'Added Entry - Geographic Name', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '752', 'Added Entry - Hierarchical Place Name', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '753', 'System Details Access to Computer Files', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '754', 'Added Entry - Taxonomic Identification', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '760', 'Main Series Entry', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '762', 'Subseries Entry', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '765', 'Original Language Entry', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '767', 'Translation Entry', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '770', 'Supplement/Special Issue Entry', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '772', 'Supplement Parent Entry', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '773', 'Host Item Entry', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '774', 'Constituent Unit Entry', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '775', 'Other Edition Entry ', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '776', 'Additional Physical Form Entry', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '777', 'Issued With Entry', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '780', 'Preceding Entry', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '785', 'Succeeding Entry', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '786', 'Data Source Entry', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '787', 'Other Relationship Entry', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '800', 'Series Added Entry - Personal Name', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '810', 'Series Added Entry - Corporate Name', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '811', 'Series Added Entry - Meeting Name', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '830', 'Series Added Entry - Uniform Title', true, '');



insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '841', 'Holdings Coded Data Values', false, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '842', 'Textual Physical Form Designator', false, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '843', 'Reproduction Note', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '844', 'Name of Unit', false, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '845', 'Terms Governing Use and Reproduction', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '850', 'Holding Institution', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '852', 'Location', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '853', 'Captions and Pattern - Basic Bibliographic Unit', true, '');


insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '854', 'Captions and Pattern - Supplementary Material', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '855', 'Captions and Pattern - Indexes', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '856', 'Electronic Location and Access', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '863', 'Enumeration and Chronology - Basic Bibliographic Unit', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '864', 'Enumeration and Chronology - Supplementary Material', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '865', 'Enumeration and Chronology - Indexes', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '866', 'Textual Holdings - Basic Bibliographic Unit', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '867', 'Textual Holdings - Supplementary Material', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '868', 'Textual Holdings - Indexes', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '876', 'Item Information - Basic Bibliographic Unit', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '877', 'Item Information - Supplementary Material', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '878', 'Item Information - Indexes', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '880', 'Alternate Graphic Representation', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '882', 'Replacement Record Information', false, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '886', 'Foreign MARC Information Field', true, '');

insert into metadata.marc_data_field(marc_data_field_id, version, code, name, repeatable, description)
values(nextval('metadata.marc_data_field_seq'), 0, '887', 'Non-MARC Information Field ', true, '');


-- ---------------------------------------------
-- Inserts into the marc relator code table
-- ---------------------------------------------
insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Actor', 'act','Use for a person or organization who principally exhibits acting skills in a musical or dramatic presentation or entertainment.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Adapter', 'adp','Use for a person or organization who 1) reworks a musical composition, usually for a different medium, or 2) rewrites novels or stories for motion pictures or other audiovisual medium.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Analyst', 'anl','Use for a person or organization that reviews, examines and interprets data or information in a specific area.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Animator', 'anm','Use for a person or organization who draws the two-dimensional figures, manipulates the three dimensional objects and/or also programs the computer to move objects and images for the purpose of animated film processing. Animation cameras, stands, celluloid screens, transparencies and inks are some of the tools of the animator.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Applicant', 'app', 'Use for a person who writes manuscript annotations on a printed item.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Architect', 'arc','Use for a person or organization who designs structures or oversees their construction.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Arranger', 'arr','Use for a person or organization who transcribes a musical composition, usually for a different medium from that of the original; in an arrangement the musical substance remains essentially unchanged.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Art copyist', 'acp','Use for a person (e.g., a painter or sculptor) who makes copies of works of visual art.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Artist', 'art','Use for a person (e.g., a painter) or organization who conceives, and perhaps also implements, an original graphic design or work of art, if specific codes (e.g., [egr], [etr]) are not desired. For book illustrators, prefer Illustrator [ill]. ');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Artistic director', 'ard','Use for a person responsible for controlling the development of the artistic style of an entire production, including the choice of works to be presented and selection of senior production staff');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Assignee', 'asg','Use for a person or organization to whom a license for printing or publishing has been transferred.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Associated name', 'asn','Use for a person or organization associated with or found in an item or collection, which cannot be determined to be that of a Former owner [fmo] or other designated relator indicative of provenance.');


insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Attributed name', 'att','Use for an author, artist, etc., relating him/her to a work for which there is or once was substantial authority for designating that person as author, creator, etc. of the work.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Auctioneer', 'auc','Use for a person or organization in charge of the estimation and public auctioning of goods, particularly books, artistic works, etc.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Author', 'aut', 'Use for a person or organization chiefly responsible for the intellectual or artistic content of a work, usually printed text. This term may also be used when more than one person or body bears such responsibility.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Author in quotations or text extracts', 'aqt','Use for a person or organization whose work is largely quoted or extracted in works to which he or she did not contribute directly. Such quotations are found particularly in exhibition catalogs, collections of photographs, etc.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Author of afterword, colophon, etc.', 'aft','Use for a person or organization responsible for an afterword, postface, colophon, etc. but who is not the chief author of a work.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Author of dialog', 'aud','Use for a person or organization responsible for the dialog or spoken commentary for a screenplay or sound recording.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Author of introduction, etc.', 'aui','Use for a person or organization responsible for an introduction, preface, foreword, or other critical introductory matter, but who is not the chief author.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Author of screenplay, etc.', 'aus','Use for a person or organization responsible for a motion picture screenplay, dialog, spoken commentary, etc.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Bibliographic antecedent', 'ant','Use for a person or organization responsible for a work upon which the work represented by the catalog record is based. This may be appropriate for adaptations, sequels, continuations, indexes, etc.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Binder', 'bnd','Use for a person or organization responsible for the binding of printed or manuscript materials.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Binding designer', 'bdd', 'Use for a person or organization responsible for the binding design of a book, including the type of binding, the type of materials used, and any decorative aspects of the binding. ');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Blurb writer', 'blw', 'Use for the named entity responsible for writing a commendation or testimonial for a work, which appears on or within the publication itself, frequently on the back or dust jacket of print publications or on advertising material for all media.');


insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Book designer', 'bkd', 'Use for a person or organization responsible for the entire graphic design of a book, including arrangement of type and illustration, choice of materials, and process used.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Book producer ', 'bkp', 'Use for a person or organization responsible for the production of books and other print media, if specific codes (e.g., [bkd], [egr], [tyd], [prt]) are not desired. ');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Bookjacket designer', 'bjd', 'Use for a person or organization responsible for the design of flexible covers designed for or published with a book, including the type of materials used, and any decorative aspects of the bookjacket.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Bookplate designer', 'bpd','Use for a person or organization responsible for the design of a book owners identification label that is most commonly pasted to the inside front cover of a book.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Bookseller', 'bsl','Use for a person or organization who makes books and other bibliographic materials available for purchase. Interest in the materials is primarily lucrative');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Calligrapher', 'cll','Use for a person or organization who writes in an artistic hand, usually as a copyist and or engrosser.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Cartographer', 'ctg','Use for a person or organization responsible for the creation of maps and other cartographic materials.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Censor', 'cns','Use for a censor, bowdlerizer, expurgator, etc., official or private.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Choreographer', 'chr','Use for a person or organization who composes or arranges dances or other movements (e.g., "master of swords") for a musical or dramatic presentation or entertainment.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Cinematographer', 'cng','Use for a person or organization who is in charge of the images captured for a motion picture film. The cinematographer works under the supervision of a director, and may also be referred to as director of photography. Do not confuse with videographer.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Client', 'cli','Use for a person or organization for whom another person or organization is acting.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Collaborator', 'clb','Use for a person or organization that takes a limited part in the elaboration of a work of another person or organization that brings complements (e.g., appendices, notes) to the work.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Collector', 'col','Use for a person or organization who has brought together material from various sources that has been arranged, described, and cataloged as a collection. A collector is neither the creator of the material nor a person to whom manuscripts in the collection may have been addressed.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Collotyper', 'clt','Use for a person or organization responsible for the production of photographic prints from film or other colloid that has ink-receptive and ink-repellent surfaces.');


insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Colorist', 'clr','Use for the named entity responsible for applying color to drawings, prints, photographs, maps, moving images, etc.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Commentator', 'cmm','Use for a person or organization who provides interpretation, analysis, or a discussion of the subject matter on a recording, motion picture, or other audiovisual medium.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Commentator for written text', 'cwt','Use for a person or organization responsible for the commentary or explanatory notes about a text. For the writer of manuscript annotations in a printed book, use Annotator [ann].');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Compiler', 'com', 'Use for a person or organization who produces a work or publication by selecting and putting together material from the works of various persons or bodies.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Complainant', 'cpl','Use for the party who applies to the courts for redress, usually in an equity proceeding.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Complainant-appellant', 'cpt','Use for a complainant who takes an appeal from one court or jurisdiction to another to reverse the judgment, usually in an equity proceeding.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Complainant-appellee', 'cpe','Use for a complainant against whom an appeal is taken from one court or jurisdiction to another to reverse the judgment, usually in an equity proceeding.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Composer', 'cmp','Use for a person or organization who creates a musical work, usually a piece of music in manuscript or printed form.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Compositor', 'cmt', 'Use for a person or organization responsible for the creation of metal slug, or molds made of other materials, used to produce the text and images in printed matter.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Conceptor', 'ccp','Use for a person or organization responsible for the original idea on which a work is based, this includes the scientific author of an audio-visual item and the conceptor of an advertisement.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Conductor', 'cnd','Use for a person who directs a performing group (orchestra, chorus, opera, etc.) in a musical or dramatic presentation or entertainment.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Conservator', 'con','Use for the named entity responsible for documenting, preserving, or treating printed or manuscript material, works of art, artifacts, or other media. ');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Consultant', 'cls', 'Use for a person or organization relevant to a resource, who is called upon for professional advice or services in a specialized field of knowledge or training.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Consultant to a project', 'csp', 'Use for a person or organization relevant to a resource, who is engaged specifically to provide an intellectual overview of a strategic or operational task and by analysis, specification, or instruction, to create or propose a cost-effective course of action or solution.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Contestant', 'cos','Use for the party who opposes, resists, or disputes, in a court of law, a claim, decision, result, etc.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Contestant-appellant', 'cot','Use for a contestant who takes an appeal from one court of law or jurisdiction to another to reverse the judgment.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Contestant-appellee', 'coe','Use for a contestant against whom an appeal is taken from one court of law or jurisdiction to another to reverse the judgment.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Contestee', 'cts','Use for the party defending a claim, decision, result, etc. being opposed, resisted, or disputed in a court of law.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Contestee-appellant ', 'ctt','Use for a contestee who takes an appeal from one court or jurisdiction to another to reverse the judgment.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Contestee-appellee ', 'cte','Use for a contestee against whom an appeal is taken from one court or jurisdiction to another to reverse the judgment.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Contractor', 'ctr','Use for a person or organization relevant to a resource, who enters into a contract with another person or organization to perform a specific task.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Contributor', 'ctb','Use for a person or organization one whose work has been contributed to a larger work, such as an anthology, serial publication, or other compilation of individual works. Do not use if the sole function in relation to a work is as author, editor, compiler or translator.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Copyright claimant', 'cpc','Use for a person or organization listed as a copyright owner at the time of registration. Copyright can be granted or later transferred to another person or organization, at which time the claimant becomes the copyright holder.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Copyright holder', 'cph','Use for a person or organization to whom copy and legal rights have been granted or transferred for the intellectual content of a work. The copyright holder, although not necessarily the creator of the work, usually has the exclusive right to benefit financially from the sale and use of the work to which the associated copyright protection applies.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Corrector', 'crr','Use for a person or organization who is a corrector of manuscripts, such as the scriptorium official who corrected the work of a scribe. For printed matter, use Proofreader.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Correspondent', 'crp','Use for a person or organization who was either the writer or recipient of a letter or other communication.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Costume designer', 'cst','Use for a person or organization who designs or makes costumes, fixes hair, etc., for a musical or dramatic presentation or entertainment.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Cover designer', 'cov','Use for a person or organization responsible for the graphic design of a book cover, album cover, slipcase, box, container, etc. For a person or organization responsible for the graphic design of an entire book, use Book designer; for book jackets, use Bookjacket designer.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Creator', 'cre','Use for a person or organization responsible for the intellectual or artistic content of a work.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Curator of an exhibition', 'cur','Use for a person or organization responsible for conceiving and organizing an exhibition.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Dancer', 'dnc','Use for a person or organization who principally exhibits dancing skills in a musical or dramatic presentation or entertainment.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Data contributor', 'dtc','Use for a person or organization that submits data for inclusion in a database or other collection of data.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Data manager', 'dtm','Use for a person or organization responsible for managing databases or other data sources.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Dedicatee', 'dte','Use for a person or organization to whom a book, manuscript, etc., is dedicated (not the recipient of a gift).');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Dedicator', 'dto','Use for the author of a dedication, which may be a formal statement or in epistolary or verse form.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Defendant', 'dfd','Use for the party defending or denying allegations made in a suit and against whom relief or recovery is sought in the courts, usually in a legal action.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Defendant-appellant', 'dft','Use for a defendant who takes an appeal from one court or jurisdiction to another to reverse the judgment, usually in a legal action.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Defendant-appellee', 'dfe','Use for a defendant against whom an appeal is taken from one court or jurisdiction to another to reverse the judgment, usually in a legal action.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Degree grantor', 'dgg','Use for the organization granting a degree for which the thesis or dissertation described was presented.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Delineator', 'dln','Use for a person or organization executing technical drawings from others designs.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Depicted', 'dpc', 'Use for an entity depicted or portrayed in a work, particularly in a work of art.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Depositor', 'dpt','Use for a person or organization placing material in the physical custody of a library or repository without transferring the legal title');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Designer', 'dsr','Use for a person or organization responsible for the design if more specific codes (e.g., [bkd], [tyd]) are not desired.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Director', 'drt','Use for a person or organization who is responsible for the general management of a work or who supervises the production of a performance for stage, screen, or sound recording.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Dissertant', 'dis','Use for a person who presents a thesis for a university or higher-level educational degree.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Distribution place', 'dbp','Use for the name of a place from which a resource, e.g., a serial, is distributed.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Distributor', 'dst','Use for a person or organization that has exclusive or shared marketing rights for an item.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Donor', 'dnr','Use for a person or organization who is the donor of a book, manuscript, etc., to its present owner. Donors to previous owners are designated as Former owner [fmo] or Inscriber [ins].');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Draftsman', 'drm','Use for a person or organization who prepares artistic or technical drawings.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Dubious author', 'dub','Use for a person or organization to which authorship has been dubiously or incorrectly ascribed.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Editor', 'edt','Use for a person or organization who prepares for publication a work not primarily his/her own, such as by elucidating text, adding introductory or other critical matter, or technically directing an editorial staff.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Electrician', 'elg','Use for a person responsible for setting up a lighting rig and focusing the lights for a production, and running the lighting at a performance. ');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Electrotyper', 'elt','Use for a person or organization who creates a duplicate printing surface by pressure molding and electrodepositing of metal that is then backed up with lead for printing.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Engineer', 'eng','Use for a person or organization that is responsible for technical planning and design, particularly with construction.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Engraver', 'egr','Use for a person or organization who cuts letters, figures, etc. on a surface, such as a wooden or metal plate, for printing.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Etcher', 'etr','Use for a person or organization who produces text or images for printing by subjecting metal, glass, or some other surface to acid or the corrosive action of some other substance.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Event place', 'evp','Use for the name of the place where an event such as a conference or a concert took place.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Expert', 'exp','Use for a person or organization in charge of the description and appraisal of the value of goods, particularly rare items, works of art, etc. ');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Facsimilist', 'fac','Use for a person or organization that executed the facsimile.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Field director', 'fld','Use for a person or organization that manages or supervises the work done to collect raw data or do research in an actual setting or environment (typically applies to the natural and social sciences)');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Film editor', 'flm','Use for a person or organization who is an editor of a motion picture film. This term is used regardless of the medium upon which the motion picture is produced or manufactured (e.g., acetate film, video tape). ');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'First party', 'fpy','Use for a person or organization who is identified as the only party or the party of the first part. In the case of transfer of right, this is the assignor, transferor, licensor, grantor, etc. Multiple parties can be named jointly as the first party');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Forger', 'frg','Use for a person or organization who makes or imitates something of value or importance, especially with the intent to defraud.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Former owner', 'fmo','Use for a person or organization who owned an item at any time in the past. Includes those to whom the material was once presented. A person or organization giving the item to the present owner is designated as Donor [dnr]');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Funder', 'fnd','Use for a person or organization that furnished financial support for the production of the work.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Geographic information specialist', 'gis', 'Use for a person responsible for geographic information system (GIS) development and integration with global positioning system data.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Honoree', 'hnr','Use for a person or organization in memory or honor of whom a book, manuscript, etc. is donated.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Host', 'hst','Use for a person who is invited or regularly leads a program (often broadcast) that includes other guests, performers, etc. (e.g., talk show host).');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Illuminator', 'ilu','Use for a person or organization responsible for the decoration of a work (especially manuscript material) with precious metals or color, usually with elaborate designs and motifs.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Illustrator', 'ill','Use for a person or organization who conceives, and perhaps also implements, a design or illustration, usually to accompany a written text.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Inscriber', 'ins','Use for a person who signs a presentation statement.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Instrumentalist', 'itr','Use for a person or organization who principally plays an instrument in a musical or dramatic presentation or entertainment.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Interviewee', 'ive', 'Use for a person or organization who is interviewed at a consultation or meeting, usually by a reporter, pollster, or some other information gathering agent.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Interviewer', 'ivr','Use for a person or organization who acts as a reporter, pollster, or other information gathering agent in a consultation or meeting involving one or more individuals.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Inventor', 'inv','Use for a person or organization who first produces a particular useful item, or develops a new process for obtaining a known item or result.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Laboratory', 'lbr','Use for an institution that provides scientific analyses of material samples.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Laboratory director', 'ldr','Use for a person or organization that manages or supervises work done in a controlled setting or environment.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Landscape architect', 'lsa','Use for a person or organization whose work involves coordinating the arrangement of existing and proposed land features and structures.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Lead', 'led','Use to indicate that a person or organization takes primary responsibility for a particular activity or endeavor. Use with another relator term or code to show the greater importance this person or organization has regarding that particular role. If more than one relator is assigned to a heading, use the Lead relator only if it applies to all the relators.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Lender', 'len','Use for a person or organization permitting the temporary use of a book, manuscript, etc., such as for photocopying or microfilming.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Libelant', 'lil','Use for the party who files a libel in an ecclesiastical or admiralty case.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Libelant-appellant', 'lit','Use for a libelant who takes an appeal from one ecclesiastical court or admiralty to another to reverse the judgment.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Libelee', 'lee','Use for a libelee against whom an appeal is taken from one ecclesiastical court or admiralty to another to reverse the judgment.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Librettist', 'lbt','Use for a person or organization who is a writer of the text of an opera, oratorio, etc.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Licensee', 'lse','Use for a person or organization who is an original recipient of the right to print or publish.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Licensor', 'lso','Use for person or organization who is a signer of the license, imprimatur, etc.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Lighting designer', 'ldg','Use for a person or organization who designs the lighting scheme for a theatrical presentation, entertainment, motion picture, etc.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Lithographer', 'ltg','Use for a person or organization who prepares the stone or plate for lithographic printing, including a graphic artist creating a design directly on the surface from which printing will be done.');


insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Lyricist', 'lyr','Use for a person or organization who is a writer of the text of a song.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Manufacture place', 'mfp','Use for the name of the place of manufacture (e.g., printing, duplicating, casting, etc.) of a resource in a published form.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Manufacturer', 'mfr','Use for a person or organization that makes an artifactual work (an object made or modified by one or more persons). Examples of artifactual works include vases, cannons or pieces of furniture.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Marbler', 'mrb','Use for the named entity responsible for marbling paper, cloth, leather, etc. used in construction of a resource. ');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Markup editor', 'mrk','Use for a person or organization performing the coding of SGML, HTML, or XML markup of metadata, text, etc.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Metadata contact', 'mdc','Use for a person or organization primarily responsible for compiling and maintaining the original description of a metadata set (e.g., geospatial metadata set).');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Metal-engraver', 'mte','Use for a person or organization responsible for decorations, illustrations, letters, etc. cut on a metal surface for printing or decoration.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Moderator', 'mod','Use for a person who leads a program (often broadcast) where topics are discussed, usually with participation of experts in fields related to the discussion.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Monitor', 'mon','Use for a person or organization that supervises compliance with the contract and is responsible for the report and controls its distribution. Sometimes referred to as the grantee, or controlling agency.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Music copyist', 'mcp','Use for a person who transcribes or copies musical notation');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Musical director', 'msd','Use for a person responsible for basic music decisions about a production, including coordinating the work of the composer, the sound editor, and sound mixers, selecting musicians, and organizing and/or conducting sound for rehearsals and performances.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Musician', 'mus','Use for a person or organization who performs music or contributes to the musical content of a work when it is not possible or desirable to identify the function more precisely.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Narrator', 'nrt','Use for a person who is a speaker relating the particulars of an act, occurrence, or course of events.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Opponent', 'opn','Use for a person or organization responsible for opposing a thesis or dissertation.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Organizer of meeting', 'orm', 'Use for a person or organization responsible for organizing a meeting for which an item is the report or proceedings.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Originator', 'org','Use for a person or organization performing the work, i.e., the name of a person or organization associated with the intellectual content of the work. This category does not include the publisher or personal affiliation, or sponsor except where it is also the corporate author.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Other', 'oth','Use for relator codes from other lists which have no equivalent in the MARC list or for terms which have not been assigned a code.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Owner', 'own','Use for a person or organization that currently owns an item or collection.');


insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Papermaker', 'ppm','Use for a person or organization responsible for the production of paper, usually from wood, cloth, or other fibrous material.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Patent applicant', 'pta','Use for a person or organization that applied for a patent.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Patent holder', 'pth','Use for a person or organization that was granted the patent referred to by the item.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Patron', 'pat','Use for a person or organization responsible for commissioning a work. Usually a patron uses his or her means or influence to support the work of artists, writers, etc. This includes those who commission and pay for individual works.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Performer', 'prf','Use for a person or organization who exhibits musical or acting skills in a musical or dramatic presentation or entertainment, if specific codes for those functions ([act], [dnc], [itr], [voc], etc.) are not used. If specific codes are used, [prf] is used for a person whose principal skill is not known or specified.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Permitting agency', 'pma','Use for an authority (usually a government agency) that issues permits under which work is accomplished.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Photographer', 'pht','Use for a person or organization responsible for taking photographs, whether they are used in their original form or as reproductions.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Plaintiff', 'ptf','Use for the party who complains or sues in court in a personal action, usually in a legal proceeding.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Plaintiff-appellant', 'ptt','Use for a plaintiff who takes an appeal from one court or jurisdiction to another to reverse the judgment, usually in a legal proceeding.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Plaintiff-appellee', 'pte','Use for a plaintiff against whom an appeal is taken from one court or jurisdiction to another to reverse the judgment, usually in a legal proceeding.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Platemaker', 'plt','Use for a person or organization responsible for the production of plates, usually for the production of printed images and/or text.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Printer', 'prt','Use for a person or organization who prints texts, whether from type or plates');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Printer of plates', 'pop','Use for a person or organization who prints illustrations from plates.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Printmaker', 'prm','Use for a person or organization who makes a relief, intaglio, or planographic printing surface.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Process contact', 'prc','Use for a person or organization primarily responsible for performing or initiating a process, such as is done with the collection of metadata sets.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Producer', 'pro','Use for a person or organization responsible for the making of a motion picture, including business aspects, management of the productions, and the commercial success of the work. Producer of book');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Production manager', 'pmn','Use for a person responsible for all technical and business matters in a production.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Production personnel', 'prd','Use for a person responsible for all technical and business matters in a production.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Production place', 'prp','Use for the name of the place of production (e.g., inscription, fabrication, construction, etc.) of a resource in an unpublished form.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Programmer', 'prg','Use for a person or organization responsible for the creation and/or maintenance of computer program design documents, source code, and machine-executable digital files and supporting documentation.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Project director', 'pdr','Use for a person or organization with primary responsibility for all essential aspects of a project, or that manages a very large project that demands senior level responsibility, or that has overall responsibility for managing projects, or provides overall direction to a project manager.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Proofreader', 'pfr','Use for a person who corrects printed matter.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Publication place ', 'pup','Use for the name of the place where a resource is published.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Publisher', 'pbl','Use for a person or organization that makes printed matter, often text, but also printed music, artwork, etc. available to the public.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Publishing director', 'pbd','Use for a person or organization who presides over the elaboration of a collective work to ensure its coherence or continuity. This includes editors-in-chief, literary editors, editors of series, etc.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Puppeteer', 'ppt','Use for a person or organization who manipulates, controls, or directs puppets or marionettes in a musical or dramatic presentation or entertainment.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Recipient', 'rcp','Use for a person or organization to whom correspondence is addressed.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Recording engineer', 'rce','Use for a person or organization who supervises the technical aspects of a sound or video recording session.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Redactor', 'red','Use for a person or organization who writes or develops the framework for an item without being intellectually responsible for its content.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Renderer', 'ren','Use for a person or organization who prepares drawings of architectural designs (i.e., renderings) in accurate, representational perspective to show what the project will look like when completed.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Reporter', 'rpt','Use for a person or organization who writes or presents reports of news or current events on air or in print.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Repository', 'rps','Use for an agency that hosts data or material culture objects and provides services to promote long term, consistent and shared use of those data or objects.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Research team head', 'rth','Use for a person who directed or managed a research project.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Research team member', 'rtm','Use for a person who participated in a research project but whose role did not involve direction or management of it.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Researcher', 'res','Use for a person or organization responsible for performing research.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Respondent', 'rsp','Use for the party who makes an answer to the courts pursuant to an application for redress, usually in an equity proceeding');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Respondent-appellant', 'rst','Use for a respondent who takes an appeal from one court or jurisdiction to another to reverse the judgment, usually in an equity proceeding.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Respondent-appellee', 'rse','Use for a respondent against whom an appeal is taken from one court or jurisdiction to another to reverse the judgment, usually in an equity proceeding.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Responsible party', 'rpy','Use for a person or organization legally responsible for the content of the published material.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Restager', 'rsg','Use for a person or organization, other than the original choreographer or director, responsible for restaging a choreographic or dramatic work and who contributes minimal new content.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Reviewer', 'rev','Use for a person or organization responsible for the review of a book, motion picture, performance, etc.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Rubricator', 'rbr', 'Use for a person or organization responsible for parts of a work, often headings or opening parts of a manuscript, that appear in a distinctive color, usually red.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Scenarist', 'sce','Use for a person or organization who is the author of a motion picture screenplay.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Scientific advisor', 'sad','Use for a person or organization who brings scientific, pedagogical, or historical competence to the conception and realization on a work, particularly in the case of audio-visual items.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Scribe', 'scr','Use for a person who is an amanuensis and for a writer of manuscripts proper.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Sculptor', 'scl','Use for a person or organization who models or carves figures that are three-dimensional representations');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Second party', 'spy','Use for a person or organization who is identified as the party of the second part. In the case of transfer of right, this is the assignee, transferee, licensee, grantee, etc. Multiple parties can be named jointly as the second party.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Secretary', 'sec','Use for a person or organization who is a recorder, redactor, or other person responsible for expressing the views of a organization.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Set designer', 'std','Use for a person or organization who translates the rough sketches of the art director into actual architectural structures for a theatrical presentation, entertainment, motion picture, etc. Set designers draw the detailed guides and specifications for building the set.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Signer', 'sgn','Use for a person whose signature appears without a presentation or other statement indicative of provenance.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Singer', 'sng','Use for a person or organization who uses his/her/their voice with or without instrumental accompaniment to produce music. A performance may or may not include actual words.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Sound designer', 'sds','Use for a person who produces and reproduces the sound score (both live and recorded), the installation of microphones, the setting of sound levels, and the coordination of sources of sound for a production.');


insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Speaker', 'spk','Use for a person who participates in a program (often broadcast) and makes a formalized contribution or presentation generally prepared in advance.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Sponsor', 'spn','Use for a person or organization that issued a contract or under the auspices of which a work has been written, printed, published, etc.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Stage manager', 'stm','Use for a person who is in charge of everything that occurs on a performance stage, and who acts as chief of all crews and assistant to a director during rehearsals.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Standards body', 'stn','Use for an organization responsible for the development or enforcement of a standard.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Stereotyper', 'str','Use for a person or organization who creates a new plate for printing by molding or copying another printing surface.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Storyteller', 'stl','Use for a person relaying a story with creative and/or theatrical interpretation.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Supporting host', 'sht','Use for a person or organization that supports (by allocating facilities, staff, or other resources) a project, program, meeting, event, data objects, material culture objects, or other entities capable of support. ');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Surveyor', 'srv','Use for a person or organization who does measurements of tracts of land, etc. to determine location, forms, and boundaries.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Teacher', 'tch','Use for a person who is ultimately in charge of scenery, props, lights and sound for a production.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Technical director', 'tcd','Use for a person who is ultimately in charge of scenery, props, lights and sound for a production.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Thesis advisor', 'ths','Use for a person under whose supervision a degree candidate develops and presents a thesis, memoire, or text of a dissertation. ');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Transcriber', 'trc','Use for a person who prepares a handwritten or typewritten copy from original material, including from dictated or orally recorded material.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Translator', 'trl','Use for a person or organization who renders a text from one language into another, or from an older form of a language into the modern form.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Type designer', 'tyd','Use for a person or organization who designed the type face used in a particular item.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Typographer', 'tyg','Use for a person or organization primarily responsible for choice and arrangement of type used in an item. If the typographer is also responsible for other aspects of the graphic design of a book (e.g., Book designer [bkd]), codes for both functions may be needed.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'University place', 'uvp','Use for the name of a place where a university that is associated with a resource is located, for example, a university where an academic dissertation or thesis was presented.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Videographer', 'vdg','Use for a person or organization in charge of a video production, e.g. the video recording of a stage production as opposed to a commercial motion picture. The videographer may be the camera operator or may supervise one or more camera operators. Do not confuse with cinematographer.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Vocalist', 'voc','Use for a person or organization who principally exhibits singing skills in a musical or dramatic presentation or entertainment.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Witness', 'wit','Use for a person who verifies the truthfulness of an event or action.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Wood-engraver', 'wde','Use for a person or organization who makes prints by cutting the image in relief on the end-grain of a wood block.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Woodcutter', 'wdc','Use for a person or organization who makes prints by cutting the image in relief on the plank side of a wood block.');

insert into                                                         
metadata.marc_relator_code (marc_relator_code_id, version, name, relator_code, description) 
values (nextval('metadata.marc_relator_code_seq'), 0, 'Writer of accompanying material', 'wam','Use for a person or organization who writes significant material which accompanies a sound recording or other audiovisual material.');



-- ---------------------------------------------
-- Inserts into the marc type of record table
-- ---------------------------------------------

insert into                                                         
metadata.marc_type_of_record (marc_type_of_record_id, version, name, record_type, description) 
values (nextval('metadata.marc_type_of_record_seq'), 0, 'Language material', 'a','');

insert into                                                         
metadata.marc_type_of_record (marc_type_of_record_id, version, name, record_type, description) 
values (nextval('metadata.marc_type_of_record_seq'), 0, 'Manuscript language material', 't','');

insert into                                                         
metadata.marc_type_of_record (marc_type_of_record_id, version, name, record_type, description) 
values (nextval('metadata.marc_type_of_record_seq'), 0, 'Projected medium', 'g','');

insert into                                                         
metadata.marc_type_of_record (marc_type_of_record_id, version, name, record_type, description) 
values (nextval('metadata.marc_type_of_record_seq'), 0, 'Two-dimensional nonprojectable graphic', 'k','');

insert into                                                         
metadata.marc_type_of_record (marc_type_of_record_id, version, name, record_type, description) 
values (nextval('metadata.marc_type_of_record_seq'), 0, 'Three-dimensional artifact or naturally occurring object.', 'r','');

insert into                                                         
metadata.marc_type_of_record (marc_type_of_record_id, version, name, record_type, description) 
values (nextval('metadata.marc_type_of_record_seq'), 0, 'Kit', 'o','');

insert into                                                         
metadata.marc_type_of_record (marc_type_of_record_id, version, name, record_type, description) 
values (nextval('metadata.marc_type_of_record_seq'), 0, 'Mixed materials', 'p','');

insert into                                                         
metadata.marc_type_of_record (marc_type_of_record_id, version, name, record_type, description) 
values (nextval('metadata.marc_type_of_record_seq'), 0, 'Cartographic material', 'e','');

insert into                                                         
metadata.marc_type_of_record (marc_type_of_record_id, version, name, record_type, description) 
values (nextval('metadata.marc_type_of_record_seq'), 0, 'Manuscript cartographic material', 'f','');

insert into                                                         
metadata.marc_type_of_record (marc_type_of_record_id, version, name, record_type, description) 
values (nextval('metadata.marc_type_of_record_seq'), 0, 'Notated music', 'c','');

insert into                                                         
metadata.marc_type_of_record (marc_type_of_record_id, version, name, record_type, description) 
values (nextval('metadata.marc_type_of_record_seq'), 0, 'Manuscript notated music', 'd','');

insert into                                                         
metadata.marc_type_of_record (marc_type_of_record_id, version, name, record_type, description) 
values (nextval('metadata.marc_type_of_record_seq'), 0, 'Nonmusical sound recording', 'i','');

insert into                                                         
metadata.marc_type_of_record (marc_type_of_record_id, version, name, record_type, description) 
values (nextval('metadata.marc_type_of_record_seq'), 0, 'Musical sound recording', 'j','');

insert into                                                         
metadata.marc_type_of_record (marc_type_of_record_id, version, name, record_type, description) 
values (nextval('metadata.marc_type_of_record_seq'), 0, 'Computer file', 'm','');

-- -----------------------------------------------
-- Insert mappings
--
-- ----------------------------------------------
INSERT INTO ir_metadata_marc.content_type_field_mapping (
    content_type_field_mapping_id, 
    content_type_id, 
    control_field_006, 
    control_field_007, 
    control_field_008, 
    is_thesis, 
    encoding_level, 
    record_status,
    marc_type_of_record_id,
    bibliographic_level, 
    type_of_control, 
    descriptive_cataloging_form, 
    version) 
    VALUES(
            nextval('ir_metadata_marc.content_type_field_mapping_seq'), 
            (select content_type.content_type_id from ir_item.content_type 
            where content_type.unique_system_code = 'BOOK'), 
            'm        d        ', 
            'cr||||||||||||', 
            '                       s                ',  
            false, 
            'K', 
            ' ', 
            (select marc_type_of_record_id from metadata.marc_type_of_record 
             where marc_type_of_record.record_type = 'a'), 
            'm', 
            ' ', 
            'a', 
            0) ;

INSERT INTO ir_metadata_marc.content_type_field_mapping (content_type_field_mapping_id,
content_type_id, 
control_field_006, 
control_field_007,
control_field_008, 
is_thesis, 
encoding_level, 
record_status, 
marc_type_of_record_id,
bibliographic_level, 
type_of_control, 
descriptive_cataloging_form, 
version) 
VALUES (nextval('ir_metadata_marc.content_type_field_mapping_seq'), 
         (select content_type.content_type_id from ir_item.content_type 
            where content_type.unique_system_code = 'THESIS'), 
        'm        d        ',
        'cr||||||||||||', 
        '      s                s                ',  
        true, 
        'K', 
        ' ', 
        (select marc_type_of_record_id from metadata.marc_type_of_record 
             where marc_type_of_record.record_type = 'a'), 
        'm', 
        ' ', 
        'a', 
        0);


INSERT INTO ir_metadata_marc.content_type_field_mapping (content_type_field_mapping_id,
content_type_id, 
control_field_006, 
control_field_007,
control_field_008, 
is_thesis, 
encoding_level, 
record_status, 
marc_type_of_record_id,
bibliographic_level, 
type_of_control, 
descriptive_cataloging_form, 
version) 
VALUES (nextval('ir_metadata_marc.content_type_field_mapping_seq'), 
         (select content_type.content_type_id from ir_item.content_type 
            where content_type.unique_system_code = 'MUSICAL_SCORE'), 
        'm        d        ',
        'cr||||||||||||', 
        '      s                s                ',  
        true, 
        'K', 
        ' ', 
        (select marc_type_of_record_id from metadata.marc_type_of_record 
             where marc_type_of_record.record_type = 'c'), 
        'm', 
        ' ', 
        'a', 
        0);