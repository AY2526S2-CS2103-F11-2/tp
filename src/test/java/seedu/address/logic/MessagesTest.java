package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.DuplicateConflict;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class MessagesTest {

    @Test
    public void format_personWithoutPhoneWithoutTelegram_success() {
        Person person = new PersonBuilder()
                .withName("Amy Bee")
                .withEmail("amy@gmail.com")
                .withGeneralTags("friend")
                .build();
        person = new Person(person.getName(), null, person.getEmail(),
                null, person.getTags());
        assertEquals("Amy Bee; Email: amy@gmail.com; Tags: [GENERAL: friend]",
                Messages.format(person));
    }

    @Test
    public void format_personWithPhoneAndTelegram_success() {
        Person person = new PersonBuilder()
                .withName("Amy Bee")
                .withPhone("85355255")
                .withEmail("amy@gmail.com")
                .withTelegramHandle("amybee")
                .withGeneralTags("friend")
                .build();

        assertEquals("Amy Bee; Phone: 85355255; Email: amy@gmail.com; Telegram: amybee; Tags: [GENERAL: friend]",
                Messages.format(person));
    }

    @Test
    public void getDuplicateConflictMessage_allConflictTypes_success() {
        assertEquals(Messages.MESSAGE_DUPLICATE_EMAIL,
                Messages.getDuplicateConflictMessage(DuplicateConflict.EMAIL));
        assertEquals(Messages.MESSAGE_DUPLICATE_TELEGRAM_HANDLE,
                Messages.getDuplicateConflictMessage(DuplicateConflict.TELEGRAM_HANDLE));
        assertEquals(Messages.MESSAGE_DUPLICATE_EMAIL_AND_TELEGRAM_HANDLE,
                Messages.getDuplicateConflictMessage(DuplicateConflict.EMAIL_AND_TELEGRAM_HANDLE));
        assertEquals(null, Messages.getDuplicateConflictMessage(DuplicateConflict.NONE));
    }
}
