package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Finds and lists all persons in the address book whose names, emails, and/or tags
 * match the specified keywords.
 *
 * <p>Matching behaviors by field:
 * <ul>
 *   <li><b>Name keywords:</b> Uses both exact substring matching and fuzzy matching to handle typos.
 *       Fuzzy matching uses Levenshtein distance with a dynamic threshold based on keyword length.</li>
 *   <li><b>Email keywords:</b> Uses exact substring matching.</li>
 *   <li><b>Tags:</b> Uses exact matching.</li>
 * </ul>
 * </p>
 *
 * <p>All matching is case-insensitive and performed on individual tokens (for names, split by whitespace).
 * Logic combination: OR within each field (any keyword matches), AND across fields (all fields must match).</p>
 *
 * @see seedu.address.model.person.NameContainsKeywordsPredicate for name matching details
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names, emails, or tags "
            + "match the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Matching behavior: Names use fuzzy matching (handles typos), emails use substring matching, "
            + "tags use exact matching.\n"
            + "The search uses OR logic within the same field (e.g. multiple name keywords) as well as AND logic "
            + "across different fields (name, email, tags).\n"
            + "Parameters: [" + PREFIX_NAME + "NAME [MORE_NAMES]] [" + PREFIX_EMAIL + "EMAIL [MORE_EMAILS]] ["
            + PREFIX_TAG + "TAGS [MORE_TAGS]].\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_NAME + "Alice Bob " + PREFIX_EMAIL + "nus "
            + PREFIX_TAG + "friends";

    private final Predicate<Person> predicate;

    /**
     * Creates a FindCommand to find matching {@code Person} using the given {@code Predicate}.
     *
     * @param predicate The predicate to determine which persons match the search criteria
     */
    public FindCommand(Predicate<Person>predicate) {
        requireNonNull(predicate);

        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
