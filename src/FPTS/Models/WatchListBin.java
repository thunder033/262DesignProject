package FPTS.Models;

import FPTS.Core.Model;
import FPTS.Data.DataBin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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

        WatchList watchList = WatchList.class.cast(instance);
        ArrayList<String> values = new ArrayList<>();

        values.add(watchList.id);

        watchList.getWatchedEquities().stream()
                .map(WatchedEquity::serialize)
                .map(Arrays::asList)
                .forEach(values::addAll);

        String[] stringVals = new String[values.size()];
        stringVals = values.toArray(stringVals);
        return stringVals;
    }
}
