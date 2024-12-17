package pl.edu.agh.to2.cleaner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pl.edu.agh.to2.cleaner.presenter.Presenter;
import pl.edu.agh.to2.cleaner.presenter.SearchPresenter;
import pl.edu.agh.to2.cleaner.presenter.SearchResultPresenter;

import java.io.IOException;

public class App extends Application  {

//    private static StackPane root;

//    public static void switchScenes(String fxmlFile) {
//        try {
//            FXMLLoader loader = new FXMLLoader(App.class.getClassLoader().getResource(fxmlFile));
//            Parent view = loader.load();
//            root.getChildren().setAll(view);
//
//            Presenter presenter = loader.getController();
//
//            presenter.setStage((Stage) root.getScene().getWindow());
//            presenter.initialize();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void start(Stage primaryStage) throws IOException {
//        root = new StackPane();
//        Scene firstScene = new Scene(root);
//
//        switchScenes("search.fxml");

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
