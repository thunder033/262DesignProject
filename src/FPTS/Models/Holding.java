package FPTS.Models;

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
}
