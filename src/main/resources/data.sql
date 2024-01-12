-- 테스트 계정
-- TODO : 테스트 계정이지만 비밀번호가 노출되었으므로 나중에 개선고민.
INSERT INTO user_account (user_id, user_password, nickname, email, memo,created_at, created_by, modified_at, modified_by)
VALUES
    ('song', '1234', 'SongSOng', 'song@foo.com', 'songsss', now(), 'song', now(), 'song'),
    ('admin', '1234', 'Admin', 'admin@foo.com', 'Admin', now(), 'Admin', now(), 'Admin');


INSERT INTO article (user_account_id,title, content, hashtag, created_at, created_by, modified_at, modified_by)
VALUES
    (1, 'Title 1', 'Content 1', '#TagA', '2024-01-09 10:00:00', 'SongSOng', '2024-01-09 10:00:00', 'SongSOng'),
    (2, 'Title 2', 'Content 2', '#TagB', '2024-01-08 14:30:00', 'Admin', '2024-01-08 14:30:00', 'Admin'),
    (1, 'Title 3', 'Content 3', '#TagC', '2024-01-07 18:45:00', 'SongSOng', '2024-01-07 18:45:00', 'SongSOng');

INSERT INTO article_comment (article_id, user_account_id, content, created_at, created_by, modified_at, modified_by)
VALUES
    (1, 1, 'Comment 1 for Article 1', '2024-01-09 10:10:00', 'SongSOng', '2024-01-09 10:10:00', 'SongSOng'),
    (1, 1, 'Comment 2 for Article 1', '2024-01-08 15:00:00', 'SongSOng', '2024-01-08 15:00:00', 'SongSOng'),
    (2, 1, 'Comment 1 for Article 2', '2024-01-07 19:30:00', 'SongSOng', '2024-01-07 19:30:00', 'SongSOng');

