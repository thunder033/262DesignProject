package FPTS.Models;

import java.util.ArrayList;

/**
 * Created by traub_000 on 4/6/2016.
 */
public class DJIA extends MarketIndex {

    private static final double divisor = 0.14602128057775;

    private ArrayList<MarketEquity> equities;

    protected DJIA(String indexName) {
        super(indexName);
    }

    /**
     * Calculates the share price of the index based on equities it contains
     * @return the aggregate share price
     */
    @Override
    public float getSharePrice()
    {
        return equities.stream()
                .map(MarketEquity::getSharePrice)
                .reduce(0.0f, (a, b) -> (float)(a + (b / divisor)));
    }
}
