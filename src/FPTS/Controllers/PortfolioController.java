package FPTS.Controllers;

import FPTS.Core.Controller;
import FPTS.Core.FPTSApp;
import FPTS.Core.Model;
import FPTS.Models.Holding;
import FPTS.Models.Portfolio;
import FPTS.PortfolioImporter.CSVImporter;
import FPTS.PortfolioImporter.Exporter;
import FPTS.PortfolioImporter.Importer;
import FPTS.Transaction.Entry;
import FPTS.Transaction.Log;
import FPTS.Views.AddHoldingView;
import FPTS.Views.LoginView;
import FPTS.Views.SimulationView;
import FPTS.Views.TransactionView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

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
    @FXML private TableColumn<Holding, String> holdingActionColumn;

    private final DirectoryChooser directoryChooser = new DirectoryChooser();
    private final FileChooser fileChooser = new FileChooser();

    private Log transactionLog;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        //holdingActionColumn.setCellFactory();
    }

    @Override
    public void Load(FPTSApp app, Portfolio portfolio) {
        super.Load(app, portfolio);
        System.out.println("Create txn log");
        transactionLog = new Log(portfolio);
        refreshView();
    }

    @FXML
    protected void handleExportAction(ActionEvent event) {
        System.out.println(_app);
        Path path = Paths.get(directoryChooser.showDialog(_app.getStage()).getPath());
        Exporter.exportPortfolio(_portfolio, path);
    }

    @Override
    public void refreshView()
    {
        System.out.println(super._app);
        System.out.println("refresh controller");
        portfolioName.setText(String.format("%1$s's Portfolio", _portfolio.getUsername()));

        ObservableList<Holding> holdings = FXCollections.observableArrayList(_portfolio.getHoldings());

        System.out.println(holdings.size());
        holdingsPane.setItems(holdings);

        if(transactionLog != null){
            ObservableList<Entry> entries = FXCollections.observableArrayList(transactionLog.getEntries());
            transactionLogPane.setItems(entries);
        }
    }

    public void handleNewHolding(ActionEvent actionEvent) {
        _app.loadView(new AddHoldingView(_app));
    }

    private void addHolding(Holding holding){
        _portfolio.addHolding(holding);
        _app.getData().addInstance(Model.class.cast(holding));
    }

    public void handleImport(ActionEvent actionEvent) {
        File file = fileChooser.showOpenDialog(_app.getStage());

        if(file != null){
            Path path = Paths.get(fileChooser.showOpenDialog(_app.getStage()).getPath());
            Importer importer = new Importer(path);
            importer.setStrategy(new CSVImporter());
            importer.importData().portfolio.getHoldings().stream().forEach(this::addHolding);
            _portfolio.save();
        }

    }

    public void handleTransaction(ActionEvent actionEvent) {
        _app.loadView(new TransactionView(_app));
    }

    public void handleSimulate(ActionEvent actionEvent) {
        _app.loadView(new SimulationView(_app));
    }

    public void handlelLogOut(ActionEvent actionEvent) {
        _app.loadView(new LoginView(_app));
    }
}
