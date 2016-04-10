package main.controller;

import main.model.GraphicalMode;
import main.model.ServerStatus;

/**
 * @author Dmitriy Albot
 */
public interface IController {
    void startServer(GraphicalMode mode);

    void pauseServer();

    void resumeServer();

    void setSendingSpeed(int speed);

    boolean setPort(String portNumber);
}

