package pl.edu.agh.to2.cleaner.gui.presenter;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import pl.edu.agh.to2.cleaner.command.FileDuplicateFinder;
import pl.edu.agh.to2.cleaner.command.FileFinder;
import pl.edu.agh.to2.cleaner.command.FileTreeIndexer;
import pl.edu.agh.to2.cleaner.command.UnionFind;
import pl.edu.agh.to2.cleaner.gui.AppController;
import pl.edu.agh.to2.cleaner.model.FileInfo;
import pl.edu.agh.to2.cleaner.repository.FileInfoRepository;
import pl.edu.agh.to2.cleaner.session.SessionService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ResultsPresenter implements Presenter{

    private final SessionService sessionService = new SessionService();
    private final FileInfoRepository repository = new FileInfoRepository(sessionService);
    private final FileTreeIndexer indexer = new FileTreeIndexer(repository);

    private AppController appController = new AppController();
    private List<FileFinder> searchingTypes = new ArrayList<>();
    private ObjectProperty<Path> path = new SimpleObjectProperty<>();
    private ObservableList<Set<FileInfo>> searchResultList = FXCollections.observableArrayList();

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
    private ListView<Set<FileInfo>> searchListView;


    public ResultsPresenter() {
    }

    @Override
    public void initialize() {
        this.appController = AppController.getInstance();
        path.addListener((source, oldValue, newValue) -> {
//            if (path.getValue() != null && !path.getValue().isEmpty()) {
//            if (validatePath(path.getValue().toString())) {
                receivedPath.setText(path.getValue().toString());
                System.out.println(path.getValue().toString());
//            }
//            else {
//                receivedPath.setText("NO DIRECTORY");
//            }
        });

        configureListView();
        searchListView.setItems(searchResultList);

        // Bindings
        deleteButton.disableProperty().bind(Bindings.isEmpty(searchListView.getSelectionModel().getSelectedItems()));
        archiveButton.disableProperty().bind(Bindings.isEmpty(searchListView.getSelectionModel().getSelectedItems()));
        moveButton.disableProperty().bind(Bindings.isEmpty(searchListView.getSelectionModel().getSelectedItems()));
        renameButton.disableProperty().bind(Bindings.isEmpty(searchListView.getSelectionModel().getSelectedItems()));
    }

    public boolean setDirectory(String stringPath) {

        if (validatePath(stringPath)) {
            Path path = Path.of(stringPath);
            this.path.setValue(path);
            System.out.println(this.path.getValue().toString() + " was set succesfully as a path!");
            return true;
        }

        System.out.println("Setting directory failed! Invalid path.");
        return false;
    }

    public boolean setSearchingTypes(List<FileFinder> givenSearchingTypes) {
        searchingTypes.clear();

        for (FileFinder search : givenSearchingTypes) {
            if (!searchingTypes.contains(search)) {
                searchingTypes.add(search);
            }
        }

        if (!searchingTypes.isEmpty()) {
            System.out.println("Succesfully added search types:\n" +
                    searchingTypes.stream()
                            .map(fileFinder -> fileFinder.getClass().getSimpleName())
                            .collect(Collectors.joining("\n")));
            return true;
        }

        else {
            System.out.println("None of the search types declared!");
            return false;
        }
    }

    @Override
    public boolean isViewAvailable() {
        return path != null;
    }

    @FXML
    public void goToFileChooser() {
        appController.changeScene("fileChoose");
    }

    private boolean validatePath(String stringPath) {
        if (stringPath != null) {
            Path path = Path.of(stringPath);
            boolean doesExist = Files.exists(path);
            boolean isDirectory = Files.isDirectory(path);
            boolean isEmpty = path.toString().isEmpty();

            return doesExist && isDirectory && !isEmpty;
        }
        return false;
    }

    public void searchForFiles() {
        if (repository.getByPath(path.getValue().toString()).isEmpty()) {
            try {
                indexer.index(path.getValue());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<FileInfo> filesToSearch = repository.getDescendants(path.getValue());

        for (FileFinder fileFinder : searchingTypes) {
            searchResultList.addAll(getFilesInGroups(filesToSearch, fileFinder));
        }

    }

    private List<Set<FileInfo>> getFilesInGroups(List<FileInfo> files, FileFinder fileFinder) {
        fileFinder.setFiles(files);
        var connections = fileFinder.find();
        var groups = new UnionFind().connectedComponentsFromEdges(connections);
        String searchType = fileFinder.getClass().getSimpleName();

        switch (searchType) {
            case "FileDuplicateFinder":
                System.out.println("Duplicates:");
                break;
            case "FileVersionsFinder":
                System.out.println("Versions:");
                break;
        }

        if (groups.isEmpty()) {
            System.out.println("No files found with that criteria");
        }
        else {
            for (var group : groups) {
                System.out.println("Group:");
                for (var file : group) {
                    System.out.println("\t" + file.getPath());
                }
            }
        }

        return groups;
    }

    private void configureListView() {
        searchListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Set<FileInfo> group, boolean empty) {
                super.updateItem(group, empty);

                if (empty || group == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    HBox groupBox = new HBox(10);
                    groupBox.setAlignment(Pos.CENTER_LEFT);

                    for (FileInfo fileInfo : group) {
                        Label fileLabel = new Label(fileInfo.getName());

                        groupBox.getChildren().addAll(fileLabel);
                    }

                    setGraphic(groupBox);
                }
            }
        });
    }
}
