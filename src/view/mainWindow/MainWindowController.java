package view.mainWindow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Status;
import model.Task;
import view.addTaskWindow.AddTaskWindow;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {
    public Button addTask;
    public Button delTask;
    public Button changeTask;

    public TableView<MainWindowRow> taskTable;

    public TableColumn<MainWindowRow, CheckBox> chooseColumn;
    public TableColumn<MainWindowRow, String> nameColumn;
    public TableColumn<MainWindowRow, String> descriptionColumn;
    public TableColumn<MainWindowRow, String> dateColumn;

    public MenuItem saveJournal;
    public MenuItem downloadJournal;

    private ArrayList<MainWindowRow> rows = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initColumns();
    }

    private void initColumns() {
        chooseColumn.setCellValueFactory(new PropertyValueFactory<>("checkBox"));

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    private void selectedCheckBox(){
        int count = 0;

        for (MainWindowRow row : rows) {
            if (row.getCheckBox().isSelected()) {
                ++count;
            }
        }

        if (count == 0){
            delTask.setDisable(true);
            changeTask.setDisable(true);
        }

        if(count == 1){
            delTask.setDisable(false);
            changeTask.setDisable(false);
        }

        if(count > 1){
            delTask.setDisable(false);
            changeTask.setDisable(true);
        }
    }

    public void addTask(Task task){
        MainWindowRow row = new MainWindowRow(task);
        rows.add(row);
        row.getCheckBox().setOnAction(actionEvent1 -> selectedCheckBox());
        taskTable.getItems().add(row);
    }

    @FXML
    public void clickAddTask(ActionEvent actionEvent) throws Exception {
        AddTaskWindow addTaskWindow = new AddTaskWindow();
        addTaskWindow.start(new Stage());
    }

    @FXML
    public void clickDelTask(ActionEvent actionEvent) {
        for (int i = 0; i < rows.size(); i++) {
            if(rows.get(i).getCheckBox().isSelected()){
                taskTable.getItems().remove(i);
                rows.remove(i);
                i--;
            }
        }
        selectedCheckBox();
    }
}