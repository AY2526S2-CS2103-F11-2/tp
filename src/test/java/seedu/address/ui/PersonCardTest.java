package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.scene.control.Label;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class PersonCardTest {

    @BeforeAll
    public static void initToolkit() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            Platform.startup(latch::countDown);
        } catch (IllegalStateException e) {
            // Toolkit already started
            latch.countDown();
        }
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void constructor_withoutPhoneAndTelegram_hidesOptionalLabels() throws Exception {
        Person person = new PersonBuilder()
                .withName("John Doe")
                .withEmail("john@example.com")
                .withAddress("Not provided")
                .withTags("friend")
                .build();
        person = new Person(person.getName(), null, person.getEmail(),
                person.getAddress(), null, person.getTags());

        PersonCard card = createCardOnFxThread(person, 1);

        Label email = getLabel(card, "email");
        Label phone = getLabel(card, "phone");
        Label telegramHandle = getLabel(card, "telegramHandle");

        assertEquals("john@example.com", email.getText());
        assertEquals("", phone.getText());
        assertFalse(phone.isManaged());
        assertFalse(phone.isVisible());
        assertEquals("", telegramHandle.getText());
        assertFalse(telegramHandle.isManaged());
        assertFalse(telegramHandle.isVisible());
    }

    @Test
    public void constructor_withPhoneAndTelegram_showsOptionalLabels() throws Exception {
        Person person = new PersonBuilder()
                .withName("Jim Beam")
                .withPhone("98765432")
                .withEmail("jim@example.com")
                .withAddress("Not provided")
                .withTelegramHandle("jimbeam_123")
                .withTags("friend")
                .build();

        PersonCard card = createCardOnFxThread(person, 2);

        Label email = getLabel(card, "email");
        Label phone = getLabel(card, "phone");
        Label telegramHandle = getLabel(card, "telegramHandle");

        assertEquals("jim@example.com", email.getText());
        assertEquals("98765432", phone.getText());
        assertTrue(phone.isManaged());
        assertTrue(phone.isVisible());
        assertEquals("jimbeam_123", telegramHandle.getText());
        assertTrue(telegramHandle.isManaged());
        assertTrue(telegramHandle.isVisible());
    }

    private PersonCard createCardOnFxThread(Person person, int index) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        final PersonCard[] holder = new PersonCard[1];
        Platform.runLater(() -> {
            holder[0] = new PersonCard(person, index);
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
        return holder[0];
    }

    private Label getLabel(PersonCard card, String fieldName) throws Exception {
        Field field = PersonCard.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (Label) field.get(card);
    }
}
