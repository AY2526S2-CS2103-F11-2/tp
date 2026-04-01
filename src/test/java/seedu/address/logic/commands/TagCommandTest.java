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

    // ---------------- CONSTRUCTOR TESTS ----------------
    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("friend", TagType.GENERAL));

        assertThrows(NullPointerException.class, () -> new TagCommand(null, tagsToAdd));
    }

    @Test
    public void constructor_nullTagsToAdd_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TagCommand(INDEX_FIRST_PERSON, null));
    }

    // ---------------- SUCCESS CASES - SINGLE TAG ----------------
    @Test
    public void execute_validIndexAddSingleTag_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Tag newTag = new Tag("mentor", TagType.ROLE);
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("mentor", TagType.ROLE));

        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Set<Tag> expectedTags = new HashSet<>(personToEdit.getTags());
        expectedTags.add(newTag);

        Person editedPerson = personToEdit.withTags(expectedTags);

        expectedModel.setPerson(personToEdit, editedPerson);
        String expectedMessage = String.format(TagCommand.MESSAGE_SUCCESS, tagsToAdd);
        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexAddSingleTagCaseInsensitive_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Tag newTag = new Tag("mentor", TagType.ROLE);
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("MeNtoR", TagType.ROLE));

        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Set<Tag> expectedTags = new HashSet<>(personToEdit.getTags());
        expectedTags.add(newTag);

        Person editedPerson = personToEdit.withTags(expectedTags);

        expectedModel.setPerson(personToEdit, editedPerson);
        String expectedMessage = String.format(TagCommand.MESSAGE_SUCCESS, tagsToAdd);
        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexAddTagToPersonWithNoTags_success() {
        // INDEX_THIRD_PERSON: a person with no tags
        Person personWithoutTags = model.getFilteredPersonList().get(INDEX_THIRD_PERSON.getZeroBased());

        Tag newTag = new Tag("friend", TagType.GENERAL);
        Set<Tag> tagsToAdd = Set.of(newTag);

        TagCommand tagCommand = new TagCommand(INDEX_THIRD_PERSON, tagsToAdd);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Set<Tag> expectedTags = new HashSet<>();
        expectedTags.add(newTag);

        Person editedPerson = personWithoutTags.withTags(expectedTags);

        expectedModel.setPerson(personWithoutTags, editedPerson);
        String expectedMessage = String.format(TagCommand.MESSAGE_SUCCESS, tagsToAdd);
        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    // ---------------- SUCCESS CASES - MULTIPLE TAGS ----------------
    @Test
    public void execute_validIndexAddMultipleTags_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("tutor", TagType.ROLE));
        tagsToAdd.add(new Tag("CS2103", TagType.COURSE));
        tagsToAdd.add(new Tag("groupmates", TagType.GENERAL));

        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Set<Tag> expectedTags = new HashSet<>(personToEdit.getTags());
        expectedTags.addAll(tagsToAdd);

        Person editedPerson = personToEdit.withTags(expectedTags);

        expectedModel.setPerson(personToEdit, editedPerson);
        String expectedMessage = String.format(TagCommand.MESSAGE_SUCCESS, tagsToAdd);
        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_sameTagNameDifferentTypes_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Tag roleTag = new Tag("mentor", TagType.ROLE);
        Tag generalTag = new Tag("mentor", TagType.GENERAL);

        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(roleTag);
        tagsToAdd.add(generalTag);

        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Set<Tag> expectedTags = new HashSet<>(personToEdit.getTags());
        expectedTags.add(roleTag);
        expectedTags.add(generalTag); // both should be added as they're different types

        Person editedPerson = personToEdit.withTags(expectedTags);
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(tagCommand, model,
                String.format(TagCommand.MESSAGE_SUCCESS, tagsToAdd),
                expectedModel);
    }

    // ---------------- PARTIAL SUCCESS CASES ----------------
    @Test
    public void execute_mixedNewAndDuplicateTags_partialSuccess() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Tag existingTag = personToEdit.getTags().iterator().next();
        Tag newTag = new Tag("mentor", TagType.ROLE);

        Set<Tag> tagsToAdd = Set.of(existingTag, newTag);

        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Set<Tag> expectedTags = new HashSet<>(personToEdit.getTags());
        expectedTags.add(newTag); // only new tag added

        Person editedPerson = personToEdit.withTags(expectedTags);
        expectedModel.setPerson(personToEdit, editedPerson);

        Set<Tag> newTags = Set.of(newTag);
        Set<Tag> existingTags = Set.of(existingTag);
        String expectedMessage = String.format(TagCommand.MESSAGE_PARTIAL_SUCCESS, newTags, existingTags);

        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_mixedNewAndMultipleDuplicateTags_partialSuccess() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Set<Tag> existingTags = new HashSet<>(personToEdit.getTags());
        Tag newTag = new Tag("mentor", TagType.ROLE);

        Set<Tag> tagsToAdd = new HashSet<>(existingTags);
        tagsToAdd.add(newTag);

        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Set<Tag> expectedTags = new HashSet<>(personToEdit.getTags());
        expectedTags.add(newTag);

        Person editedPerson = personToEdit.withTags(expectedTags);
        expectedModel.setPerson(personToEdit, editedPerson);

        Set<Tag> newTags = Set.of(newTag);
        String expectedMessage = String.format(TagCommand.MESSAGE_PARTIAL_SUCCESS, newTags, existingTags);

        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    // ---------------- FAILURE CASES - NO NEW TAGS ----------------
    @Test
    public void execute_duplicateTagsOnly_throwsCommandException() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Tag existingTag = personToEdit.getTags().iterator().next();
        Set<Tag> tagsToAdd = Set.of(existingTag);

        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        assertCommandFailure(tagCommand, model, TagCommand.MESSAGE_NO_NEW_TAGS);
    }

    @Test
    public void execute_duplicateTagsMultipleOnly_throwsCommandException() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Set<Tag> tagsToAdd = new HashSet<>(personToEdit.getTags());

        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        assertCommandFailure(tagCommand, model, TagCommand.MESSAGE_NO_NEW_TAGS);
    }

    // ---------------- FAILURE CASES - INVALID INDEX ----------------
    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("friend", TagType.GENERAL));

        TagCommand tagCommand = new TagCommand(outOfBoundIndex, tags);
        String expected = String.format(Messages.MESSAGE_PERSON_NOT_FOUND_DISPLAYED_INDEX,
                outOfBoundIndex.getOneBased());
        assertCommandFailure(tagCommand, model, expected);
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
    public void toStringMethod() {
        Set<Tag> tagsToAdd = Set.of(
                new Tag("cs2103", TagType.COURSE),
                new Tag("tutor", TagType.ROLE)
        );

        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        String expected = TagCommand.class.getCanonicalName()
                + "{index=" + INDEX_FIRST_PERSON + ", tagsToAdd=" + tagsToAdd + "}";

        assertEquals(expected, tagCommand.toString());
    }

    @Test
    public void undo_afterExecute_restoresOriginalPerson() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> tagsToAdd = Set.of(new Tag("mentor", TagType.ROLE));
        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        Model expectedAfterExecute = new ModelManager(model.getAddressBook(), new UserPrefs());
        Set<Tag> expectedTags = new HashSet<>(personToEdit.getTags());
        expectedTags.addAll(tagsToAdd);
        Person editedPerson = personToEdit.withTags(expectedTags);
        expectedAfterExecute.setPerson(personToEdit, editedPerson);
        assertCommandSuccess(tagCommand, model, String.format(TagCommand.MESSAGE_SUCCESS, tagsToAdd),
                expectedAfterExecute);

        Model expectedAfterUndo = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        assertUndoSuccess(tagCommand, model,
                String.format(TagCommand.MESSAGE_UNDO_SUCCESS, Messages.format(personToEdit)),
                expectedAfterUndo);
    }

    @Test
    public void undo_beforeExecute_throwsCommandException() {
        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, Set.of(new Tag("mentor", TagType.ROLE)));
        assertUndoFailure(tagCommand, model, TagCommand.MESSAGE_UNDO_FAILURE);
    }

    @Test
    public void undo_afterExecuteOriginalPersonNull_throwsCommandException() throws Exception {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> tagsToAdd = Set.of(new Tag("mentor", TagType.ROLE));
        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        Model expectedAfterExecute = new ModelManager(model.getAddressBook(), new UserPrefs());
        Set<Tag> expectedTags = new HashSet<>(personToEdit.getTags());
        expectedTags.addAll(tagsToAdd);
        Person editedPerson = personToEdit.withTags(expectedTags);
        expectedAfterExecute.setPerson(personToEdit, editedPerson);
        assertCommandSuccess(tagCommand, model, String.format(TagCommand.MESSAGE_SUCCESS, tagsToAdd),
                expectedAfterExecute);

        Field originalPersonField = TagCommand.class.getDeclaredField("originalPerson");
        originalPersonField.setAccessible(true);
        originalPersonField.set(tagCommand, null);

        assertUndoFailure(tagCommand, model, TagCommand.MESSAGE_UNDO_FAILURE);
    }

    @Test
    public void undo_afterExecuteUpdatedPersonNull_throwsCommandException() throws Exception {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> tagsToAdd = Set.of(new Tag("mentor", TagType.ROLE));
        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        Model expectedAfterExecute = new ModelManager(model.getAddressBook(), new UserPrefs());
        Set<Tag> expectedTags = new HashSet<>(personToEdit.getTags());
        expectedTags.addAll(tagsToAdd);
        Person editedPerson = personToEdit.withTags(expectedTags);
        expectedAfterExecute.setPerson(personToEdit, editedPerson);
        assertCommandSuccess(tagCommand, model, String.format(TagCommand.MESSAGE_SUCCESS, tagsToAdd),
                expectedAfterExecute);

        Field updatedPersonField = TagCommand.class.getDeclaredField("updatedPerson");
        updatedPersonField.setAccessible(true);
        updatedPersonField.set(tagCommand, null);

        assertUndoFailure(tagCommand, model, TagCommand.MESSAGE_UNDO_FAILURE);
    }

    @Test
    public void isUndoable_returnsTrue() {
        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, Set.of(new Tag("mentor", TagType.ROLE)));
        assertTrue(tagCommand.isUndoable());
    }
}
