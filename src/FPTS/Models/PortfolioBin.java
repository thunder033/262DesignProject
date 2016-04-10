package FPTS.Models;

import FPTS.Core.Model;
import FPTS.Data.DataBin;
import FPTS.Data.FPTSData;

import java.util.ArrayList;

/**
 * @author: Greg
 * Created: 3/10/16
 * Revised: 3/13/16
 * Description: Responsible for
 * mapping portfolio and converting between
 * instances and values arrays.
 */
public class PortfolioBin extends DataBin {

    public PortfolioBin()
    {
        dataClass = Portfolio.class;
        fileName = "portfolios.csv";
    }

    /**
     * Convert a values array into a portfolio instance. This requires the holdings already
     * be loaded into the system
     * @param values values [username, pass-hash, [holdings, ...]]
     * @return a portfolio instance
     */
    @Override
    public Model fromValueArray(String[] values) {
        Portfolio portfolio = new Portfolio(values[0], values[1]);

        for (int h = 2; h < values.length; h++) {
            Holding holding = Holding.deserializeHolding(values[h]);

            if(holding != null)
            {
                portfolio.addHolding(holding);
            }
        }

        return portfolio;
    }

    /**
     * Serialize a portfolio into a values array
     * @param instance instance to serialize
     * @return an array of values representing the portfolio
     */
    @Override
    public String[] toValueArray(Model instance) {

        Portfolio portfolio = Portfolio.class.cast(instance);
        ArrayList<String> values = new ArrayList<>();
        values.add(portfolio.id);
        values.add(portfolio._passHash);
        portfolio.holdings.values().stream().forEach(holding -> values.add(Holding.serializeHolding(holding)));

        System.out.println(values.stream().reduce("Equities in " + portfolio.id, (a, b) -> a + ", " + b));

        String[] stringVals = new String[values.size()];
        stringVals = values.toArray(stringVals);
        return stringVals;
    }
}
