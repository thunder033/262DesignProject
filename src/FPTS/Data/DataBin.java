package FPTS.Data;

import FPTS.Core.Model;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: Greg
 * Created: 3/11/16
 * Revised: 3/13/16
 * Description: Stores a map of model
 * instances and provides an interface
 * for persistence and retrieval.
 */
public abstract class DataBin {

    public final class ModelInitializer {
        public final Date dateCreated;
        public final Date dateDeleted;

        ModelInitializer(long dateCreated, long dateDeleted) {
            this.dateCreated = dateCreated == 0 ? null : new Date(dateCreated);
            this.dateDeleted = dateDeleted == 0 ? null : new Date(dateDeleted);
        }
    }

    protected String fileName;
    protected Class<?> dataClass;
    protected boolean useFullValueArray = false;

    private Map<String, Model> instanceMap;

    private String deletedFlag = "1";

    protected DataBin() {
        instanceMap = new HashMap<>();
    }

    /**
     * Get all model instances stored in the bin
     * @return a list of model instance
     */
    ArrayList<Model> getAll() {
        return instanceMap.values().stream()
                .filter(i -> !i.isDeleted())
                .collect(Collectors.toCollection(ArrayList<Model>::new));
    }

    /**
     * Retrieve a model instance by it's ID
     * @param id the id of model
     * @return the model instance or null
     */
    Model getByID(String id) {
        return instanceMap.get(id);
    }

    /**
     * Load model instance into the data bin
     */
    final void loadInstances() {
        Path filePath = Paths.get(FPTSData.dataPath, fileName);
        try {
            //create a new CSV interface and read data
            CSV csv = new CSV(filePath);
            String[][] data = csv.Read();
            //create a new model instance for each line of the CSV
            for (String[] line : data) {

                int size = useFullValueArray ? line.length : line.length - 2;
                Model instance = fromValueArray(Arrays.copyOf(line, size));

                //If a model is initialized as not persistent, don't look for created/deleted dates
                if(instance.isPersistent){
                    long createdDate = Long.parseLong(line[line.length - 2]);
                    long deletedDate = Long.parseLong(line[line.length - 1]);
                    instance.Load(new ModelInitializer(createdDate, deletedDate));
                }

                instance.ignoreChanges();
                addInstance(instance);
            }
        } catch (IOException ex) {
            System.out.println("WARNING: " + getClass().toString() + " failed to read file at " + filePath);
            ex.printStackTrace();
        }
    }

    private String[] buildValueArray(Model model){
        String[] values =  toValueArray(model);
        String[] modelValues = new String[values.length + 1];
        System.arraycopy(values, 0, modelValues, 0, values.length);
        modelValues[values.length] = Long.toString(model.getDateCreated().getTime());
        modelValues[values.length] = model.isDeleted() ? Long.toString(model.getDateDeleted().getTime()) : "0";
        return modelValues;
    }

    /**
     * Serialize all model instances in the bin and output the CSV file
     */
    final void writeInstances()
    {
        Path filePath = Paths.get(FPTSData.dataPath, fileName);
        System.out.println("Write instances from " + getClass().toString() + " to " + filePath.toString());
        try {
            //Create a new CSV interface
            CSV csv = new CSV(filePath);
            //serialize all model instances in the Bin
            ArrayList<String[]> serializedInstances = new ArrayList<>();
            instanceMap.values().stream()
                    .filter(Model::getIsPersistent)
                    .map(this::buildValueArray)
                    .forEach(serializedInstances::add);
            //convert to an array and write to CSV file
            String[][] data = new String[serializedInstances.size()][];
            data = serializedInstances.toArray(data);
            csv.Write(data);
        } catch (IOException ex) {
            System.out.println("WARNING: " + getClass().toString() + " failed to write to file at " + filePath);
        }
    }

    /**
     * Add a new model instance to the bin and observe it
     * @param element the instance to add
     */
    protected void addInstance(Model element) {
        instanceMap.put(element.id, element);
        element.addObserver(FPTSData.getDataRoot());
    }

    /**
     * Remove a model instance from the bin and stop observing it
     * @param element the instance to remove
     */
    protected void removeInstance(Model element) {
        element.deleteObserver(FPTSData.getDataRoot());
        instanceMap.remove(element.id);
    }

    /**
     * Logic to parse a values array into a model instance
     * @param values an array of string values read from CSV
     * @return a model created from the values array
     */
    public abstract Model fromValueArray(String[] values);

    /**
     * Logic to serialize a model instance into a string values array
     * @param instance instance to serialize
     * @return an array of string values representing the model
     */
    public abstract String[] toValueArray(Model instance);
}
