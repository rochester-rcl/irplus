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
values (nextval('metadata.marc_relator_code_seq'), 0, 'Thesis advisor', 'ths','Use for a person under whose supervision a degree candidate develops and presents a thesis, mémoire, or text of a dissertation. ');

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







