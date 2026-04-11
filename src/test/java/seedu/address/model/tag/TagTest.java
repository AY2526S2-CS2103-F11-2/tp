package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void constructor_validTagNameAndType_success() {
        // preserves original casing
        Tag tag = new Tag("friend", TagType.GENERAL);
        assertEquals("friend", tag.tagName);
        assertEquals("friend", tag.normalizedTagName);
        assertEquals(TagType.GENERAL, tag.getType());

        // mixed case - preserves original casing
        Tag tagMixed = new Tag("FrIeNd", TagType.GENERAL);
        assertEquals("FrIeNd", tagMixed.tagName);
        assertEquals("friend", tagMixed.normalizedTagName);
        assertEquals(TagType.GENERAL, tagMixed.getType());

        // uppercase - preserves original casing
        Tag tagUpper = new Tag("FRIEND", TagType.GENERAL);
        assertEquals("FRIEND", tagUpper.tagName);
        assertEquals("friend", tagMixed.normalizedTagName);
        assertEquals(TagType.GENERAL, tagUpper.getType());
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
        // empty string
        assertThrows(IllegalArgumentException.class, () -> new Tag("", TagType.GENERAL));

        // spaces only
        assertThrows(IllegalArgumentException.class, () -> new Tag(" ", TagType.GENERAL));

        // special characters
        assertThrows(IllegalArgumentException.class, () -> new Tag("#friends", TagType.GENERAL));

        // contains space
        assertThrows(IllegalArgumentException.class, () -> new Tag("test 123", TagType.GENERAL));
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
        assertFalse(Tag.isValidTagName(" tag   ")); // contains space
    }

    @Test
    public void isValidTagName_validTagName_true() {
        assertTrue(Tag.isValidTagName("friends"));
        assertTrue(Tag.isValidTagName("cs2103"));
        assertTrue(Tag.isValidTagName("MA1522"));
        assertTrue(Tag.isValidTagName("Friends"));
        assertTrue(Tag.isValidTagName("CS2103"));
    }

    @Test
    public void equals() {
        Tag tag1 = new Tag("friends", TagType.GENERAL);
        Tag tag2 = new Tag("friends", TagType.GENERAL);
        Tag tag3 = new Tag("friends", TagType.ROLE);
        Tag tag4 = new Tag("family", TagType.GENERAL);
        Tag tag5 = new Tag("FRIENDS", TagType.GENERAL);

        // same object
        assertTrue(tag1.equals(tag1));

        // same values
        assertTrue(tag1.equals(tag2));

        // same name different case -> equal
        assertTrue(tag1.equals(tag5));

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

        assertEquals(tag1, tag2);
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void hashCode_consistentWithEquals_caseInsensitive() {
        Tag tagLower = new Tag("friends", TagType.GENERAL);
        Tag tagUpper = new Tag("FRIENDS", TagType.GENERAL);
        Tag tagMixed = new Tag("FrIeNdS", TagType.GENERAL);

        assertEquals(tagLower, tagUpper);
        assertEquals(tagLower, tagMixed);

        assertEquals(tagLower.hashCode(), tagUpper.hashCode());
        assertEquals(tagLower.hashCode(), tagMixed.hashCode());
    }

    @Test
    public void hashCode_differentType_returnsDifferentHashCode() {
        Tag tagGeneral = new Tag("mentor", TagType.GENERAL);
        Tag tagRole = new Tag("mentor", TagType.ROLE);

        assertNotEquals(tagGeneral.hashCode(), tagRole.hashCode());
    }

    @Test
    public void hashCode_differentName_returnsDifferentHashCode() {
        Tag tag1 = new Tag("friends", TagType.GENERAL);
        Tag tag2 = new Tag("family", TagType.GENERAL);

        assertNotEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void toString_returnsOriginalCasing() {
        Tag tagLower = new Tag("friends", TagType.GENERAL);
        String expectedLower = TagType.GENERAL + ": friends";
        assertEquals(tagLower.toString(), expectedLower);

        Tag tagMixed = new Tag("StUdYgRoUp", TagType.GENERAL);
        String expectedMixed = TagType.GENERAL + ": StUdYgRoUp";
        assertEquals(tagMixed.toString(), expectedMixed);

        Tag tagUpper = new Tag("PROFESSOR", TagType.ROLE);
        String expectedUpper = TagType.ROLE + ": PROFESSOR";
        assertEquals(tagUpper.toString(), expectedUpper);
    }

    @Test
    public void toString_differentTags_formatCorrect() {
        Tag roleTag = new Tag("Professor", TagType.ROLE);
        String expectedRoleTag = TagType.ROLE + ": Professor";
        assertEquals(roleTag.toString(), expectedRoleTag);

        Tag courseTag = new Tag("CS2103T", TagType.COURSE);
        String expectedCourseTag = TagType.COURSE + ": CS2103T";
        assertEquals(courseTag.toString(), expectedCourseTag);

        Tag generalTag = new Tag("StudyGroup", TagType.GENERAL);
        String expectedGeneralTag = TagType.GENERAL + ": StudyGroup";
        assertEquals(generalTag.toString(), expectedGeneralTag);
    }
}
