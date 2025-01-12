package pl.edu.agh.to2.cleaner.gui;

import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.edu.agh.to2.cleaner.gui.presenter.FileChoosePresenter;
import pl.edu.agh.to2.cleaner.gui.presenter.MainPagePresenter;
import pl.edu.agh.to2.cleaner.gui.presenter.Presenter;
import pl.edu.agh.to2.cleaner.gui.presenter.ResultsPresenter;

import java.io.IOException;

public class AppController extends Application {

    private static Stage stage;

    private FileChoosePresenter fileChoosePresenter;

    private MainPagePresenter mainPagePresenter;

    private ResultsPresenter resultsPresenter;

    public AppController() {
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        // AtlantaFX theme setup
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());

        // Presenters initialization
        this.fileChoosePresenter = new FileChoosePresenter();
        this.mainPagePresenter = new MainPagePresenter();
        this.resultsPresenter = new ResultsPresenter();

        // Starting application
        changeScene("main-page.fxml");
//        FXMLLoader loader = new FXMLLoader(AppController.class.getClassLoader().getResource("main-page.fxlm"));
//        Parent root = loader.load();
//        Scene scene = new Scene(root);
//        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void changeScene(String sceneName) throws IOException {
        FXMLLoader loader = new FXMLLoader(AppController.class.getClassLoader().getResource(sceneName));
        Parent root = loader.load();

        Presenter presenter = loader.getController();

        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}
