package FPTS.Core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

/**
 * @author: Greg
 * Created: 3/12/16
 * Revised: 3/13/16
 * Description: A view is a definition of discrete segment of functionality provided
 * by the system to users. The view associates controllers with GUI components
 * and handles switching between views.
 */
public abstract class View {
    static String fxmlPath = "/assets";

    protected Controller _controller;
    protected Scene _scene;
    protected String _fxmlName;
    protected FPTSApp _app;
    protected Stage _stage;

    protected boolean newWindow = false;
    public String title = "ThunderForge FPTS";
    protected int width = 800;
    protected int height = 600;
    public static String name;

    public View(FPTSApp app)
    {
        _app = app;
        _stage = app.getStage();
    }

    public void Load()
    {
        _controller.Load(_app, Controller._portfolio);
    }

    public void Exit() {
        _controller.Exit();
    }

    public Scene getScene() {
        Scene scene = null;
        try {
            if(_fxmlName != null) {
                String path = Paths.get(fxmlPath, _fxmlName).toString();
                URL url = _app.getClass().getResource(path.toString().replace("\\", "/"));

                if(url == null)
                {
                    throw new IOException("No FXML file exists at " + path);
                }

                FXMLLoader loader = new FXMLLoader(url);
                Pane layout = loader.load();
                scene = new Scene(layout, width, height);
                _controller = loader.getController();
                System.out.println(_controller);
            }
            else {
                scene = _scene;
            }
        } catch (IOException ex) {
            System.out.println("Failed to get scene for " + getClass());
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        if(newWindow) {
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.setWidth(width);
            stage.setHeight(height);
            stage.show();
            _stage = stage;
        }

        return scene;
    }
}
