-- Добавление пользователей
INSERT INTO users (name, login, email, birthday) VALUES
('Alice', 'alice123', 'alice@example.com', '1990-01-01 00:00:00'),
('Bob', 'bob456', 'bob@example.com', '1985-05-15 00:00:00'),
('Charlie', 'charlie789', 'charlie@example.com', '1992-07-20 00:00:00');

INSERT INTO ratingMPA (name) VALUES
('G'),
('PG'),
('PG-13'),
('R'),
('NC-17');

INSERT INTO genres (name) VALUES
('Комедия'),
('Драма'),
('Мультфильм'),
('Триллер'),
('Документальный'),
('Боевик');

-- Добавление фильмов
INSERT INTO films (title, description, duration, ratingMPA_id, release_date) VALUES
('Inception', 'A mind-bending thriller', 148, 3, '2010-07-16 00:00:00'),
('The Matrix', 'A sci-fi classic', 136, 4, '1999-03-31 00:00:00');

-- Добавление друзей
INSERT INTO friends (user_id, friend_id) VALUES
(1, 2), -- Alice добавила Bob в друзья
(2, 1),
(1, 3), -- Alice добавила Charlie в друзья
(3, 1),
(2, 3),
(3, 2);

-- Добавление лайков
INSERT INTO likes (user_id, film_id) VALUES
(1, 1), -- Alice поставила лайк фильму Inception
(2, 1), -- Bob поставил лайк фильму Inception
(3, 2); -- Charlie поставил лайк фильму The Matrix

INSERT INTO films_genre (film_id, genre_id) VALUES
(1, 4),
(2, 6);