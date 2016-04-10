package main.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Dmitriy Albot
 */
public class TestPainterTest {
    @Test
    public void readingTest() {
        Assert.assertEquals("60:21:C0:2A:E0:33;start;0.15703125;0.28644067;-16777216", new TestPainter().startPainting());
    }
}
