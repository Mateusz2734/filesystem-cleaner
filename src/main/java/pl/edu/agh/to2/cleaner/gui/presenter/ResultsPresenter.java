package pl.edu.agh.to2.cleaner.gui.presenter;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import pl.edu.agh.to2.cleaner.command.FileFinder;
import pl.edu.agh.to2.cleaner.gui.AppController;
import pl.edu.agh.to2.cleaner.model.FileInfo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ResultsPresenter implements Presenter{

    private AppController appController;
    private ObjectProperty<String> directory = new SimpleObjectProperty<>();
    private List<FileFinder> searchingTypes = new ArrayList<>();

    @FXML
    private Button backButton;

    @FXML
    private Label receivedPath;

    public ResultsPresenter() {
    }

    @Override
    public void initialize() {
        this.appController = AppController.getInstance();
        directory.addListener((source, oldValue, newValue) -> {
            if (directory.getValue() != null && !directory.getValue().isEmpty()) {
                receivedPath.setText(directory.getValue());
                System.out.println(directory.getValue());
            }
            else {
                receivedPath.setText("NO DIRECTORY");
            }
        });
    }

    public boolean setDirectory(String directory) {
        if (validatePath(directory)) {
            this.directory.setValue(directory);
            System.out.println(this.directory.toString());
            return true;
        }
        return false;
    }

    public boolean setSearchingTypes(List<FileFinder> searchingTypes) {
        for (FileFinder search : searchingTypes) {
            if (!searchingTypes.contains(search)) {
                searchingTypes.add(search);
            }
        }
        return !searchingTypes.isEmpty();
    }

    @Override
    public boolean isViewAvailable() {
        if (this.directory != null) {
            return true;
        }
        return false;
    }

    @FXML
    public void gobackHandle() {
        appController.changeScene("fileChoose");
    }

    public boolean validatePath(String path) {
        if (path != null) {
            Path directoryPath = Paths.get(path);
            boolean doesExist = Files.exists(directoryPath);
            boolean isDirectory = Files.isDirectory(directoryPath);

            return doesExist && isDirectory;
        }
        return false;
    }
}
