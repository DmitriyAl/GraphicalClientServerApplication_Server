package main.model;

/**
 * @author Dmitriy Albot
 */
public class Model implements IModel {
    private Painter painter;

    @Override
    public ServerAnswer startServer(GraphicalMode mode) {
        painter = PainterFactory.getGrapher(mode);
        painter.startPainting();
        return null;
    }

    @Override
    public ServerAnswer pauseServer() {
        return null;
    }

    @Override
    public ServerAnswer stopServer() {
        return null;
    }

    @Override
    public void notifyClients() {

    }

    @Override
    public void addObserver(Observer observer) {

    }
}
