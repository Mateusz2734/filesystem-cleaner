package pl.edu.agh.to2.cleaner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pl.edu.agh.to2.cleaner.presenter.SearchPresenter;

import java.io.IOException;

public class App extends Application  {
    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("search.fxml"));
        AnchorPane searchView = loader.load();

        SearchPresenter presenter = loader.getController();
        presenter.setStage(primaryStage);

        Scene firstScene = new Scene(searchView);
        primaryStage.setScene(firstScene);
        primaryStage.setTitle("Cleaner");
        primaryStage.show();
    }
}
