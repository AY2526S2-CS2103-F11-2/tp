package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.ADD_EDIT_COMMAND_PREFIXES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM_HANDLE;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;


/**
 * Parses input arguments and creates a new {@link EditCommand} object.
 * <p>
 * Expected input format: {@code INDEX n/NAME p/PHONE e/EMAIL t/TELEGRAM_HANDLE},
 * where {@code INDEX} is a positive integer and at least one field must be provided.
 * Duplicate prefixes are not allowed.
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code args} string in the context of the {@link EditCommand}
     * and returns an {@code EditCommand} object for execution.
     *
     * <p>The method performs the following validations in order:
     * <ol>
     *   <li>Rejects any unrecognised prefixes not in {@link CliSyntax#ADD_EDIT_COMMAND_PREFIXES}.</li>
     *   <li>Ensures the preamble contains exactly one token (the target index).</li>
     *   <li>Parses the preamble as a valid one-based {@link Index}.</li>
     *   <li>Rejects duplicate prefixes for name, phone, email, and telegram handle.</li>
     *   <li>Ensures at least one editable field is specified.</li>
     * </ol>
     *
     * @param args the user-supplied arguments string (must not be {@code null}).
     * @return an {@code EditCommand} targeting the parsed index with the specified field updates.
     * @throws ParseException if the arguments contain invalid prefixes, a missing or malformed
     *         index, duplicate prefixes, or no editable fields.
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ParserUtil.validateNoInvalidPrefixInputs(args, ADD_EDIT_COMMAND_PREFIXES);

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, ADD_EDIT_COMMAND_PREFIXES);

        ParserUtil.validateNoEmptyPrefixValues(argMultimap, ADD_EDIT_COMMAND_PREFIXES);

        String preamble = argMultimap.getPreamble().trim();
        if (preamble.isEmpty() || preamble.contains(" ")) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        Index index;
        try {
            index = ParserUtil.parseIndex(preamble);
        } catch (ParseException pe) {
            throw new ParseException(pe.getMessage());
        }

        argMultimap.verifyNoDuplicatePrefixesFor(ADD_EDIT_COMMAND_PREFIXES);

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editPersonDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editPersonDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            editPersonDescriptor.setEmail(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get()));
        }
        if (argMultimap.getValue(PREFIX_TELEGRAM_HANDLE).isPresent()) {
            editPersonDescriptor.setTelegramHandle(ParserUtil.parseTelegramHandle(argMultimap
                    .getValue(PREFIX_TELEGRAM_HANDLE).get()));
        }

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EditCommand.MESSAGE_USAGE));
        }

        return new EditCommand(index, editPersonDescriptor);
    }
}
