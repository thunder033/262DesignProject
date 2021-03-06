package FPTS.Models;

import FPTS.Core.Model;
import FPTS.Data.YFSClient;
import javafx.application.Platform;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * Created by Greg on 4/5/2016.
 * Stores a list of equities to be monitored and initiates a lookup for those equities at the specified
 * interval
 */
public class WatchList extends Model {

    private final Map<String, WatchedEquity> equities;
    private Callable<Void> subscriber;
    private int updateInterval;
    private Timer timer;

    public WatchList(Portfolio portfolio) {
        super(portfolio.id);
        equities = new HashMap<>();
    }

    WatchList(String id) {
        super(id);
        equities = new HashMap<>();
    }

    /**
     * Adds an equity to the watchlist if it is not already being watched
     * @param equity a market equity to watch
     */
    public void addEquity(MarketEquity equity) {
        if(!equities.containsKey(equity.getTickerSymbol())){
            equities.put(equity.getTickerSymbol(), new WatchedEquity(equity));
            setChanged();
        }
    }

    void loadWatchedEquity(WatchedEquity watchedEquity) {
        equities.put(watchedEquity.getEquity().getTickerSymbol(), watchedEquity);
    }

    /**
     * Removes the equity from the watch list
     * @param equity equity to stop watching
     */
    public void removeEquity(MarketEquity equity) {
        equities.remove(equity.getTickerSymbol());
    }

    /**
     * @return List of all watched equities
     */
    public List<WatchedEquity> getWatchedEquities(){
        return new ArrayList<>(equities.values());
    }

    public int getUpdateInterval(){
        return updateInterval / 1000;
    }

    /**
     * Set the interval (in seconds) at which to refresh the equities
     * @param interval number of seconds between updates
     */
    public void setUpdateInterval(int interval){
        updateInterval = interval * 1000;
        YFSClient.instance().setMaxCacheAge(interval * 1000);
    }

    /**
     * Returns the count of watched equities that are currently triggered
     * @return number of triggered watches
     */
    public int getTriggeredCount() {
        return (int) getWatchedEquities().stream()
                .filter(WatchedEquity::isTriggered)
                .count();
    }

    /**
     * Resets the trigger state of all equities in the watch list
     */
    public void resetTriggers(){
        getWatchedEquities().stream().forEach(WatchedEquity::resetTriggers);
    }

    /**
     * A method to be executed when equity prices are updated. There
     * can only be ONE subscriber at a time
     * @param callback a method
     */
    public void subscribe(Callable<Void> callback){
        subscriber = callback;
    }

    /**
     * Begins monitoring the equities in the watch list
     */
    public void beginWatch(){
        if(timer != null){
            throw new UnsupportedOperationException("Watch List is already running watch operation.");
        }

        System.out.println("begin Check prices for " + equities.size() + " in " + id);
        timer = new Timer();
        YFSClient.instance().setMaxCacheAge(updateInterval);

        //don't let the cache be less than 30 seconds
        YFSClient.instance().setMaxCacheAge(Math.max(updateInterval - 500, 30 * 1000));

        timer.schedule(new TimerTask() {
            public void run() {
                Platform.runLater(() -> {
                    equities.values().stream().forEach(WatchedEquity::checkPrice);

                    //attempt to call the watchlist subscriber
                    if(subscriber != null){
                        try {
                            System.out.println("Call subscriber");
                            subscriber.call();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        }, 0, updateInterval);
    }

    /**
     * Stop watching the equities in the list
     */
    public void endWatch(){
        if(timer != null){
            timer.cancel();
        }
    }
}
