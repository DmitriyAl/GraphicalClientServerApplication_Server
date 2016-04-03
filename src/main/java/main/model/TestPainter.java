package main.model;

/**
 * @author Dmitriy Albot
 */
public class TestPainter implements Painter {
    @Override
    public void startPainting() {
        for (int i = 0; i < 10; i++) {
            System.out.println(i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void pausePainting() {

    }
}
