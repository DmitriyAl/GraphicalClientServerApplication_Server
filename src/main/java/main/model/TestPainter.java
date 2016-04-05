package main.model;

import java.io.*;

/**
 * @author Dmitriy Albot
 */
public class TestPainter implements Painter {
    private BufferedReader br;

    public TestPainter() {
        init();
    }

    private void init() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("test.txt").getFile());
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String startPainting() {
        try {
            String result = br.readLine();
            try {
                Thread.sleep(42);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
