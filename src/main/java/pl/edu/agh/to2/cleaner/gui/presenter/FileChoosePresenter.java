package pl.edu.agh.to2.cleaner.gui.presenter;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import pl.edu.agh.to2.cleaner.command.FileDuplicateFinder;
import pl.edu.agh.to2.cleaner.command.FileFinder;
import pl.edu.agh.to2.cleaner.command.FileVersionsFinder;
import pl.edu.agh.to2.cleaner.gui.AppController;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileChoosePresenter implements Presenter{

    private AppController appController;
    private ObjectProperty<String> directoryPath = new SimpleObjectProperty<>();
    private Map<String, CheckBox> searchTypesCheckboxMap = new HashMap<>();
    private List<FileFinder> searchTypesList = new ArrayList<>();


    @FXML
    private Button directoryChooseButton;

    @FXML
    private Button enterPathButton;

    @FXML
    private Button goSearchButton;

    @FXML
    private TextField pathTextField;

    @FXML
    private VBox checkBoxContainer;

    @FXML
    private Label pathLabel;

    @FXML
    private Label errorLabel;


    public FileChoosePresenter() {
    }

    @Override
    public void initialize() {
//        TODO creating checkboxes depending on functionality
        this.appController = AppController.getInstance();
        loadSearchTypes();

        // LABEL ERROR AS OBSERVER
        directoryPath.addListener((source, oldValue, newValue) -> {
            pathLabel.setText(newValue);
        });
    }

    // MAYBE DI IN THE FUTURE?
    private void loadSearchTypes() {
        List<String> namesList = List.of(
            "duplicate",
            "version"
        );

        for (String name : namesList) {
            searchTypesCheckboxMap.put(name, createCheckBox(name));
        }

        checkBoxContainer.getChildren().addAll(searchTypesCheckboxMap.values());
    }

    private CheckBox createCheckBox(String checkboxName) {
        CheckBox checkBox = new CheckBox(checkboxName + "FindCheckbox");
        checkBox.setMnemonicParsing(false);
        checkBox.setText(checkboxName.substring(0, 1).toUpperCase() + checkboxName.substring(1) + " search");
        return checkBox;
    }

    @Override
    public boolean isViewAvailable() {
        return true;
    }

    @FXML
    public void chooseDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Find destination");

        File chosenFile = directoryChooser.showDialog(new Stage());

        if (chosenFile != null) {
            directoryPath.set(chosenFile.getAbsolutePath());
        }
    }

    @FXML
    public void enterPath() {
        directoryPath.setValue(pathTextField.getText());
    }

    @FXML
    public void goSearch() {
        createFindersByCheckboxes();
        errorLabel.setText("");

//        if (searchTypesList.isEmpty()) {
//            errorLabel.setText("CHECK THE SEARCH TYPE");
//        }
        if (!appController.passSearchInfo(directoryPath.get(), searchTypesList)) {
            errorLabel.setText("Invalid input!");
        }
        else {
            appController.searchFiles();
            appController.changeScene("results");
        }
    }

    public void createFindersByCheckboxes() {
        searchTypesList.clear();
        CheckBox selectedCheckBox;
        String key;

        for (Map.Entry<String, CheckBox> entry : searchTypesCheckboxMap.entrySet()) {
            key = entry.getKey();
            selectedCheckBox = entry.getValue();

            if (selectedCheckBox.isSelected()) {
                switch (key) {
                    case "duplicate":
                        searchTypesList.add(new FileDuplicateFinder());
                        break;
                    case "version":
                        searchTypesList.add(new FileVersionsFinder());
                        break;
                }
            }

        }
//        if (searchTypesCheckboxMap.containsKey("duplicate")) {
//            selectedCheckBox = searchTypesCheckboxMap.get("duplicate");
//            if selectedCheckBox
//                searchTypesList.add(new FileDuplicateFinder());
//        }
//        else if (searchTypesCheckboxMap.containsKey("version")) {
//            searchTypesList.add(new FileVersionsFinder());
//        }
//        else {
//            searchTypesList.clear();
//        }
    }
}
