ALTER TABLE upload_image
    DROP CONSTRAINT upload_image_diary_id_fkey;
ALTER TABLE upload_image
    ADD CONSTRAINT upload_image_diary_id_fkey
    FOREIGN KEY (diary_id)
    REFERENCES diary(diary_id) ON DELETE CASCADE;
ALTER TABLE refresh_token
    DROP CONSTRAINT refresh_token_member_id_fkey;
ALTER TABLE refresh_token
    ADD CONSTRAINT refresh_token_member_id_fkey
        FOREIGN KEY (member_id)
            REFERENCES member(member_id) ON DELETE CASCADE;
