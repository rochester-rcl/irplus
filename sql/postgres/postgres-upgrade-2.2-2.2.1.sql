-- Add new column to determine if user can play file
ALTER TABLE ir_item.item_file ADD COLUMN can_view_in_player BOOLEAN DEFAULT FALSE NOT NULL;