---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams are in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2526S2-CS2103T-F11-2/tp/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefsStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

**Adapter pattern for JSON serialization**

The `Storage` component uses an adapter pattern to bridge between the domain model and JSON representation. Three adapter classes handle the conversion:

* `JsonSerializableAddressBook` — wraps the entire address book for serialization; its `toModelType()` method converts back to a `ReadOnlyAddressBook`, checking for duplicate persons in the process.
* `JsonAdaptedPerson` — represents a single `Person` in JSON form. The fields `phone` and `telegramHandle` are optional (nullable); `name` and `email` are required. `toModelType()` validates each field and throws `IllegalValueException` if any value is invalid.
* `JsonAdaptedTag` — represents a single `Tag`, storing both `tagName` and `tagType`.

**JSON data format**

Data is stored in two JSON files:

* `data/addressbook.json` — contact list:
  ```json
  {
    "addressbook": {
      "persons": [
        {
          "name": "Alex Yeoh",
          "phone": "87438807",
          "email": "alexyeoh@example.com",
          "telegramHandle": "alexyeoh",
          "tags": [{ "tagName": "cs2103t", "tagType": "COURSE" }]
        }
      ]
    }
  }
  ```
* `preferences.json` — GUI window size/position and the address book file path:
  ```json
  {
    "guiSettings": { "windowWidth": 740.0, "windowHeight": 574.0, "windowCoordinates": { "x": 100, "y": 100 } },
    "addressBookFilePath": "data/addressbook.json"
  }
  ```

**Error handling on startup**

When CampusBridge starts, it attempts to read the address book file and handles three cases:
* **File not found** — sample data is loaded and the file is created on the next save.
* **File is malformed or contains invalid data** — an empty address book is used and a warning is logged; the corrupted file is left untouched.
* **File is valid** — data is loaded normally.

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

  ### Current Undo feature

#### Current Implementation

Currently, the undo feature is implemented in each undoable commands, and is now handled by LogicManager.

Undoable commands, once executed, will be added in to a deque.

When an undo command is called, the deque will peek at the first item to be out.

If the undo of the command is successfully executed, the deque will pop.

More UML diagrams are to be added in a later.

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:
NUS undergraduate students who
* Need to organize contact information of their Professors, Teaching Assistants and Groupmates.
* Values efficiency and prefers tools that save time and reduce friction.
* Prefer using CLI over GUI.

**Value proposition**:
CampusBridge helps NUS undergraduate students to organize and access contact information for their academic peers across different modules and faculties.

It does so by providing a centralized, easy-to-use system to save, search, and manage academic contacts efficiently.

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                     | I can …​                                                                | So that I can…​                                                        |
|----------|-----------------------------|-------------------------------------------------------------------------|------------------------------------------------------------------------|
| `* * *`  | user                        | add a contact                                                           | store and organize important academic contact information in one place |
| `* * *`  | user                        | view all my contacts                                                    | quickly see everyone in one place                                      |
| `* * *`  | user                        | delete a contact                                                        | keep my contact list accurate and organized                            |
| `* * *`  | user                        | exit the application                                                    | safely close CampusBridge when I am done using it                      |
| `* * *`  | user                        | have my contacts saved automatically                                    | prevent losing my data when the application closes                     |
| `* *`    | user                        | add new tags to an existing contact                                     | keep their information updated as the semester evolves                 |
| `* *`    | user                        | delete specific tags from a contact without deleting the entire contact | keep my contact information accurate                                   |
| `* *`    | regular user                | mark a preferred contact method                                         | know the fastest way to reach someone                                  |
| `*`      | frequent user               | set custom reminders for prof/TA office hours                           | stay on top of opportunities for academic help                         |
| `*`      | user desiring full control  | customize the GUI theme                                                 | personalize my experience                                              |

See the full list on [GitHub](https://github.com/AY2526S2-CS2103-F11-2/tp/issues)

### Use cases

(For all use cases below, the **System** is the `CampusBridge` and the **Actor** is the `user`, unless specified otherwise)

#### Use Case: UC01 - Add a contact

**Preconditions: Application is running**

**MSS:**
1. User requests to add a contact.
2. User provides the contact details.
3. CampusBridge validates the input.
4. CampusBridge adds the contact and updates the contact list.
5. CampusBridge shows a success message.

Use case ends.

**Extension:**
* 3a. Input does not follow the specified format.
  * 3a1. CampusBridge shows an error message indicating the invalid format.
  * 3a2. CampusBridge requests the user to re-enter input.
  * 3a3. User enters a new input.
  
  Steps 3a1 - 3a3 are repeated until input is valid.  
  Use case resumes at step 4.

* 3b. Email already exists in the contact list.
  * 3b1. CampusBridge shows a failure message indicating that email already exists.

  Use case ends.

* 4a. Contact cannot be added.
    * 4a1. CampusBridge shows an error message indicating the contact could not be added.

  Use case ends.

* 4b. Storage file cannot be written or accessed.
  * 4b1. CampusBridge shows an error message indicating the contact list could not be saved.

  Use case ends.


#### Use Case: UC02 - Edit a contact

**Preconditions: Application is running and the user has added a contact.**

**MSS:**
1. User <ins>requests to list contacts (UC04)</ins>.
2. User requests to edit a contact in the list.
3. User provides new contact details for that contact.
4. CampusBridge validates the input.
5. CampusBridge edits the contact and updates the contact list.
6. CampusBridge shows a success message.

Use case ends.

**Extension:**
* 4a. Target contact identifier does not exist.
  * 4a1. CampusBridge shows an error message indicating the contact does not exist.

  Use case ends.

* 4b. Input does not follow the specified format.
    * 4b1. CampusBridge shows an error message indicating the invalid format.
    * 4b2. CampusBridge requests the user to re-enter input.
    * 4b3. User enters a new input.

  Steps 4b1 - 4b3 are repeated until input is valid.  
  Use case resumes at step 5.

* 5a. Contact cannot be updated.
    * 5a1. CampusBridge shows an error message indicating the contact could not be updated.

  Use case ends.

* 5b. Storage file cannot be written or accessed.
  * 5b1. CampusBridge shows an error message indicating the contact list could not be saved.

  Use case ends.


#### Use Case: UC03 - Delete a contact

**Preconditions: Application is running and the user has added a contact.**

**MSS:**
1. User <ins>requests to list contacts (UC04)</ins>.
2. User requests to delete a contact in the list.
3. CampusBridge validates the input.
4. CampusBridge deletes the contact and updates the contact list.
5. CampusBridge shows a success message.

Use case ends.

**Extensions:**
* 3a. Target contact identifier does not exist.
    * 3a1. CampusBridge shows an error message indicating the contact does not exist.

  Use case ends.

* 3b. Input does not follow the specified format.
    * 3b1. CampusBridge shows an error message indicating the invalid format.
    * 3b2. CampusBridge requests the user to re-enter input.
    * 3b3. User enters a new input.

  Steps 3b1 - 3b3 are repeated until input is valid.  
  Use case resumes at step 4.

* 4a. Contact cannot be deleted.
    * 4a1. CampusBridge shows an error message indicating the contact could not be deleted.

  Use case ends.

* 4b. Storage file cannot be written or accessed.
    * 4b1. CampusBridge shows an error message indicating the contact list could not be saved.

  Use case ends.


#### Use Case: UC04 - View all contacts

**Preconditions: Application is running**

**MSS:**
1. User requests to list contacts.
2. CampusBridge shows a list of all contacts.
3. User can view details of each contact in the list.

Use case ends.

**Extensions:**
* 1a. Input does not follow the specified format.
    * 1a1. CampusBridge shows an error message indicating the invalid format.
    * 1a2. CampusBridge requests the user to re-enter input.
    * 1a3. User enters a new input.

  Steps 1a1 - 1a3 are repeated until input is valid.  
  Use case resumes at step 2.

* 2a. No contacts exist in the list.
  * 2a1. CampusBridge informs the user that the contact list is empty.

  Use case ends.


#### Use Case: UC05 - Search contacts

**Preconditions: Application is running**

**MSS:**
1. User requests to search for contacts.
2. User provides the search details.
3. CampusBridge validates the input.
4. CampusBridge shows the contacts matching the search query.

Use case ends.

**Extensions:**
* 3a. Input does not follow the specified format.
    * 3a1. CampusBridge shows an error message indicating the invalid format.
    * 3a2. CampusBridge requests the user to re-enter input.
    * 3a3. User enters a new input.

  Steps 3a1 - 3a3 are repeated until input is valid.  
  Use case resumes at step 4.

* 4a. No contacts exist in the list.
    * 4a1. CampusBridge informs the user that no contacts match the search query.

  Use case ends.


#### Use Case: UC06 - Add a tag to an existing contact

**Preconditions: Application is running**

**MSS:**
1. User requests to tag a contact in the list.
2. User provides tag details for that contact.
3. CampusBridge validates the input.
4. CampusBridge adds the tag and updates the contact list.
5. CampusBridge shows a success message.

Use case ends.

**Extensions:**
* 3a. Target contact identifier does not exist.
    * 3a1. CampusBridge shows an error message indicating the contact does not exist.

  Use case ends.

* 3b. Input does not follow the specified format.
    * 3b1. CampusBridge shows an error message indicating the invalid format.
    * 3b2. CampusBridge requests the user to re-enter input.
    * 3b3. User enters a new input.

  Steps 3b1 - 3b3 are repeated until input is valid.  
  Use case resumes at step 4.

* 3c. Tag already exists for contact.
    * 3c1. CampusBridge informs user that the contact already has this tag.

  Use case ends.

* 4a. Tag cannot be added.
    * 4a1. CampusBridge shows an error message indicating the tag could not be added.

  Use case ends.

* 4b. Storage file cannot be written or accessed.
    * 4b1. CampusBridge shows an error message indicating the contact list could not be saved.

  Use case ends.


### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2. Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. Changes should be implemented incrementally, i.e. in a way that allows the app to be usable after each increment, even if some features are not yet implemented.
5. All logics and storage should be implemented locally, to ensure testability and usability in secure environments without internet access.
6. The distributed JAR file should not be bloated, preferably less than 10MB, to ensure that it can be easily downloaded and stored on devices with limited storage.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Private contact detail**: A contact detail that is not meant to be shared with others

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Viewing help

1. Opening general help

   1. Test case: `help`<br>
      Expected: The User Guide opens in the system default browser. Status message shows `Opened user guide in browser.`

   1. Alternative: Press <kbd>F1</kbd>.<br>
      Expected: Same as above.

1. Opening command-specific help

   1. Test case: `help add`<br>
      Expected: The User Guide opens in the system default browser at the `add` command section. Status message shows `Opening user guide for 'add' command.`

   1. Other valid command names to try: `help list`, `help edit`, `help delete`, `help find`, `help sort`, `help tag`, `help untag`, `help cleartag`, `help clear`, `help exit`<br>
      Expected: The User Guide opens at the respective command section. Status message names the command.

1. Invalid help arguments

   1. Test case: `help INVALID`<br>
      Expected: The User Guide does not open. Error details shown in the status message.

   1. Test case: `help ADD` (uppercase)<br>
      Expected: Same as above. Command names are case-sensitive and must be lowercase.

   1. Test case: `help add extra`<br>
      Expected: Same as above. Only a single command name is accepted; extra words cause a format error.

### Sorting persons

1. Sorting by a valid field

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `sort o/name`<br>
      Expected: Contact list is sorted alphabetically by name (ascending). Status message shows `Sorted by name (ascending).`

   1. Test case: `sort o/name r/`<br>
      Expected: Contact list is sorted alphabetically by name (descending). Status message shows `Sorted by name (descending).`

   1. Test case: `sort o/email`<br>
      Expected: Contact list is sorted by email address (ascending). Status message shows `Sorted by email (ascending).`

   1. Test case: `sort o/phone`<br>
      Expected: Contact list is sorted by phone number (ascending). Contacts with no phone number appear at the end of the list. Status message shows `Sorted by phone (ascending).`

   1. Test case: `sort o/email r/`<br>
      Expected: Contact list is sorted by email address (descending). Status message shows `Sorted by email (descending).`

1. Resetting sort order

   1. Test case: `sort o/none`<br>
      Expected: Contact list reverts to the default (insertion) order. Status message shows `Sort order reset to default.`

   1. Test case: `sort o/none r/`<br>
      Expected: Sort order is not reset. Error details shown in the status message indicating that `r/` cannot be used with `o/none`.

1. Invalid sort commands

   1. Test case: `sort`<br>
      Expected: List is not sorted. Error details shown in the status message.

   1. Test case: `sort o/INVALID`<br>
      Expected: List is not sorted. Error details shown in the status message indicating the unsupported order value.

   1. Test case: `sort o/NAME` (uppercase)<br>
      Expected: List is sorted by name. Order values are case-insensitive.

   1. Test case: `sort o/name r/value`<br>
      Expected: List is not sorted. Error details shown in the status message indicating that `r/` does not accept a value.

   1. Test case: `sort o/name o/email`<br>
      Expected: List is not sorted. Error details shown in the status message indicating duplicate `o/` prefix.

### Navigating command history

1. Navigating to previous commands

   1. Prerequisites: Execute at least two commands, e.g. `list` then `help`.

   1. Press the <kbd>↑</kbd> (Up) arrow key while the command box is focused.<br>
      Expected: The most recently executed command (`help`) appears in the command box.

   1. Press <kbd>↑</kbd> again.<br>
      Expected: The earlier command (`list`) appears in the command box.

   1. Press <kbd>↑</kbd> when already at the oldest command.<br>
      Expected: Nothing happens. Navigation stops at the oldest entry.

1. Navigating forward through history

   1. Prerequisites: Navigate back through history using <kbd>↑</kbd> as above.

   1. Press the <kbd>↓</kbd> (Down) arrow key.<br>
      Expected: The next more recent command appears in the command box.

   1. Press <kbd>↓</kbd> until past the most recent command.<br>
      Expected: The command box is cleared (shows an empty input field), ready for a new command.

   1. Press <kbd>↓</kbd> when already at the empty new-command position.<br>
      Expected: Nothing happens.

1. Empty commands and history

   1. Prerequisites: Clear the command box and press Enter without typing anything.

   1. Press <kbd>↑</kbd>.<br>
      Expected: The last non-empty command appears. The empty submission was not added to history.

1. Clearing the command box

   1. While the command box contains any text, press the <kbd>Delete</kbd> key.<br>
      Expected: The command box is cleared immediately. This does not affect command history.

1. History is session-only

   1. Execute several commands, then close and relaunch the app.<br>
      Expected: Press <kbd>↑</kbd> — no history is available. History is not persisted across sessions.

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
