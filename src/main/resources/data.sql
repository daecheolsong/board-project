INSERT INTO article (title, content, hashtag, created_at, created_by, modified_at, modified_by)
VALUES
    ('Title 1', 'Content 1', '#TagA', '2024-01-09 10:00:00', 'Admin', '2024-01-09 10:00:00', 'Admin'),
    ('Title 2', 'Content 2', '#TagB', '2024-01-08 14:30:00', 'User1', '2024-01-08 14:30:00', 'User1'),
    ('Title 3', 'Content 3', '#TagC', '2024-01-07 18:45:00', 'User2', '2024-01-07 18:45:00', 'User2');

INSERT INTO article_comment (article_id, content, created_at, created_by, modified_at, modified_by)
VALUES
    (1, 'Comment 1 for Article 1', '2024-01-09 10:10:00', 'User1', '2024-01-09 10:10:00', 'User1'),
    (1, 'Comment 2 for Article 1', '2024-01-08 15:00:00', 'User2', '2024-01-08 15:00:00', 'User2'),
    (2, 'Comment 1 for Article 2', '2024-01-07 19:30:00', 'Admin', '2024-01-07 19:30:00', 'Admin');