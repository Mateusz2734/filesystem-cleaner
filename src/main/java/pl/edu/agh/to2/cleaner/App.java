package pl.edu.agh.to2.cleaner;

import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.stage.Stage;
import pl.edu.agh.to2.cleaner.gui.AppController;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {

        // AtlantaFX theme setup
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());

        // Setting main controller
        AppController appController = AppController.getInstance();
        appController.loadMaps();
        appController.setStage(primaryStage);
        appController.setStageSettings();

        // First scene
        appController.changeScene("mainPage");
        appController.start();
    }
}
