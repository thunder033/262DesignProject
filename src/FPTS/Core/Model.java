package FPTS.Core;

import java.util.Observable;

/**
 * Created by Greg on 3/9/2016.
 */
public class Model extends Observable {
    public String id;

    public Model(String _id)
    {
        id = _id;
    }

    public void save(){
        notifyObservers();
    }
}