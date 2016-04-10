package FPTS.Models;

import java.util.ArrayList;

/**
 * @author: Greg
 * Created: 3/10/16
 * Revised: 3/13/16
 * Description: Contains a group of
 * equities based on a market index or sector.
 */
public class MarketIndex extends MarketEquity {

    protected ArrayList<MarketEquity> equities;

    protected MarketIndex(String indexName)
    {
        super(indexName);
        id = indexName;
        _name = indexName;
        equities = new ArrayList<>();
    }

    /**
     * Calculates the share price of the index based on equities it contains
     * @return the aggregate share price
     */
    @Override
    public float getSharePrice()
    {
        return (float) equities.stream()
                .mapToDouble(MarketEquity::getSharePrice).average().getAsDouble();
    }

    /**
     * Adds a new market equity to the index
     * @param equity the equity to add
     */
    public void addEquity(MarketEquity equity) {
        equities.add(equity);
    }

    /**
     * Indicates if the equity is contained in the index
     * @param equity a market equity
     * @return boolean indicating if the equity is present in the index
     */
    public boolean contains(MarketEquity equity) {
        return equities.contains(equity);
    }

    /**
     * Get all equities in the index
     * @return list of market equites in the index
     */
    public ArrayList<MarketEquity> getEquities()
    {
        return equities;
    }
}
