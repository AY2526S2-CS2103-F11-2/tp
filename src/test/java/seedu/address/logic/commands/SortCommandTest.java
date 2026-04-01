package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_SORT_RESET;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.GEORGE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

public class SortCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        SortCommand sortByNameAsc = new SortCommand("name", false);
        SortCommand sortByNameDesc = new SortCommand("name", true);

        // same object -> returns true
        assertEquals(sortByNameAsc, sortByNameAsc);

        // same order and reverse -> returns true
        SortCommand sortByNameAscCopy = new SortCommand("name", false);
        assertEquals(sortByNameAsc, sortByNameAscCopy);

        // different reverse -> returns false
        assertNotEquals(sortByNameAsc, sortByNameDesc);

        // different order -> returns false
        SortCommand sortByEmailAsc = new SortCommand("email", false);
        assertNotEquals(sortByNameAsc, sortByEmailAsc);

        // different type -> returns false
        assertNotEquals(sortByNameAsc, 1);

        // null -> returns false
        assertFalse(sortByNameAsc.equals(null));
    }

    @Test
    public void execute_sortByNameAscending_success() throws CommandException {
        SortCommand command = new SortCommand("name", false);
        CommandResult result = command.execute(model);

        assertEquals(new CommandResult(SortCommand.buildSuccessMessage("name", false)), result);
        assertEquals(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE),
                model.getFilteredPersonList());
    }

    @Test
    public void execute_sortByNameDescending_success() throws CommandException {
        SortCommand command = new SortCommand("name", true);
        CommandResult result = command.execute(model);

        assertEquals(new CommandResult(SortCommand.buildSuccessMessage("name", true)), result);
        assertEquals(Arrays.asList(GEORGE, FIONA, ELLE, DANIEL, CARL, BENSON, ALICE),
                model.getFilteredPersonList());
    }

    @Test
    public void execute_sortByEmailAscending_success() throws CommandException {
        SortCommand command = new SortCommand("email", false);
        CommandResult result = command.execute(model);

        assertEquals(new CommandResult(SortCommand.buildSuccessMessage("email", false)), result);
        // alice@, anna@, cornelia@, heinz@, johnd@, lydia@, werner@
        assertEquals(Arrays.asList(ALICE, GEORGE, DANIEL, CARL, BENSON, FIONA, ELLE),
                model.getFilteredPersonList());
    }

    @Test
    public void execute_sortByEmailDescending_success() throws CommandException {
        SortCommand command = new SortCommand("email", true);
        CommandResult result = command.execute(model);

        assertEquals(new CommandResult(SortCommand.buildSuccessMessage("email", true)), result);
        assertEquals(Arrays.asList(ELLE, FIONA, BENSON, CARL, DANIEL, GEORGE, ALICE),
                model.getFilteredPersonList());
    }

    @Test
    public void execute_sortByPhoneAscending_success() throws CommandException {
        SortCommand command = new SortCommand("phone", false);
        CommandResult result = command.execute(model);

        assertEquals(new CommandResult(SortCommand.buildSuccessMessage("phone", false)), result);
        // 87652533, 94351253, 9482224, 9482427, 9482442, 95352563, 98765432 (lexicographic)
        assertEquals(Arrays.asList(DANIEL, ALICE, ELLE, FIONA, GEORGE, CARL, BENSON),
                model.getFilteredPersonList());
    }

    @Test
    public void execute_sortByPhoneDescending_success() throws CommandException {
        SortCommand command = new SortCommand("phone", true);
        CommandResult result = command.execute(model);

        assertEquals(new CommandResult(SortCommand.buildSuccessMessage("phone", true)), result);
        assertEquals(Arrays.asList(BENSON, CARL, GEORGE, FIONA, ELLE, ALICE, DANIEL),
                model.getFilteredPersonList());
    }

    @Test
    public void execute_sortNone_resetsOrder() throws CommandException {
        // Sort descending so order differs from insertion order
        new SortCommand("name", true).execute(model);
        assertEquals(Arrays.asList(GEORGE, FIONA, ELLE, DANIEL, CARL, BENSON, ALICE),
                model.getFilteredPersonList());
        // Reset
        SortCommand resetCommand = new SortCommand("none", false);
        CommandResult result = resetCommand.execute(model);

        assertEquals(new CommandResult(MESSAGE_SORT_RESET), result);
        // Insertion order restored
        assertEquals(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE),
                model.getFilteredPersonList());
    }

    @Test
    public void execute_sortByPhone_nullPhoneLast() throws CommandException {
        Model freshModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Person noPhone = new Person(new Name("Zara Zara"), null, new Email("zara@example.com"), new HashSet<>());
        freshModel.addPerson(noPhone);

        new SortCommand("phone", false).execute(freshModel);

        // null phone sorts last (Comparator.nullsLast)
        List<Person> sorted = (List<Person>) freshModel.getFilteredPersonList();
        assertEquals(noPhone, sorted.get(sorted.size() - 1));
    }

    @Test
    public void execute_sortByPhoneReversed_nullPhoneLast() throws CommandException {
        Model freshModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Person noPhone = new Person(new Name("Zara Zara"), null, new Email("zara@example.com"), new HashSet<>());
        freshModel.addPerson(noPhone);

        new SortCommand("phone", true).execute(freshModel);

        List<Person> sorted = (List<Person>) freshModel.getFilteredPersonList();
        assertEquals(noPhone, sorted.get(sorted.size() - 1));
    }

    @Test
    public void toStringMethod() {
        SortCommand command = new SortCommand("name", false);
        String str = command.toString();
        assertTrue(str.contains("order=name"));
        assertTrue(str.contains("reverse=false"));
    }
}
