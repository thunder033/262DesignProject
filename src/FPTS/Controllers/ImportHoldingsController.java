package FPTS.Controllers;

import FPTS.Core.Controller;
import FPTS.PortfolioImporter.HoldingImportHandler;
import FPTS.PortfolioImporter.ImportItem;
import FPTS.PortfolioImporter.InvalidChoiceException;
import FPTS.Views.ImportHoldingsView;
import FPTS.Views.PortfolioView;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by traub_000 on 3/25/2016.
 *
 * Description: This controller handles actions on
 * the import holdings page
 */
public class ImportHoldingsController extends Controller{
    @FXML private TableView<ImportItem> holdingsPane;
    @FXML private Button importButton;
    @FXML private GridPane grid;
    @FXML private Label CAerrorMessage;
    @FXML private TextField newCAName;
    @FXML private TextField newCAValue;
    @FXML private Label choiceError;

    @FXML private TableColumn<ImportItem, ImportItem> actionColumn;

    final FileChooser fileChooser = new FileChooser();
    private GridPane newGrid = new GridPane();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        actionColumn.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue()));
        actionColumn.setCellFactory(actionColumn -> {
            ObservableList<ImportItem.Action> actions = FXCollections.observableArrayList(ImportItem.Action.values());
            final ComboBox<ImportItem.Action> comboBox = new ComboBox<>(actions);
            comboBox.setMinWidth(60);

            TableCell<ImportItem, ImportItem> cell = new TableCell<ImportItem, ImportItem>() {
                @Override protected void updateItem(final ImportItem importItem, boolean empty) {
                    super.updateItem(importItem, empty);
                    if(importItem == null){
                        return;
                    }

                    if(importItem.isConflicted()){
                        comboBox.setValue(importItem.getAction());
                        setGraphic(comboBox);
                    } else {
                        setGraphic(new Text(importItem.getAction().toString()));
                    }
                }
            };

            comboBox.valueProperty().addListener((observable, oldAction, action) -> {
                if(action == null){
                    return;
                }

                cell.getItem().setAction(action);
                holdingsPane.refresh();
            });

            return cell;
        });
    }

    /**
     * This method stores information about the import file
     * and displays the holdings to be imported
     * @param event unused
     */
    public void fileSelected(ActionEvent event) {
        File file = fileChooser.showOpenDialog(_app.getStage());
        if(file != null){
            Path path = Paths.get(file.getPath());
            holdingsPane.setItems(HoldingImportHandler.getNewHoldings(path, _portfolio));
            showHoldings();
        }
    }

    public void addCashAccount(ActionEvent event) {

        try {
            holdingsPane.setItems(HoldingImportHandler.getNewHoldings(newCAName.getText(), newCAValue.getText()));
            showHoldings();
        }catch (NumberFormatException e){
            CAerrorMessage.setText("Invalid value for new cash account.");
        }catch (IllegalArgumentException e) {
            CAerrorMessage.setText(e.getMessage());
        }
    }

    private void showHoldings(){
        importButton.setVisible(true);
        HoldingImportHandler.determineConflicts(_portfolio);
    }

    /**
     * This method saves the holdings to the portfolio
     * and determines what to do with duplicate names
     * @param event unused
     */
    public void importHoldings(ActionEvent event){
        // Pass newGrid into below
        try {
            ArrayList<ImportItem> items = holdingsPane.getItems().stream()
                    .map(actionColumn::getCellObservableValue)
                    .map(ObservableValue::getValue)
                    .collect(Collectors.toCollection(ArrayList::new));

            HoldingImportHandler.importHoldings(items);
            _app.loadView(new ImportHoldingsView(_app));
        } catch (InvalidChoiceException e) {
            choiceError.setText("Invalid choice for " + e.getMessage());
        }
    }

    /**
     * Returns the portfolio view
     * @param event unused
     */
    public void returnToPortfolio(ActionEvent event) {
        _app.loadView(new PortfolioView(_app));
    }


}
