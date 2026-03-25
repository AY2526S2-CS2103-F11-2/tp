package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.TelegramHandle;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagType;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[]{
            new Person(new Name("Tan Wei Ming"), new Phone("65162345"),
                new Email("tanwm@comp.nus.edu.sg"),
                new TelegramHandle("tanwm_nus"),
                combineTags(getRoleTagSet("Professor"), getCourseTagSet("CS2103T"))),
            new Person(new Name("Lim Beng Huat"), new Phone("91234567"),
                new Email("limbenghuat@u.nus.edu"),
                new TelegramHandle("limbenghuat"),
                combineTags(getRoleTagSet("TeachingAssistant"), getCourseTagSet("CS2103T"))),
            new Person(new Name("Alice Tan"), new Phone("87654321"),
                new Email("alicetan@u.nus.edu"),
                new TelegramHandle("alicetan_sg"),
                combineTags(getCourseTagSet("CS2103T", "CS2101"), getGeneralTagSet("ProjectMate"))),
            new Person(new Name("Bob Chen"), new Phone("92345678"),
                new Email("bobchen@u.nus.edu"),
                new TelegramHandle("bobchen_nus"),
                combineTags(getCourseTagSet("CS2040S"), getGeneralTagSet("StudyGroup"))),
            new Person(new Name("Priya Suresh"), new Phone("83456789"),
                new Email("priyasuresh@u.nus.edu"),
                new TelegramHandle("priyasuresh"),
                combineTags(getRoleTagSet("TeachingAssistant"), getCourseTagSet("CS2101"))),
            new Person(new Name("David Lim"), new Phone("65161234"),
                new Email("davidlim@comp.nus.edu.sg"),
                new TelegramHandle("davidlim_nus"),
                combineTags(getRoleTagSet("Professor"), getCourseTagSet("CS1101S"))),
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Combines multiple tag sets into one set.
     */
    @SafeVarargs
    private static Set<Tag> combineTags(Set<Tag>... tagSets) {
        return Arrays.stream(tagSets)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a set of general tags.
     */
    public static Set<Tag> getGeneralTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(s -> new Tag(s, TagType.GENERAL))
                .collect(Collectors.toSet());
    }

    /**
     * Returns a set of role tags.
     */
    public static Set<Tag> getRoleTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(s -> new Tag(s, TagType.ROLE))
                .collect(Collectors.toSet());
    }

    /**
     * Returns a set of course tags.
     */
    public static Set<Tag> getCourseTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(s -> new Tag(s, TagType.COURSE))
                .collect(Collectors.toSet());
    }
}
