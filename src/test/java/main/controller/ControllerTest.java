package main.controller;

import main.model.Model;
import main.view.View;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author Dmitriy Albot
 */
public class ControllerTest {
    @Test
    public void setPortTest() {
        View view = Mockito.mock(View.class);
        Model model = Mockito.mock(Model.class);
        Controller controller = new Controller(model,view);
        Assert.assertTrue(controller.setPort("0"));
        Assert.assertTrue(controller.setPort("65535"));
        Assert.assertFalse(controller.setPort("-1"));
        Assert.assertFalse(controller.setPort("65536"));
    }
}
