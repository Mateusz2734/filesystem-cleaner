package pl.edu.agh.to2.cleaner.gui.presenter;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import pl.edu.agh.to2.cleaner.gui.AppController;

public class MainPagePresenter implements Presenter{

    private AppController appController;

    public MainPagePresenter() {
    }

    @Override
    public void initialize() {
        this.appController = AppController.getInstance();
    }

    @Override
    public boolean isViewAvailable() {
        return true;
    }

    @FXML
    public void cleanLabelHandler(MouseEvent event) {
//        try {
            appController.changeScene("file-choose.fxml");
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
    }


    @FXML
    public void historyLabelHandler(MouseEvent event) {
        appController.changeScene("fileChoose");
    }

    @FXML
    public void authorsLabelHandler(MouseEvent event) {

    }


}
