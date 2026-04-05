package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNEXPECTED_EXTRA_INPUT;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TELEGRAM_HANDLE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.TELEGRAM_HANDLE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.TELEGRAM_HANDLE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TELEGRAM_HANDLE_BOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM_HANDLE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalPersons.AMY_NO_TAGS;
import static seedu.address.testutil.TypicalPersons.BOB_NO_TAGS;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddCommand;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.TelegramHandle;
import seedu.address.testutil.PersonBuilder;

public class AddCommandParserTest {

    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Person expectedPerson = new PersonBuilder(BOB_NO_TAGS)
                .withTelegramHandle(VALID_TELEGRAM_HANDLE_BOB)
                .build();
        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + TELEGRAM_HANDLE_DESC_BOB, new AddCommand(expectedPerson));
    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedPersonString = NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + TELEGRAM_HANDLE_DESC_BOB;

        // multiple names
        assertParseFailure(parser, NAME_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple phones
        assertParseFailure(parser, PHONE_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple emails
        assertParseFailure(parser, EMAIL_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // multiple telegram handles
        assertParseFailure(parser, TELEGRAM_HANDLE_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_TELEGRAM_HANDLE));

        // multiple fields repeated
        assertParseFailure(parser,
                validExpectedPersonString + PHONE_DESC_AMY + EMAIL_DESC_AMY + NAME_DESC_AMY
                        + TELEGRAM_HANDLE_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(
                        PREFIX_NAME, PREFIX_EMAIL, PREFIX_PHONE, PREFIX_TELEGRAM_HANDLE));

        // invalid value followed by valid value

        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, INVALID_EMAIL_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, INVALID_PHONE_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid telegram handle
        assertParseFailure(parser, INVALID_TELEGRAM_HANDLE_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_TELEGRAM_HANDLE));


        // valid value followed by invalid value

        // invalid name
        assertParseFailure(parser, validExpectedPersonString + INVALID_NAME_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, validExpectedPersonString + INVALID_EMAIL_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, validExpectedPersonString + INVALID_PHONE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid telegram handle
        assertParseFailure(parser, validExpectedPersonString + INVALID_TELEGRAM_HANDLE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_TELEGRAM_HANDLE));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        Person expectedPerson = new Person(
                AMY_NO_TAGS.getName(),
                null,
                AMY_NO_TAGS.getEmail(),
                null,
                java.util.Collections.emptySet());

        assertParseSuccess(parser, NAME_DESC_AMY + EMAIL_DESC_AMY,
                new AddCommand(expectedPerson));
    }

    @Test
    public void parse_telegramWithoutPhone_success() {
        Person expectedPerson = new Person(
                BOB_NO_TAGS.getName(),
                null,
                BOB_NO_TAGS.getEmail(),
                new TelegramHandle(VALID_TELEGRAM_HANDLE_BOB),
                java.util.Collections.emptySet());

        assertParseSuccess(parser, NAME_DESC_BOB + EMAIL_DESC_BOB + TELEGRAM_HANDLE_DESC_BOB,
                new AddCommand(expectedPerson));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB,
                expectedMessage);

        // missing email prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + VALID_EMAIL_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_PHONE_BOB + VALID_EMAIL_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser,
                INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB,
                Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser,
                NAME_DESC_BOB + INVALID_PHONE_DESC + EMAIL_DESC_BOB,
                Phone.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + INVALID_EMAIL_DESC,
                Email.MESSAGE_CONSTRAINTS);

        // invalid telegram handle
        assertParseFailure(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_TELEGRAM_HANDLE_DESC,
                TelegramHandle.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser,
                PREAMBLE_NON_EMPTY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_knownNonAddPrefixes_failure() {
        assertParseFailure(parser,
                NAME_DESC_BOB + EMAIL_DESC_BOB + " " + PREFIX_ROLE_TAG + "tutor",
                String.format(MESSAGE_UNEXPECTED_EXTRA_INPUT, "tr/tutor"));

        assertParseFailure(parser,
                NAME_DESC_BOB + EMAIL_DESC_BOB + " " + PREFIX_INDEX + "3",
                String.format(MESSAGE_UNEXPECTED_EXTRA_INPUT, "i/3"));

        assertParseFailure(parser,
                NAME_DESC_BOB + EMAIL_DESC_BOB + " " + PREFIX_TAG + "friend",
                String.format(MESSAGE_UNEXPECTED_EXTRA_INPUT, "t/friend"));

        assertParseFailure(parser,
                " " + PREFIX_TAG + "friend" + NAME_DESC_BOB + EMAIL_DESC_BOB,
                String.format(MESSAGE_UNEXPECTED_EXTRA_INPUT, "t/friend"));
    }

    @Test
    public void parse_unknownSlashToken_failure() {
        assertParseFailure(parser,
                NAME_DESC_BOB + EMAIL_DESC_BOB + " x/y",
                String.format(MESSAGE_UNEXPECTED_EXTRA_INPUT, "x/y"));
    }

    @Test
    public void parse_plainWordThatLooksLikeCommandWord_success() {
        Person expectedPerson = new Person(
                new Name("Chloe tag"),
                null,
                AMY_NO_TAGS.getEmail(),
                null,
                java.util.Collections.emptySet());

        assertParseSuccess(parser, " n/Chloe tag e/" + AMY_NO_TAGS.getEmail().value,
                new AddCommand(expectedPerson));
    }

    @Test
    public void parse_unknownTrailingText_keepsExistingValidationFailure() {
        assertParseFailure(parser,
                " n/Chloe e/chloe@example.com gibberish",
                Email.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_multipleKnownNonAddPrefixes_reportsEarliestPrefix() {
        assertParseFailure(parser,
                NAME_DESC_BOB + EMAIL_DESC_BOB + " " + PREFIX_INDEX + "3 " + PREFIX_ROLE_TAG + "tutor",
                String.format(MESSAGE_UNEXPECTED_EXTRA_INPUT, "i/3"));
    }

    @Test
    public void parse_multipleKnownNonAddPrefixes_replacesWithEarlierPrefix() {
        assertParseFailure(parser,
                NAME_DESC_BOB + EMAIL_DESC_BOB + " " + PREFIX_ROLE_TAG + "tutor " + PREFIX_INDEX + "3",
                String.format(MESSAGE_UNEXPECTED_EXTRA_INPUT, "tr/tutor"));
    }
}
