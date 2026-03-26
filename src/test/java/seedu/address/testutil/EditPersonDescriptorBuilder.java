package seedu.address.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.TelegramHandle;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagType;

/**
 * A utility class to help with building EditPersonDescriptor objects.
 */
public class EditPersonDescriptorBuilder {

    private EditPersonDescriptor descriptor;

    /**
     * Creates an {@code EditPersonDescriptorBuilder} with a new empty descriptor.
     */
    public EditPersonDescriptorBuilder() {
        descriptor = new EditPersonDescriptor();
    }

    /**
     * Creates an {@code EditPersonDescriptorBuilder} with a copy of the given descriptor.
     *
     * @param descriptor The descriptor to copy.
     */
    public EditPersonDescriptorBuilder(EditPersonDescriptor descriptor) {
        this.descriptor = new EditPersonDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditPersonDescriptor} with fields containing {@code person}'s details.
     * Only sets tag types that the person actually has to avoid "clear all" semantics.
     *
     * @param person The person whose details are used to populate the descriptor.
     */
    public EditPersonDescriptorBuilder(Person person) {
        descriptor = new EditPersonDescriptor();
        descriptor.setName(person.getName());
        descriptor.setPhone(person.getPhone());
        descriptor.setEmail(person.getEmail());
        descriptor.setTelegramHandle(person.getTelegramHandle());

        Set<Tag> allTags = person.getTags();
        Set<Tag> roleTags = Tag.filterByType(allTags, TagType.ROLE);
        Set<Tag> courseTags = Tag.filterByType(allTags, TagType.COURSE);
        Set<Tag> generalTags = Tag.filterByType(allTags, TagType.GENERAL);

        if (!roleTags.isEmpty()) {
            descriptor.setRoleTags(roleTags);
        }
        if (!courseTags.isEmpty()) {
            descriptor.setCourseTags(courseTags);
        }
        if (!generalTags.isEmpty()) {
            descriptor.setGeneralTags(generalTags);
        }
    }

    /**
     * Sets the {@code Name} of the {@code EditPersonDescriptor} that we are building.
     *
     * @param name The name to set.
     * @return This builder for method chaining.
     */
    public EditPersonDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditPersonDescriptor} that we are building.
     *
     * @param phone The phone number to set.
     * @return This builder for method chaining.
     */
    public EditPersonDescriptorBuilder withPhone(String phone) {
        descriptor.setPhone(new Phone(phone));
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code EditPersonDescriptor} that we are building.
     *
     * @param email The email to set.
     * @return This builder for method chaining.
     */
    public EditPersonDescriptorBuilder withEmail(String email) {
        descriptor.setEmail(new Email(email));
        return this;
    }

    /**
     * Sets the {@code TelegramHandle} of the {@code EditPersonDescriptor} that we are building.
     *
     * @param telegramHandle The telegram handle to set.
     * @return This builder for method chaining.
     */
    public EditPersonDescriptorBuilder withTelegramHandle(String telegramHandle) {
        descriptor.setTelegramHandle(new TelegramHandle(telegramHandle));
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} with {@code TagType.ROLE}
     * and sets it to the {@code EditPersonDescriptor} that we are building.
     *
     * @param tags The role tag names to set.
     * @return This builder for method chaining.
     */
    public EditPersonDescriptorBuilder withRoleTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(t -> new Tag(t, TagType.ROLE)).collect(Collectors.toSet());
        descriptor.setRoleTags(tagSet);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} with {@code TagType.COURSE}
     * and sets it to the {@code EditPersonDescriptor} that we are building.
     *
     * @param tags The course tag names to set.
     * @return This builder for method chaining.
     */
    public EditPersonDescriptorBuilder withCourseTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(t -> new Tag(t, TagType.COURSE)).collect(Collectors.toSet());
        descriptor.setCourseTags(tagSet);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} with {@code TagType.GENERAL}
     * and sets it to the {@code EditPersonDescriptor} that we are building.
     *
     * @param tags The general tag names to set.
     * @return This builder for method chaining.
     */
    public EditPersonDescriptorBuilder withGeneralTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(t -> new Tag(t, TagType.GENERAL)).collect(Collectors.toSet());
        descriptor.setGeneralTags(tagSet);
        return this;
    }

    /**
     * Builds and returns the {@code EditPersonDescriptor}.
     *
     * @return The built descriptor.
     */
    public EditPersonDescriptor build() {
        return descriptor;
    }
}
