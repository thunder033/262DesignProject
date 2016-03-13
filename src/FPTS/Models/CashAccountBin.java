package FPTS.Models;

import FPTS.Core.Model;
import FPTS.Data.DataBin;

/**
 * Cash Account Bin
 * Responsible for mapping cash accounts and converting between
 * instances and values arrays.
 */
public class CashAccountBin extends DataBin {

    public CashAccountBin(){
        fileName = "cashAccounts.csv";
        dataClass = CashAccount.class;
    }

    /**
     * Parses a values array into cash account instance
     * @param values [id, name, amount]
     * @return a values array instance
     */
    @Override
    public Model fromCSV(String[] values) {
        CashAccount cashAccount = new CashAccount(values[0]);
        cashAccount.name = values[1];
        cashAccount._value = Float.parseFloat(values[2]);

        return cashAccount;
    }

    /**
     * Converts a Cash Account to a values array
     * @param instance a cash account instance
     * @return values [id, name, amount]
     */
    @Override
    public String[] toCSV(Model instance) {
        CashAccount cashAccount = CashAccount.class.cast(instance);
        String[] values = new String[3];
        values[0] = cashAccount.id;
        values[1] = cashAccount.name;
        values[2] = Float.toString(cashAccount._value);

        return values;
    }
}
