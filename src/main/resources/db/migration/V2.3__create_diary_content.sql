CREATE TABLE IF NOT EXISTS diary_content
(
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    page INTEGER NOT NULL,
    content VARCHAR(255) NOT NULL,
    diary_id BIGINT NOT NULL,
    CONSTRAINT diary_content_diary_id_fkey FOREIGN KEY (diary_id) REFERENCES diary (id) ON DELETE CASCADE
);

INSERT INTO diary_content (created_at, updated_at, page, content, diary_id)
    SELECT created_at, updated_at, 1, content, id FROM diary;

ALTER TABLE diary DROP COLUMN IF EXISTS content;
