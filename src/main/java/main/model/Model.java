package main.model;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Dmitriy Albot
 */
public class Model implements IModel {
    private Painter painter;
    private List<Observer> observers;
    private List<PrintWriter> writers;
    private volatile boolean isPaused;
    private volatile boolean isStopped;
    private ServerStatus status;
    private final Object lock = new Object();

    public Model() {
        this.status = ServerStatus.OK;
        observers = new ArrayList<>();
        writers = new ArrayList<>();
    }

    public class RequestHandler implements Runnable {

        @Override
        public void run() {
            while (true) {
                synchronized (lock) {
                    String command = painter.startPainting();
                    System.out.println(command);
                    tellClients(command);
                    if (command == null) {
                        break;
                    }
                }
            }
            status = ServerStatus.FINISHED;
            notifyModelObservers();
        }
    }

    private void tellClients(String command) {
        Iterator<PrintWriter> iterator = writers.iterator();
        while (iterator.hasNext()) {
            PrintWriter writer = iterator.next();
            writer.println(command);
            writer.flush();
        }
    }

    @Override
    public ServerStatus getStatus() {
        return status;
    }

    @Override
    public void startServer(GraphicalMode mode) {
        establishConnection(mode);
    }

    private void establishConnection(GraphicalMode mode) {
        isStopped = false;
        painter = PainterFactory.getPainter(mode);
        try (ServerSocket serverSocket = new ServerSocket(29228)) {
            while (!isStopped) {
                status = ServerStatus.OK;
                notifyModelObservers();
                Socket socket = serverSocket.accept();
                PrintWriter writer = null;
                try {
                    writer = new PrintWriter(socket.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                writers.add(writer);
                new Thread(new RequestHandler()).start();
            }
        } catch (IOException e) {
            status = ServerStatus.ERROR;
            notifyModelObservers();
            System.out.println("Server socket is unavailable");
        }
    }

    @Override
    public void pauseServer() {
        isPaused = true;
    }

    @Override
    public void continueServer() {
        isPaused = false;
        lock.notifyAll();
    }

    @Override
    public void stopServer() {
        isStopped = true;
    }

    @Override
    public void notifyModelObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }
}
