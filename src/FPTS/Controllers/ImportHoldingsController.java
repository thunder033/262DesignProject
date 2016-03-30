package FPTS.Controllers;

import FPTS.Core.Controller;
import FPTS.Core.Model;
import FPTS.Models.Holding;
import FPTS.Models.Portfolio;
import FPTS.PortfolioImporter.CSVImporter;
import FPTS.PortfolioImporter.CustomComboBoxFactory;
import FPTS.PortfolioImporter.Importer;
import FPTS.Views.AddHoldingView;
import FPTS.Views.PortfolioView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by traub_000 on 3/25/2016.
 *
 * Description: This controller handles actions on
 * the import holdings page
 */
public class ImportHoldingsController extends Controller{
    @FXML private TableView holdingsPane;
    @FXML private Button importButton;
    @FXML private javafx.scene.control.TableColumn duplicateCol;
    final FileChooser fileChooser = new FileChooser();

    /**
     * This method stores information about the import file
     * and displays the holdings to be imported
     * @param event unused
     */
    public void fileSelected(ActionEvent event) {
        Path path = Paths.get(fileChooser.showOpenDialog(_app.getStage()).getPath());
        Importer importer = new Importer(path);
        importer.setStrategy(new CSVImporter());
        ObservableList<Holding> holdings = FXCollections.observableArrayList(importer.importData().getHoldings());
        holdingsPane.setItems(holdings);

//        String[] choices = {"Merge", "Replace", "Ignore"};
//        ObservableList items = FXCollections.observableArrayList(choices);
//        CustomComboBoxFactory<Portfolio, String> cellFactory = new CustomComboBoxFactory<>();
//        duplicateCol.setEditable(true);
//        duplicateCol.setCellValueFactory(new PropertyValueFactory<>("value"));
//        duplicateCol.setCellFactory(ComboBoxTableCell.<Portfolio, String>forTableColumn(items));


        Map<String, String> holdingNames = new HashMap<>();
        for(Holding hold: _portfolio.getHoldings()){
            holdingNames.put(hold.getName(),hold.getType());
        }
        for(Holding newHolding: holdings) {
            String newName = newHolding.getName();
            if(holdingNames.containsKey(newName) && holdingNames.get(newName).equals(newHolding.getType())){

            }
        }
        importButton.setVisible(true);
//        importer.importData().getHoldings().stream().forEach(this::addHolding);
//        _portfolio.save();
    }

    private void addHolding(Holding holding){
        _portfolio.addHolding(holding);
        _app.getData().addInstance(Model.class.cast(holding));
    }

    /**
     * This method saves the holdings to the portfolio
     * and determines what to do with duplicate names
     * @param event unused
     */
    public void importHoldings(ActionEvent event){
        
    }

    /**
     * Returns the portfolio view
     * @param event unused
     */
    public void returnToPortfolio(ActionEvent event) {
        _app.loadView(new PortfolioView(_app));
    }
}
