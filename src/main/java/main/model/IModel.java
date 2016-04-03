package main.model;

/**
 * @author Dmitriy Albot
 */
public interface IModel {
    void startServer(GraphicalMode mode);

    ServerAnswer pauseServer();

    ServerAnswer continueServer();

    ServerAnswer stopServer();

    void notifyClients();

    void addObserver(Observer observer);
}
