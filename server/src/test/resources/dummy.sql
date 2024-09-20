TRUNCATE TABLE static_image CASCADE;
TRUNCATE TABLE diary CASCADE;
TRUNCATE TABLE sticker CASCADE;

INSERT INTO static_image (image_id, created_at, updated_at, type, url)
    VALUES (1, '2024-09-14', '2024-09-14', 'STICKER', 'image-url1'),
            (2, '2024-09-14', '2024-09-14', 'STICKER', 'image-url2'),
            (3, '2024-09-14', '2024-09-14', 'STICKER','image-url4'),
            (4, '2024-09-20', '2024-09-14', 'MOOD', 'image-url3');

INSERT INTO diary (diary_id, created_at, updated_at, content, mood_image_id)
    VALUES (1, '2024-09-16', '2024-09-16', 'content', 1);
