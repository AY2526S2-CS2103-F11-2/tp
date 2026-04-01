package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.DuplicateConflict;
import seedu.address.model.person.Person;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_EMAIL = Messages.MESSAGE_DUPLICATE_EMAIL_IN_STORAGE;
    public static final String MESSAGE_DUPLICATE_TELEGRAM_HANDLE =
            Messages.MESSAGE_DUPLICATE_TELEGRAM_HANDLE_IN_STORAGE;
    public static final String MESSAGE_DUPLICATE_EMAIL_AND_TELEGRAM_HANDLE =
            Messages.MESSAGE_DUPLICATE_EMAIL_AND_TELEGRAM_HANDLE_IN_STORAGE;

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given persons.
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("persons") List<JsonAdaptedPerson> persons) {
        this.persons.addAll(persons);
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedPerson::new).collect(Collectors.toList()));
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();
        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            Person person = jsonAdaptedPerson.toModelType();
            DuplicateConflict duplicateConflict = addressBook.getDuplicateConflict(person);

            if (duplicateConflict == DuplicateConflict.EMAIL_AND_TELEGRAM_HANDLE) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_EMAIL_AND_TELEGRAM_HANDLE);
            }
            if (duplicateConflict == DuplicateConflict.EMAIL) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_EMAIL);
            }
            if (duplicateConflict == DuplicateConflict.TELEGRAM_HANDLE) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_TELEGRAM_HANDLE);
            }
            addressBook.addPerson(person);
        }
        return addressBook;
    }

}
