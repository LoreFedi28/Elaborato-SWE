# **Progetto Elaborato-SWE**

## **Descrizione**
Il progetto **Elaborato-SWE** è un sistema di gestione per la prenotazione di visite mediche, sviluppato in **Java** con **PostgreSQL** come database relazionale.

### **Funzionalità principali:**
- **Gestione Dottori**: registrazione, modifica e visualizzazione delle informazioni.
- **Gestione Pazienti**: archiviazione dati personali e storico delle visite.
- **Prenotazione Visite Mediche**: creazione, modifica e cancellazione delle prenotazioni.
- **Gestione Tag**: categorizzazione delle visite per una migliore organizzazione.

L’architettura segue il pattern **Model-View-Controller (MVC)**, garantendo separazione tra logica di business e gestione dei dati.  
Per assicurare affidabilità e correttezza, il progetto include **test unitari** che verificano le principali funzionalità.

L'applicazione utilizza **Maven** per la gestione delle dipendenze e **JUnit 5** per i test.

---

## **Struttura del Progetto**

### **Codice Principale (`src/main/java/`)**
- **`businessLogic/`** → Contiene i **controller** che implementano la logica dell'applicazione.
- **`dao/`** → Contiene le classi **DAO** (Data Access Object) responsabili dell'interazione con PostgreSQL.
- **`domainModel/`** → Contiene le **classi di dominio** (Doctor, Patient, Visit, ecc.).
- **`Main.java`** → Classe principale che avvia l'applicazione.

### **Test (`src/test/java/`)**
- Test per i **controller** (`DoctorsControllerTest`, `VisitsControllerTest`, ecc.).
- Test per i **DAO** (`PostgreSQLDoctorDAOTest`, `PostgreSQLVisitDAOTest`, ecc.).
- Test per il **database** (`DatabaseTest`).

### **Database (`src/main/resources/database/`)**
- Contiene lo **script `schema.sql`**, che definisce la struttura del database principale **GestionaleVisiteMediche**.
- Questo script deve essere eseguito prima dell'avvio dell'applicazione.

### **Database di Test (`src/test/resources/database/`)**
- Contiene lo **script `schema_test.sql`**, che inizializza il database di test **GestionaleVisiteMediche_test**.
- Questo script viene eseguito automaticamente prima dell’esecuzione dei test.

---

## **Requisiti**
Per eseguire il progetto, è necessario installare:
- **Java 17+**
- **PostgreSQL**
- **Maven**
- **JUnit 5**
- **IntelliJ IDEA** (o un altro IDE compatibile con Maven)

---

## **Configurazione**

### 1️⃣ **Clonare il repository**
Aprire il terminale ed eseguire:
```sh
  git clone <URL_DEL_REPOSITORY>
  cd Elaborato-SWE
```

### 2️⃣ **Configurare PostgreSQL**
Assicurarsi che il server PostgreSQL sia in esecuzione e creare i database richiesti:
```sql
CREATE DATABASE GestionaleVisiteMediche;
CREATE DATABASE GestionaleVisiteMediche_test;
```
Eseguire poi lo script SQL per il database principale:
```sh
   psql -U <utente> -d GestionaleVisiteMediche -f src/main/resources/database/schema.sql
```
Per il database di test, lo script **`schema_test.sql`** viene caricato automaticamente prima dei test.

### 3️⃣ **Configurare le credenziali del database**
Modificare il file di configurazione per inserire le credenziali corrette di PostgreSQL.  
Se utilizzi un file `.properties` o variabili d'ambiente, aggiornarle con i dati corretti.

### 4️⃣ **Aprire il progetto con IntelliJ IDEA (o un altro IDE compatibile).**

---

## **Esecuzione del Database di Test**
Prima di eseguire i test, il database di test viene inizializzato automaticamente tramite **`schema_test.sql`**.  
L'inizializzazione avviene prima di ogni test con:
```java
Database.initDatabase(true);
```

---

## **Esecuzione dei Test**
Per eseguire i test unitari, utilizzare il comando:
```sh
  mvn test
```
I test verificano il corretto funzionamento di:  
**Controller** (logica di business)  
**DAO** (interazione con il database)  
**Operazioni sui dati**

---

## **Autori**
**Gianmarco De Laurentiis**  
**Lorenzo Fedi**
