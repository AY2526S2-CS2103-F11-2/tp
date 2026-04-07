package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_newPerson_success() {
        Person validPerson = new PersonBuilder().build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(validPerson);

        assertCommandSuccess(new AddCommand(validPerson), model,
                String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validPerson))
                        + "\n" + Messages.MESSAGE_NON_NUS_EMAIL,
                expectedModel);
    }

    @Test
    public void execute_duplicateEmail_throwsCommandException() {
        Person personInList = model.getAddressBook().getPersonList().get(0);
        assertCommandFailure(new AddCommand(personInList), model,
                AddCommand.MESSAGE_DUPLICATE_EMAIL);
    }

    @Test
    public void execute_duplicateTelegramHandle_throwsCommandException() {
        Person existingPerson = new PersonBuilder()
                .withName("Telegram Existing")
                .withEmail("telegramexisting@example.com")
                .withTelegramHandle("alice123")
                .build();
        model.addPerson(existingPerson);

        Person personWithSameTelegramHandle = new PersonBuilder()
                .withName("Telegram Duplicate")
                .withEmail("different@example.com")
                .withTelegramHandle("alice123")
                .build();

        assertCommandFailure(new AddCommand(personWithSameTelegramHandle), model,
                AddCommand.MESSAGE_DUPLICATE_TELEGRAM_HANDLE);
    }

    @Test
    public void execute_duplicateTelegramHandleDifferentCase_throwsCommandException() {
        Person existingPerson = new PersonBuilder()
                .withName("Telegram Existing")
                .withEmail("telegramexisting@example.com")
                .withTelegramHandle("test1")
                .build();
        model.addPerson(existingPerson);

        Person personWithSameTelegramHandle = new PersonBuilder()
                .withName("Telegram Duplicate")
                .withEmail("different@example.com")
                .withTelegramHandle("TEST1")
                .build();

        assertCommandFailure(new AddCommand(personWithSameTelegramHandle), model,
                AddCommand.MESSAGE_DUPLICATE_TELEGRAM_HANDLE);
    }


    @Test
    public void execute_duplicateEmailAndTelegramHandleFromSamePerson_throwsCommandException() {
        Person existingPerson = new PersonBuilder()
                .withName("Telegram Existing")
                .withEmail("duplicate@example.com")
                .withTelegramHandle("alice123")
                .build();
        model.addPerson(existingPerson);

        Person duplicatePerson = new PersonBuilder()
                .withName("Telegram Duplicate")
                .withEmail("duplicate@example.com")
                .withTelegramHandle("alice123")
                .build();

        assertCommandFailure(new AddCommand(duplicatePerson), model,
                AddCommand.MESSAGE_DUPLICATE_EMAIL_AND_TELEGRAM_HANDLE);
    }

    @Test
    public void execute_duplicateEmailAndTelegramHandleFromDifferentPersons_throwsCommandException() {
        Person personWithDuplicateEmail = new PersonBuilder()
                .withName("Email Existing")
                .withEmail("duplicate@example.com")
                .withTelegramHandle("emailperson")
                .build();
        model.addPerson(personWithDuplicateEmail);

        Person personWithDuplicateTelegramHandle = new PersonBuilder()
                .withName("Telegram Existing")
                .withEmail("telegram@example.com")
                .withTelegramHandle("sharedhandle")
                .build();
        model.addPerson(personWithDuplicateTelegramHandle);

        Person duplicatePerson = new PersonBuilder()
                .withName("Conflicting Person")
                .withEmail("duplicate@example.com")
                .withTelegramHandle("sharedhandle")
                .build();

        assertCommandFailure(new AddCommand(duplicatePerson), model,
                AddCommand.MESSAGE_DUPLICATE_EMAIL_AND_TELEGRAM_HANDLE);
    }
}
