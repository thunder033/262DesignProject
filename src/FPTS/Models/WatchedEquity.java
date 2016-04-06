package FPTS.Models;

/**
 * Created by Greg on 4/5/2016.
 */
public class WatchedEquity {
    private final MarketEquity equity;

    protected float lowerTrigger = 0;
    protected float upperTrigger = 0;

    public WatchedEquity(MarketEquity equity){
        this.equity = equity;
    }

    public MarketEquity getEquity(){
        return equity;
    }
}
