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
    public void pauseServer() {
        model.pauseServer();
    }

    @Override
    public void resumeServer() {
        Thread resumeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                model.resumeServer();
            }
        });
        resumeThread.start();
    }

    @Override
    public void setSendingSpeed(final int speed) {
        Thread changeSendingSpeedThread = new Thread(new Runnable() {
            @Override
            public void run() {
                model.setSendingSpeed(speed);
            }
        });
        changeSendingSpeedThread.start();
    }

    @Override
    public boolean setPort(String portNumber) {
        int value;
        try {
            value = Integer.parseInt(portNumber);
            if (value < 0 || value > 65535) {
                return false;
            }
            model.setPort(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
