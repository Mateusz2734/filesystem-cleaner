package pl.edu.agh.to2.cleaner.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.edu.agh.to2.cleaner.gui.presenter.Presenter;
import pl.edu.agh.to2.cleaner.gui.presenter.ResultsPresenter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AppController {

    private static AppController instance;
    private Stage stage;

    private Map<String, Presenter> presenters = new HashMap<>();
    private Map<String, Scene> scenes = new HashMap<>();

    public AppController() {
    }

    // Singleton getter
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

        Presenter presenter = presenters.get(sceneName);
        Scene scene = scenes.get(sceneName);

        // if false no such key in scenes
        if (scene != null) {
            if (presenter.isViewAvailable()) {
                this.stage.setScene(scene);
            }
        }
    }

    public void start() {
        this.stage.show();
    }

    public boolean passDirectory(String directory) {
        Presenter presenter = presenters.get("results");

        if (presenter.getClass() == ResultsPresenter.class) {
            presenter = ((ResultsPresenter) presenter);
            if (((ResultsPresenter) presenter).validatePath(directory)) {
                ((ResultsPresenter) presenter).setDirectory(directory);
                return true;
            }
        }
        return false;
    }
}

