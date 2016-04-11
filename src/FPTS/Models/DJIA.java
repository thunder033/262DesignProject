package FPTS.Models;

import java.util.ArrayList;

/**
 * Created by traub_000 on 4/6/2016.
 *
 * Description: Represents the Dow Jones Industrial Average
 * The same as a MarketIndex except for how its value is calculated
 */
public class DJIA extends MarketIndex {

    private static final double divisor = 0.14602128057775;

    protected DJIA(String indexName) {
        super(indexName);
    }

    /**
     * Calculates the share price of the DJIA based on equities it contains
     * and the current DJIA divisor
     * @return the aggregate share price
     */
    @Override
    public float getSharePrice()
    {
        return equities.stream()
                .map(MarketEquity::getSharePrice)
                .reduce(0.0f, (a, b) -> (float)(a + (b / divisor)));
    }

    /**
     * Adds a new market equity to the index
     * @param equity the equity to add
     */
    public void addEquity(MarketEquity equity) {
        super.addEquity(equity);
    }

}
