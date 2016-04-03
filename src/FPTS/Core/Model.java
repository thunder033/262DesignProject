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
    private boolean deleted = false;

    private static int incrementId = 0;
    private static String incrementIdCache = "modelId.config";
    private FPTSData dataRoot;

    public Model(String _id) {
        dataRoot = FPTSData.getDataRoot();
        this.id = _id;
        setChanged();
    }

    public Model() {
        dataRoot = FPTSData.getDataRoot();
        id = Integer.toString(autoIncrementId());
        setChanged();
    }

    protected <T extends Model> T findById(Class<T> type, String id){
        return dataRoot.getInstanceById(type, id);
    }

    public void save(){
        Model indexedInstance = findById(this.getClass(), this.id);
        if(isPersistent && indexedInstance == null){
            System.out.println("Add instance " + this);
            dataRoot.addInstance(this);
        }
        else if(!isPersistent && indexedInstance != null){
            dataRoot.deleteInstance(this);
        }
        notifyObservers();
    }

    public void setDeleted()
    {
        deleted = true;
    }

    public void delete(){
        deleted = true;
        setChanged();
        save();
    }

    public boolean isDeleted(){
        return deleted;
    }

    public void ignoreChanges(){
        clearChanged();
    }

    public void saveModels(Class<? extends Model> type){
        dataRoot.writeBin(type);
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
                    System.out.println("Read cached increment ID " + incrementId);
                }
                else {
                    incrementId = 1;
                }
            } catch (IOException ex) {
                System.out.println("WARNING: Failed to load Model ID cache file: " + configFile);
                System.out.println(ex.getMessage());
            }
        }

        incrementId++;

        System.out.println("Create model " + incrementId);
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