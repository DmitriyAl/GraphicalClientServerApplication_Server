package main.model;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Dmitriy Albot
 */
public class TestPainter implements Painter {
    private BufferedReader br;
    private static Logger log = Logger.getLogger(TestPainter.class);

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
            log.trace(result);
            return result;
        } catch (IOException e) {
            log.warn("Failure to read line from file", e);
        }
        return null;
    }
}
