package main.model;

/**
 * @author Dmitriy Albot
 */
public interface IModel {
    void startServer(GraphicalMode mode);

    void pauseServer();

    void resumeServer();

    ServerStatus getStatus();

    void notifyModelObservers();

    void addObserver(Observer observer);
}
