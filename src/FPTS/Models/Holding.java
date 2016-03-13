package FPTS.Models;

/**
 * Created by gjr8050 on 3/10/2016.
 * Interface for managing holdings in a portfolio
 */
public interface Holding {
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
