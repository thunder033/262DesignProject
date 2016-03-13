package FPTS.Models;

import FPTS.Core.Model;
import FPTS.Data.DataBin;

/**
 * Created by Greg on 3/12/2016.
 */
public class CashAccountBin extends DataBin {

    public CashAccountBin(){
        fileName = "cashAccounts.csv";
        dataClass = CashAccount.class;
    }

    @Override
    public Model fromCSV(String[] values) {
        CashAccount cashAccount = new CashAccount(values[0]);
        cashAccount.name = values[1];
        cashAccount._value = Float.parseFloat(values[2]);

        return cashAccount;
    }

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
