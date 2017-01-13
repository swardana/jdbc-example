INSERT INTO publisher(id, name) VALUES
(1, 'DC'),
(2, 'MARVEL'),
(3, 'DAILY PLANET');

INSERT INTO book(id, publisher_id, book_code, title, summary, keywords) VALUES
(1, 1, 'DC-001', 'BATMAN RETURNS', 'When the Dark Knight Returns', 'action, hero'),
(2, 2, 'MV-001', 'THE BERSEKER', 'when the Heroes Make an Aliance', 'action, hero'),
(3, 3, 'DP-001', 'SUICIDE TEAM', 'When the Vilains want to be Hero', 'action, hero');

INSERT INTO author(id, name) VALUES
(1, 'STANLEY'),
(2, 'BILL FINGER'),
(3, 'J.J THOMSON');

INSERT INTO book_detail(id, book_id, author_id) VALUES
(1, 1, 2),
(2, 2, 1),
(3, 3, 3);