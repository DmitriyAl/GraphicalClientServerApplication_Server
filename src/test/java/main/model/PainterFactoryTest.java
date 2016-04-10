package main.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Dmitriy Albot
 */
public class PainterFactoryTest {
    @Test
    public void factoryTest() {
        Assert.assertTrue(PainterFactory.getPainter(GraphicalMode.TEST) instanceof TestPainter);
    }
}
