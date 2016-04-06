package FPTS.Models;

import FPTS.Core.Model;
import FPTS.Data.YFSClient;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.collections.ObservableList;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * Created by Greg on 4/5/2016.
 * Stores a list of equities to be monitored and initiates a lookup for those equities at the specified
 * interval
 */
public class WatchList extends Model {

    private Map<String, WatchedEquity> equities;
    private Callable subscriber;
    private int updateInterval;

    public WatchList(Portfolio portfolio) {
        super(portfolio.id);
        equities = new HashMap<>();
    }

    public void addEquity(MarketEquity equity) {
        if(!equities.containsKey(equity.getTickerSymbol())){
            equities.put(equity.getTickerSymbol(), new WatchedEquity(equity));
            setChanged();
        }
    }

    public void removeEquity(MarketEquity equity) {
        equities.remove(equity.getTickerSymbol());
    }

    public List<WatchedEquity> getList(){
        return new ArrayList<>(equities.values());
    }

    /**
     * Set the interval (in seconds) at which to refresh the equities
     * @param interval number of seconds between updates
     */
    public void setUpdateInterval(int interval){
        updateInterval = interval;
        YFSClient.instance().setMaxCacheAge(interval);
    }

    /**
     * Begins monitoring the equities in the watch list
     */
    public void Begin(){
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            public void run() {
                Platform.runLater(() -> {

                    equities.values().stream()
                            .map(WatchedEquity::getEquity)
                            .forEach(MarketEquity::getSharePrice);

                    //TODO: check for triggers

                    if(subscriber != null){
                        try {
                            subscriber.call();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }
                });
            }
        }, 0, updateInterval);
    }
}
