package FPTS.PortfolioImporter;

import FPTS.Data.CSV;
import FPTS.Models.Equity;
import FPTS.Models.Holding;
import FPTS.Models.Portfolio;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Greg on 3/13/2016.
 * Serializes a Portfolio and its holdings into a values array
 */
public class Exporter {

    /**
     * Convert a holding to a CSV pair
     * @param holding the holding to serialize
     * @return an array of [tickerSymbol, shares] or [accName, value]
     */
    public static String[] serializeHolding(Holding holding) {
        String[] values = new String[2];
        if(holding instanceof Equity) {
            Equity equity = Equity.class.cast(holding);
            values[0] = equity.getTickerSymbol();
            values[1] = Float.toString(equity.getShares());
        }
        else {
            values[0] = holding.getName();
            values[1] = Float.toString(holding.getValue());
        }
        return values;
    }

    /**
     * Convert a portfolio to a list of holdings represented as CSV pairs
     * @param portfolio the portfolio to serialize
     * @return an array values representing the holdings
     */
    public static String[] serializePortfolio(Portfolio portfolio) {

        ArrayList<String> values = new ArrayList<>();

        portfolio.getHoldings().stream()
                .forEach(holding -> Collections.addAll(values, serializeHolding(holding)));

        String[] stringVals = new String[values.size()];
        stringVals = values.toArray(stringVals);
        return stringVals;
    }

    /**
     * Serializes a portfolio to CSV and outputs it
     * @param portfolio the portfolio to export
     * @param path the location to export to
     */
    public static void exportPortfolio(Portfolio portfolio, Path path) {
        String[] values = serializePortfolio(portfolio);
        String[][] line = new String[1][];
        line[0] = values;

        try {
            CSV csv = new CSV(path);
            csv.Write(line);
        } catch (IOException ex) {
            System.out.println("Warning Portfolio:" + portfolio.id + " export to " + path + " failed");
            System.out.println(ex.getMessage());
        }
    }
}
