package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Address;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class MessagesTest {

    @Test
    public void format_personWithoutPhoneWithoutTelegram_success() {
        Person person = new PersonBuilder()
                .withName("Amy Bee")
                .withEmail("amy@gmail.com")
                .withAddress(Address.DEFAULT_ADDRESS)
                .withTags("friend")
                .build();
        person = new Person(person.getName(), null, person.getEmail(),
                person.getAddress(), null, person.getTags());
        assertEquals("Amy Bee; Email: amy@gmail.com; Tags: [GENERAL: friend]",
                Messages.format(person));
    }

    @Test
    public void format_personWithPhoneAndTelegram_success() {
        Person person = new PersonBuilder()
                .withName("Amy Bee")
                .withPhone("85355255")
                .withEmail("amy@gmail.com")
                .withAddress(Address.DEFAULT_ADDRESS)
                .withTelegramHandle("amybee")
                .withTags("friend")
                .build();

        assertEquals("Amy Bee; Phone: 85355255; Email: amy@gmail.com; Telegram: amybee; Tags: [GENERAL: friend]",
                Messages.format(person));
    }
}
