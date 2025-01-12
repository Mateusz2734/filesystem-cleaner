package pl.edu.agh.to2.cleaner.gui;

import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import pl.edu.agh.to2.cleaner.gui.presenter.FileChoosePresenter;
import pl.edu.agh.to2.cleaner.gui.presenter.MainPagePresenter;
import pl.edu.agh.to2.cleaner.gui.presenter.Presenter;
import pl.edu.agh.to2.cleaner.gui.presenter.ResultsPresenter;

import java.io.IOException;

public class AppController extends Application {

    private static Stage stage;

    private static FileChoosePresenter fileChoosePresenter;

    private static MainPagePresenter mainPagePresenter;

    private static ResultsPresenter resultsPresenter;

    public AppController() {
        this.fileChoosePresenter = new FileChoosePresenter();
        this.mainPagePresenter = new MainPagePresenter();
        this.resultsPresenter = new ResultsPresenter();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        // AtlantaFX theme setup
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());

        // Starting application
        AppController.stage = primaryStage;
        changeScene("main-page.fxml");

        stageSettings();
        AppController.stage.show();
    }

    // Method used by presenters to change between each others
    public static void changeScene(String sceneName) throws IOException {
//        FXMLLoader loader = new FXMLLoader(AppController.class.getClassLoader().getResource(sceneName));
//        Parent root = loader.load();
//
//        Presenter presenter = loader.getController();
//
//        System.out.println(presenter.getClass());
//
//        if (presenter.isViewAvailable()) {
//            System.out.println("PREZENTER DA SIE");
//            Scene scene = new Scene(root);
//            stage.setScene(scene);
//        }
//        else {
//            System.out.println("PREZENTER NIE DA SIE");
//        }

        FXMLLoader loader = new FXMLLoader(AppController.class.getClassLoader().getResource(sceneName));
        Parent root = loader.load();

        Presenter presenter;
        switch (sceneName) {
            case "main-page.fxml":
                presenter = mainPagePresenter;

        }
    }

    public static void setScene(Scene scene) {
        AppController.stage.setScene(scene);
    }

    private void stageSettings() {
        AppController.stage.setTitle("Cleaner 2025");
        AppController.stage.setResizable(false);
        AppController.stage.setWidth(960);
        AppController.stage.setHeight(540);
    }

//POMYSŁY NA DEKOMPOZYCJE
}

