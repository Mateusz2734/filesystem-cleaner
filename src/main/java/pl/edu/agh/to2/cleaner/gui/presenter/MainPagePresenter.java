package pl.edu.agh.to2.cleaner.gui.presenter;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import pl.edu.agh.to2.cleaner.gui.AppController;

import java.io.IOException;

public class MainPagePresenter implements Presenter{

    private AppController appController;

    public MainPagePresenter() {
    }

    public MainPagePresenter(AppController appController){
        this.appController = appController;
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
        System.out.println("WOW");
//        try {
            appController.changeScene("fileChoose");
//        }
//        catch (IOException e) {
////            System.out.println("11111111111111111111111111111111111");
//            e.printStackTrace();
//        }
    }

    @FXML
    public void authorsLabelHandler(MouseEvent event) {

    }

    @Override
    public void initialize() {
        this.appController = AppController.getInstance();
    }

    @Override
    public boolean isViewAvailable() {
        return true;
    }


}
