package pl.edu.agh.to2.cleaner.gui.presenter;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import pl.edu.agh.to2.cleaner.gui.AppController;

import java.io.IOException;

public class MainPagePresenter implements Presenter{

    public MainPagePresenter(){}
    @FXML
    public void cleanLabelHandler(MouseEvent event) {
        try {
            AppController.changeScene("file-choose.fxml");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void historyLabelHandler(MouseEvent event) {
        System.out.println("WOW");
        try {
            AppController.changeScene("file-choose.fxml");
        }
        catch (IOException e) {
//            System.out.println("11111111111111111111111111111111111");
            e.printStackTrace();
        }
    }

    @FXML
    public void authorsLabelHandler(MouseEvent event) {

    }

    @Override
    public void initialize() {
    }

    @Override
    public boolean isViewAvailable() {
        return true;
    }


}
