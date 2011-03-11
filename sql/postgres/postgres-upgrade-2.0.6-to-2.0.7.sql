-- --------------------------------------
-- Fix for the publisher date information
-- --------------------------------------

-- DROP the constraint
ALTER TABLE ir_item.published_date DROP CONSTRAINT external_published_item_published_date_id_fkey;

-- set all published date ids to the correct value
UPDATE ir_item.published_date SET external_published_item_id = 
(SELECT external_published_item.published_date_id 
FROM ir_item.external_published_item
WHERE external_published_item.published_date_id = published_date.published_date_id)
WHERE published_date.external_published_item_id IS NULL 

-- Set the column to not null
ALTER TABLE ir_item.published_date ALTER COLUMN external_published_item_id SET NOT NULL;
ALTER TABLE ir_item.published_date ADD CONSTRAINT published_date_external_published_item_id_key UNIQUE (external_published_item_id);

-- drop the bad column
ALTER TABLE ir_item.external_published_item DROP COLUMN published_date_id;

-- delete bad data
DELETE FROM ir_item.published_date 
WHERE  published_date.month = 0 
AND  published_date.day = 0 
AND published_date.year = 0;


-- --------------------------------------
-- Fix for the first available item creation date
-- --------------------------------------

-- set all first available creation date ids to the correct value
UPDATE ir_item.first_available_date SET item_id = 
(SELECT item.item_id
FROM ir_item.item
WHERE item.first_available_date_id = first_available_date.first_available_date_id)
WHERE first_available_date.item_id IS NULL;

-- Set the column to not null
ALTER TABLE ir_item.first_available_date ALTER COLUMN item_id SET NOT NULL;
ALTER TABLE ir_item.first_available_date ADD CONSTRAINT first_available_date_item_id_key UNIQUE (item_id);

-- drop the bad column
ALTER TABLE ir_item.item DROP COLUMN first_available_date_id;

-- delete bad data
DELETE FROM ir_item.first_available_date 
WHERE  first_available_date.month = 0 
AND first_available_date.day = 0 
AND first_available_date.year = 0;


-- --------------------------------------
-- Fix for the original item creation date
-- --------------------------------------

-- set all original item creation date ids to the correct value
UPDATE ir_item.original_item_creation_date SET item_id = 
(SELECT item.item_id
FROM ir_item.item
WHERE item.original_item_creation_date_id = original_item_creation_date.original_item_creation_date_id)
WHERE original_item_creation_date.item_id IS NULL;

-- Set the column to not null
ALTER TABLE ir_item.original_item_creation_date ALTER COLUMN item_id SET NOT NULL;

ALTER TABLE ir_item.original_item_creation_date ADD CONSTRAINT original_item_creation_date_item_id_key UNIQUE (item_id);

-- drop the bad column
ALTER TABLE ir_item.item DROP COLUMN original_item_creation_date_id;


-- delete bad data
DELETE FROM ir_item.original_item_creation_date
WHERE  original_item_creation_date.month = 0 
AND original_item_creation_date.day = 0 
AND original_item_creation_date.year = 0;

-- --------------------------------------
-- Add item id to ir_item.external_published_item
-- remove external_published_item_id from ir_item.item table
-- The relation should be set on the external_publised_item table
-- --------------------------------------

-- create the new column
ALTER TABLE ir_item.external_published_item
ADD COLUMN item_id BIGINT;

-- set the item id on the column new column
UPDATE ir_item.external_published_item SET item_id = 
(SELECT item.item_id
FROM ir_item.item
WHERE item.external_published_item_id = external_published_item.external_published_item_id);

-- set the new column to not null
ALTER TABLE ir_item.external_published_item ALTER COLUMN item_id SET NOT NULL;
ALTER TABLE ir_item.external_published_item  ADD CONSTRAINT external_published_item_item_id_key UNIQUE (item_id);
-- Add the constraint
ALTER TABLE ir_item.external_published_item ADD CONSTRAINT external_published_item_item_id_fkey FOREIGN KEY (item_id)
REFERENCES ir_item.item (item_id);

-- drop the bad column
ALTER TABLE ir_item.item DROP COLUMN external_published_item_id;

