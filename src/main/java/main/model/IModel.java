package main.model;

/**
 * @author Dmitriy Albot
 */
public interface IModel {
    void startServer(GraphicalMode mode);

    ServerStatus getStatus();

    void notifyModelObservers();

    void addObserver(Observer observer);
}
