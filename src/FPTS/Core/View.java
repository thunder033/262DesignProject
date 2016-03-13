package FPTS.Core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

/**
 * Created by Greg on 3/12/2016.
 * A view is a definition of discrete segment of functionality provided
 * by the system to users. The view associates controllers with GUI components
 * and handles switching between views.
 */
public abstract class View {
    static String fxmlPath = "/assets";

    protected Controller _controller;
    protected Scene _scene;
    protected String _fxmlName;
    protected FPTSApp _app;

    public int width = 800;
    public int height = 600;

    public View(FPTSApp app)
    {
        _app = app;
    }

    public void Load()
    {
        _controller.Load(_app, _controller._portfolio);
    }

    public void Exit() {
        _controller.Exit();
    }

    public Scene getScene() {
        Scene scene = null;
        try {
            if(_fxmlName != null) {
                String path = Paths.get(fxmlPath, _fxmlName).toString();
                URL url = _app.getClass().getResource("/assets/portfolio.fxml");

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
        }

        return scene;
    }
}
