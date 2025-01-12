package pl.edu.agh.to2.cleaner;

import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.stage.Stage;
import pl.edu.agh.to2.cleaner.gui.AppController;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());

        AppController appController = AppController.getInstance();
        appController.loadMaps();
        appController.setStage(primaryStage);
        appController.setStageSettings();


        System.out.println("HDSFBJVDSG");


        appController.changeScene("mainPage");
        appController.start();
    }
}
