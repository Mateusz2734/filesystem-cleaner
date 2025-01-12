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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppController {

    private static AppController instance;
    private Stage stage;

    private Map<String, Presenter> presenters = new HashMap<>();
    private Map<String, Scene> scenes = new HashMap<>();

    public AppController() {
    }

    // Singleton class
    public static AppController getInstance() {
        if (instance == null) {
            instance = new AppController();
        }
        return instance;
    }

    private void loadSceneAndPresenter(String fxmlFile, String key) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlFile));
        Parent root = loader.load();

        scenes.put(key, new Scene(root));
        presenters.put(key, loader.getController());
    }

    public void loadMaps() {
        try {
            loadSceneAndPresenter("main-page.fxml", "mainPage");
            loadSceneAndPresenter("results.fxml", "results");
            loadSceneAndPresenter("file-choose.fxml", "fileChoose");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void start(Stage primaryStage) throws IOException {
//
//        // AtlantaFX theme setup
//        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
//
//        // Starting application
//        this.stage = primaryStage;
//        changeScene("main-page.fxml");
//
//        setStageSettings();
//    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setStageSettings() {
        this.stage.setTitle("Cleaner 2025");
        this.stage.setResizable(false);
        this.stage.setWidth(960);
        this.stage.setHeight(540);
    }
    // Method used by presenters to change between each others

    public void changeScene(String sceneName) {
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

        Scene scene = scenes.get(sceneName);

        System.out.println("HFDJSBFHJVDSJVGFVGSDVGFDSJVFGJDVGS");

        if (scene != null) {
            this.stage.setScene(scene);
        }
    }

    public void start() {
        this.stage.show();

    }
}

