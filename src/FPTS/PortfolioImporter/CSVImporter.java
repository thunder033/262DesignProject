package FPTS.PortfolioImporter;

import FPTS.Data.CSV;
import FPTS.Data.FPTSData;
import FPTS.Models.*;
import FPTS.Transaction.Log;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author: Greg
 * Created: 3/13/16
 * Revised: 3/13/16
 * Description: Parses a ThunderForge
 * CSV into a portfolio instance.
 */
public class CSVImporter implements ImportStrategy {

    private enum parseMode {
        HOLDING, TRANSACTION
    }

    /**
     * Creates a temporary portfolio from a values array of holdings
     * @param values [tickerSymbol, shares], [accName, value], ...
     * @return a temporary portfolio
     */
    private Portfolio deserializeValues(String[] values) {
        String name = "temp_" + System.currentTimeMillis();
        Portfolio portfolio = new Portfolio(name, "");
        portfolio.isPersistent = false;
        //holdings occupy 2 values each
        parseMode mode = parseMode.HOLDING;

        Log txnLog = new Log(portfolio);

        Map<parseMode, Integer> parseIncrements = new HashMap<>();
        parseIncrements.put(parseMode.HOLDING, 2);
        parseIncrements.put(parseMode.TRANSACTION, 6);

        for(int i = 0; i < values.length; i += parseIncrements.get(mode)) {

            //"T" delimits the transactions section of the portfolio csv
            if(values[i].equals("T")){
                mode = parseMode.TRANSACTION;
                i++;
            }

            switch (mode){

                case HOLDING:
                    MarketEquity marketEquity = FPTSData.getDataRoot().getInstanceById(MarketEquity.class, values[i]);
                    if(marketEquity != null) {
                        Equity equity = new Equity(marketEquity);
                        float value = Float.parseFloat(values[i + 1]) * marketEquity.getSharePrice();
                        equity.addValue(value);
                        portfolio.addHolding(equity);
                    }
                    else {
                        float value = Float.parseFloat(values[i + 1]);
                        CashAccount account = new CashAccount(values[i], value);
                        portfolio.addHolding(account);
                    }
                    break;
                case TRANSACTION:
                    Holding source = null;
                    Holding dest = null;

                    if(!values[i].equals("")){
                        final String identifier = values[i];
                        source = portfolio.getHoldings().stream()
                                .filter(holding -> holding.getExportIdentifier().equals(identifier))
                                .findFirst().get();
                    }

                    if(!values[i + 2].equals("")){
                        final String identifier = values[i + 2];
                        dest = portfolio.getHoldings().stream()
                                .filter(holding -> holding.getExportIdentifier().equals(identifier))
                                .findFirst().get();
                    }

                    float sourcePrice = Float.parseFloat(values[i + 1]);

                    float destPrice = Float.parseFloat(values[i + 3]);
                    Date date = new Date(Long.parseLong(values[i + 4]));
                    float value = Float.parseFloat(values[i + 5]);

                    txnLog.addTransaction(new Transaction(source, sourcePrice, dest, destPrice, date, value));

                    break;
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
