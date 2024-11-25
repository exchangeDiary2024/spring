BEGIN;

CREATE TABLE IF NOT EXISTS image
(
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    image_file_name VARCHAR(255) NOT NULL,
    "order" INTEGER NOT NULL,
    diary_id BIGINT NOT NULL,
    CONSTRAINT image_diary_id_fkey FOREIGN KEY (diary_id) REFERENCES diary (id) ON DELETE CASCADE
);

INSERT INTO image (created_at, updated_at, image_file_name, "order", diary_id)
    SELECT created_at, updated_at, image_file_name,1,id FROM diary
        WHERE image_file_name IS NOT NULL;

ALTER TABLE diary DROP COLUMN image_file_name;

COMMIT;
