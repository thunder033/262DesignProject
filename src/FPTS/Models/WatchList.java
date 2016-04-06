package FPTS.Models;

import FPTS.Core.Model;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Greg on 4/5/2016.
 */
public class WatchList extends Model {

    private List<MarketEquity> equities;
    int updateInterval;

    public WatchList(Portfolio portfolio) {
        super(portfolio.id);
        equities = new ArrayList<>();
    }

    public void addEquity(MarketEquity equity) {
        if(!equities.contains(equity)){
            equities.add(equity);
            setChanged();
        }
    }

    public void removeEquity(MarketEquity equity) {
        equities.remove(equity);
    }

    public List<MarketEquity> getList(){
        return equities;
    }

    public void Begin(){
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            public void run() {
                Platform.runLater(() -> {
                    setChanged();
                    save();
                });
            }
        }, 0, updateInterval);

    }
}
