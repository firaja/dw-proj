USE dw;

INSERT INTO cities (name)
VALUES ('Torino'),
       ('Milano'),
       ('Roma');

INSERT INTO city_translation (name, translation)
VALUES('rome', 'Roma'),
       ('milan1', 'Milano'),
       ('torino', 'Torino');


INSERT INTO precipitations (minimum_rain, maximum_rain, level, name)
VALUES (0, 1, 0, 'no rain'),
       (1, 2, 1, 'light rain'),
       (2, 10, 2, 'moderate rain'),
       (10, 50, 3, 'heavy rain'),
       (50, 1000, 4, 'violent rain');


INSERT INTO temperatures (minimum_temp, maximum_temp, level, name)
VALUES (-30, 0, 0, 'freezing'),
       (0, 15, 1, 'cold'),
       (15, 24, 2, 'warm'),
       (24, 30, 3, 'hot'),
       (30, 60, 4, 'very hot');


INSERT INTO winds (minimum_wind, maximum_wind, level, name)
VALUES (0, 1, 0, 'calm'),
       (1, 19, 1, 'breeze'),
       (19, 38, 2, 'moderate breeze'),
       (38, 74, 3, 'gale'),
       (74, 150, 4, 'storm');
