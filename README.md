[![CI Status](https://github.com/se-edu/addressbook-level3/workflows/Java%20CI/badge.svg)](https://github.com/AY2526S2-CS2103-F11-2/tp/actions)
[![codecov](https://codecov.io/github/AY2526S2-CS2103-F11-2/tp/graph/badge.svg?token=EWW9WYZODP)](https://codecov.io/github/AY2526S2-CS2103-F11-2/tp)

![Ui](docs/images/Ui.png)

* Product name: CampusBridge
* Value proposition:Students at NUS need to organize and access contact information for their professors, teaching assistants, and peers across different modules and faculties. These students frequently communicate via multiple platforms and require a centralized, easy-to-use system to save, search, and manage academic contacts efficiently.
* This project is developed by Team F11-2 for CS2103T (AY25/26 Sem 2).

* Version 1.0

* How to start:
Open a command terminal, cd into the folder you put the jar file in, and use the java -jar addressbook.jar command to run the application.

* Feature list:
Type the command in the command box and press Enter to execute it. e.g. typing help and pressing Enter will open the help window.

* Some example commands you can try:
1. list : Lists all contacts.
2. add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01 : Adds a contact named John Doe to the Address Book.
3. delete 3 : Deletes the 3rd contact shown in the current list.
4. clear : Deletes all contacts.
5. exit : Exits the app.

Known issues
1. When using multiple screens, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the preferences.json file created by the application before running the application again.
2. If you minimize the Help Window and then run the help command (or use the Help menu, or the keyboard shortcut F1) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

* For the detailed documentation of this project, see the **[CampusBridge Product Website](https://ay2526s2-cs2103-f11-2.github.io/tp/)**.

**Acknowledgements**

* This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org).
