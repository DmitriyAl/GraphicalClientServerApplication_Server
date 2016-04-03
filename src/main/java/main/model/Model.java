package main.model;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitriy Albot
 */
public class Model implements IModel {
    private Painter painter;
    private List<Observer> observers;
    private volatile boolean isPaused;
    private volatile boolean isStopped;
    private Object lock = new Object();

    public Model() {
        observers = new ArrayList<>();
    }

    @Override
    public void startServer(GraphicalMode mode) {
        isStopped = false;
        painter = PainterFactory.getPainter(mode);
        try (ServerSocket serverSocket = new ServerSocket(29228)) {
            while (!isStopped) {
                Socket socket = serverSocket.accept();
                String command = painter.startPainting();
                PrintWriter writer = new PrintWriter(socket.getOutputStream());
                writer.println(command);
                writer.close();
                System.out.println(command);
                socket.close();
            }
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Server socket is unavailable");
        } finally {
        }
    }

    @Override
    public ServerAnswer pauseServer() {
        isPaused = true;
        return null;
    }

    @Override
    public ServerAnswer continueServer() {
        isPaused = false;
        lock.notifyAll();
        return null;
    }

    @Override
    public ServerAnswer stopServer() {
        isStopped = true;
        return null;
    }

    @Override
    public void notifyClients() {

    }

    @Override
    public void addObserver(Observer observer) {

    }
}
