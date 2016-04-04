package FPTS.Controllers;

import FPTS.Core.Controller;
import FPTS.PortfolioImporter.HoldingImportHandler;
import FPTS.Views.ImportHoldingsView;
import FPTS.Views.PortfolioView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by traub_000 on 3/25/2016.
 *
 * Description: This controller handles actions on
 * the import holdings page
 */
public class ImportHoldingsController extends Controller{
    @FXML private TableView holdingsPane;
    @FXML private Button importButton;
    @FXML private GridPane grid;
    private ArrayList<ComboBox> duplicateBoxes;
    final FileChooser fileChooser = new FileChooser();
    private GridPane newGrid = new GridPane();

    /**
     * This method stores information about the import file
     * and displays the holdings to be imported
     * @param event unused
     */
    public void fileSelected(ActionEvent event) {
        Path path = Paths.get(fileChooser.showOpenDialog(_app.getStage()).getPath());
        holdingsPane.setItems(HoldingImportHandler.getNewHoldings(path));
        importButton.setVisible(true);

        HoldingImportHandler.determineConflicts(_portfolio);

        boolean[] duplCAIndices = HoldingImportHandler.getDuplCAIndices();

        ComboBox box = new ComboBox();
        box.setVisible(false);
        newGrid.addColumn(0, box);
        for (boolean duplCAIndex : duplCAIndices) {
            box = new ComboBox(HoldingImportHandler.getDuplOpts());
            box.setPrefHeight(18);
            box.setVisible(duplCAIndex);
            newGrid.addColumn(0, box);
        }
        grid.add(newGrid, 6, 2);
    }

    /**
     * This method saves the holdings to the portfolio
     * and determines what to do with duplicate names
     * @param event unused
     */
    public void importHoldings(ActionEvent event){
        // Pass newGrid into below
        HoldingImportHandler.importHoldings(_app, newGrid);
        _app.loadView(new ImportHoldingsView(_app));
    }

    /**
     * Returns the portfolio view
     * @param event unused
     */
    public void returnToPortfolio(ActionEvent event) {
        _app.loadView(new PortfolioView(_app));
    }
}
