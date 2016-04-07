package FPTS.Models;

import FPTS.Core.Model;
import FPTS.Data.DataBin;
import FPTS.Data.FPTSData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Greg on 4/5/2016.
 */
public class WatchListBin extends DataBin {

    public WatchListBin(){
        dataClass = WatchList.class;
        fileName = "watchLists.csv";
    }

    @Override
    public Model fromValueArray(String[] values) {

        int i = 0;
        WatchList watchList = new WatchList(values[i++]);
        watchList.setUpdateInterval(Integer.valueOf(values[i++]) / 1000);

        while(i < values.length) {
            MarketEquity equity = FPTSData.getDataRoot().getInstanceById(MarketEquity.class, values[i++]);
            WatchedEquity watchedEquity = new WatchedEquity(equity);
            watchedEquity.lowerTrigger = Float.parseFloat(values[i++]);
            watchedEquity.upperTrigger = Float.parseFloat(values[i++]);
            watchList.loadWatchedEquity(watchedEquity);
        }

        return watchList;
    }

    @Override
    public String[] toValueArray(Model instance) {

        WatchList watchList = WatchList.class.cast(instance);
        ArrayList<String> values = new ArrayList<>();

        values.add(watchList.id);
        values.add(Integer.toString(watchList.getUpdateInterval()));

        watchList.getWatchedEquities().stream()
                .map(WatchedEquity::serialize)
                .map(Arrays::asList)
                .forEach(values::addAll);

        String[] stringVals = new String[values.size()];
        stringVals = values.toArray(stringVals);
        return stringVals;
    }
}
