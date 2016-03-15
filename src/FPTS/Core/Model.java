package FPTS.Core;

import FPTS.Data.FPTSData;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Observable;

/**
 * @author: Greg
 * Created: 3/9/16
 * Revised: 3/13/16
 * Description: A model instance encapsulates a related set of parameters.
 * The abstract model is responsible for generating an a unique
 * id for each new instance and providing a core set of functions
 * for interacting with models
 */
public abstract class Model extends Observable {
    public String id;
    public boolean isPersistent = true;

    private static int incrementId = 0;
    private static String incrementIdCache = "modelId.config";

    public Model(String _id) {
        id = _id;
        setChanged();
    }

    public Model() {
        id = Integer.toString(autoIncrementId());
        setChanged();
    }

    protected <T extends Model> T findById(Class<T> type, String id){
        return FPTSData.getDataRoot().getInstanceById(type, id);
    }

    public void save(){
        notifyObservers();
    }

    public void save(boolean addInstance){
        if(addInstance && FPTSData.getDataRoot().getInstanceById(this.getClass(), this.id) == null){
            FPTSData.getDataRoot().addInstance(this);
        }
        save();
    }

    public boolean getIsPersistent() {
        return isPersistent;
    }

    private int autoIncrementId() {
        if(incrementId == 0) {
            Path configFile = Paths.get(FPTSData.dataPath, incrementIdCache);
            try {
                if(Files.isReadable(configFile)){
                    byte[] data = Files.readAllBytes(configFile);
                    incrementId = ByteBuffer.wrap(data).getInt();
                }
                else {
                    incrementId = 1;
                }
            } catch (IOException ex) {
                System.out.println("WARNING: Failed to load Model ID cache file: " + configFile);
                System.out.println(ex.getMessage());
            }
        }
        else {
            incrementId++;
        }

        writeAutoIncrementId(incrementId);
        return incrementId;
    }

    private void writeAutoIncrementId(int id) {
        byte[] bytes = ByteBuffer.allocate(4).putInt(id).array();
        try {
            Path configFile = Paths.get(FPTSData.dataPath, incrementIdCache);
            Files.write(configFile, bytes);
        } catch (IOException ex) {
            System.out.println("WARNING: Failed to save Model ID cache file.");
            System.out.println(ex.getMessage());
        }
    }
}