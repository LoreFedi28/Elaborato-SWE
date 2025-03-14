-- Drop tables if they already exist
DROP TABLE IF EXISTS visitsTags;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS visits;
DROP TABLE IF EXISTS patients;
DROP TABLE IF EXISTS doctors;

-- Tabella dei medici
CREATE TABLE doctors
(
    cf VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    iban VARCHAR(50) NOT NULL
);

-- (Opzionale) Creazione di una sequenza per i medici, se serve in altri contesti
CREATE SEQUENCE IF NOT EXISTS doctors_id_seq
    START WITH 1
    INCREMENT BY 1;

-- Tabella dei pazienti
CREATE TABLE patients
(
    cf VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    urgencyLevel VARCHAR(50) NOT NULL
);

-- Tabella dei tag
CREATE TABLE tags
(
    tag VARCHAR(100) NOT NULL,
    tagType VARCHAR(50) NOT NULL,
    PRIMARY KEY (tag, tagType)
);

-- Tabella delle visite
CREATE TABLE visits
(
    idVisit SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    startTime TIMESTAMP NOT NULL,
    endTime TIMESTAMP NOT NULL,
    price NUMERIC(10,2) NOT NULL,
    doctorCF VARCHAR(50) NOT NULL,
    state VARCHAR(50) NOT NULL,
    stateExtraInfo VARCHAR(100),
    CONSTRAINT fk_doctor FOREIGN KEY (doctorCF) REFERENCES doctors (cf) ON DELETE CASCADE
);

-- Tabella di associazione tra visite e tag
CREATE TABLE visitsTags
(
    idVisit INTEGER NOT NULL,
    tagType VARCHAR(50) NOT NULL,
    tag VARCHAR(100) NOT NULL,
    PRIMARY KEY (idVisit, tagType, tag),
    CONSTRAINT fk_visit FOREIGN KEY (idVisit) REFERENCES visits (idVisit) ON DELETE CASCADE,
    CONSTRAINT fk_tag FOREIGN KEY (tag, tagType) REFERENCES tags (tag, tagType) ON DELETE CASCADE
);