
[![CI Status](https://github.com/se-edu/addressbook-level3/workflows/Java%20CI/badge.svg)](https://github.com/AY2526S2-CS2103-F11-2/tp/actions)
[![codecov](https://codecov.io/github/AY2526S2-CS2103-F11-2/tp/graph/badge.svg?token=EWW9WYZODP)](https://codecov.io/github/AY2526S2-CS2103-F11-2/tp)

# CampusBridge

![Ui](/docs/images/Ui.png)

**CampusBridge** helps NUS students organise and access contact information for professors, teaching assistants, and peers across different modules and faculties. It provides a centralised, easy-to-use system to save, search, and manage academic contacts efficiently.

> Developed by Team F11-2 for CS2103 (AY25/26 Sem 2)

---

## Getting Started

1. Ensure you have Java `17` or above installed.
2. Download the latest `.jar` file from [here](https://github.com/AY2526S2-CS2103-F11-2/tp/releases).
3. Run `java -jar CampusBridge-v1.6.jar` in your terminal.

---

## Example Commands

| Command | Description |
|---------|-------------|
| `help` | Opens the help page |
| `add n/John Doe e/johnd@example.com p/98765432 h/johndoe` | Adds a new contact |
| `edit 1 n/Alice Doe e/aliced@example.com h/alicedoe` | Edits an existing contact |
| `delete i/3` | Deletes a contact by index or email |
| `tag 1 tg/friends tc/cs2103` | Tags a contact with role, course, or general tags |
| `untag 3 tr/tutor tc/cs2103` | Removes tags from a contact |
| `cleartag 1 tg/` | Clears a specific tag type from a contact |
| `list` | Lists all contacts |
| `sort o/name` | Sorts the contact list by name or email |
| `find n/alex e/gmail t/friends` | Searches for contacts matching all specified criteria |
| `undo` | Undoes the last undoable command |
| `clear` | Deletes all contacts |
| `exit` | Exits the app |

Type any command in the command box and press **Enter** to execute it.

## Known Issues

- **Multiscreen issue:** If the app is moved to a secondary screen and you later switch to using only the primary screen, the GUI may open off-screen. Fix: delete `preferences.json` and relaunch the app.

---

## Documentation

See the **[CampusBridge Product Website](https://ay2526s2-cs2103-f11-2.github.io/tp/)** for full documentation.

---

## Acknowledgements

- Libraries used: [JavaFX](https://openjfx.io/), [Jackson](https://github.com/FasterXML/jackson), [JUnit5](https://github.com/junit-team/junit5)
- This project is based on the [AddressBook-Level3](https://se-education.org/addressbook-level3/) project created by the [SE-EDU initiative](https://se-education.org).
