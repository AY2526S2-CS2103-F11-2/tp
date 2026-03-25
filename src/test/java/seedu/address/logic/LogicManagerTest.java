package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.AMY_NO_TAGS;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.Deque;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.TypicalPersons;

public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy IO exception");
    private static final IOException DUMMY_AD_EXCEPTION = new AccessDeniedException("dummy access denied exception");

    @TempDir
    public Path temporaryFolder;

    private Model model = new ModelManager();
    private Logic logic;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("addressBook.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = "delete i/9";
        assertCommandException(deleteCommand, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        String listCommand = ListCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, ListCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_IO_EXCEPTION, String.format(
                LogicManager.FILE_OPS_ERROR_FORMAT, DUMMY_IO_EXCEPTION.getMessage()));
    }

    @Test
    public void execute_storageThrowsAdException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_AD_EXCEPTION, String.format(
                LogicManager.FILE_OPS_PERMISSION_ERROR_FORMAT, DUMMY_AD_EXCEPTION.getMessage()));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredPersonList().remove(0));
    }

    @Test
    public void execute_undoWithoutHistory_throwsCommandException() {
        assertCommandException(UndoCommand.COMMAND_WORD, UndoCommand.MESSAGE_NO_HISTORY);
    }

    @Test
    public void execute_undoAfterAdd_removesPerson() throws Exception {
        String addCommand = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY;
        logic.execute(addCommand);

        Person amy = new PersonBuilder(AMY_NO_TAGS).build();
        ModelManager expectedAfterAdd = new ModelManager();
        expectedAfterAdd.addPerson(amy);
        assertEquals(expectedAfterAdd, model);

        logic.execute(UndoCommand.COMMAND_WORD);
        assertEquals(new ModelManager(), model);
    }

    @Test
    public void execute_undoAfterClear_restoresAddressBook() throws Exception {
        model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("typicalForUndo.json"));
        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("typicalForUndoPrefs.json"));
        logic = new LogicManager(model, new StorageManager(addressBookStorage, userPrefsStorage));

        logic.execute(ClearCommand.COMMAND_WORD);
        ModelManager expectedAfterClear = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        expectedAfterClear.setAddressBook(new AddressBook());
        assertEquals(expectedAfterClear, model);

        logic.execute(UndoCommand.COMMAND_WORD);
        assertEquals(new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs()), model);
    }

    @Test
    public void execute_list_doesNotConsumeUndoHistorySlot() throws Exception {
        String addCommand = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY;
        logic.execute(addCommand);
        logic.execute(ListCommand.COMMAND_WORD);
        logic.execute(UndoCommand.COMMAND_WORD);
        assertEquals(new ModelManager(), model);
    }

    @Test
    public void execute_twoAddsTwoUndos_restoresEmptyAddressBookInLifoOrder() throws Exception {
        String addAmy = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY;
        String addBob = AddCommand.COMMAND_WORD + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB;
        logic.execute(addAmy);
        logic.execute(addBob);

        logic.execute(UndoCommand.COMMAND_WORD);
        ModelManager expectedOnlyAmy = new ModelManager();
        expectedOnlyAmy.addPerson(new PersonBuilder(AMY_NO_TAGS).build());
        assertEquals(expectedOnlyAmy, model);

        logic.execute(UndoCommand.COMMAND_WORD);
        assertEquals(new ModelManager(), model);

        assertCommandException(UndoCommand.COMMAND_WORD, UndoCommand.MESSAGE_NO_HISTORY);
    }

    @Test
    public void execute_deleteThenClearTwoUndos_restoresTypicalAddressBookInLifoOrder() throws Exception {
        model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("multiUndoTypical.json"));
        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("multiUndoTypicalPrefs.json"));
        logic = new LogicManager(model, new StorageManager(addressBookStorage, userPrefsStorage));

        logic.execute("delete i/7");
        logic.execute(ClearCommand.COMMAND_WORD);
        assertEquals(new ModelManager(), model);

        logic.execute(UndoCommand.COMMAND_WORD);

        ModelManager expectedAfterUndoClear = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        Person george = expectedAfterUndoClear.getAddressBook().getPersonList().get(6);
        expectedAfterUndoClear.deletePerson(george);
        assertEquals(expectedAfterUndoClear, model);

        logic.execute(UndoCommand.COMMAND_WORD);
        assertEquals(new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs()), model);
    }

    @Test
    public void execute_undoFailure_historyUnchanged() throws Exception {
        model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("undoFailAb.json"));
        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("undoFailPrefs.json"));
        logic = new LogicManager(model, new StorageManager(addressBookStorage, userPrefsStorage));

        logic.execute("delete i/1");
        Person benson = model.getFilteredPersonList().get(0);
        model.setPerson(benson, new PersonBuilder(benson).withEmail(TypicalPersons.ALICE.getEmail().value).build());

        assertThrows(CommandException.class, DeleteCommand.MESSAGE_UNDO_FAILURE, ()
                -> logic.execute(UndoCommand.COMMAND_WORD));
        assertEquals(1, getUndoHistorySize((LogicManager) logic));
    }

    private static int getUndoHistorySize(LogicManager logicManager) throws Exception {
        Field field = LogicManager.class.getDeclaredField("undoHistory");
        field.setAccessible(true);
        return ((Deque<?>) field.get(logicManager)).size();
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
                                      Model expectedModel) throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
                                      String expectedMessage) {
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedModel);
    }

    /**
     * Executes the command and confirms that
     * - the {@code expectedException} is thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
                                      String expectedMessage, Model expectedModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedModel, model);
    }

    /**
     * Tests the Logic component's handling of an {@code IOException} thrown by the Storage component.
     *
     * @param e the exception to be thrown by the Storage component
     * @param expectedMessage the message expected inside exception thrown by the Logic component
     */
    private void assertCommandFailureForExceptionFromStorage(IOException e, String expectedMessage) {
        Path prefPath = temporaryFolder.resolve("ExceptionUserPrefs.json");

        // Inject LogicManager with an AddressBookStorage that throws the IOException e when saving
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(prefPath) {
            @Override
            public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath)
                    throws IOException {
                throw e;
            }
        };

        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("ExceptionUserPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);

        logic = new LogicManager(model, storage);

        // Triggers the saveAddressBook method by executing an add command
        String addCommand = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY;
        Person expectedPerson = new PersonBuilder(AMY_NO_TAGS)
                .build();
        ModelManager expectedModel = new ModelManager();
        expectedModel.addPerson(expectedPerson);
        assertCommandFailure(addCommand, CommandException.class, expectedMessage, expectedModel);
    }
}
