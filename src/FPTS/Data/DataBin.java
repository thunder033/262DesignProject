package FPTS.Data;

import FPTS.Models.Model;

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

    public DataBin()
    {
        instanceMap = new HashMap<>();
    }

    public ArrayList<Model> getAll()
    {
        return new ArrayList<>(instanceMap.values());
    }

    public Model getByID(String id)
    {
        return instanceMap.get(id);
    }

    public final void loadInstances()
    {
        Path filePath = Paths.get(FPTSData.dataPath, fileName);
        try {
            CSV csv = new CSV(filePath);
            String[][] data = csv.Read();
            for (String[] line : data) {
                addInstance(fromCSV(line));
            }
        }
        catch (IOException ex)
        {
            System.out.println("WARNING: " + getClass().toString() + " failed to read file at " + filePath);
        }
    }

    protected void addInstance(Model element)
    {
        instanceMap.put(element.id, element);
    }

    public abstract Model fromCSV(String[] values);
}
