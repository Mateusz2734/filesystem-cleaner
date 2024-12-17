package pl.edu.agh.to2.cleaner.presenter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import pl.edu.agh.to2.cleaner.command.FileDuplicateFinder;
import pl.edu.agh.to2.cleaner.command.FileVersionsFinder;
import pl.edu.agh.to2.cleaner.command.UnionFind;
import pl.edu.agh.to2.cleaner.model.FileInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class SearchResultPresenter implements Presenter{

    private Stage stage;

    private String searchDirectory;


    @FXML
    private TableView<FileInfo> resultsTable;

    @FXML
    private TableColumn<FileInfo, String> nameColumn;

    @FXML
    private TableColumn<FileInfo, String> pathColumn;

    @FXML
    private TableColumn<FileInfo, Long> sizeColumn;

    @FXML
    private TableColumn<FileInfo, Long> modificationTimeColumn;

    @FXML
    private TableColumn<FileInfo, Long> creationTimeColumn;

    private final ObservableList<FileInfo> tableData = FXCollections.observableArrayList();


    public SearchResultPresenter() {};

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setDirectory(String directory) {
        this.searchDirectory = directory;
    }

    public void searchInDirectory() throws IOException {
//        tableData.clear();
//
//        var files = Files.walk(Path.of(searchDirectory))
//                .filter(Files::isRegularFile)
//                .map(path -> {
//                    try {
//                        return new FileInfo(path.toFile());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        return null;
//                    }
//                })
//                .filter(Objects::nonNull)
//                .toList();
//
//        var versionConnections = new FileVersionsFinder(files).find();
//        var versionGroups = new UnionFind().connectedComponentsFromEdges(versionConnections);
//
//        System.out.println("Versions (similar name):");
//        for (var group : versionGroups) {
//            System.out.println("Group:");
//            for (var file : group) {
//                System.out.println("\t" + file.getPath());
//                if (!tableData.contains(file)) {
//                    tableData.add(file);
//                }
//            }
//        }
//
//        var duplicateConnections = new FileDuplicateFinder(files).find();
//        var duplicateGroups = new UnionFind().connectedComponentsFromEdges(duplicateConnections);
//
//        System.out.println("Duplicates (same size and name):");
//        for (var group : duplicateGroups) {
//            System.out.println("Group:");
//            for (var file : group) {
//                System.out.println("\t" + file.getPath());
//                if (!tableData.contains(file)) {
//                    tableData.add(file);
//                }
//            }
//        }
    }
}
