-- Drop tables if they already exist
DROP TABLE IF EXISTS visitsTags;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS visits;
DROP TABLE IF EXISTS patients;
DROP TABLE IF EXISTS doctors;

-- Table: doctors
CREATE TABLE doctors
(
    cf          TEXT PRIMARY KEY,
    name        TEXT NOT NULL,
    surname     TEXT NOT NULL,
    iban        TEXT NOT NULL
);

-- Table: patients
CREATE TABLE patients
(
    cf          TEXT PRIMARY KEY,
    name        TEXT NOT NULL,
    surname     TEXT NOT NULL,
    urgencyLevel TEXT NOT NULL
);

-- Table: visits
CREATE TABLE visits
(
    idVisit        SERIAL PRIMARY KEY,
    doctorCF       TEXT NOT NULL,
    patientCF      TEXT,
    title          TEXT NOT NULL,
    description    TEXT,
    startTime      TIMESTAMP NOT NULL,
    endTime        TIMESTAMP NOT NULL,
    price          FLOAT NOT NULL CHECK(price >= 0),
    state          TEXT NOT NULL,
    stateExtraInfo TEXT,
    FOREIGN KEY (doctorCF) REFERENCES doctors (cf) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (patientCF) REFERENCES patients (cf) ON UPDATE CASCADE ON DELETE SET NULL
);

-- Table: tags
CREATE TABLE tags
(
    tag         TEXT NOT NULL,
    tagType     TEXT NOT NULL,
    PRIMARY KEY (tag, tagType)
);

-- Table: visitsTags
CREATE TABLE visitsTags
(
    tag         TEXT NOT NULL,
    tagType     TEXT NOT NULL,
    idVisit     INTEGER NOT NULL,
    PRIMARY KEY (tag, tagType, idVisit),
    FOREIGN KEY (tag, tagType) REFERENCES tags (tag, tagType) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (idVisit) REFERENCES visits (idVisit) ON UPDATE CASCADE ON DELETE CASCADE
);
