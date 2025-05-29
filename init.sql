CREATE TABLE IF NOT EXISTS short_url (
    url_nr SERIAL PRIMARY KEY,
    short_code VARCHAR(255) NOT NULL UNIQUE,
    original_url TEXT NOT NULL UNIQUE,
    strategy VARCHAR(255) NOT NULL
);