package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagType;
import seedu.address.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> person.getTags().remove(0));
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // same email, all other attributes different -> returns true
        Person editedAlice = new PersonBuilder(ALICE)
                .withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB)
                .withRoleTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // same telegram handle, different email -> returns true
        Person aliceWithTelegram = new PersonBuilder(ALICE).withTelegramHandle("alice123").build();
        Person editedTelegramAlice = new PersonBuilder(aliceWithTelegram)
                .withEmail(VALID_EMAIL_BOB)
                .build();
        assertTrue(aliceWithTelegram.isSamePerson(editedTelegramAlice));

        // different email and different telegram handle -> returns false
        Person differentAlice = new PersonBuilder(ALICE)
                .withEmail(VALID_EMAIL_BOB)
                .withTelegramHandle("alice123")
                .build();
        assertFalse(ALICE.isSamePerson(differentAlice));

        // different email, telegram handle missing on one side -> returns false
        Person noTelegram = new PersonBuilder(ALICE).build();
        Person withTelegram = new PersonBuilder(ALICE)
                .withEmail(VALID_EMAIL_BOB)
                .withTelegramHandle("alice123")
                .build();
        assertFalse(noTelegram.isSamePerson(withTelegram));

        // different email, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new PersonBuilder(ALICE).withRoleTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void equals_differentTelegramHandle_false() {
        Person editedAlice = new PersonBuilder(ALICE).withTelegramHandle("alice123").build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void hashCode_sameValuesSameHashCode() {
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertEquals(ALICE.hashCode(), aliceCopy.hashCode());
    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName() + "{name=" + ALICE.getName() + ", phone=" + ALICE.getPhone()
                + ", email=" + ALICE.getEmail()
                + ", telegramHandle=" + ALICE.getTelegramHandle() + ", tags=" + ALICE.getTags() + "}";
        assertEquals(expected, ALICE.toString());
    }

    @Test
    public void toStringMethod_withTelegramHandle() {
        Person person = new PersonBuilder(ALICE).withTelegramHandle("alice123").build();
        String expected = Person.class.getCanonicalName() + "{name=" + person.getName() + ", phone=" + person.getPhone()
                + ", email=" + person.getEmail()
                + ", telegramHandle=" + person.getTelegramHandle() + ", tags=" + person.getTags() + "}";
        assertEquals(expected, person.toString());
    }

    @Test
    public void constructor_withoutTelegramHandle_success() {
        Person person = new Person(
                ALICE.getName(),
                ALICE.getPhone(),
                ALICE.getEmail(),
                ALICE.getTags()
        );

        assertEquals(ALICE.getName(), person.getName());
        assertEquals(ALICE.getPhone(), person.getPhone());
        assertEquals(ALICE.getEmail(), person.getEmail());
        assertEquals(ALICE.getTags(), person.getTags());

        // telegram should be null
        assertEquals(null, person.getTelegramHandle());
    }

    @Test
    public void constructor_withoutTags_success() {
        Person person = new Person(
                ALICE.getName(),
                ALICE.getPhone(),
                ALICE.getEmail(),
                ALICE.getTelegramHandle()
        );

        assertEquals(ALICE.getName(), person.getName());
        assertEquals(ALICE.getPhone(), person.getPhone());
        assertEquals(ALICE.getEmail(), person.getEmail());
        assertEquals(ALICE.getTelegramHandle(), person.getTelegramHandle());

        // tags should be empty
        assertTrue(person.getTags().isEmpty());
    }

    @Test
    public void constructor_defensiveCopy_tagsNotAffected() {
        Set<Tag> originalTags = new HashSet<>(ALICE.getTags());

        Person person = new Person(
                ALICE.getName(),
                ALICE.getPhone(),
                ALICE.getEmail(),
                ALICE.getTelegramHandle(),
                originalTags
        );

        // mutate original set
        originalTags.clear();

        // person should NOT be affected
        assertFalse(person.getTags().isEmpty());
    }

    @Test
    public void withTags_validTags_success() {
        Set<Tag> newTags = Set.of(new Tag("tutor", TagType.ROLE));

        Person updated = ALICE.withTags(newTags);

        // tags updated
        assertEquals(newTags, updated.getTags());

        // other fields unchanged
        assertEquals(ALICE.getName(), updated.getName());
        assertEquals(ALICE.getPhone(), updated.getPhone());
        assertEquals(ALICE.getEmail(), updated.getEmail());
        assertEquals(ALICE.getTelegramHandle(), updated.getTelegramHandle());
    }

    @Test
    public void withTags_nullTags_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ALICE.withTags(null));
    }

    @Test
    public void withTags_defensiveCopy_tagsNotAffected() {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("friend", TagType.GENERAL));

        Person updated = ALICE.withTags(tags);

        // mutate original set
        tags.clear();

        // person should NOT be affected
        assertFalse(updated.getTags().isEmpty());
    }
}
