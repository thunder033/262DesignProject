package FPTS.Data;

import FPTS.Core.Model;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores a map of model instances and provides an interface for persistence and retrieval
 */
public abstract class DataBin {
    protected String fileName;
    protected Class<?> dataClass;

    private Map<String, Model> instanceMap;

    public DataBin() {
        instanceMap = new HashMap<>();
    }

    /**
     * Get all model instances stored in the bin
     * @return a list of model instance
     */
    public ArrayList<Model> getAll() {
        return new ArrayList<>(instanceMap.values());
    }

    /**
     * Retrieve a model instance by it's ID
     * @param id the id of model
     * @return the model instance or null
     */
    public Model getByID(String id) {
        return instanceMap.get(id);
    }

    /**
     * Load model instance into the data bin
     */
    public final void loadInstances() {
        Path filePath = Paths.get(FPTSData.dataPath, fileName);
        try {
            //create a new CSV interface and read data
            CSV csv = new CSV(filePath);
            String[][] data = csv.Read();
            //create a new model instance for each line of the CSV
            for (String[] line : data) {
                addInstance(fromCSV(line));
            }
        } catch (IOException ex) {
            System.out.println("WARNING: " + getClass().toString() + " failed to read file at " + filePath);
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Serialize all model instances in the bin and output the CSV file
     */
    public final void writeInstances()
    {
        Path filePath = Paths.get(FPTSData.dataPath, fileName);
        try {
            //Create a new CSV interface
            CSV csv = new CSV(filePath);
            //serialize all model instances in the Bin
            ArrayList<String[]> serializedInstances = new ArrayList<>();
            instanceMap.values().stream()
                    .filter(Model::getIsPersistent)
                    .map(i -> serializedInstances.add(toCSV(i)));
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
    public abstract Model fromCSV(String[] values);

    /**
     * Logic to serialize a model instance into a string values array
     * @param instance instance to serialize
     * @return an array of string values representing the model
     */
    public abstract String[] toCSV(Model instance);
}
