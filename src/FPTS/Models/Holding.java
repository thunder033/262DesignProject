package FPTS.Models;

import FPTS.Core.Model;
import FPTS.Data.FPTSData;

/**
 * @author: Greg
 * Created: 3/10/16
 * Revised: 3/13/16
 * Interface for managing holdings in a portfolio.
 */
public abstract class Holding extends Model {
    String type = "Holding";

    protected Holding(String id){
        super(id);
    }

    protected Holding(){
        super();
    }

    /**
     * @return name of the holding
     */
    public abstract String getName();

    /**
     * @return the external facing unique identifier of the holding
     */
    public abstract String getExportIdentifier();

    /**
     * @return monetary value of holding
     */
    public abstract float getValue();

    /**
     * increase the value of the holding
     * @param value the amount of monetary value to add
     */
    public abstract void addValue(float value);

    /**
     * decrease the value of the holding
     * @param value the amount of monetary value to remove
     */
    public abstract void removeValue(float value);

    /**
     * @return a descriptor of the holding type;
     */
    public abstract String getType();

    /**
     * Get the type-id hash for a holding
     * @param holding a holding
     * @return type-id hash: [Type][ID], ex. C12 or E333
     */
    static String serializeHolding(Holding holding){
        if(holding == null){
            return "";
        }

        return holding instanceof CashAccount ? "C" + holding.id : "E" + holding.id;
    }

    /**
     * Locate a holding based on its ID hash
     * @param hash a holding ID hash, ex. E23
     * @return the cash account or equity holding
     */
    static Holding deserializeHolding(String hash){
        if(hash.length() == 0){
            return null;
        }

        String id = hash.substring(1);
        Class type = hash.charAt(0) == 'C' ? CashAccount.class : Equity.class;
        return Holding.class.cast(FPTSData.getDataRoot().getInstanceById(type, id));
    }
}
