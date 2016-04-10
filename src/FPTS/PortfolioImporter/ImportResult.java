package FPTS.PortfolioImporter;

import FPTS.Models.Portfolio;
import FPTS.Transaction.Log;

/**
 * Created by Greg on 3/28/2016.
 * Stores the results of an imported portfolio
 */
public class ImportResult {

    public final Portfolio portfolio;
    public final Log transactionLog;

    ImportResult(Portfolio portfolio, Log log){
        this.portfolio = portfolio;
        this.transactionLog = log;
    }
}
