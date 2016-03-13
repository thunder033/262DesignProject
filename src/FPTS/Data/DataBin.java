package FPTS.Data;

import FPTS.Core.Model;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Greg on 3/9/2016.
 */
public abstract class DataBin {
    protected String fileName;
    protected Class<?> dataClass;

    private Map<String, Model> instanceMap;

    public DataBin() {
        instanceMap = new HashMap<>();
    }

    public ArrayList<Model> getAll() {
        return new ArrayList<>(instanceMap.values());
    }

    public Model getByID(String id) {
        return instanceMap.get(id);
    }

    public final void loadInstances() {
        Path filePath = Paths.get(FPTSData.dataPath, fileName);
        try {
            CSV csv = new CSV(filePath);
            String[][] data = csv.Read();
            for (String[] line : data) {
                addInstance(fromCSV(line));
            }
        } catch (IOException ex) {
            System.out.println("WARNING: " + getClass().toString() + " failed to read file at " + filePath);
        }
    }

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

    protected void addInstance(Model element) {
        instanceMap.put(element.id, element);
        element.addObserver(FPTSData.getDataRoot());
    }

    protected void removeInstance(Model element) {
        element.deleteObserver(FPTSData.getDataRoot());
        instanceMap.remove(element.id);
    }

    public abstract Model fromCSV(String[] values);

    public abstract String[] toCSV(Model instance);
}
