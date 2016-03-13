package FPTS.PortfolioImporter;

import FPTS.Data.CSV;
import FPTS.Data.FPTSData;
import FPTS.Models.CashAccount;
import FPTS.Models.Equity;
import FPTS.Models.MarketEquity;
import FPTS.Models.Portfolio;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author: Greg
 * Created: 3/13/16
 * Revised: 3/13/16
 * Description: Parses a ThunderForge
 * CSV into a portfolio instance.
 */
public class CSVImporter implements ImportStrategy {

    /**
     * Creates a temporary portfolio from a values array of holdings
     * @param values [tickerSymbol, shares], [accName, value], ...
     * @return a temporary portfolio
     */
    private Portfolio deserializeValues(String[] values) {
        String name = "temp_" + System.currentTimeMillis();
        Portfolio portfolio = new Portfolio(name, "");
        portfolio.isPersistent = false;

        for(int i = 0; i < values.length; i += 2) {
            MarketEquity marketEquity = FPTSData.getDataRoot().getInstanceById(MarketEquity.class, values[i]);
            if(marketEquity != null) {
                Equity equity = new Equity(marketEquity);
                float value = Float.parseFloat(values[i + 1]) / marketEquity.getSharePrice();
                equity.addValue(value);
                portfolio.addHolding(equity);
            }
            else {
                float value = Float.parseFloat(values[i + 1]);
                CashAccount account = new CashAccount(values[i], value);
                portfolio.addHolding(account);
            }
        }

        return portfolio;
    }

    /**
     * Import the CSV data from the given path and deserialize it to a portfolio
     * @param path the location of CSV file w/ holdings
     * @return a temporary portfolio
     */
    @Override
    public Portfolio execute(Path path) {

        Portfolio portfolio = null;
        String[][] data = null;
        try {
            CSV csv = new CSV(path);
            data = csv.Read();
        } catch (IOException ex) {
            System.out.println("Failed to import portfolio data from " + path);
            System.out.println(ex.getMessage());
        }

        if(data != null) {
            portfolio = deserializeValues(data[0]);
        }

        return portfolio;
    }
}
