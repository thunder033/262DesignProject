package FPTS.Controllers;

import FPTS.Core.Controller;
import FPTS.Core.FPTSApp;
import FPTS.Core.Model;
import FPTS.Models.CashAccount;
import FPTS.Models.Holding;
import FPTS.Models.Portfolio;
import FPTS.PortfolioImporter.CSVImporter;
import FPTS.PortfolioImporter.Exporter;
import FPTS.PortfolioImporter.Importer;
import FPTS.Transaction.Entry;
import FPTS.Transaction.Log;
import FPTS.Views.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.Date;

/**
 * @author: Alexander Kidd
 * Created: 3/11/2016
 * Revised: 3/13/2016
 * Description: This controller handles requests
 * to alternate the portfolio UI based on internal
 * model changes and UI changes that alter the internal model state.
 */
public class PortfolioController extends Controller {
    @FXML private Text portfolioName;
    @FXML private TableView<Holding> holdingsPane;
    @FXML private TableView<Entry> transactionLogPane;
    @FXML private TableColumn<Holding, Holding> holdingActionColumn;

    private final DirectoryChooser directoryChooser = new DirectoryChooser();
    private final FileChooser fileChooser = new FileChooser();

    private Log transactionLog;

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

        transactionLog = new Log(portfolio);
        refreshView();
    }

    @FXML
    protected void handleExportAction(ActionEvent event) {
        Path path = Paths.get(directoryChooser.showDialog(_app.getStage()).getPath());
        Exporter.exportPortfolio(_portfolio, path);
    }

    @Override
    public void refreshView()
    {
        portfolioName.setText(String.format("%1$s's Portfolio", _portfolio.getUsername()));
        ObservableList<Holding> holdings = FXCollections.observableArrayList(_portfolio.getHoldings());
        holdingsPane.setItems(holdings);

        if(transactionLog != null){
            ObservableList<Entry> entries = FXCollections.observableArrayList(transactionLog.getEntries());
            transactionLogPane.setItems(entries);
        }
    }

    public void handleNewHolding(ActionEvent actionEvent) {
        _app.loadView(new AddHoldingView(_app));
    }

    public void handleImport(ActionEvent actionEvent) { _app.loadView(new ImportHoldingsView(_app)); }

    public void handleTransaction(ActionEvent actionEvent) {
        _app.loadView(new TransactionView(_app));
    }

    public void handleSimulate(ActionEvent actionEvent) {
        _app.loadView(new SimulationView(_app));
    }

    public void handlelLogOut(ActionEvent actionEvent) {
        _app.loadView(new LoginView(_app));
    }

    public void handleUndo(ActionEvent actionevent) {
        int i=0;
        if(transactionLog.getTransactions().isEmpty()) {return;}
        while(transactionLog.getTransactions().get(i).isEnabled()){
            if (++i==transactionLog.getTransactions().size()){
                break;
            }
        }
        if(i>0)
            transactionLog.getTransactions().get(i-1).rollback();
    }

    public void handleRedo(ActionEvent actionevent) {
        int i=0;
        if(transactionLog.getTransactions().isEmpty()) {return;}
        while(transactionLog.getTransactions().get(i).isEnabled()){
            if (++i==transactionLog.getTransactions().size()){
                break;
            }
        }
        transactionLog.getTransactions().get(i).redo(new Date());
    }
}
