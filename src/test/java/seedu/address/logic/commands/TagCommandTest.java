package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
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
    public void constructor_nullIndex_throwsNullPointerException() {
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("friend", TagType.GENERAL));

        assertThrows(NullPointerException.class, () -> new TagCommand(null, tagsToAdd));
    }

    @Test
    public void constructor_nullTagsToAdd_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TagCommand(INDEX_FIRST_PERSON, null));
    }

    @Test
    public void execute_validIndex_addSingleTag_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Tag newTag = new Tag("mentor", TagType.ROLE);
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("mentor", TagType.ROLE));

        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Set<Tag> expectedTags = new HashSet<>(personToEdit.getTags());
        expectedTags.add(newTag);

        Person editedPerson = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getTelegramHandle(),
                expectedTags
        );

        expectedModel.setPerson(personToEdit, editedPerson);
        String expectedMessage = String.format(TagCommand.MESSAGE_SUCCESS, tagsToAdd);
        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndex_addMultipleTags_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("tutor", TagType.ROLE));
        tagsToAdd.add(new Tag("CS2103", TagType.COURSE));
        tagsToAdd.add(new Tag("friends", TagType.GENERAL));

        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Set<Tag> expectedTags = new HashSet<>(personToEdit.getTags());
        expectedTags.addAll(tagsToAdd);

        Person editedPerson = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getTelegramHandle(),
                expectedTags
        );

        expectedModel.setPerson(personToEdit, editedPerson);
        String expectedMessage = String.format(TagCommand.MESSAGE_SUCCESS, tagsToAdd);
        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndex_addTagToPersonWithNoTags_success() {
        // INDEX_THIRD_PERSON: a person with no tags
        Person personWithoutTags = model.getFilteredPersonList().get(INDEX_THIRD_PERSON.getZeroBased());

        Tag newTag = new Tag("friend", TagType.GENERAL);
        Set<Tag> tagsToAdd = Set.of(newTag);

        TagCommand tagCommand = new TagCommand(INDEX_THIRD_PERSON, tagsToAdd);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Set<Tag> expectedTags = new HashSet<>();
        expectedTags.add(newTag);

        Person editedPerson = new Person(
                personWithoutTags.getName(),
                personWithoutTags.getPhone(),
                personWithoutTags.getEmail(),
                personWithoutTags.getTelegramHandle(),
                expectedTags
        );

        expectedModel.setPerson(personWithoutTags, editedPerson);
        String expectedMessage = String.format(TagCommand.MESSAGE_SUCCESS, tagsToAdd);
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
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Set<Tag> tags = new HashSet<>(personToEdit.getTags()); // same tags

        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, tags);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        // should remain unchanged
        assertCommandSuccess(tagCommand, model,
                String.format(TagCommand.MESSAGE_SUCCESS, tags),
                expectedModel);
    }

    @Test
    public void execute_mixedNewAndDuplicateTags_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Tag existingTag = personToEdit.getTags().iterator().next();
        Tag newTag = new Tag("mentor", TagType.ROLE);

        Set<Tag> tagsToAdd = Set.of(existingTag, newTag);

        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Set<Tag> expectedTags = new HashSet<>(personToEdit.getTags());
        expectedTags.add(newTag); // only new tag added

        Person editedPerson = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getTelegramHandle(),
                expectedTags
        );

        expectedModel.setPerson(personToEdit, editedPerson);
        String expectedMessage = String.format(TagCommand.MESSAGE_SUCCESS, tagsToAdd);
        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
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
        Set<Tag> tagsToAdd = Set.of(
                new Tag("cs2103", TagType.COURSE),
                new Tag("tutor", TagType.ROLE)
        );

        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        String expected = TagCommand.class.getCanonicalName()
                + "{tagsToAdd=" + tagsToAdd + "}";

        assertEquals(expected, tagCommand.toString());
    }
}
