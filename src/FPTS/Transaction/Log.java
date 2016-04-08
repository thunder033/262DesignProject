package FPTS.Transaction;

import FPTS.Data.FPTSData;
import FPTS.Models.Portfolio;
import FPTS.Models.Transaction;

import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Created by Greg on 3/26/2016.
 * Provides behavior to manage transactions on a portfolio
 */
public class Log {
    private Portfolio portfolio;
    private Stack<Transaction> transactions;

    public Log(Portfolio portfolio){
        this.portfolio = portfolio;
        transactions = new Stack<>();
    }

    /**
     * Build a log of transactions on the portfolio, sorted by descending date
     * @return a list of transactions
     */
    public List<Transaction> getTransactions(){
        return FPTSData.getDataRoot().getInstances(Transaction.class).stream()
                //get transactions for the portfolio based on their source holding
                .filter(transaction -> portfolio.getHoldings(true).contains(transaction.getIndexHolding()))
                //sort transactions by date descending
                .sorted((a, b) -> Long.compare(b.getDateTime().getTime(), a.getDateTime().getTime()))
                .collect(Collectors.toList());
    }

    public void importTransactions(List<Transaction> transactions){
        this.transactions.addAll(transactions);
    }

    public void addTransaction(Transaction transaction){
        transactions.add(transaction);
    }

    public List<Entry> getEntries(){
        return getTransactions().stream()
                .map(Entry::new)
                .collect(Collectors.toList());
    }
}
