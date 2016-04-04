package FPTS.Controllers;

import FPTS.Controls.NumericField;
import FPTS.Core.Controller;
import FPTS.Core.FPTSApp;
import FPTS.Core.Model;
import FPTS.Models.CashAccount;
import FPTS.Models.Holding;
import FPTS.Models.Portfolio;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author: Alexander Kidd
 * Created: 4/4/2016
 * Revised: 4/4/2016
 * Description: Facilitates in communication
 * between the watch list logic and the watch list UI View.
 */
public class WatchListController extends Controller {
    @FXML private Text portfolioName;
    @FXML private NumericField minutesInterval;
    @FXML private NumericField secondsInterval;
    @FXML private TableView<Holding> holdingsPane;
    @FXML private TableColumn<Holding, Holding> holdingActionColumn;
    @FXML private  Text errorMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        holdingActionColumn.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue()));
        holdingActionColumn.setCellFactory(actionColumn -> {
            final Button button = new Button();
            button.setMinWidth(60);
            TableCell<Holding, Holding> cell = new TableCell<Holding, Holding>() {
                @Override protected void updateItem(final Holding holding, boolean empty) {
                    super.updateItem(holding, empty);

                    if(!empty && holding != null && holding.getClass() == CashAccount.class) {
                        button.setText("Delete");
                        setGraphic(button);
                    }
                    else {
                        setGraphic(null);
                    }
                }
            };

            button.setOnAction((e) -> {
                System.out.println("Delete CA " + cell.getItem().getName());
                Model instance = Model.class.cast(cell.getItem());
                _portfolio.removeHolding(cell.getItem());
                instance.delete();
                _portfolio.save();
            });

            return cell;
        });
    }


    @Override
    public void Load(FPTSApp app, Portfolio portfolio) {
        super.Load(app, portfolio);
        refreshView();
    }

    @Override
    public void refreshView() {
        portfolioName.setText(String.format("%1$s's Watch List", _portfolio.getUsername()));
        ObservableList<Holding> holdings = FXCollections.observableArrayList(_portfolio.getHoldings());
        holdingsPane.setItems(holdings);
    }


    @FXML
    public void handleTimer() {
        System.out.println("Timer Set To " + minutesInterval.getText() + ":" + secondsInterval.getText());
    }
}
