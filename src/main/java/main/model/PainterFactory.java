package main.model;

/**
 * @author Dmitriy Albot
 */
public class PainterFactory {
    public static Painter getGrapher(GraphicalMode mode) {
        switch (mode) {
            case TEST:
                return new TestPainter();
            default:
                throw new NoSuchGrapherException("Model doesn't have this grafer");
        }
    }
}
