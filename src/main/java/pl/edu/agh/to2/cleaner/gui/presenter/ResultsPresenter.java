package pl.edu.agh.to2.cleaner.gui.presenter;

import pl.edu.agh.to2.cleaner.gui.view.ResultsView;
import pl.edu.agh.to2.cleaner.model.FileInfo;

public class ResultsPresenter implements Presenter{
    private FileInfo directory;

    private ResultsView resultsView;

    public ResultsPresenter() {

    }

    public void setDirectory(FileInfo directory) {
        this.directory = directory;
    }
}
