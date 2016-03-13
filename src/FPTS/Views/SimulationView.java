package FPTS.Views;

import FPTS.Core.FPTSApp;
import FPTS.Core.View;

/**
 * @author: Alexander Kidd
 * Created: 3/12/2016
 * Revised: 3/12/2016
 * Description: In charge of displaying simulations page in the UI.
 */
public class SimulationView extends View {

    public SimulationView(FPTSApp app)
    {
        super(app);
        _fxmlName = "simulation.fxml";
    }
}
