package main;

import main.controller.Controller;
import main.controller.IController;
import main.model.IModel;
import main.model.Model;
import main.view.IView;
import main.view.View;

/**
 * @author Dmitriy Albot
 */
public class Main {
    public static void main(String[] args) {
        IModel model = new Model();
        IView view = View.getInstance(model);
        IController controller = new Controller(model, view);
        view.setController(controller);
    }
}
