package main.model;

import java.io.*;

/**
 * @author Dmitriy Albot
 */
public class TestPainter implements Painter {
    private BufferedReader br;
    private int sendingSpeed;

    public TestPainter() {
        init();
    }

    private void init() {
        InputStream in = getClass().getResourceAsStream("/test.txt");
        br = new BufferedReader(new InputStreamReader(in));
    }

    @Override
    public String startPainting() {
        try {
            String result = br.readLine();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
