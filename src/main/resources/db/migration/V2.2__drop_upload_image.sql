BEGIN;

-- diary 테이블에 image_file_name 컬럼 추가
ALTER TABLE diary ADD COLUMN IF NOT EXISTS image_file_name VARCHAR(15);
ALTER TABLE diary ALTER COLUMN image_file_name SET DATA TYPE VARCHAR(15);

UPDATE diary SET
    image_file_name = (SELECT to_char(d.created_at, 'YYYYMMdd."jpg"') FROM upload_image ui
                        JOIN diary d ON ui.diary_id = d.id
                        JOIN "group" g ON d.group_id = g.id);

-- upload image 제약조건 제거
ALTER TABLE upload_image DROP CONSTRAINT IF EXISTS upload_image_diary_id_fkey;
ALTER TABLE upload_image DROP CONSTRAINT IF EXISTS upload_image_diary_id_key;

COMMIT;
