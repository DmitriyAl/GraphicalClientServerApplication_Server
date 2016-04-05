package main.controller;

import main.model.GraphicalMode;
import main.model.IModel;
import main.model.ServerStatus;
import main.view.IView;

/**
 * @author Dmitriy Albot
 */
public class Controller implements IController {
    private IModel model;
    private IView view;

    public Controller(IModel model, IView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public ServerStatus startServer(final GraphicalMode mode) {
        Thread startServerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                model.startServer(mode);
            }
        });
        startServerThread.start();
        return null;
    }

    @Override
    public ServerStatus stopServer() {
        model.stopServer();
        return null;
    }

    @Override
    public ServerStatus pauseServer() {
        model.pauseServer();
        return null;
    }

    @Override
    public ServerStatus continueServer() {
        model.continueServer();
        return null;
    }
}
