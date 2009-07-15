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
values (nextval('person.contributor_type_seq'), 0, 'Advisor', 'ADVISOR', null);

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
values (nextval('person.contributor_type_seq'), 0, 'Anthropologist', 'ANTHROPOLOGIST', null);

insert into 
person.contributor_type ( contributor_type_id, version, name, unique_system_code, description) 
values (nextval('person.contributor_type_seq'), 0, 'Librarian', 'LIBRARIAN', null);

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
select nextval('ir_user.role_seq'), 0, 'ROLE_COLLECTION_ADMIN', 'Indicates the user is a 
Collection Administrator';

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
select nextval('ir_user.department_seq'), 'The Institute of Optics', 
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

