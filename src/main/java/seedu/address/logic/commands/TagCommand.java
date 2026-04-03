package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GENERAL_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE_TAG;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Adds tags to an existing person in the address book.
 */
public class TagCommand extends Command {

    public static final String COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Add tags to the person identified by the index number used in the displayed person list.\n"
            + "At least one tag must be provided.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_ROLE_TAG + "ROLE_TAG]... "
            + "[" + PREFIX_COURSE_TAG + "COURSE_TAG]... "
            + "[" + PREFIX_GENERAL_TAG + "GENERAL_TAG]... \n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_ROLE_TAG + "tutor "
            + PREFIX_COURSE_TAG + "cs2103 " + PREFIX_GENERAL_TAG + "friends";

    public static final String MESSAGE_SUCCESS = "New tags added: %1$s";
    public static final String MESSAGE_PARTIAL_SUCCESS =
            "New tags added: %1$s\nTags already existing (no changes made): %2$s";
    public static final String MESSAGE_NO_NEW_TAGS = "All tags already exist for this person. No changes made.";
    public static final String MESSAGE_UNDO_SUCCESS = "Undo tag operation for: %1$s";
    public static final String MESSAGE_UNDO_FAILURE = "Cannot undo tag before command execution.";

    private final Index index;
    private final Set<Tag> tagsToAdd;
    private Person originalPerson;
    private Person updatedPerson;

    /**
     * @param index     of the person in the filtered person list to add tags.
     * @param tagsToAdd the tags to add.
     */
    public TagCommand(Index index, Set<Tag> tagsToAdd) {
        requireNonNull(index);
        requireNonNull(tagsToAdd);

        this.index = index;
        this.tagsToAdd = tagsToAdd;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(
                    String.format(Messages.MESSAGE_PERSON_NOT_FOUND_DISPLAYED_INDEX, index.getOneBased())
            );
        }

        Person personToAddTag = lastShownList.get(index.getZeroBased());
        originalPerson = personToAddTag;

        TagDifference tagDifference = computeTagDifference(personToAddTag);
        if (tagDifference.newTags.isEmpty()) {
            throw new CommandException(MESSAGE_NO_NEW_TAGS);
        }

        // merge existing tags with new tags
        Set<Tag> updatedTags = new HashSet<>(personToAddTag.getTags());
        updatedTags.addAll(tagDifference.newTags);

        Person editedPerson = personToAddTag.withTags(updatedTags);
        updatedPerson = editedPerson;

        model.setPerson(personToAddTag, editedPerson);

        if (tagDifference.existingTags.isEmpty()) {
            return new CommandResult(String.format(MESSAGE_SUCCESS, tagDifference.newTags));
        }
        return new CommandResult(String.format(
                MESSAGE_PARTIAL_SUCCESS, tagDifference.newTags, tagDifference.existingTags
        ));
    }

    @Override
    public boolean isUndoable() {
        return true;
    }

    @Override
    public CommandResult undo(Model model) throws CommandException {
        requireNonNull(model);
        if (originalPerson == null || updatedPerson == null) {
            throw new CommandException(MESSAGE_UNDO_FAILURE);
        }

        model.setPerson(updatedPerson, originalPerson);
        return new CommandResult(String.format(MESSAGE_UNDO_SUCCESS, Messages.format(originalPerson)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TagCommand)) {
            return false;
        }

        TagCommand otherTagCommand = (TagCommand) other;
        return index.equals(otherTagCommand.index)
                && tagsToAdd.equals(otherTagCommand.tagsToAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("tagsToAdd", tagsToAdd)
                .toString();
    }

    /**
     * Represents the difference between input tags and existing tags.
     */
    private record TagDifference(Set<Tag> newTags, Set<Tag> existingTags) {
    }

    /**
     * Computes which tags are new and which already exist on the person.
     *
     * @param person the person to check existing tags against.
     * @return a TagDifference containing new tags and existing tags from the input.
     */
    private TagDifference computeTagDifference(Person person) {
        Set<Tag> existingTags = person.getTags();

        Set<Tag> newTags = tagsToAdd.stream()
                .filter(tag -> !existingTags.contains(tag))
                .collect(Collectors.toSet());

        Set<Tag> existingTagsFromInput = tagsToAdd.stream()
                .filter(tag -> existingTags.contains(tag))
                .collect(Collectors.toSet());

        return new TagDifference(newTags, existingTagsFromInput);
    }
}
