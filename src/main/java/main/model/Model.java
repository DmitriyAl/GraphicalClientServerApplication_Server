package main.model;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Dmitriy Albot
 */
public class Model implements IModel {
    private int port;
    private int sendingSpeed;
    private Painter painter;
    private List<Observer> observers;
    private List<PrintWriter> writers;
    private ServerStatus status;
    private ServerSocket serverSocket;
    private volatile boolean isPaused;
    private final Object lock = new Object();
    private static Logger log = Logger.getLogger(Model.class);

    public Model() {
        this.status = ServerStatus.OK;
        observers = new ArrayList<>();
        writers = new ArrayList<>();
    }

    @Override
    public ServerStatus getStatus() {
        return status;
    }

    @Override
    public void setSendingSpeed(int speed) {
        this.sendingSpeed = speed;
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void startServer(GraphicalMode mode) {
        establishConnection();
        startSendingMessage(mode);
    }

    @Override
    public void pauseServer() {
        isPaused = true;
        log.info("Server is paused");
    }

    @Override
    public void resumeServer() {
        synchronized (lock) {
            isPaused = false;
            lock.notifyAll();
            log.info("Server is resumed");
        }
    }

    private void establishConnection() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                log.error("Failure to close server socket", e);
            }
        }
        try {
            serverSocket = new ServerSocket(port);
            log.info("Server socket port:" + port + " is opened");
        } catch (IOException e) {
            status = ServerStatus.ERROR;
            notifyModelObservers();
            log.error("Failure to open service socket", e);
        }
    }

    private void startSendingMessage(GraphicalMode mode) {
        painter = PainterFactory.getPainter(mode);
        try {
            while (true) {
                status = ServerStatus.OK;
                notifyModelObservers();
                Socket socket = serverSocket.accept();
                try {
                    PrintWriter writer = new PrintWriter(socket.getOutputStream());
                    writers.add(writer);
                    SocketAddress remoteSocketAddress = socket.getRemoteSocketAddress();
                    log.info("Got connection from " + remoteSocketAddress.toString());
                } catch (IOException e) {
                    log.warn("Failure to open a new PrintWriter to client", e);
                }
                new Thread(new RequestHandler()).start();
            }
        } catch (IOException e) {
            log.info("Server socket port:" + port + " is closed");
        }
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
                            log.warn("Failure to pause the server", e);
                        }
                    }
                    try {
                        Thread.sleep(sendingSpeed);
                    } catch (InterruptedException e) {
                        log.warn("Failure to change command sending speed", e);
                    }
                    String command = painter.startPainting();
                    if (command == null) {
                        break;
                    }
                    tellClients(command);
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
