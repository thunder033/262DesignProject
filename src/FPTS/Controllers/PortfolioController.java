package FPTS.Controllers;

import FPTS.Core.Controller;
import FPTS.Core.Model;
import FPTS.Models.Holding;
import FPTS.PortfolioImporter.CSVImporter;
import FPTS.PortfolioImporter.Exporter;
import FPTS.PortfolioImporter.Importer;
import FPTS.Views.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.nio.file.Path;
import java.nio.file.Paths;

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
    @FXML private TableView holdingsPane;

    final DirectoryChooser directoryChooser = new DirectoryChooser();
    final FileChooser fileChooser = new FileChooser();

    public PortfolioController() {
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
}
