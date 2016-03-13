package FPTS.Core;

/**
 * Created by Greg on 3/12/2016.
 */
public abstract class View {
    static String fxmlPath;

    Controller _controller;
    String _fxmlName;
    public static FPTSApp _app;

    public View(FPTSApp app)
    {
        _app = app;
    }

    public void Load()
    {

    }

    public void Exit() {
        _controller.Exit();
    }
}
