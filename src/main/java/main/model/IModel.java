package main.model;

/**
 * @author Dmitriy Albot
 */
public interface IModel {
    void startServer(GraphicalMode mode);

    void pauseServer();

    void continueServer();

    void stopServer();

    ServerStatus getStatus();

    void notifyModelObservers();

    void addObserver(Observer observer);
}
