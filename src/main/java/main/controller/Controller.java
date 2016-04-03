package main.controller;

import main.model.GraphicalMode;
import main.model.IModel;
import main.model.ServerAnswer;
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
    public ServerAnswer startServer(final GraphicalMode mode) {
        Thread startThread = new Thread(new Runnable() {
            @Override
            public void run() {
                model.startServer(mode);
            }
        });
        startThread.start();
        return null;
    }

    @Override
    public ServerAnswer stopServer() {
        return null;
    }

    @Override
    public ServerAnswer pauseServer() {

        return null;
    }

    @Override
    public ServerAnswer continueServer() {
        System.out.println("continued");
        return null;
    }
}
