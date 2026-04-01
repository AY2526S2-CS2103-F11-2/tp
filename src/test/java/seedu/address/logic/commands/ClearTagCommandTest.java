package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.assertUndoFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertUndoSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
 * {@code ClearTagCommand}.
 */
public class ClearTagCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ClearTagCommand(null, TagType.GENERAL));
    }

    @Test
    public void constructor_nullTagType_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ClearTagCommand(INDEX_FIRST_PERSON, null));
    }

    @Test
    public void execute_validIndexClearRoleTag_success() {
        // INDEX_FIRST_PERSON is guarantee to have role tag
        Person personWithRoleTag = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        // only role tags
        Set<Tag> roleTag = personWithRoleTag.getTags().stream()
                .filter(tag -> tag.getType() == TagType.ROLE)
                .collect(Collectors.toSet());

        ClearTagCommand command = new ClearTagCommand(INDEX_FIRST_PERSON, TagType.ROLE);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Set<Tag> expectedTags = new HashSet<>(personWithRoleTag.getTags());
        expectedTags.removeAll(roleTag);

        Person editedPerson = personWithRoleTag.withTags(expectedTags);

        expectedModel.setPerson(personWithRoleTag, editedPerson);
        String expectedMessage = String.format(ClearTagCommand.MESSAGE_SUCCESS, TagType.ROLE, roleTag);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexClearCourseTag_success() {
        // INDEX_SECOND_PERSON is guarantee to have course tag
        Person personWithCourseTag = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());

        // only course tag
        Set<Tag> courseTag = personWithCourseTag.getTags().stream()
                .filter(tag -> tag.getType() == TagType.COURSE)
                .collect(Collectors.toSet());

        ClearTagCommand command = new ClearTagCommand(INDEX_SECOND_PERSON, TagType.COURSE);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Set<Tag> expectedTags = new HashSet<>(personWithCourseTag.getTags());
        expectedTags.removeAll(courseTag);

        Person editedPerson = personWithCourseTag.withTags(expectedTags);

        expectedModel.setPerson(personWithCourseTag, editedPerson);
        String expectedMessage = String.format(ClearTagCommand.MESSAGE_SUCCESS, TagType.COURSE, courseTag);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexClearMultipleGeneralTags_success() {
        // INDEX_SECOND_PERSON is guarantee to have multiple general tags
        Person personWithGeneralTags = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());

        // only general tags
        Set<Tag> generalTags = personWithGeneralTags.getTags().stream()
                .filter(tag -> tag.getType() == TagType.GENERAL)
                .collect(Collectors.toSet());

        ClearTagCommand command = new ClearTagCommand(INDEX_SECOND_PERSON, TagType.GENERAL);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Set<Tag> expectedTags = new HashSet<>(personWithGeneralTags.getTags());
        expectedTags.removeAll(generalTags);

        Person editedPerson = personWithGeneralTags.withTags(expectedTags);

        expectedModel.setPerson(personWithGeneralTags, editedPerson);
        String expectedMessage = String.format(ClearTagCommand.MESSAGE_SUCCESS, TagType.GENERAL, generalTags);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_clearTagTypeWhenNoTagsOfTypeExist_throwsCommandException() {
        // INDEX_SECOND_PERSON is guarantee to no role tags
        ClearTagCommand clearTagCommand = new ClearTagCommand(INDEX_SECOND_PERSON, TagType.ROLE);

        assertCommandFailure(clearTagCommand, model,
                String.format(ClearTagCommand.MESSAGE_NO_TAGS_FOUND, TagType.ROLE));
    }

    @Test
    public void execute_clearTagTypeWhenPersonHasNoTags_throwsCommandException() {
        // Get a person with no tags (INDEX_THIRD_PERSON from typical persons)
        ClearTagCommand clearTagCommand = new ClearTagCommand(INDEX_THIRD_PERSON, TagType.GENERAL);

        assertCommandFailure(clearTagCommand, model,
                String.format(ClearTagCommand.MESSAGE_NO_TAGS_FOUND, TagType.GENERAL));
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        ClearTagCommand clearTagCommand = new ClearTagCommand(outOfBoundIndex, TagType.GENERAL);

        String expected = String.format(Messages.MESSAGE_PERSON_NOT_FOUND_DISPLAYED_INDEX,
                outOfBoundIndex.getOneBased());
        assertCommandFailure(clearTagCommand, model, expected);
    }

    @Test
    public void equals() {
        ClearTagCommand command = new ClearTagCommand(INDEX_FIRST_PERSON, TagType.GENERAL);

        // same object -> true
        assertTrue(command.equals(command));

        // same values -> true
        ClearTagCommand commandCopy = new ClearTagCommand(INDEX_FIRST_PERSON, TagType.GENERAL);
        assertTrue(command.equals(commandCopy));

        // different index -> false
        ClearTagCommand differentIndex = new ClearTagCommand(INDEX_SECOND_PERSON, TagType.GENERAL);
        assertFalse(command.equals(differentIndex));

        // different tag type -> false
        ClearTagCommand differentType = new ClearTagCommand(INDEX_FIRST_PERSON, TagType.ROLE);
        assertFalse(command.equals(differentType));

        // different type -> false
        assertFalse(command.equals(1));

        // null -> false
        assertFalse(command.equals(null));
    }

    @Test
    public void toStringMethod() {
        ClearTagCommand clearTagCommand = new ClearTagCommand(INDEX_FIRST_PERSON, TagType.GENERAL);

        String expected = ClearTagCommand.class.getCanonicalName()
                + "{index=" + INDEX_FIRST_PERSON + ", typeToClear=" + TagType.GENERAL + "}";

        assertEquals(expected, clearTagCommand.toString());
    }

    @Test
    public void undo_afterExecute_restoresOriginalPerson() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        TagType typeToClear = TagType.ROLE;
        ClearTagCommand clearTagCommand = new ClearTagCommand(INDEX_FIRST_PERSON, typeToClear);

        Set<Tag> removedTags = personToEdit.getTags().stream()
                .filter(tag -> tag.getType() == typeToClear)
                .collect(Collectors.toSet());
        Set<Tag> expectedTags = personToEdit.getTags().stream()
                .filter(tag -> tag.getType() != typeToClear)
                .collect(Collectors.toSet());

        Model expectedAfterExecute = new ModelManager(model.getAddressBook(), new UserPrefs());
        Person editedPerson = personToEdit.withTags(expectedTags);
        expectedAfterExecute.setPerson(personToEdit, editedPerson);
        assertCommandSuccess(clearTagCommand, model,
                String.format(ClearTagCommand.MESSAGE_SUCCESS, typeToClear, removedTags),
                expectedAfterExecute);

        Model expectedAfterUndo = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        assertUndoSuccess(clearTagCommand, model,
                String.format(ClearTagCommand.MESSAGE_UNDO_SUCCESS, typeToClear, Messages.format(personToEdit)),
                expectedAfterUndo);
    }

    @Test
    public void undo_beforeExecute_throwsCommandException() {
        ClearTagCommand clearTagCommand = new ClearTagCommand(INDEX_FIRST_PERSON, TagType.ROLE);
        assertUndoFailure(clearTagCommand, model, ClearTagCommand.MESSAGE_UNDO_FAILURE);
    }

    @Test
    public void undo_afterExecuteOriginalPersonNull_throwsCommandException() throws Exception {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        TagType typeToClear = TagType.ROLE;
        ClearTagCommand clearTagCommand = new ClearTagCommand(INDEX_FIRST_PERSON, typeToClear);

        Set<Tag> removedTags = personToEdit.getTags().stream()
                .filter(tag -> tag.getType() == typeToClear)
                .collect(Collectors.toSet());
        Set<Tag> expectedTags = personToEdit.getTags().stream()
                .filter(tag -> tag.getType() != typeToClear)
                .collect(Collectors.toSet());

        Model expectedAfterExecute = new ModelManager(model.getAddressBook(), new UserPrefs());
        Person editedPerson = personToEdit.withTags(expectedTags);
        expectedAfterExecute.setPerson(personToEdit, editedPerson);
        assertCommandSuccess(clearTagCommand, model,
                String.format(ClearTagCommand.MESSAGE_SUCCESS, typeToClear, removedTags),
                expectedAfterExecute);

        Field originalPersonField = ClearTagCommand.class.getDeclaredField("originalPerson");
        originalPersonField.setAccessible(true);
        originalPersonField.set(clearTagCommand, null);

        assertUndoFailure(clearTagCommand, model, ClearTagCommand.MESSAGE_UNDO_FAILURE);
    }

    @Test
    public void undo_afterExecuteUpdatedPersonNull_throwsCommandException() throws Exception {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        TagType typeToClear = TagType.ROLE;
        ClearTagCommand clearTagCommand = new ClearTagCommand(INDEX_FIRST_PERSON, typeToClear);

        Set<Tag> removedTags = personToEdit.getTags().stream()
                .filter(tag -> tag.getType() == typeToClear)
                .collect(Collectors.toSet());
        Set<Tag> expectedTags = personToEdit.getTags().stream()
                .filter(tag -> tag.getType() != typeToClear)
                .collect(Collectors.toSet());

        Model expectedAfterExecute = new ModelManager(model.getAddressBook(), new UserPrefs());
        Person editedPerson = personToEdit.withTags(expectedTags);
        expectedAfterExecute.setPerson(personToEdit, editedPerson);
        assertCommandSuccess(clearTagCommand, model,
                String.format(ClearTagCommand.MESSAGE_SUCCESS, typeToClear, removedTags),
                expectedAfterExecute);

        Field updatedPersonField = ClearTagCommand.class.getDeclaredField("updatedPerson");
        updatedPersonField.setAccessible(true);
        updatedPersonField.set(clearTagCommand, null);

        assertUndoFailure(clearTagCommand, model, ClearTagCommand.MESSAGE_UNDO_FAILURE);
    }

    @Test
    public void isUndoable_returnsTrue() {
        ClearTagCommand clearTagCommand = new ClearTagCommand(INDEX_FIRST_PERSON, TagType.ROLE);
        assertTrue(clearTagCommand.isUndoable());
    }
}
