package FPTS;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.text.DecimalFormat;

/**
 * Created by traub_000 on 3/11/2016.
 */
public class SimulationCtrl extends Controller{

    // It seems I don't need this False
    private static Simulation sim = new Simulation(100.00);

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



    @FXML protected void simulateClicked(ActionEvent event) {
        try {
            perAnnum = Double.parseDouble(perAnnumText.getText()) / 100.0;
            System.out.println(perAnnum);
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
        double portfolioValue = sim.simulate();
        DecimalFormat df = new DecimalFormat("#.##");
        value.setText(df.format(portfolioValue));

    }

    @FXML protected void revertClicked(ActionEvent event){
        value.setText(Double.toString(sim.revertToActualValue()));
        revertButton.setVisible(false);
    }
}
