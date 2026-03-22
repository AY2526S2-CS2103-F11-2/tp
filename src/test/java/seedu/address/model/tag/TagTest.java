package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void constructor_validTagNameAndType_success() {
        Tag tag = new Tag("friend", TagType.GENERAL);
        assertEquals("friend", tag.tagName);
        assertEquals(TagType.GENERAL, tag.getType());
    }

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null, null));
    }

    @Test
    public void constructor_nullTagName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null, TagType.GENERAL));
    }

    @Test
    public void constructor_nullType_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag("friends", null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Tag(" ", TagType.GENERAL));
        assertThrows(IllegalArgumentException.class, () -> new Tag("#friends", TagType.GENERAL));
    }


    @Test
    public void isValidTagName_invalidTagName_false() {
        // null tag name
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));

        // invalid tag names
        assertFalse(Tag.isValidTagName("")); // empty
        assertFalse(Tag.isValidTagName(" ")); // spaces
        assertFalse(Tag.isValidTagName("#friends")); // special characters
        assertFalse(Tag.isValidTagName("friends!")); // punctuation
    }

    @Test
    public void isValidTagName_validTagName_true() {
        assertTrue(Tag.isValidTagName("friends"));
        assertTrue(Tag.isValidTagName("cs2103"));
        assertTrue(Tag.isValidTagName("MA1522"));
    }

    @Test
    public void equals() {
        Tag tag1 = new Tag("friends", TagType.GENERAL);
        Tag tag2 = new Tag("friends", TagType.GENERAL);
        Tag tag3 = new Tag("friends", TagType.ROLE);
        Tag tag4 = new Tag("family", TagType.GENERAL);

        // same object
        assertTrue(tag1.equals(tag1));

        // same values
        assertTrue(tag1.equals(tag2));

        // different type
        assertFalse(tag1.equals(tag3));

        // different name
        assertFalse(tag1.equals(tag4));

        // null
        assertFalse(tag1.equals(null));

        // different type object
        assertFalse(tag1.equals(5));
    }

    @Test
    public void hashCode_consistentWithEquals() {
        Tag tag1 = new Tag("friends", TagType.GENERAL);
        Tag tag2 = new Tag("friends", TagType.GENERAL);

        assertTrue(tag1.equals(tag2));
        assertTrue(tag1.hashCode() == tag2.hashCode());
    }

    @Test
    public void toString_formatCorrect() {
        Tag tag = new Tag("friends", TagType.GENERAL);
        String expected = TagType.GENERAL + ": friends";
        assertTrue(tag.toString().equals(expected));
    }
}
