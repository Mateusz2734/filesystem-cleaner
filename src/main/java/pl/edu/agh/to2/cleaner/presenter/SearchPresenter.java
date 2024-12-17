package pl.edu.agh.to2.cleaner.presenter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.edu.agh.to2.cleaner.command.FileDuplicateFinder;
import pl.edu.agh.to2.cleaner.command.FileVersionsFinder;
import pl.edu.agh.to2.cleaner.command.UnionFind;
import pl.edu.agh.to2.cleaner.model.FileInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;


public class SearchPresenter implements Presenter {

    private Stage stage;

    @FXML
    private Label errorLabel;
    
    @FXML
    private TextField pathTextField;

    @FXML
    private CheckBox versionCheckbox;

    @FXML
    private CheckBox duplicateCheckbox;

    @FXML
    private Button fileChooser;

    @FXML
    private Button searchFiles;

    public SearchPresenter() {

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void openFileChooser(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Find destination");

//        File startingDirectory = new File(System.getProperty("user.home"));
//        if (startingDirectory.exists()) {
//            System.out.println("LOLO");
//            fileChooser.setInitialDirectory(startingDirectory);
//        }

        File chosenFile = directoryChooser.showDialog(stage);

        if (chosenFile != null) {
            pathTextField.setText(chosenFile.getAbsolutePath());
            System.out.println(chosenFile.getAbsolutePath());
        }
    }

    @FXML
    public void enterDirectory(ActionEvent event) throws IOException {
        var dir = pathTextField.getText();

        if (dir.isEmpty()) {
            errorLabel.setText("Enter the proper destination");
        }
        else if (!versionCheckbox.isSelected() && !duplicateCheckbox.isSelected()) {
            errorLabel.setText("Choose variant of search");
        }
        else {
            errorLabel.setText("");
//            var files = Files.walk(Path.of(dir))
//                    .filter(Files::isRegularFile)
//                    .map(path -> {
//                        try {
//                            return new FileInfo(path.toFile());
//                        } catch (Exception e) {
//                            return null;
//                        }
//                    })
//                    .filter(Objects::nonNull)
//                    .toList();
//
//            {
//                var connections = new FileVersionsFinder(files).find();
//                var groups = new UnionFind().connectedComponentsFromEdges(connections);
//
//                System.out.println("Versions (similar name):");
//                for (var group : groups) {
//                    System.out.println("Group:");
//                    for (var file : group) {
//                        System.out.println("\t" +file.getPath());
//                    }
//                }
//            }
//
//            System.out.println("\n");
//
//            {
//                var connections = new FileDuplicateFinder(files).find();
//                var groups = new UnionFind().connectedComponentsFromEdges(connections);
//
//                System.out.println("Duplicates (same size and name):");
//                for (var group : groups) {
//                    System.out.println("Group:");
//                    for (var file : group) {
//                        System.out.println("\t" + file.getPath());
//                    }
//                }
//            }
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("searchResult.fxml"));
                Parent searchResult = loader.load();

                SearchResultPresenter presenter = loader.getController();

                presenter.setStage(stage);
                presenter.setDirectory(dir);
                presenter.searchInDirectory();

                stage.setScene(new Scene(searchResult));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}
