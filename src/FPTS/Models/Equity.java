package FPTS.Models;

import FPTS.Core.Model;

/**
 * @author: Greg
 * Created: 3/10/16
 * Revised: 3/13/16
 * Description: Represents a
 * share of an equity on the
 * market held in a portfolio.
 */
public class Equity extends Model implements Holding {
    MarketEquity _marketEquity;
    float shares;
    public static final String type = "Equity";

    public Equity(String id, MarketEquity marketEquity)
    {
        super(id);
        _marketEquity = marketEquity;
    }

    public Equity(MarketEquity marketEquity) {
        _marketEquity = marketEquity;
    }

    /**
     * @return the number of shares owned
     */
    public float getShares() {
        return shares;
    }

    /**
     * @return name of the equity on the market
     */
    @Override
    public String getName() {
        return String.format("%s (%s, $%.2f)", _marketEquity.getName(), _marketEquity.getTickerSymbol(), _marketEquity.getSharePrice());
    }

    public float getCurrentSharePrice(){
        return _marketEquity.getSharePrice();
    }

    /**
     * @return the ticker symbol of the equity
     */
    public String getTickerSymbol() {
        return _marketEquity.getTickerSymbol();
    }

    /**
     * calculate the monetary value of the held shares
     * @return value of equity holding
     */
    @Override
    public float getValue() {
        return shares * _marketEquity.getSharePrice();
    }

    /**
     * add more value to the equity holding, adding more shares
     * @param value invested amount
     */
    @Override
    public void addValue(float value) {
        shares += value / _marketEquity.getSharePrice();
        setChanged();
    }

    /**
     * remove value from the equity holding, adding more shares
     * @param value invested amount
     */
    @Override
    public void removeValue(float value) {
        shares -= value / _marketEquity.getSharePrice();
        setChanged();
    }

    /**
     * @return descriptor of the holding type
     */
    @Override
    public String getType(){
        return type;
    }
}
