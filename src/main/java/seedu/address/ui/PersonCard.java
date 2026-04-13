package seedu.address.ui;

import java.util.Comparator;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import seedu.address.model.person.Person;
import seedu.address.model.tag.TagType;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {
    private static final double PAUSE_TIME = 0.5;
    private static final String COPIED_TEXT = "✓ Copied!";

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label email;
    @FXML
    private Label telegramHandle;
    @FXML
    private FlowPane tags;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        email.setText("Email: " + person.getEmail().value);

        boolean hasPhone = person.getPhone() != null;
        phone.setText(hasPhone ? "Phone: " + person.getPhone().value : "");
        phone.setManaged(hasPhone);
        phone.setVisible(hasPhone);

        boolean hasTelegram = person.getTelegramHandle() != null;
        telegramHandle.setText(hasTelegram ? "Telegram: " + person.getTelegramHandle().value : "");
        telegramHandle.setManaged(hasTelegram);
        telegramHandle.setVisible(hasTelegram);

        name.getStyleClass().add("copyable-label");
        email.getStyleClass().add("copyable-label");
        phone.getStyleClass().add("copyable-label");
        telegramHandle.getStyleClass().add("copyable-label");

        setupCopyable(name, person.getName().fullName, person.getName().fullName);
        setupCopyable(email, "Email: " + person.getEmail().value, person.getEmail().value);
        if (hasPhone) {
            setupCopyable(phone, "Phone: " + person.getPhone().value, person.getPhone().value);
        }
        if (hasTelegram) {
            setupCopyable(telegramHandle, "Telegram: " + person.getTelegramHandle().value,
                    person.getTelegramHandle().value);
        }

        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> {
                    Label tagLabel = new Label(tag.tagName);
                    tagLabel.getStyleClass().add(getTagStyleClass(tag.getType()));
                    tagLabel.setTooltip(new Tooltip(tag.tagName + "\nClick to copy"));
                    tagLabel.setOnMouseClicked(e -> copyToClipboard(tagLabel, tag.tagName, tag.tagName));
                    tags.getChildren().add(tagLabel);
                });
    }

    private String getTagStyleClass(TagType type) {
        return switch (type) {
        case ROLE -> "tag-role";
        case COURSE -> "tag-course";
        default -> "tag-general";
        };
    }

    private void setupCopyable(Label label, String displayText, String copiedText) {
        label.setTooltip(new Tooltip("Click to copy"));
        label.setOnMouseClicked(e -> copyToClipboard(label, displayText, copiedText));
    }

    private void copyToClipboard(Label label, String displayText, String copiedText) {
        if (COPIED_TEXT.equals(label.getText())) {
            return;
        }

        ClipboardContent content = new ClipboardContent();
        content.putString(copiedText);
        Clipboard.getSystemClipboard().setContent(content);

        label.setText(COPIED_TEXT);
        PauseTransition pause = new PauseTransition(Duration.seconds(PAUSE_TIME));
        pause.setOnFinished(e -> label.setText(displayText));
        pause.play();
    }
}
