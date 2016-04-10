package FPTS.PortfolioImporter;

import FPTS.Models.CashAccount;
import FPTS.Models.Equity;
import FPTS.Models.Holding;
import FPTS.Models.Portfolio;

/**
 * Created by Greg on 4/9/2016.
 */
public class ImportItem {

    public enum Action {
        MERGE("Merge"),
        REPLACE("Replace"),
        IGNORE("Ignore");

        private final String name;

        Action(String s){
            name = s;
        }

        public String toString(){
            return name;
        }
    }

    private final Holding holding;
    private Action action = Action.MERGE;
    private Portfolio portfolio;
    //private boolean conflicted = false;

    public ImportItem(Holding holding, Portfolio portfolio){
        this.portfolio = portfolio;
        this.holding = holding;
    }

    public boolean isConflicted(){
        return holding.getClass() == CashAccount.class && portfolio.containsHolding(holding);
    }

    public void setAction(Action action){
        this.action = action;
    }

    public Action getAction(){
        return action;
    }

    public Holding getHolding(){
        return holding;
    }

    public String getType(){
        return holding.getType();
    }

    public String getName(){
        return holding.getName();
    }

    public String getValue(){
        return String.format("$%.2f", holding.getValue());
    }

    public String getShares(){
        return holding.getClass() == Equity.class ? String.format("%.4f", Equity.class.cast(holding).getShares()) : "";
    }

    public String getResult(){

        Holding existing = portfolio.getHolding(holding.getExportIdentifier());
        float value = 0;
        switch (getAction()){
            case MERGE:
                value = holding.getValue() + (existing != null ? existing.getValue() : 0);
                break;
            case REPLACE:
                value = holding.getValue();
                break;
            case IGNORE:
                value = existing != null ? existing.getValue() : 0;
                break;
        }

        return String.format("$%.2f", value);
    }
}
