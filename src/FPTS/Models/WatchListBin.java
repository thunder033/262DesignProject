package FPTS.Models;

import FPTS.Core.Model;
import FPTS.Data.DataBin;

/**
 * Created by Greg on 4/5/2016.
 */
public class WatchListBin extends DataBin {

    public WatchListBin(){
        dataClass = WatchList.class;
        fileName = "watchlists.csv";
    }

    @Override
    public Model fromValueArray(String[] values) {
        return null;
    }

    @Override
    public String[] toValueArray(Model instance) {
        return new String[0];
    }
}
