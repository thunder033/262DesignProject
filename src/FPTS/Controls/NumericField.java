package FPTS.Controls;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

/**
 * Created by gjr8050 on 3/13/2016.
 */
public class NumericField extends TextField {
    public NumericField() {
        this.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                char c = event.getCharacter().charAt(0);
                if(!(c >= '0' && c <= '9' || c == '.')) {
                    event.consume();
                }
            }
        });
    }
}
