ALTER TABLE diary_content
DROP CONSTRAINT diary_content_diary_id_fkey;
ALTER TABLE diary_content
    ADD CONSTRAINT diary_content_diary_id_fkey
        FOREIGN KEY (diary_id)
            REFERENCES diary(diary_id) ON DELETE CASCADE;

ALTER TABLE refresh_token
DROP CONSTRAINT refresh_token_member_id_fkey;
ALTER TABLE refresh_token
    ADD CONSTRAINT refresh_token_member_id_fkey
        FOREIGN KEY (member_id)
            REFERENCES member(member_id) ON DELETE CASCADE;

ALTER TABLE notification
DROP CONSTRAINT notification_member_id_fkey;
ALTER TABLE notification
    ADD CONSTRAINT notification_member_id_fkey
        FOREIGN KEY (member_id)
            REFERENCES member(member_id) ON DELETE CASCADE;
