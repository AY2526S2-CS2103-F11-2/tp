package seedu.address.model.person;

import java.util.Locale;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Tests that a Person's tags match all the tags given.
 */
public class PersonContainsTagsPredicate implements Predicate<Person> {
    private final Set<String> lowerCaseTagNames;

    /**
     * Extract all the tag names
     * @param tags The input tags by user
     */
    public PersonContainsTagsPredicate(Set<Tag> tags) {
        this.lowerCaseTagNames = tags.stream()
                .map(tag -> tag.tagName.toLowerCase(Locale.ROOT))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean test(Person person) {
        return person.getTags().stream()
                .map(tag -> tag.tagName.toLowerCase(Locale.ROOT))
                .anyMatch(lowerCaseTagNames::contains);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonContainsTagsPredicate)) {
            return false;
        }

        PersonContainsTagsPredicate otherPersonContainsTagsPredicate = (PersonContainsTagsPredicate) other;
        return lowerCaseTagNames.equals(otherPersonContainsTagsPredicate.lowerCaseTagNames);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("tags", lowerCaseTagNames).toString();
    }
}
