package FPTS.Controllers;

import FPTS.Core.Controller;
import FPTS.Core.FPTSApp;
import FPTS.MarketSimulation.Simulation;
import FPTS.Models.Holding;
import FPTS.Models.Portfolio;
import FPTS.Views.PortfolioView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.text.DecimalFormat;

/**
 * Created by traub_000 on 3/11/2016.
 */
public class SimulationController extends Controller {

    // It seems I don't need this False
    private static Simulation sim;


    @Override
    public void Load(FPTSApp app, Portfolio portfolio) {
        super.Load(app, portfolio);
        float portfolioValue = _portfolio.getHoldings().stream()
                .map(Holding::getValue)
                .reduce(0.0f, (a, b) -> a + b);
        sim = new Simulation(portfolioValue);
    }

    @FXML private Label value;

    @FXML private TextField perAnnumText;

    @FXML private TextField timeStepsText;

    @FXML private ToggleGroup timeIntervalToggle;

    @FXML private RadioButton year;

    @FXML private RadioButton month;

    @FXML private RadioButton day;

    @FXML private RadioButton bear;

    @FXML private RadioButton bull;

    @FXML private RadioButton noGrowth;

    @FXML private Label marketError;

    @FXML private ToggleGroup simulationToggle;

    @FXML private Button revertButton;

    @FXML private Label intervalError;

    private double perAnnum;

    private int timeSteps;



    @FXML protected void simulateOrStepClicked(ActionEvent event) {

        try {
            perAnnum = Double.parseDouble(perAnnumText.getText()) / 100.0;
        } catch (NumberFormatException e) {
            perAnnumText.setText("Enter a number in this field");
        }
        try {
            timeSteps = Integer.parseInt(timeStepsText.getText());
        } catch (NumberFormatException e) {
            timeStepsText.setText("Enter a number in this field");
        }
        year.setUserData("year");
        month.setUserData("month");
        day.setUserData("day");
        String interval = "";
        try {
            interval = timeIntervalToggle.getSelectedToggle().getUserData().toString();
        } catch (NullPointerException e) {
            intervalError.setVisible(true);
        }
        if (simulationToggle.getSelectedToggle() != null) {

            if (bull.isSelected()) {
                sim.setBullAlgorithm(perAnnum, timeSteps, interval, sim.getPortfolioValue());
                marketError.setVisible(false);
                revertButton.setVisible(true);
            } else if (bear.isSelected()) {
                sim.setBearAlgorithm(perAnnum, timeSteps, interval, sim.getPortfolioValue());
                marketError.setVisible(false);
                revertButton.setVisible(true);
            } else if (noGrowth.isSelected()) {
                sim.setNoGrowthAlgorithm(perAnnum, timeSteps, interval, sim.getPortfolioValue());
                marketError.setVisible(false);
                revertButton.setVisible(true);
            }
        }else {
            marketError.setVisible(true);
        }
        double portfolioValue;
        if (((Button) event.getSource()).getText().equals("Run Simulation")) {
            portfolioValue = sim.simulate();
        }else{
            portfolioValue = sim.step();
            timeStepsText.setText(Integer.toString(sim.getCurrentAlgorithm().getOriginalTimeSteps()
            - sim.getCurrentAlgorithm().getSimulatedTimeSteps()));
        }
        DecimalFormat df = new DecimalFormat("#.##");
        value.setText(df.format(portfolioValue));

    }

    @FXML protected void revertClicked(ActionEvent event){
        value.setText(Double.toString(sim.revertToActualValue()));
        revertButton.setVisible(false);
    }


    public void navPortfolio(ActionEvent actionEvent) {
        _app.loadView(new PortfolioView(_app));
    }
}
