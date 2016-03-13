package FPTS.Core;

import FPTS.Data.FPTSData;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Observable;

/**
 * Created by Greg on 3/9/2016.
 */
public abstract class Model extends Observable {
    public String id;
    public boolean isPersistent = true;

    private static int incrementId = 0;
    private static String incrementIdCache = "modelId.config";

    public Model(String _id) {
        id = _id;
    }

    public Model() {
        id = Integer.toString(autoIncrementId());
    }

    public void save(){
        notifyObservers();
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