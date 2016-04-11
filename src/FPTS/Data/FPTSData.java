package FPTS.Data;


import FPTS.Core.Model;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: Greg
 * Created: 3/9/16
 * Revised: 3/13/16
 * Description: Provides an interface
 * for retrieving persisted data models, managing
 * model instances, and writing changes to models.
 */
public class FPTSData implements Observer {

    static public String dataPath = "data/";
    static private Map<Class, DataBin> bins = new HashMap<>();
    static private FPTSData _instance;
    
    protected FPTSData() {

    }

    public static FPTSData getDataRoot() {
        if(_instance == null) {
            _instance = new FPTSData();
        }

        return _instance;
    }

    /**
     * Imports the DataBin instance into the data index, and invokes the bin load operation
     * @param bin the bin to import
     */
    public void importBin(DataBin bin) {
        bins.put(bin.dataClass, bin);
        bin.loadInstances();
        System.out.println("Loaded " + bin.getAll().size() + " [" + bin.dataClass.toString() + "] instances into [" + bin.getClass().toString() +"] from " + bin.fileName);
    }

    /**
     * Load all defined model data bins
     */
    public void loadBins(List<Class<? extends DataBin>> binTypes) {
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
     * Invokes the write operation for the bin with the given type, saving all changes
     * @param type the bin type to write to
     * @param <T> A model sub-class with an associated DataBin
     */
    public <T extends Model> void writeBin(Class<T> type){
        bins.get(type).writeInstances();
    }

    public void writeAll(){
        bins.values().forEach(DataBin::writeInstances);
    }

    @Override
    public void update(Observable o, Object arg) {
        bins.get(o.getClass()).writeInstances();
    }

    /**
     * Get a model instance by its Class and ID
     *
     * @param type the type of model instance
     * @param id   the ID of the instance
     * @param <T>  a subclass of Model
     * @return a model instance or null if the bin/model doesn't exist
     */
    public <T extends Model> T getInstanceById(Class<T> type, String id) {
        //get the appropriate data bin by type and then request an instance by ID
        DataBin bin = bins.get(type);
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
    public <T extends Model> ArrayList<T> getInstances(Class<T> type) {
        //Get all instances the given type
        ArrayList<Model> models = bins.get(type).getAll();
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
    public <T extends Model> ArrayList<T> getInstances(Class<T> type, String[] ids) {
        ArrayList<String> idsList = new ArrayList<>(Arrays.asList(ids));
        //create a stream from the arrayList
        return getInstances(type).stream()
                //filter out instances that are not contained in the array of ids
                .filter(e -> idsList.contains(e.id))
                //collect the stream into an arrayList
                .collect(Collectors.toCollection(ArrayList<T>::new));
    }

    /**
     * Add an instance to the appropriate data bin
     * @param instance the instance to add
     * @param <T> a subclass of model
     */
    public <T extends Model> void addInstance(T instance) {
        bins.get(instance.getClass()).addInstance(instance);
    }

    /**
     * Remove an instance from the appropriate data bin
     * @param instance the instance to remove
     * @param <T> a subclass of Model
     */
    public <T extends Model> void deleteInstance(T instance) {
        bins.get(instance.getClass()).removeInstance(instance);
    }
}
