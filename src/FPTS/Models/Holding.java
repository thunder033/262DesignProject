package FPTS.Models;

/**
 * Created by gjr8050 on 3/10/2016.
 */
public interface Holding {
    String getName();
    float getValue();
    void addValue(float value);
    void removeValue(float value);
    String getType();
}
