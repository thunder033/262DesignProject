package FPTS.Controllers;

import FPTS.Controls.NumericField;
import FPTS.Core.Controller;
import FPTS.Core.FPTSApp;
import FPTS.Models.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * @author: Alexander Kidd
 * Created: 4/4/2016
 * Revised: 4/10/2016
 * Description: Facilitates in communication
 * between the watch list logic and the watch list UI View.
 */
public class WatchListController extends Controller {
    @FXML private Text portfolioName;

    @FXML private NumericField minutesInterval;
    @FXML private NumericField secondsInterval;

    @FXML private TableView<WatchedEquity> holdingsPane;
    @FXML private TableColumn<WatchedEquity, WatchedEquity> priceTriggerColumn;
    @FXML private TableColumn<WatchedEquity, WatchedEquity> nameColumn;
    @FXML private TableColumn<WatchedEquity, WatchedEquity> symbolColumn;
    @FXML private TableColumn<WatchedEquity, WatchedEquity> priceColumn;
    @FXML private TableColumn<WatchedEquity, WatchedEquity> lowTriggerColumn;
    @FXML private TableColumn<WatchedEquity, WatchedEquity> highTriggerColumn;
    @FXML private TableColumn<WatchedEquity, WatchedEquity> setTriggersColumn;
    @FXML private TableColumn<WatchedEquity, WatchedEquity> deleteHoldingColumn;

    @FXML private Text errorMessage;

    @FXML private TextField newEquitySymbol;
    @FXML private Button submitNewWatchEquity;

    ArrayList<TextField> lowTriggerFields = new ArrayList<>();
    ArrayList<TextField> highTriggerFields = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        for(int i = 0; i < _app.getData().getInstanceById(WatchList.class, _portfolio.id).getWatchedEquities().size(); i++) {
            lowTriggerFields.add(new TextField());
        }

        for(int j = 0; j < _app.getData().getInstanceById(WatchList.class, _portfolio.id).getWatchedEquities().size(); j++) {
            highTriggerFields.add(new TextField());
        }

        priceTriggerColumn.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue()));
        nameColumn.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue()));
        symbolColumn.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue()));
        priceColumn.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue()));
        lowTriggerColumn.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue()));
        highTriggerColumn.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue()));
        setTriggersColumn.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue()));
        deleteHoldingColumn.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue()));

        lowTriggerColumn.setCellFactory(priceColumn -> {
            TableCell<WatchedEquity, WatchedEquity> cell = new TableCell<WatchedEquity, WatchedEquity>() {
                @Override protected void updateItem(final WatchedEquity watchedEquity, boolean empty) {
                    super.updateItem(watchedEquity, empty);
                    if(watchedEquity != null && !empty) {
                        int rowIndex = getTableRow().getIndex();
                        lowTriggerFields.get(rowIndex).setText(Float.toString(watchedEquity.getLowerTrigger()));
                        lowTriggerFields.get(rowIndex).setMinWidth(130);
                        setGraphic(lowTriggerFields.get(rowIndex));
                    }
                }
            };

            return cell;
        });

        highTriggerColumn.setCellFactory(priceColumn -> {
            TableCell<WatchedEquity, WatchedEquity> cell = new TableCell<WatchedEquity, WatchedEquity>() {
                @Override protected void updateItem(final WatchedEquity watchedEquity, boolean empty) {
                    super.updateItem(watchedEquity, empty);
                    if(watchedEquity != null && !empty) {
                        int rowIndex = getTableRow().getIndex();
                        highTriggerFields.get(rowIndex).setText(Float.toString(watchedEquity.getUpperTrigger()));
                        highTriggerFields.get(rowIndex).setMinWidth(130);
                        setGraphic(highTriggerFields.get(rowIndex));
                    }
                }
            };

            return cell;
        });

        priceColumn.setCellFactory(priceColumn -> {
            TableCell<WatchedEquity, WatchedEquity> cell = new TableCell<WatchedEquity, WatchedEquity>() {
                @Override protected void updateItem(final WatchedEquity watchedEquity, boolean empty) {
                    super.updateItem(watchedEquity, empty);
                    if(watchedEquity != null && !empty) {
                        setText(Float.toString(watchedEquity.getEquity().getSharePrice()));
                    }
                }
            };

            return cell;
        });

        symbolColumn.setCellFactory(symbolColumn -> {
            TableCell<WatchedEquity, WatchedEquity> cell = new TableCell<WatchedEquity, WatchedEquity>() {
                @Override protected void updateItem(final WatchedEquity watchedEquity, boolean empty) {
                    super.updateItem(watchedEquity, empty);
                    if(watchedEquity != null && !empty) {
                        setText(watchedEquity.getEquity().getTickerSymbol());
                    }
                }
            };

            return cell;
        });

        nameColumn.setCellFactory(nameColumn -> {
            TableCell<WatchedEquity, WatchedEquity> cell = new TableCell<WatchedEquity, WatchedEquity>() {
                @Override protected void updateItem(final WatchedEquity watchedEquity, boolean empty) {
                    super.updateItem(watchedEquity, empty);
                    if(watchedEquity != null && !empty) {
                        setText(watchedEquity.getEquity().getName());
                    }
                }
            };

            return cell;
        });

        priceTriggerColumn.setCellFactory(priceTriggerColumn -> {
            TableCell<WatchedEquity, WatchedEquity> cell = new TableCell<WatchedEquity, WatchedEquity>() {
                @Override protected void updateItem(final WatchedEquity watchedEquity, boolean empty) {
                    super.updateItem(watchedEquity, empty);
                    if(watchedEquity != null && !empty) {
                        switch(watchedEquity.getTriggerState()) {
                            case PREVIOUSLY_ABOVE:
                                setGraphic(new ImageView(new Image(this.getClass().getResourceAsStream("/assets/upArrow.png"))));
                                break;
                            case CURRENTLY_ABOVE:
                                setGraphic(new ImageView(new Image(this.getClass().getResourceAsStream("/assets/upArrowTriggered.png"))));
                                break;
                            case NONE:
                                setGraphic(new ImageView(new Image(this.getClass().getResourceAsStream("/assets/samePriceIcon.png"))));
                                break;
                            case CURRENTLY_BELOW:
                                setGraphic(new ImageView(new Image(this.getClass().getResourceAsStream("/assets/downArrowTriggered.png"))));
                                break;
                            case PREVIOUSLY_BELOW:
                                setGraphic(new ImageView(new Image(this.getClass().getResourceAsStream("/assets/downArrow.png"))));
                                break;
                            default:
                                setGraphic(new ImageView(new Image(this.getClass().getResourceAsStream("/assets/samePriceIcon.png"))));
                                break;
                        }
                    }
                }
            };

            return cell;
        });

        setTriggersColumn.setCellFactory(setTriggersColumn -> {
            final Button button = new Button();
            button.setMinWidth(190);
            TableCell<WatchedEquity, WatchedEquity> cell = new TableCell<WatchedEquity, WatchedEquity>() {
                @Override protected void updateItem(final WatchedEquity watchedEquity, boolean empty) {
                    super.updateItem(watchedEquity, empty);
                    if(watchedEquity != null && !empty) {
                        button.setText("Set Triggers");
                        setGraphic(button);
                    }
                }
            };

            button.setOnAction((e) -> {
                int rowIndex = cell.getTableRow().getIndex();
                cell.getItem().setLowerTrigger(Float.parseFloat(lowTriggerFields.get(rowIndex).getText()));
                cell.getItem().setUpperTrigger(Float.parseFloat(highTriggerFields.get(rowIndex).getText()));
                System.out.println("Set Triggers To " + cell.getItem().getLowerTrigger() + "(Lower) "
                        + cell.getItem().getUpperTrigger() + "(Higher)");
                FPTSApp.getInstance().getData().getInstanceById(WatchList.class, _portfolio.id).save();
                refreshView();
            });

            return cell;
        });

        deleteHoldingColumn.setCellFactory(deleteHoldingColumn -> {
            final Button button = new Button();
            button.setMinWidth(60);
            TableCell<WatchedEquity, WatchedEquity> cell = new TableCell<WatchedEquity, WatchedEquity>() {
                @Override protected void updateItem(final WatchedEquity watchedEquity, boolean empty) {
                    super.updateItem(watchedEquity, empty);
                    if(watchedEquity != null && !empty) {
                        button.setText("Delete");
                        setGraphic(button);
                    }
                }
            };

            button.setOnAction((e) -> {
                System.out.println("Deleted From Watchlist Equity: " + cell.getItem().getEquity().getName());
                FPTSApp.getInstance().getData().getInstanceById(WatchList.class, _portfolio.id).removeEquity(cell.getItem().getEquity());
                FPTSApp.getInstance().getData().getInstanceById(WatchList.class, _portfolio.id).save();
                refreshView();
            });

            return cell;
        });
    }

    @Override
    public void Load(FPTSApp app, Portfolio portfolio) {
        super.Load(app, portfolio);
        minutesInterval.setText(Integer.toString(_app.getData().getInstanceById(WatchList.class, _portfolio.id).getUpdateInterval() / 60));
        secondsInterval.setText(Integer.toString(_app.getData().getInstanceById(WatchList.class, _portfolio.id).getUpdateInterval() % 60));
        refreshView();
    }

    @Override
    public void refreshView() {
        portfolioName.setText(String.format("%1$s's Watch List", _portfolio.getUsername()));
        ObservableList<WatchedEquity> watchedEquities = FXCollections.observableArrayList(FPTSApp.getInstance().getData()
                .getInstanceById(WatchList.class, _portfolio.id).getWatchedEquities());
        holdingsPane.refresh();
        holdingsPane.setItems(watchedEquities);

        highTriggerColumn.setVisible(false);
        highTriggerColumn.setVisible(true);
    }

    @FXML
    public void handleTimer() {
        _app.getData().getInstanceById(WatchList.class, _portfolio.id).setUpdateInterval((Integer.parseInt(minutesInterval.getText()) * 60)
        + Integer.parseInt(secondsInterval.getText()));
        System.out.println("Timer Set To " + _app.getData().getInstanceById(WatchList.class, _portfolio.id).getUpdateInterval());
    }

    @FXML
    public void handleAddWatchedEquity() {
        MarketEquity marketEquity = _app.getData().getInstanceById(MarketEquity.class, newEquitySymbol.getText());
        if(marketEquity != null) {
            _app.getData().getInstanceById(WatchList.class, _portfolio.id).addEquity(marketEquity);
            lowTriggerFields.add(new TextField());
            highTriggerFields.add(new TextField());
            refreshView();
        }
    }
}