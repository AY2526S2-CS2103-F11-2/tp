package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagType;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code TagCommand}.
 */
public class TagCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndex_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Set<Tag> newTags = new HashSet<>();
        newTags.add(new Tag("tutor", TagType.ROLE));
        newTags.add(new Tag("CS2103", TagType.COURSE));
        newTags.add(new Tag("friends", TagType.GENERAL));

        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, newTags);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Set<Tag> expectedTags = new HashSet<>(personToEdit.getTags());
        expectedTags.addAll(newTags);

        Person editedPerson = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getTelegramHandle(),
                expectedTags
        );

        expectedModel.setPerson(personToEdit, editedPerson);
        String expectedMessage = String.format(TagCommand.MESSAGE_SUCCESS, newTags);
        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("friend", TagType.GENERAL));

        TagCommand tagCommand = new TagCommand(outOfBoundIndex, tags);
        assertCommandFailure(tagCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_duplicateTags_noDuplicatesAdded() {
        Person person = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Set<Tag> tags = new HashSet<>(person.getTags()); // same tags

        TagCommand command = new TagCommand(INDEX_FIRST_PERSON, tags);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        // should remain unchanged
        assertCommandSuccess(command, model,
                String.format(TagCommand.MESSAGE_SUCCESS, tags),
                expectedModel);
    }

    @Test
    public void equals() {
        Set<Tag> tags1 = new HashSet<>();
        tags1.add(new Tag("friend", TagType.GENERAL));

        Set<Tag> tags2 = new HashSet<>();
        tags2.add(new Tag("tutor", TagType.ROLE));

        TagCommand command = new TagCommand(INDEX_FIRST_PERSON, tags1);

        // same object -> true
        assertTrue(command.equals(command));

        // same values -> true
        TagCommand commandCopy = new TagCommand(INDEX_FIRST_PERSON, tags1);
        assertTrue(command.equals(commandCopy));

        // different index -> false
        TagCommand differentIndex = new TagCommand(INDEX_SECOND_PERSON, tags1);
        assertFalse(command.equals(differentIndex));

        // different tags -> false
        TagCommand differentTags = new TagCommand(INDEX_FIRST_PERSON, tags2);
        assertFalse(command.equals(differentTags));

        // different type -> false
        assertFalse(command.equals(1));

        // null -> false
        assertFalse(command.equals(null));
    }

    @Test
    public void toStringMethod_containsFields() {
        Set<Tag> tags = Set.of(
                new Tag("friend", TagType.GENERAL),
                new Tag("tutor", TagType.ROLE)
        );

        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, tags);

        String expected = TagCommand.class.getCanonicalName()
                + "{tags=" + tags + "}";

        assertEquals(expected, tagCommand.toString());
    }
}
