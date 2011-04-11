-- --------------------------------------
-- Fix for the publisher date information
-- --------------------------------------


-- set all published date ids to the correct value
UPDATE ir_item.published_date SET external_published_item_id = 
(SELECT external_published_item.published_date_id 
FROM ir_item.external_published_item
WHERE external_published_item.published_date_id = published_date.published_date_id)
WHERE published_date.external_published_item_id IS NULL;


UPDATE ir_item.external_published_item SET published_date_id = NULL
WHERE published_date_id in ( SELECT published_date_id 
FROM ir_item.published_date 
WHERE  published_date.month = 0 
AND  published_date.day = 0 
AND published_date.year = 0 );

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

UPDATE ir_item.item SET first_available_date_id = NULL
WHERE  first_available_date_id in ( SELECT  first_available_date_id 
FROM ir_item.first_available_date 
WHERE  first_available_date.month = 0 
AND first_available_date.day = 0 
AND first_available_date.year = 0);


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


UPDATE ir_item.item SET original_item_creation_date_id = NULL
WHERE  original_item_creation_date_id in ( SELECT  original_item_creation_date_id 
FROM ir_item.original_item_creation_date
WHERE  original_item_creation_date.month = 0 
AND original_item_creation_date.day = 0 
AND original_item_creation_date.year = 0);



-- delete bad data
DELETE FROM ir_item.original_item_creation_date
WHERE  original_item_creation_date.month = 0 
AND original_item_creation_date.day = 0 
AND original_item_creation_date.year = 0;

