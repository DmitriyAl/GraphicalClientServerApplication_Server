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
    private int port;
    private Painter painter;
    private List<Observer> observers;
    private List<PrintWriter> writers;
    private ServerStatus status;
    private ServerSocket serverSocket;
    private final Object lock = new Object();
    private volatile boolean isPaused;
    private int sendingSpeed;

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
                    if (isPaused) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(sendingSpeed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String command = painter.startPainting();
                    tellClients(command);
                    if (command == null) {
                        break;
                    }
                    System.out.println(command); //todo delete
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
        establishConnection();
        startSendingMessage(mode);
    }

    @Override
    public void pauseServer() {
        isPaused = true;
    }

    @Override
    public void resumeServer() {
        synchronized (lock) {
            isPaused = false;
            lock.notifyAll();
        }
    }

    private void establishConnection() {
        if (serverSocket == null) {
            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException e) {
                status = ServerStatus.ERROR;
                notifyModelObservers();
//                e.printStackTrace(); //todo logging
            }
        }
    }

    private void startSendingMessage(GraphicalMode mode) {
        painter = PainterFactory.getPainter(mode);
        try {
            while (true) {
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
            System.out.println("Server socket is unavailable"); //todo delete
        }
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

    @Override
    public void setSendingSpeed(int speed) {
        this.sendingSpeed = speed;
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }
}
