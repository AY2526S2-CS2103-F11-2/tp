package seedu.address.ui;

import java.nio.file.Path;

import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;
import seedu.address.testutil.TypicalPersons;

/**
 * Abstract base class for automated UI integration tests using TestFX.
 * Each subclass gets a full {@link MainWindow} wired to {@link TypicalPersons} data,
 * backed by a real {@link LogicManager} and storage redirected to a temp directory.
 *
 * <p>Use {@link #submitCommand(String)} and {@link #pressKey(KeyCode)} instead of
 * the TestFX robot methods {@code write()} and {@code type()}, which require
 * OS-level window focus that is not available in Gradle test runs.
 */
public abstract class UiTestBase extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        Path tempDir = Path.of(System.getProperty("java.io.tmpdir"), "campusbridge-uitest");
        StorageManager storage = new StorageManager(
                new JsonAddressBookStorage(tempDir.resolve("addressbook.json")),
                new JsonUserPrefsStorage(tempDir.resolve("prefs.json")));
        Logic logic = new LogicManager(model, storage);
        stage.setHeight(900);
        MainWindow mainWindow = new MainWindow(stage, logic);
        mainWindow.show();
        mainWindow.fillInnerParts();
    }

    /**
     * Submits {@code commandText} by setting the command text field value and firing
     * an action event directly on the JavaFX Application Thread.
     * This avoids the OS window-focus requirement of the TestFX robot's {@code write()}.
     */
    protected void submitCommand(String commandText) {
        interact(() -> {
            TextField field = lookup("#commandTextField").queryAs(TextField.class);
            field.setText(commandText);
            field.fireEvent(new ActionEvent(field, field));
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    /**
     * Fires a {@code KEY_PRESSED} event for {@code key} directly on the command text
     * field on the JavaFX Application Thread.
     * Used for testing command history navigation (UP/DOWN arrows).
     */
    protected void pressKey(KeyCode key) {
        interact(() -> {
            TextField field = lookup("#commandTextField").queryAs(TextField.class);
            field.fireEvent(new KeyEvent(
                    KeyEvent.KEY_PRESSED, "", "", key, false, false, false, false));
        });
        WaitForAsyncUtils.waitForFxEvents();
    }
}
