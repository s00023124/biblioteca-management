# Sistema di Gestione Biblioteca

## Panoramica del Progetto

Un sistema completo per la gestione di una biblioteca che permette di gestire documenti (libri e riviste), utenti e prestiti. Ho sviluppato questo progetto utilizzando i principali design pattern studiati durante il corso e applicando le best practice di programmazione Java.

### Funzionalità Principali

- **Gestione Documenti**: Aggiunta, ricerca e rimozione di libri e riviste
- **Gestione Utenti**: Registrazione di studenti, docenti e utenti esterni con limiti di prestito differenziati
- **Sistema Prestiti**: Creazione e restituzione con calcolo automatico delle scadenze (14 giorni)
- **Ricerca Avanzata**: Quattro diverse strategie di ricerca (per titolo, autore, ID o ricerca globale)
- **Notifiche**: Sistema di notifiche in tempo reale per le operazioni sui prestiti
- **Persistenza**: Salvataggio automatico dei dati su file

---

## Come Eseguire il Progetto

### Requisiti
- Java 11 o superiore
- Maven 3.6+

### Compilazione
```bash
mvn clean compile
```

### Esecuzione dei Test
```bash
mvn test
```
Il progetto include 45 test unitari che verificano il corretto funzionamento di tutti i componenti.

### Avvio dell'Applicazione
```bash
mvn package
java -jar target/biblioteca-management-1.0.0-jar-with-dependencies.jar
```

L'applicazione mostrerà un menu interattivo da console per utilizzare tutte le funzionalità.

---

## Tecnologie e Pattern Implementati

### Pattern Obbligatori

#### 1. Factory Pattern
Ho implementato il Factory Pattern in `DocumentFactory` per centralizzare la creazione di documenti. Questo mi permette di:
- Validare gli input prima della creazione
- Controllare duplicati di ID
- Mantenere il codice più pulito e manutenibile

```java
DocumentFactory factory = DocumentFactory.getInstance();
Book book = factory.createBook(params);
```

#### 2. Composite Pattern
Utilizzato per organizzare i documenti in categorie gerarchiche. Ho creato una struttura ad albero dove:
- `DocumentCategory` può contenere altre categorie o documenti
- `DocumentLeaf` rappresenta i singoli documenti
- È possibile navigare l'intera struttura in modo uniforme

#### 3. Iterator Pattern
Ho implementato un iterator custom per attraversare le collezioni di documenti in modo sicuro e controllato:
- `Iterator<T>` interface generica
- `DocumentIterator` implementazione concreta
- Metodi: `hasNext()`, `next()`, `reset()`, `getCurrentPosition()`

#### 4. Exception Shielding
Questo è stato uno degli aspetti più importanti per la sicurezza. Ho creato:
- Una gerarchia di eccezioni custom (`LibraryException` e sottoclassi)
- `ExceptionHandler` che cattura tutte le eccezioni
- Log completo su file per debug
- Messaggi user-friendly senza stack trace esposti

### Tecnologie Core

- **Collections Framework**: `ArrayList` per documenti e prestiti, `HashMap` per utenti
- **Generics**: Iterator generico, collezioni type-safe
- **Java I/O**: `BufferedReader/Writer` per persistenza, gestione file nella cartella `data/`
- **Logging**: Sistema custom `LibraryLogger` con livelli DEBUG/INFO/WARNING/ERROR
- **JUnit Testing**: 45 test organizzati in 5 classi di test

### Pattern e Tecnologie Opzionali

#### Strategy Pattern
Quattro strategie di ricerca intercambiabili:
- `TitleSearchStrategy`: ricerca per titolo
- `AuthorSearchStrategy`: ricerca per autore
- `IdSearchStrategy`: ricerca esatta per ID
- `GlobalSearchStrategy`: ricerca in tutti i campi

Il pattern permette di cambiare strategia runtime senza modificare il codice client.

#### Observer Pattern
Sistema di notifiche per eventi sui prestiti. Ho implementato:
- `LoanNotificationSystem` come Subject
- Tre Observer: `ConsoleNotifier`, `EmailNotifier` (simulato), `FileNotifier`
- Notifiche automatiche alla creazione e restituzione prestiti

#### Builder Pattern
Utilizzato in `DocumentCreationParams.Builder` per costruire parametri complessi in modo fluente e leggibile.

#### Singleton Pattern
Applicato a `DocumentFactory` e `LibraryLogger` per garantire un'unica istanza globale.

#### Template Method
Implementato in `Document.getDisplayInfo()` con hook method `getSpecificInfo()` sovrascritto dalle sottoclassi.

#### Stream API & Lambdas
Utilizzo intensivo di Stream API per operazioni su collezioni:
```java
documents.stream()
    .filter(doc -> doc.getTitle().toLowerCase().contains(query))
    .collect(Collectors.toList());
```

---

## Architettura del Progetto

```
src/main/java/com/biblioteca/
├── model/              # Entità del dominio
│   ├── Document.java       # Classe astratta base
│   ├── Book.java           # Libro con ISBN, pagine, genere
│   ├── Magazine.java       # Rivista con numero, periodicità
│   ├── User.java           # Utente (Student/Teacher/External)
│   └── Loan.java           # Prestito con date
│
├── factory/            # Factory Pattern
│   ├── DocumentFactory.java
│   └── DocumentCreationParams.java  # Builder Pattern
│
├── composite/          # Composite Pattern
│   ├── DocumentComponent.java
│   ├── DocumentCategory.java
│   └── DocumentLeaf.java
│
├── iterator/           # Iterator Pattern
│   ├── Iterator.java
│   ├── Collection.java
│   ├── DocumentIterator.java
│   └── DocumentCollection.java
│
├── strategy/           # Strategy Pattern
│   ├── SearchStrategy.java
│   ├── TitleSearchStrategy.java
│   ├── AuthorSearchStrategy.java
│   ├── IdSearchStrategy.java
│   ├── GlobalSearchStrategy.java
│   └── SearchContext.java
│
├── observer/           # Observer Pattern
│   ├── Subject.java
│   ├── Observer.java
│   ├── LoanNotificationSystem.java
│   ├── ConsoleNotifier.java
│   ├── EmailNotifier.java
│   └── FileNotifier.java
│
├── service/            # Business Logic
│   └── LibraryService.java
│
├── exception/          # Exception Shielding
│   ├── LibraryException.java
│   ├── ExceptionHandler.java
│   ├── DocumentNotFoundException.java
│   ├── UserNotFoundException.java
│   ├── LoanException.java
│   ├── InvalidInputException.java
│   ├── InvalidDocumentException.java
│   └── DataAccessException.java
│
├── io/                 # Persistenza
│   ├── FileManager.java
│   └── DataPersistence.java
│
├── util/               # Utility
│   ├── LibraryLogger.java      # Singleton
│   └── InputValidator.java
│
├── ui/                 # Interfaccia Utente
│   └── ConsoleUI.java
│
└── Main.java           # Entry point
```

---

## Diagrammi UML

### Class Diagram (Struttura Principale)

```
┌─────────────────┐
│   <<abstract>>  │
│    Document     │
├─────────────────┤
│ - id: String    │
│ - title: String │
│ - author: String│
│ - pubDate: Date │
│ - available: bool│
├─────────────────┤
│ + getDisplayInfo()│
│ + getSpecificInfo()│ (abstract)
└────────┬────────┘
         │
    ┌────┴─────┐
    │          │
┌───▼──┐   ┌──▼────┐
│ Book │   │Magazine│
├──────┤   ├────────┤
│-isbn │   │-issueNo│
│-pages│   │-period │
│-genre│   │        │
└──────┘   └────────┘


┌────────────────────┐
│ DocumentFactory    │  (Singleton)
├────────────────────┤
│ - instance: static │
├────────────────────┤
│ + getInstance()    │
│ + createBook()     │
│ + createMagazine() │
└────────────────────┘


┌──────────────────┐        ┌──────────────┐
│  SearchContext   │ ──────>│SearchStrategy│
├──────────────────┤        ├──────────────┤
│ - strategy       │        │ + search()   │
├──────────────────┤        └──────┬───────┘
│ + setStrategy()  │               │
│ + executeSearch()│        ┌──────┴───────────┐
└──────────────────┘        │                  │
                      ┌─────▼────┐      ┌─────▼─────┐
                      │  Title   │      │  Author   │
                      │ Strategy │      │ Strategy  │
                      └──────────┘      └───────────┘
```

### Architectural Diagram (Flusso Operazioni)

```
┌─────────────┐
│ ConsoleUI   │  (Presentazione)
└──────┬──────┘
       │
       ▼
┌─────────────────┐
│ LibraryService  │  (Business Logic)
└────────┬────────┘
         │
    ┌────┼─────────┬──────────┐
    │    │         │          │
    ▼    ▼         ▼          ▼
┌────────┐  ┌──────────┐  ┌─────────┐  ┌──────────┐
│Factory │  │Strategy  │  │Observer │  │Exception │
│Pattern │  │Pattern   │  │Pattern  │  │Shielding │
└────────┘  └──────────┘  └─────────┘  └──────────┘
    │            │             │             │
    └────────────┴─────────────┴─────────────┘
                      │
                      ▼
               ┌──────────────┐
               │ Data Layer   │
               │ (File I/O)   │
               └──────────────┘
```

---

## Scelte Implementative e Giustificazioni

### Perché il Factory Pattern?
Inizialmente stavo usando i costruttori direttamente, ma mi sono resa conto che serviva un punto centralizzato per:
- Validare gli ISBN (formato specifico)
- Controllare ID duplicati
- Gestire la logica di creazione complessa

Il Factory Pattern mi ha permesso di isolare questa logica.

### Perché quattro strategie di ricerca?
Ho scelto di implementare diverse strategie perché in una biblioteca reale gli utenti cercano in modi diversi:
- Spesso sanno solo il titolo approssimativo
- A volte ricordano l'autore
- Gli operatori cercano per ID esatto
- La ricerca globale è utile quando non si è sicuri

### Come funziona Exception Shielding?
Tutte le eccezioni vengono catturate da `ExceptionHandler` che:
1. Logga l'errore completo (con stack trace) su `biblioteca.log`
2. Mostra all'utente solo un messaggio chiaro e sicuro
3. Non espone mai dettagli tecnici o percorsi di sistema

Questo previene information leakage e migliora l'esperienza utente.

### Persistenza File vs Database
Ho scelto file di testo per:
- Semplicità di implementazione
- Nessuna dipendenza esterna
- Facile debugging (posso aprire i file e vedere i dati)

I file sono organizzati nella cartella `data/`:
- `documents.txt` - tutti i documenti
- `users.txt` - utenti registrati  
- `loans.txt` - prestiti attivi e storici

---

## Test Suite

Ho organizzato 45 test in 5 classi:

1. **DocumentFactoryTest** (7 test)
   - Creazione documenti validi
   - Validazione input
   - Gestione errori

2. **DocumentIteratorTest** (8 test)
   - Navigazione collezioni
   - Gestione liste vuote
   - Reset e posizione corrente

3. **DocumentCompositeTest** (10 test)
   - Creazione categorie
   - Operazioni ricorsive
   - Display gerarchico

4. **SearchStrategyTest** (8 test)
   - Tutte le 4 strategie
   - Cambio strategia runtime
   - Case sensitivity

5. **LibraryServiceTest** (13 test)
   - Integrazione completa
   - CRUD operations
   - Business rules (limiti prestiti, disponibilità)
   - Persistenza con `@TempDir`

Tutti i test passano al 100%.

---

## Sicurezza Implementata

Il progetto rispetta tutte le best practice di sicurezza richieste:

**Nessuno stack trace esposto** - Solo messaggi user-friendly  
**Input sanitization** - Validazione con regex per email, ID, telefono  
**No hardcoded credentials** - Nessuna password o chiave nel codice  
**Exception propagation controllata** - ExceptionHandler centralizzato  
**Logging sicuro** - Stack trace solo su file, mai in console

---

## Limitazioni Conosciute

1. **Interfaccia Console-only** - Nessuna GUI grafica
2. **Storage su file** - Non c'è un database relazionale
3. **Single-user** - Nessun supporto per accesso concorrente
4. **Email simulate** - Le notifiche email sono solo stampate su log
5. **Solo Book e Magazine** - Altri tipi di documenti (DVD, giornali) sono definiti ma non implementati

---

## Possibili Sviluppi Futuri

Se dovessi continuare questo progetto, vorrei aggiungere:

- **Sistema di Prenotazioni** - Prenotare libri già in prestito
- **Autenticazione** - Login per utenti e staff con ruoli diversi
- **Multe** - Calcolo automatico multe per ritardi
- **Statistiche Avanzate** - Dashboard con grafici e report

---

## Note Tecniche

### File Generati
- `biblioteca.log` - Log applicazione (nella root del progetto)
- `data/documents.txt` - Persistenza documenti
- `data/users.txt` - Persistenza utenti
- `data/loans.txt` - Persistenza prestiti

La cartella `data/` viene creata automaticamente al primo avvio se non esiste.

### Formato Date
Il sistema usa il formato **yyyy-MM-dd** (es: 2024-02-06) per tutte le date.

### Limiti Prestiti
- **Student**: massimo 5 prestiti contemporanei
- **Teacher**: massimo 10 prestiti contemporanei
- **External**: massimo 3 prestiti contemporanei

I prestiti hanno una durata standard di **14 giorni** dalla data di creazione.

---

## Conclusioni

Questo progetto mi ha permesso di mettere in pratica tutti i concetti studiati durante il corso. La parte più interessante è stata l'integrazione dei diversi pattern per creare un sistema coeso. La più impegnativa è stata sicuramente la gestione delle eccezioni e il testing completo.

Sono soddisfatta del risultato finale e credo che il codice sia pulito, ben organizzato e facilmente estendibile per future funzionalità.

---

**Erika Panciroli**  
Febbraio 2026
