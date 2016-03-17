package FPTS.Models;

/**
 * Created by gjr8050 on 3/15/2016.
 * Exception indicating the system encountered a ticker symbol
 * or index name that does not exist
 */
public class UnknownMarketEquityException extends Exception {
    public UnknownMarketEquityException(String tickerSymbol){
        super(String.format("No Market Equity or Index exists with symbol/name \"%s\"", tickerSymbol));
    }
}
