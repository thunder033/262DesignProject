package FPTS;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.text.DecimalFormat;

/**
 * Created by traub_000 on 3/11/2016.
 */
public class SimulationCtrl extends Controller{

    // It seems I don't need this False
    private static Simulation sim = new Simulation(100.00);

    @FXML private Label value;

    @FXML protected void simulateClicked(ActionEvent event){
        sim.setBullAlgorithm(.10, 12, "month", sim.getPortfolioValue());
        double portfolioValue = sim.simulate();
        DecimalFormat df = new DecimalFormat("#.##");
        value.setText(df.format(portfolioValue));
        //value.setText(Double.toString(sim.revertToActualValue()));
    }
}
