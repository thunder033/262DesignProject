package FPTS.Models;

/**
 * Created by Greg on 4/5/2016.
 */
public class WatchedEquity {

    public enum TriggerState {
        NONE,
        CURRENTLY_ABOVE,
        PREVIOUSLY_ABOVE,
        CURRENTLY_BELOW,
        PREVIOUSLY_BELOW
    }

    private final MarketEquity equity;

    protected float lowerTrigger = 0;
    protected float upperTrigger = 0;
    private TriggerState triggerState = TriggerState.NONE;

    public WatchedEquity(MarketEquity equity){
        this.equity = equity;
    }

    public MarketEquity getEquity(){
        return equity;
    }

    public void checkPrice(){
        float price = equity.getSharePrice();
        if(price > upperTrigger){
            triggerState = TriggerState.CURRENTLY_ABOVE;
        } else if (triggerState == TriggerState.CURRENTLY_ABOVE){
            triggerState = TriggerState.PREVIOUSLY_ABOVE;
        }

        if( price < lowerTrigger) {
            triggerState = TriggerState.CURRENTLY_BELOW;
        } else if (triggerState == TriggerState.CURRENTLY_BELOW) {
            triggerState = TriggerState.PREVIOUSLY_BELOW;
        }
    }

    public void resetTriggers(){
        checkPrice();
        if(triggerState != TriggerState.CURRENTLY_ABOVE && triggerState != TriggerState.CURRENTLY_BELOW){
            triggerState = TriggerState.NONE;
        }
    }

    public TriggerState getTriggerState(){
        return triggerState;
    }

    public boolean isTriggered(){
        return triggerState != TriggerState.NONE;
    }

    public String[] serialize(){
        return new String[]{
                equity.getTickerSymbol(),
                Float.toString(lowerTrigger),
                Float.toString(upperTrigger)
        };
    }
}
