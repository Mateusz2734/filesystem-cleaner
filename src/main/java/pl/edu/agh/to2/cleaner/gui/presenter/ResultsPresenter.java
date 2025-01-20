package pl.edu.agh.to2.cleaner.gui.presenter;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import pl.edu.agh.to2.cleaner.command.FileFinder;
import pl.edu.agh.to2.cleaner.gui.AppController;
import pl.edu.agh.to2.cleaner.model.FileInfo;
import pl.edu.agh.to2.cleaner.repository.FileInfoRepository;
import pl.edu.agh.to2.cleaner.session.SessionService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ResultsPresenter implements Presenter{

    private SessionService sessionService = new SessionService();
    private FileInfoRepository fileInfoRepository;
    private AppController appController;
//    private ObjectProperty<String> directory = new SimpleObjectProperty<>();
    private List<FileFinder> searchingTypes = new ArrayList<>();
    private ObjectProperty<Path> path = new SimpleObjectProperty<>();

    @FXML
    private Button backButton;

    @FXML
    private Button searchButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button archiveButton;

    @FXML
    private Button moveButton;

    @FXML
    private Button renameButton;

    @FXML
    private Label receivedPath;

    @FXML
    private ListView<HBox> searchListView;


    public ResultsPresenter() {
        this.fileInfoRepository = new FileInfoRepository(sessionService);
    }

    @Override
    public void initialize() {
        this.appController = AppController.getInstance();
        path.addListener((source, oldValue, newValue) -> {
//            if (path.getValue() != null && !path.getValue().isEmpty()) {
            if (validatePath(path.getValue().toString())) {
                receivedPath.setText(path.getValue().toString());
                System.out.println(path.getValue().toString());
            }
            else {
                receivedPath.setText("NO DIRECTORY");
            }
        });



//        for (int i = 0; i < 10; i++) {
//            searchListView.getItems().add(new);
//        }

        // Bindings
//        deleteButton.disableProperty().bind(Bindings.isEmpty(searchPane.getSelec));
//        archiveButton;
//        moveButton;
//        renameButton
    }

    public boolean setDirectory(String stringPath) {

        if (validatePath(stringPath)) {
            Path path = Path.of(stringPath);
            this.path.setValue(path);
            System.out.println(this.path.getValue().toString());
            return true;
        }

        return false;
    }

    public boolean setSearchingTypes(List<FileFinder> givenSearchingTypes) {
        searchingTypes.clear();

        for (FileFinder search : givenSearchingTypes) {
            if (!searchingTypes.contains(search)) {
                searchingTypes.add(search);
            }
        }
        return !searchingTypes.isEmpty();
    }

    @Override
    public boolean isViewAvailable() {
        return path != null;
    }

    @FXML
    public void gobackHandle() {
        appController.changeScene("fileChoose");
    }

    private boolean validatePath(String stringPath) {
        if (stringPath != null) {
            Path path = Path.of(stringPath);
            boolean doesExist = Files.exists(path);
            boolean isDirectory = Files.isDirectory(path);
            boolean isEmpty = path.toString().isEmpty();
//            System.out.println("Ścieżka: " + path + path.toString().isEmpty());

            return doesExist && isDirectory && !isEmpty;
        }
        return false;
    }
}
