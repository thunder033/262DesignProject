package FPTS.Core;

import javafx.application.Application;

/**
 * Created by Greg on 3/12/2016.
 */
public abstract class View {
    static String fxmlPath;

    Controller _controller;
    String _fxmlName;
    Application _app;

    public View(Application app)
    {
        _app = app;
    }

    public void Load()
    {
        _controller.Load(_app);
    }

    public void Exit() {
        _controller.Exit();
    }
}
