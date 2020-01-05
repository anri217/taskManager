import controller.Controller;
import controller.util.IOUtil;
import exceptions.BackupFileException;
import exceptions.PropertyParserInitException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Alert;
import model.Journal;
import model.Task;
import view.MainWindow;

import java.util.List;

/**
 * This is main class, here we start our program
 */

public class Main  {
    public static void main(String[] args) throws ClassNotFoundException, PropertyParserInitException, BackupFileException {
        try {
            IOUtil ioUtil = IOUtil.getInstance();
            Journal journal = (Journal) ioUtil.restoreFunction();
            List<Task> tasks = journal.getAll();
            for (Task task : tasks) {
                Controller.getInstance().addTask(task);
            }
            MainWindow.run(args);
            ioUtil.backupFunction(Controller.getInstance().getJournal());
        } catch (BackupFileException e) {
            showAlert(e.getMessage());
            throw new BackupFileException(e.getMessage());
        }
        catch (PropertyParserInitException e) {
            showAlert(e.getMessage());
            throw new PropertyParserInitException(e.getMessage());
        }
        catch (ClassNotFoundException e) {
            showAlert(e.getMessage());
            throw new ClassNotFoundException(e.getMessage());
        }
    }

    private static void showAlert(String message) {
        new JFXPanel();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);

                alert.setTitle("ALERT");
                alert.setHeaderText(null);
                alert.setContentText(message);

                alert.showAndWait();
            }
        });
    }
}
