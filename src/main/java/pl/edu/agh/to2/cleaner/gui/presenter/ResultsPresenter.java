package pl.edu.agh.to2.cleaner.gui.presenter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import pl.edu.agh.to2.cleaner.gui.AppController;
import pl.edu.agh.to2.cleaner.model.FileInfo;

import java.io.IOException;

public class ResultsPresenter implements Presenter{
//    private FileInfo directory;

    private String directory;

    @FXML
    private Button fuckgoback;

    public ResultsPresenter() {

    }

    public ResultsPresenter(String directory) {
        this.directory = directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
        System.out.println(this.directory);
    }

    @Override
    public boolean isViewAvailable() {
        if (this.directory != null) {
            return true;
        }
        return false;
    }

    @Override
    public void initialize() {
    }

    @FXML
    public void fuckgobackHandle() {
        try {
            AppController.changeScene("file-choose.fxml");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
