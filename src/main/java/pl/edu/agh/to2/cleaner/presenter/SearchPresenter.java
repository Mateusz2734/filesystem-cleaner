package pl.edu.agh.to2.cleaner.presenter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;


public class SearchPresenter {

    private Stage stage;
    @FXML
    private TextField pathTextField;
    @FXML
    private Button fileChooser;

    public SearchPresenter() {

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void openFileChooser(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Znajdź plik");

        File chosenFile = fileChooser.showOpenDialog(stage);
        System.out.println(chosenFile.getPath());
    }

}
