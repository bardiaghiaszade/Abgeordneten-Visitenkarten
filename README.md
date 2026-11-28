For a better and quicker understanding of the project, take a look at the UML diagrams in `UML/`.

Small Java 21 / Maven project for Bundestag members (Abgeordnete) and their speeches (Reden). It imports a JSON file into MongoDB, lists the members in a console menu and exports static HTML pages.

## Setup and run

1. Put these two files into the project root. Both are in `.gitignore` and not part of the repository:
   - `PPR_2026_060.txt`: database access with the keys `remote_host`, `remote_port`, `remote_database`, `remote_user`, `remote_password`
   - `mdb_speeches.json`: the input data
2. Connect to the university network (VPN).
3. Run `Main` from the project root (all paths are relative to it).

## Menu

| Option | What it does |
|---|---|
| `1` Daten importieren | Reads the JSON file and writes it into MongoDB. Input-Path optional, empty = `mdb_speeches.json`. If data already exists: `Bereits importiert`. |
| `2` Abgeordnete auflisten | Lists the Fraktionen, then the Abgeordnete sorted by number of speeches. Enter a Fraktion name (case does not matter) or leave empty for all. |
| `3` HTML exportieren | Writes the HTML pages. Output-Path optional, empty = `output/`. A custom path must be an existing folder. If `index.html` already exists there: `Bereits exportiert`. |
| `0` Beenden | Exits the program. |

Wrong input shows `Fehler: ...` and the menu continues.

After the import MongoDB contains the collections `abgeordnete` (4613 documents) and `reden` (38633 documents).
To import again, drop both collections. To export again, delete the output folder.

Result of the export:

- `output/index.html`: all Abgeordnete per Fraktion, sorted by number of speeches, linked to their pages
- `output/abgeordnete/<id>.html`: one Visitenkarte per Abgeordneter with details and speeches

## Project structure

```
.
├── pom.xml
├── README.md
├── REQUIREMENTS.md             # task description
├── PPR_2026_060.txt            # database access (not in Git)
├── mdb_speeches.json           # input data (not in Git)
├── output/                     # generated HTML (not in Git)
├── UML/
│   ├── Übung_3.pdf             # finished diagrams
│   └── latex/                  # tikz-uml sources (usecase, package, class)
└── src/main/java/
    ├── Main.java               # console menu
    ├── database/               # MongoDBConnector
    ├── exception/              # DatabaseException, InvalidInputException
    ├── importer/               # Importer
    ├── factory/                # Factory
    ├── export/                 # Exporter, HTMLExporter, HTMLService
    └── model/                  # Abgeordneter, Rede
        ├── json/               # JSONAbgeordneter, JSONRede
        └── mongo/              # MongoAbgeordneter, MongoRede
```

## How it works

```
Import:  JSON file -> Importer -> JSON objects -> toDocument() -> MongoDB
Reading: MongoDB -> Factory (aggregation) -> Mongo objects -> menu / HTMLExporter
```

Selecting, counting and sorting by number of speeches happen in the database (aggregation), not in Java.
The Fraktion is the field `partei` of the data.

## Classes

database

- `MongoDBConnector`: connection to MongoDB. Reads the access data with `Properties` and offers `insert`, `find`, `count`, `aggregate` and `createIndex`.

model

- `Abgeordneter`, `Rede` (interfaces): the data structures. The rest of the program only uses these.
- `JSONAbgeordneter`, `JSONRede`: implementation for the JSON file. Creates the documents for the import with `toDocument()`. A `JSONAbgeordneter` creates and owns its `JSONRede` objects.
- `MongoAbgeordneter`, `MongoRede`: other implementation for reading. They hold the `Document` from MongoDB.

importer

- `Importer`: imports the JSON file into the collections `abgeordnete` and `reden`. Nothing happens if data already exists.

factory

- `Factory`: the only access to the data: `getFraktionen`, `getAbgeordnete(fraktion)` (sorted by number of speeches), `getReden(id)`. Creates an index on `reden.abgeordneterId`.

export

- `Exporter` (interface): export of all data.
- `HTMLExporter`: reads through the `Factory` and writes the pages.
- `HTMLService`: static functions that create the HTML text.

exception

- `DatabaseException`: configuration, connection, database and import file errors.
- `InvalidInputException`: wrong input in the menu.

## To be developed further

- Fraktion names are taken as they are in the data, spelling variants are not merged.
- The import collects all documents before writing them, which needs a lot of memory for large files.
- The HTML is plain (no styling) and texts are not escaped.
- The export creates one file per Abgeordneter (about 4600 files).
- No automated tests yet.
- The menu cannot search by the name of an Abgeordneter.