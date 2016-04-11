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
public class Equity extends Holding {
    private MarketEquity _marketEquity;
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
     * @return name of the equity on the market.
     */
    @Override
    public String getName() {
        return _marketEquity.getName();
    }

    /**
     * the unique external id of an equity is it's ticker symbol
     * @return the equity ticker symbol
     */
    @Override
    public String getExportIdentifier() {
        return _marketEquity.getTickerSymbol();
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
