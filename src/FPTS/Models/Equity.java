package FPTS.Models;

import FPTS.Core.Model;

/**
 * Created by gjr8050 on 3/10/2016.
 * Represents a share of an equity on the market held in a portfolio
 */
public class Equity extends Model implements Holding {
    MarketEquity _marketEquity;
    float shares;

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
        return _marketEquity.getName();
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
    }

    /**
     * remove value from the equity holding, adding more shares
     * @param value invested amount
     */
    @Override
    public void removeValue(float value) {
        shares -= value / _marketEquity.getSharePrice();
    }

    /**
     * @return descriptor of the holding type
     */
    public String getType(){
        return "Equity";
    }
}
