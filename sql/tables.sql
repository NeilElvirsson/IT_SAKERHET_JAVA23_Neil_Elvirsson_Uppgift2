CREATE TABLE IF NOT EXISTS users (
user_name TEXT NOT NULL PRIMARY KEY,
password TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS timecapsule (
title TEXT NOT NULL,
text TEXT,
user_name TEXT NOT NULL,
FOREIGN KEY (user_name) REFERENCES users (user_name) ON DELETE CASCADE
);