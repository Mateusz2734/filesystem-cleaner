package pl.edu.agh.to2.cleaner.gui.presenter;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import pl.edu.agh.to2.cleaner.gui.AppController;
import pl.edu.agh.to2.cleaner.model.FileInfo;

public class ResultsPresenter implements Presenter{

    private AppController appController;
    private ObjectProperty<String> directory = new SimpleObjectProperty<>();

    @FXML
    private Button fuckgoback;

    @FXML
    private Label receivedPath;

    public ResultsPresenter() {
    }

    @Override
    public void initialize() {
        this.appController = AppController.getInstance();
    }

    public void setDirectory(String directory) {
        this.directory.setValue(directory);
        System.out.println(this.directory.toString());
    }

    @Override
    public boolean isViewAvailable() {
        if (this.directory != null) {
            return true;
        }
        return false;
    }

    @FXML
    public void fuckgobackHandle() {
        appController.changeScene("fileChoose");
    }

    public boolean validatePath(String path) {
        return true;
    }
}
