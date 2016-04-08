package FPTS.Controllers;

import FPTS.Controls.NumericField;
import FPTS.Core.Controller;
import FPTS.Core.FPTSApp;
import FPTS.Core.Model;
import FPTS.Models.*;
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
    @FXML private TableView<WatchedEquity> holdingsPane;
    @FXML private TableColumn<WatchedEquity, WatchedEquity> holdingActionColumn;
    @FXML private  Text errorMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        holdingActionColumn.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue()));
        holdingActionColumn.setCellFactory(actionColumn -> {
            final Button button = new Button();
            button.setMinWidth(60);
            TableCell<WatchedEquity, WatchedEquity> cell = new TableCell<WatchedEquity, WatchedEquity>() {
                @Override protected void updateItem(final WatchedEquity watchedEquity, boolean empty) {
                    super.updateItem(watchedEquity, empty);
                }
            };

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
        if(_app.getData().getInstanceById(WatchList.class, _portfolio.id).getWatchedEquities() != null) {
            ObservableList<WatchedEquity> watchedEquities = FXCollections.observableArrayList(_app.getData().getInstanceById(WatchList.class, _portfolio.id).getWatchedEquities());
            holdingsPane.setItems(watchedEquities);
        }
    }


    @FXML
    public void handleTimer() {
        System.out.println("Timer Set To " + minutesInterval.getText() + ":" + secondsInterval.getText());
    }
}
