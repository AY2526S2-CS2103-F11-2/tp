---
layout: page
title: User Guide
---

CampusBridge is a **desktop app for managing contacts, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, CampusBridge can get your contact management tasks done faster than traditional GUI apps.

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/se-edu/addressbook-level3/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your CampusBridge application.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar addressbook.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add n/John Doe e/johnd@example.com p/98765432 h/johndoe123 t/friend` : Adds a contact named `John Doe` to CampusBridge.

   * `delete i/3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help : `help`

Opens the help window with a link to this user guide, or opens the user guide directly to the section for a specific command.

![help message](images/helpMessage.png)

Format: `help [COMMAND]`

* `COMMAND` is optional. When provided, it must be a valid command name (e.g. `add`, `edit`).
* If `COMMAND` is omitted, the help window is shown.
* If `COMMAND` is provided, your browser opens the user guide at the section for that command.

Examples:
* `help` — opens the help window.
* `help add` — opens the user guide at the **Adding a person** section.
* `help sort` — opens the user guide at the **Sorting persons** section.


### Adding a person: `add`

Adds a person to the address book.

Format: `add n/NAME e/EMAIL [p/PHONE_NUMBER] [h/TELEGRAM_HANDLE] [t/TAG]…​`

* `n/NAME` and `e/EMAIL` are required.
* `p/PHONE_NUMBER`, `h/TELEGRAM_HANDLE`, and `t/TAG` are optional.
* A person can have any number of tags (including 0).
* If no phone number is provided, the contact will be created without one.
* If no Telegram handle is provided, the contact will be created without one.
* Email must be unique. You cannot add two persons with the same email address.

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
Parameters can be entered in any order, as long as each value is preceded by the correct prefix.
</div>

Examples:
* `add n/John Doe e/johnd@example.com`
* `add n/Betsy Crowe e/betsycrowe@example.com p/1234567 t/friend t/criminal`
* `add n/Alex Lim e/alexlim@example.com h/alex_lim123`
* `add e/berniceyu@example.com n/Bernice Yu p/98765432 h/bernice_yu t/project`

### Listing all persons : `list`

Shows a list of all persons in the address book.

Format: `list`

### Sorting persons : `sort`

Sorts the list of persons by the specified order.

Format: `sort o/ORDER [r/]`

* `ORDER` is case-insensitive. The currently supported value is:
  * `name` — sorts persons alphabetically by name (A–Z)
* The `r/` flag is optional. When included, the sort order is reversed (Z–A for `name`).

Examples:
* `sort o/name` sorts all persons alphabetically by name.
* `sort o/name r/` sorts all persons in reverse alphabetical order.

### Editing a person : `edit`

Edits an existing person in the address book.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [h/TELEGRAM_HANDLE] [t/TAG]…​`

* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, the existing tags of the person will be removed i.e adding of tags is not cumulative.
* You can remove all the person’s tags by typing `t/` without
    specifying any tags after it.

Examples:
*  `edit 1 p/91234567 e/johndoe@example.com` Edits the phone number and email address of the 1st person to be `91234567` and `johndoe@example.com` respectively.
*  `edit 2 n/Betsy Crower h/betsyy t/` Edits the name of the 2nd person to be `Betsy Crower`, the telegram handle to be `betsyy` and clears all existing tags.

### Filtering persons by tags: `filter`

Filters the list of persons by one or more tags.

Format: `filter t/TAG [MORE_TAGS]…`

* At least one `t/` prefix with a tag must be provided.
* Persons with **any** of the specified tags will be shown.
* The search with tags is case-insensitive. For example, `friend` matches `Friend`.
* Tags can be combined in a single command to filter more broadly.

Examples:
* `filter t/friend` — shows all persons tagged as `friend`.
* `filter t/friend t/colleague` — shows all persons who are tagged as either `friend` **or** `colleague`.
* `filter t/family t/friend t/neighbor` — shows all persons who have **at least one** of the tags: `family`, `friend`, or `neighbor`.

### Locating persons by name/email: `find`

Finds persons whose names or emails contain any of the given keywords.

Format: `find n/NAME [MORE_NAMES] e/EMAIL [MORE_EMAILS]`

* Both `n/` and `e/` are optional, but at least one must be present.
* The search is case-insensitive for both name and email. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* For name, only full words will be matched e.g. `Han` will not match `Hans`
* For email, partial substrings will be matched e.g. `gmail` will match `alice@gmail.com`
* Persons matching at least one name/email keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `find n/John` returns `john` and `John Doe`
* `find n/alex david` returns `Alex Yeoh`, `David Li`<br>
* `find e/david` returns `Alex Yeoh`, `David Li`<br>
* `find n/alex e/doe` returns `Alex Yeoh`, `John Doe`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)

### Deleting a person : `delete`

Deletes the specified person from the address book.

Format: 
* `delete i/INDEX`
  * Deletes the person at the specified `INDEX`. 
  * The index refers to the index number shown in the displayed person list.
  * The index **must be a positive integer** 1, 2, 3, …​

* `delete e/EMAIL`
  * Deletes the person with the specified `EMAIL`.
  * The email refers to the email address of a person shown in the displayed person list.
  * The email **must be a valid email address**. 
  * Email matching is **case-insensitive**.

<div markdown="block" class="alert alert-info">:information_source: **NOTE:**
Only one of `i/INDEX` or `e/EMAIL` can be provided at a time.
</div>

Examples:
* Delete by index
  * `list` followed by `delete i/2` deletes the 2nd person in the address book.
  * `find n/Betsy` followed by `delete i/1` deletes the 1st person in the results of the `find` command.
  
* Delete by email
  * `list` followed by `delete e/betsy@example.com` deletes the person with email `betsy@example.com` in the address book.
  * `find n/Betsy` followed by `delete e/BETSY@example.com` deletes the person with email `BETSY@example.com` in the results of the `find` command (case-insensitive match also works).

### Clearing all entries : `clear`

Clears all entries from the address book.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

CampusBridge data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

CampusBridge data are saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, CampusBridge will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause CampusBridge to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous CampusBridge home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add** | `add n/NAME e/EMAIL [p/PHONE_NUMBER] [h/TELEGRAM_HANDLE] [t/TAG]…​` <br> e.g., `add n/James Ho e/jamesho@example.com p/22224444 h/james_ho t/friend t/colleague`
**Clear** | `clear`
**Delete** | `delete i/INDEX OR delete e/EMAIL`<br> e.g., `delete i/3 OR delete e/jameslee@example.com `
**Edit** | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [h/TELEGRAM_HANDLE] [t/TAG]…​`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com h/jlee01`
**Find** | `find n/NAME [MORE_NAMES] e/EMAIL [MORE_EMAILS]`<br> e.g., `find n/alex e/doe`
**Filter** | `filter t/TAG [MORE_TAGS]…`<br> e.g., `filter t/friend t/colleague`
**List** | `list`
**Sort** | `sort o/ORDER [r/]`<br> e.g., `sort o/name`, `sort o/name r/`
**Help** | `help [COMMAND]`<br> e.g., `help`, `help add`, `help sort`
