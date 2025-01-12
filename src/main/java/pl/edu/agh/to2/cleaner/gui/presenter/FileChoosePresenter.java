package pl.edu.agh.to2.cleaner.gui.presenter;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import pl.edu.agh.to2.cleaner.gui.AppController;

import java.io.File;
import java.io.IOException;

public class FileChoosePresenter implements Presenter{

    private AppController appController;

    @FXML
    private Button directoryChooseButton;

    @FXML
    private TextField pathTextField;

    @FXML
    private Label pathLabel;

    private ObjectProperty<String> directoryPath = new SimpleObjectProperty<>();

    public FileChoosePresenter() {
    }

    @Override
    public void initialize() {
        this.appController = AppController.getInstance();
        directoryPath.addListener((source, oldValue, newValue) -> {
            pathLabel.setText(newValue);
        });
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
            System.out.println(chosenFile.getAbsolutePath());
        }
    }

    @FXML
    public void goHandle() {
//        try {
//            System.out.println("DZIALA");
////            ResultsPresenter resultsPresenter = new ResultsPresenter();
////            resultsPresenter.setDirectory("WOW");
////            AppController.changeScene("results.fxml");
//            FXMLLoader loader = new FXMLLoader(AppController.class.getClassLoader().getResource("results.fxml"));
//            Parent root = loader.load();
//
//            ResultsPresenter presenter = loader.getController();
//            presenter.setDirectory("WOW");
//
//            System.out.println(presenter.getClass());
//
//            if (presenter.isViewAvailable()) {
//                System.out.println("PREZENTER DA SIE");
//                Scene scene = new Scene(root);
//                appController.setScene(scene);
//            }
//            else {
//                System.out.println("PREZENTER NIE DA SIE");
//            }
//        }
//        catch (IOException e) {
//            System.out.println("NIE DZIALA");
//
//            e.printStackTrace();
//        }
        appController.changeScene("results");
    }
}
