package FPTS.Controllers;

import java.util.Observable;
import java.util.Observer;

/**
 * @author: Greg Rozmarynowycz
 * Created: 3/11/2016
 * Revised: 3/11/2016
 * Description: A superclass for controllers.
 * These components handle requests for user input
 * in the UI that alters the internal state (model) of FPTS.
 * Conversely, it handles communication between a changed model
 * and the UI.  Observer implementation helps with notifications
 * between the UI and model components.
 */

public class Controller implements Observer {

    public void update(Observable subject, Object obj)
    {

    }
}
