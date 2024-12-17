package pl.edu.agh.to2.cleaner.presenter;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SearchResultPresenter implements Presenter{

    private Stage stage;

    private String searchDirectory;

    @FXML
    private Label result;

    public SearchResultPresenter() {};

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setDirectory(String directory) {
        this.searchDirectory = directory;
    }

    public void searchInDirectory() {

    }
}
