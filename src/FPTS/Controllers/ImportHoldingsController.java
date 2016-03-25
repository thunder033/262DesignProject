package FPTS.Controllers;

import FPTS.Core.Controller;
import FPTS.Core.Model;
import FPTS.Models.Holding;
import FPTS.PortfolioImporter.CSVImporter;
import FPTS.PortfolioImporter.Importer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by traub_000 on 3/25/2016.
 */
public class ImportHoldingsController extends Controller{
    @FXML private TableView holdingsPane;
    @FXML private Button importButton;
    final FileChooser fileChooser = new FileChooser();

    public void fileSelected(ActionEvent event) {
        Path path = Paths.get(fileChooser.showOpenDialog(_app.getStage()).getPath());
        Importer importer = new Importer(path);
        importer.setStrategy(new CSVImporter());
        ObservableList<Holding> holdings = FXCollections.observableArrayList(importer.importData().getHoldings());
        holdingsPane.setItems(holdings);
        importButton.setVisible(true);
//        importer.importData().getHoldings().stream().forEach(this::addHolding);
//        _portfolio.save();
    }

    private void addHolding(Holding holding){
        _portfolio.addHolding(holding);
        _app.getData().addInstance(Model.class.cast(holding));
    }

    public void importHoldings(ActionEvent event){
        
    }
}
