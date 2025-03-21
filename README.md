# **Progetto Elaborato-SWE**

## **Descrizione**
Il progetto **Elaborato-SWE** è un sistema di gestione per la prenotazione di visite mediche, sviluppato in **Java** con **PostgreSQL** come database relazionale.

### **Funzionalità principali:**
- **Gestione Dottori**: registrazione, modifica e visualizzazione delle informazioni.
- **Gestione Pazienti**: archiviazione dati personali e storico delle visite.
- **Prenotazione Visite Mediche**: creazione, modifica e cancellazione delle prenotazioni.
- **Gestione Tag**: categorizzazione delle visite per una migliore organizzazione.
- **Gestione Stati delle Visite**: implementata attraverso il pattern **State**, consente di gestire cambiamenti di stato come **Booked**, **Cancelled** e **Completed**.

L’architettura segue il pattern **Model-View-Controller (MVC)**, garantendo separazione tra logica di business e gestione dei dati.  
Il progetto include **test unitari e funzionali** per verificare il corretto funzionamento delle operazioni.

L'applicazione utilizza **Maven** per la gestione delle dipendenze e **JUnit 5** per i test.

---

## **Struttura del Progetto**

### **Codice Principale (`src/main/java/`)**
- **`businessLogic/`** → Contiene i **controller** che implementano la logica dell'applicazione.
    - `DoctorsController` → Gestione dei dottori.
    - `PatientsController` → Gestione dei pazienti.
    - `VisitsController` → Creazione e gestione delle visite.
    - `TagsController` → Gestione dei tag delle visite.
    - `StateController` → Gestisce la transizione degli stati delle visite.
- **`dao/`** → Contiene le classi **DAO** (Data Access Object) responsabili dell'interazione con PostgreSQL.
- **`domainModel/`** → Contiene le **classi di dominio** (Doctor, Patient, Visit, ecc.).
- **`Main.java`** → Classe principale che avvia l'applicazione.

### **Test (`src/test/java/`)**
Il progetto include:
- **Test unitari** per verificare i singoli componenti.
- **Test funzionali** per simulare flussi reali di utilizzo.

Esempi:
- `DoctorsControllerTest`, `VisitsControllerTest` → Verificano la logica applicativa.
- `PostgreSQLDoctorDAOTest`, `PostgreSQLVisitDAOTest` → Controllano l'integrazione con il database.
- `VisitsFunctionalTest` → Simula scenari completi, come la prenotazione e cancellazione di una visita.

### **Database (`src/main/resources/database/`)**
- Contiene lo **script `schema.sql`**, che definisce la struttura del database principale **GestionaleVisiteMediche**.
- Questo script deve essere eseguito prima dell'avvio dell'applicazione.

### **Database di Test (`src/test/resources/database/`)**
- Contiene lo **script `schema_test.sql`**, che inizializza il database di test **GestionaleVisiteMediche_test**.
- Questo script viene eseguito automaticamente prima dei test.

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

### **Clonare il repository**
Aprire il terminale ed eseguire:
```sh
  git clone <URL_DEL_REPOSITORY>
  cd Elaborato-SWE
```

### **Configurare PostgreSQL**
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

### **Configurare le credenziali del database**
Modificare il file `Database.java` per impostare correttamente le credenziali:
```java
private static String URL = "jdbc:postgresql://localhost:5432/GestionaleVisiteMediche";
private static final String USER = "postgres";  
private static final String PASSWORD = "1234"; 
```
Se utilizzi un file `.properties` o variabili d'ambiente, aggiornarle con i dati corretti.

### **Aprire il progetto con IntelliJ IDEA (o un altro IDE compatibile).**

---

## **Esecuzione del Database di Test**
Prima di eseguire i test, il database di test viene inizializzato automaticamente tramite **`schema_test.sql`**.  
L'inizializzazione avviene con:
```java
Database.initDatabase(true);  // Usa il database di test
```
Per inizializzare il database di produzione:
```java
Database.initDatabase(false);  // Usa il database principale
```

---

## **Esecuzione dei Test**
Per eseguire i test unitari e funzionali, utilizzare il comando:
```sh
  mvn test
```
I test verificano il corretto funzionamento di:  
**Controller** (logica di business)  
**DAO** (interazione con il database)  
**Flussi funzionali completi** (prenotazione, cancellazione, ricerca visite, ecc.)

---

## ** Differenza tra Test Unitari e Funzionali**
- **Test Unitari** → Verificano il funzionamento di singole unità di codice (es. `VisitsController` o `PostgreSQLVisitDAO`).
- **Test Funzionali** → Simulano scenari reali e l'interazione tra più componenti (es. un paziente prenota una visita e poi la cancella).

Esempio di test funzionale:
```java
@Test
void testDoctorCannotHaveOverlappingVisits() throws Exception {
    List<Tag> tags = new ArrayList<>();
    visitsController.addVisit("Visita Cardiologica", "Check-up cardiologico",
            LocalDateTime.of(2025, 3, 22, 10, 0),
            LocalDateTime.of(2025, 3, 22, 11, 0),
            60.0, "doctor1", tags);

    Exception exception = assertThrows(
            RuntimeException.class,
            () -> visitsController.addVisit("Visita Neurologica", "Esame neurologico",
                    LocalDateTime.of(2025, 3, 22, 10, 30),
                    LocalDateTime.of(2025, 3, 22, 11, 30),
                    70.0, "doctor1", tags)
    );

    assertTrue(exception.getMessage().contains("Il medico selezionato è già occupato"));
}
```

---

## **Autori**
**Gianmarco De Laurentiis**  
**Lorenzo Fedi**