package main.model;

/**
 * @author Dmitriy Albot
 */
public interface IModel {
    ServerAnswer startServer(GraphicalMode mode);

    ServerAnswer pauseServer();

    ServerAnswer stopServer();

    void notifyClients();

    void addObserver(Observer observer);
}
