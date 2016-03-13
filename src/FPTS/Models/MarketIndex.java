package FPTS.Models;

import java.util.ArrayList;

/**
 * Created by gjr8050 on 3/10/2016.
 * Contains a group of equities based on a market index or sector
 */
public class MarketIndex extends MarketEquity {

    private ArrayList<MarketEquity> equities;

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
        //TODO: Calculate share price from market equities in index
        return 0;
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
