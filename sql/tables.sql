CREATE TABLE IF NOT EXISTS users (
user_name TEXT NOT NULL PRIMARY KEY,
password TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS timecapsules (
id TEXT NOT NULL PRIMARY KEY,
title TEXT NOT NULL,
text TEXT,
user_name TEXT NOT NULL,
created INTEGER DEFAULT (strftime('%s','now')),
FOREIGN KEY (user_name) REFERENCES users (user_name) ON DELETE CASCADE
);
DELETE FROM timecapsules;
INSERT INTO timecapsules(id, title, text, user_name) VALUES ('123', 'MatsJournal', 'timecasule bla bla', 'Neil');
INSERT INTO timecapsules(id, title, text, user_name) VALUES ('321', 'katt journal', 'timecasule, drabbel', 'Neil');
INSERT INTO timecapsules(id, title, text, user_name) VALUES ('1234', 'Journal', 'Hejhejhejhej', 'Neil');
