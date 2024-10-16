ALTER TABLE upload_image
    DROP CONSTRAINT upload_image_diary_id_fkey;
ALTER TABLE upload_image
    ADD CONSTRAINT upload_image_diary_id_fkey
    FOREIGN KEY (diary_id)
    REFERENCES diary(diary_id) ON DELETE CASCADE;
