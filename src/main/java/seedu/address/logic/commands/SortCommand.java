package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ORDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REVERSE;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Sorts the person list by a specified order.
 */
public class SortCommand extends Command {
    public static final String COMMAND_WORD = "sort";

    public static final Map<String, Comparator<Person>> SORT_COMPARATORS = Map.of(
            "name", Comparator.comparing(p -> p.getName().fullName, String.CASE_INSENSITIVE_ORDER),
            "email", Comparator.comparing((Person p) -> p.getEmail().value),
            "phone", Comparator.comparing(
                    p -> p.getPhone() != null ? p.getPhone().value : null,
                    Comparator.nullsLast(Comparator.naturalOrder())));

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sorts the persons by a specified field, or resets sort order.\n"
            + "Parameters: " + PREFIX_ORDER + "ORDER [" + PREFIX_REVERSE + "]\n"
            + "Valid ORDER values: "
            + String.join(", ", new TreeMap<>(SORT_COMPARATORS).keySet()) + ", none\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_ORDER + "name " + PREFIX_REVERSE;

    private final String order;
    private final boolean reverse;
    private final Comparator<Person> comparator;

    /**
     * Creates a SortCommand that sorts persons by the given order, optionally reversed.
     */
    public SortCommand(String order, boolean reverse) {
        this.order = order;
        this.reverse = reverse;
        Comparator<Person> cmp = SORT_COMPARATORS.get(order);
        if (cmp == null) {
            this.comparator = null;
        } else if (reverse && "phone".equals(order)) {
            this.comparator = Comparator.comparing((Person p) ->
                    p.getPhone() != null ? p.getPhone().value : null,
                    Comparator.nullsLast(Comparator.<String>naturalOrder().reversed()));
        } else {
            this.comparator = reverse ? cmp.reversed() : cmp;
        }
    }

    /**
     * Builds the success message for a completed sort operation.
     *
     * @param order   the sort field (e.g. "name", "email", "phone")
     * @param reverse whether the sort is descending
     * @return the formatted success message
     */
    public static String buildSuccessMessage(String order, boolean reverse) {
        return String.format("Sorted by %s (%s).", order, reverse ? "descending" : "ascending");
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.updateSortedPersonList(comparator);
        if ("none".equals(order)) {
            return new CommandResult(Messages.MESSAGE_SORT_RESET);
        }
        return new CommandResult(buildSuccessMessage(order, reverse));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof SortCommand otherCommand)) {
            return false;
        }
        return order.equals(otherCommand.order) && reverse == otherCommand.reverse;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("order", order)
                .add("reverse", reverse)
                .toString();
    }
}
