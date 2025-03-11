-- Schema that represents the database structure
-- Syntax: SQLite

-- Drop tables if they already exist
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS visitsTags;
DROP TABLE IF EXISTS doctors;
DROP TABLE IF EXISTS patients;
DROP TABLE IF EXISTS visits;


-- Table: doctors
CREATE TABLE IF NOT EXISTS doctors
(
    cf          TEXT PRIMARY KEY,
    name        TEXT NOT NULL,
    surname     TEXT NOT NULL,
    iban        TEXT NOT NULL
);

-- Table: patients
CREATE TABLE IF NOT EXISTS patients
(
    cf          TEXT PRIMARY KEY,
    name        TEXT NOT NULL,
    surname     TEXT NOT NULL,
    level       TEXT NOT NULL
);

-- Table: visits
CREATE TABLE IF NOT EXISTS visits
(
    idVisit       INTEGER PRIMARY KEY AUTOINCREMENT,
    doctorCF       TEXT NOT NULL,
    title           TEXT NOT NULL,
    description     TEXT,
    startTime       TEXT NOT NULL,
    endTime         TEXT NOT NULL,
    price           FLOAT NOT NULL CHECK(price >= 0),
    state           TEXT NOT NULL,
    stateExtraInfo  TEXT,
    FOREIGN KEY (doctorsCF) REFERENCES doctors (cf) ON UPDATE CASCADE ON DELETE CASCADE
    );


-- Table: tags
CREATE TABLE IF NOT EXISTS tags
(
    tag         TEXT NOT NULL,
    tagType     TEXT NOT NULL,
    PRIMARY KEY (tag, tagType)
    );

-- Table: visitsTags
CREATE TABLE IF NOT EXISTS visitsTags
(
    tag         TEXT NOT NULL,
    tagType     TEXT NOT NULL,
    idVisit  INTEGER NOT NULL,
    PRIMARY KEY (tag, tagType, idVisit),
    FOREIGN KEY (tag, tagType) REFERENCES tags (tag, tagType) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (idVisit) REFERENCES lessons (idVisit) ON UPDATE CASCADE ON DELETE CASCADE
    );