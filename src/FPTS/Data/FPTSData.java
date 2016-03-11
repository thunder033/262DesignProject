package FPTS.Data;


import FPTS.Models.Model;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Greg on 3/9/2016.
 */
public class FPTSData {

    static public String dataPath = "data/";
    static private Map<Class, DataBin> elementLookup = new HashMap<>();

    protected FPTSData() {

    }

    public static void importBin(DataBin bin) {
        elementLookup.put(bin.dataClass, bin);
        bin.loadInstances();
    }

    /**
     * Load all defined model data bins
     */
    public static void loadBins(List<Class<? extends DataBin>> binTypes) {
        for (Class binType : binTypes) {
            try {
                DataBin bin = (DataBin) binType.newInstance();
                importBin(bin);
            }
            catch (InstantiationException|IllegalAccessException ex) {
                System.out.println("Failed Instantiate DataBin " + binType.toString());
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
     * Get a model instance by its Class and ID
     *
     * @param type the type of model instance
     * @param id   the ID of the instance
     * @param <T>  a subclass of Model
     * @return a model instance or null if the bin/model doesn't exist
     */
    public static <T extends Model> T getInstanceById(Class<T> type, String id) {
        //get the appropriate data bin by type and then request an instance by ID
        DataBin bin = elementLookup.get(type);
        Object instance = bin != null ? bin.getByID(id) : null;
        //finally, cast the instance to the requested type
        return instance != null ? type.cast(instance) : null;
    }

    /**
     * Get a list of all model instances of a given type
     *
     * @param type the type of model instance
     * @param <T>  a subclass of Model
     * @return a list of model instances
     */
    public static <T extends Model> ArrayList<T> getInstances(Class<T> type) {
        //Get all instances the given type
        ArrayList<Model> models = elementLookup.get(type).getAll();
        //create a stream from the arrayList
        return models.stream()
                //cast each element in the stream to the provided type
                .map(model -> model != null ? type.cast(model) : null)
                //collect the stream into an arrayList
                .collect(Collectors.toCollection(ArrayList<T>::new));
    }

    /**
     * Get a list of all instances of the model type, filtered by an array of instance IDs
     *
     * @param type the type of model instance to get
     * @param ids  array of instance IDs
     * @param <T>  a subclass of Model
     * @return a filtered list of Model instances
     */
    public static <T extends Model> ArrayList<T> getInstances(Class<T> type, String[] ids) {
        ArrayList<String> idsList = new ArrayList<>(Arrays.asList(ids));
        //create a stream from the arrayList
        return getInstances(type).stream()
                //filter out instances that are not contained in the array of ids
                .filter(e -> idsList.contains(e.id))
                //collect the stream into an arrayList
                .collect(Collectors.toCollection(ArrayList<T>::new));
    }
}
