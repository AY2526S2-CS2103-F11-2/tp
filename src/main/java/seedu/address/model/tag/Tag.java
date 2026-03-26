package seedu.address.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a Tag in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTagName(String)}
 */
public class Tag {

    public static final String MESSAGE_CONSTRAINTS = "Tags names should be alphanumeric";
    public static final String MESSAGE_CONSTRAINTS_TAG_TYPE = "Invalid tag type: %1$s";
    public static final String VALIDATION_REGEX = "\\p{Alnum}+";

    public final String tagName;
    public final TagType type;

    /**
     * Constructs a {@code Tag}.
     *
     * @param tagName A valid tag name.
     * @param type    A valid tag type.
     */
    public Tag(String tagName, TagType type) {
        requireNonNull(tagName);
        requireNonNull(type);

        checkArgument(isValidTagName(tagName), MESSAGE_CONSTRAINTS);

        this.tagName = tagName.toLowerCase(Locale.ROOT);
        this.type = type;
    }

    public TagType getType() {
        return type;
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidTagName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Tag)) {
            return false;
        }

        Tag otherTag = (Tag) other;
        return tagName.equals(otherTag.tagName)
                && type.equals(otherTag.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagName, type);
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return type + ": " + tagName;
    }
    /**
     * Filters a set of tags by the specified tag type.
     *
     * @param tags The set of tags to filter.
     * @param type The tag type to filter by.
     * @return A new set containing only tags of the specified type.
     */
    public static Set<Tag> filterByType(Set<Tag> tags, TagType type) {
        return tags.stream()
                .filter(t -> t.getType() == type)
                .collect(Collectors.toSet());
    }
}
