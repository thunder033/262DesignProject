package FPTS.Core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

/**
 * Created by Greg on 3/12/2016.
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
        if(_controller == null) {
            _controller = new Controller();
        }

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

                Pane layout = FXMLLoader.load(url);
                scene = new Scene(layout, width, height);
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
