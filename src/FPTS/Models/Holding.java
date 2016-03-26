package FPTS.Models;

import FPTS.Core.Model;
import FPTS.Data.FPTSData;

/**
 * @author: Greg
 * Created: 3/10/16
 * Revised: 3/13/16
 * Interface for managing holdings in a portfolio.
 */
public interface Holding {
    String type = "Holding";

    /**
     * @return name of the holding
     */
    String getName();

    /**
     * @return monetary value of holding
     */
    float getValue();

    /**
     * increase the value of the holding
     * @param value the amount of monetary value to add
     */
    void addValue(float value);

    /**
     * decrease the value of the holding
     * @param value the amount of monetary value to remove
     */
    void removeValue(float value);

    /**
     * @return a descriptor of the holding type;
     */
    String getType();

    /**
     * Get the type-id hash for a holding
     * @param holding a holding
     * @return type-id hash: [Type][ID], ex. C12 or E333
     */
    static String serializeHolding(Holding holding){
        Model model = Model.class.cast(holding);
        return holding instanceof CashAccount ? "C" + model.id : "E" + model.id;
    }

    /**
     * Locate a holding based on its ID hash
     * @param hash a holding ID hash, ex. E23
     * @return the cash account or equity holding
     */
    static Holding deserializeHolding(String hash){
        String id = hash.substring(1);
        Class type = hash.charAt(0) == 'C' ? CashAccount.class : Equity.class;
        return Holding.class.cast(FPTSData.getDataRoot().getInstanceById(type, id));
    }
}
