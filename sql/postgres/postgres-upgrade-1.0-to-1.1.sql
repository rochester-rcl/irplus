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


DROP TABLE ir_statistics.ir_file_roll_up;
