package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.PersonContainsTagsPredicate;
import seedu.address.model.tag.Tag;

/**
 * Contains integration tests (interaction with the Model) for {@code FilterCommand}.
 */
public class FilterCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        PersonContainsTagsPredicate firstPredicate =
                new PersonContainsTagsPredicate(Set.of(new Tag("friends")));
        PersonContainsTagsPredicate secondPredicate =
                new PersonContainsTagsPredicate(Set.of(new Tag("colleagues")));

        FilterCommand filterFirstCommand = new FilterCommand(firstPredicate);
        FilterCommand filterSecondCommand = new FilterCommand(secondPredicate);

        assertTrue(filterFirstCommand.equals(filterFirstCommand));

        FilterCommand filterFirstCommandCopy = new FilterCommand(firstPredicate);
        assertTrue(filterFirstCommand.equals(filterFirstCommandCopy));

        assertFalse(filterFirstCommand.equals(1));
        assertFalse(filterFirstCommand.equals(null));
        assertFalse(filterFirstCommand.equals(filterSecondCommand));
    }

    @Test
    public void execute_singleTag_multiplePersonsFound() {
        PersonContainsTagsPredicate predicate =
                new PersonContainsTagsPredicate(Set.of(new Tag("friends")));
        FilterCommand command = new FilterCommand(predicate);

        expectedModel.updateFilteredPersonList(predicate);

        String expectedMessage = String.format(
                MESSAGE_PERSONS_LISTED_OVERVIEW,
                expectedModel.getFilteredPersonList().size());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);

        assertEquals(
                Set.copyOf(expectedModel.getFilteredPersonList()),
                Set.copyOf(model.getFilteredPersonList())
        );
    }

    @Test
    public void execute_multipleTags_multiplePersonsFound() {
        PersonContainsTagsPredicate predicate =
                new PersonContainsTagsPredicate(Set.of(
                        new Tag("friends"), new Tag("colleagues")));
        FilterCommand command = new FilterCommand(predicate);

        expectedModel.updateFilteredPersonList(predicate);

        String expectedMessage = String.format(
                MESSAGE_PERSONS_LISTED_OVERVIEW,
                expectedModel.getFilteredPersonList().size());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);

        assertEquals(
                Set.copyOf(expectedModel.getFilteredPersonList()),
                Set.copyOf(model.getFilteredPersonList())
        );
    }

    @Test
    public void execute_noMatchingTag_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);

        PersonContainsTagsPredicate predicate =
                new PersonContainsTagsPredicate(Set.of(new Tag("unknown")));
        FilterCommand command = new FilterCommand(predicate);

        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }

    @Test
    public void toStringMethod() {
        PersonContainsTagsPredicate predicate =
                new PersonContainsTagsPredicate(Set.of(new Tag("friends")));
        FilterCommand command = new FilterCommand(predicate);

        String expected = FilterCommand.class.getCanonicalName()
                + "{predicate=" + predicate + "}";
        assertEquals(expected, command.toString());
    }
}
