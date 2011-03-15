-- --------------------------------------
-- Number of rows that will be updated 
-- based on null external published item id.
-- --------------------------------------

SELECT count(*) 
FROM ir_item.published_date
WHERE published_date.external_published_item_id IS NULL; 

-- --------------------------------------
-- Number of rows that will be updated 
-- based on null item id on the first_available date table.
-- --------------------------------------
SELECT count(*) 
FROM ir_item.first_available_date
WHERE first_available_date.item_id IS NULL;

-- --------------------------------------
-- Number of rows that will be updated 
-- based on null item id on the original_item_creation date table.
-- --------------------------------------
SELECT count(*) 
FROM ir_item.original_item_creation_date
WHERE original_item_creation_date.item_id IS NULL;

