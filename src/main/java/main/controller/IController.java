package main.controller;

import main.model.GraphicalMode;
import main.model.ServerAnswer;

/**
 * @author Dmitriy Albot
 */
public interface IController {
    ServerAnswer startServer(GraphicalMode mode);

    ServerAnswer stopServer();

    ServerAnswer pauseServer();

    ServerAnswer continueServer();
}

