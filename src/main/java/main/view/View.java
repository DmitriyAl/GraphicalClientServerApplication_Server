package main.view;

import main.controller.IController;
import main.model.GraphicalMode;
import main.model.IModel;
import main.model.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Dmitriy Albot
 */
public class View implements IView, Observer {
    private IController controller;
    private IModel model;
    private JFrame frame;
    private JPanel background;
    private JButton startButton;
    private JRadioButton pauseButton;
    private JButton stopButton;
    private JComboBox<GraphicalMode> graphicalMode;
    private JLabel status;
    private static View instance;

    private View(IModel model) {
        this.model = model;
        initGraphics();
    }

    public static View getInstance(IModel model) {
        if (instance == null) {
            instance = new View(model);
        }
        return instance;
    }

    @Override
    public void setController(IController controller) {
        this.controller = controller;
    }

    private void initGraphics() {
        frame = new JFrame("Graphical Server");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        GroupLayout layout = new GroupLayout(frame.getContentPane());
        frame.getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        graphicalMode = new JComboBox<>(GraphicalMode.values());
        startButton = new JButton("Start server");
        pauseButton = new JRadioButton("Pause server");
        stopButton = new JButton("Stop server");
        status = new JLabel("Ok");
        startButton.setEnabled(true);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);
        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addComponent(graphicalMode)
                        .addComponent(pauseButton))
                .addGroup(layout.createSequentialGroup()
                        .addComponent(startButton)
                        .addComponent(stopButton))
                .addComponent(status));
        layout.linkSize(SwingConstants.HORIZONTAL, graphicalMode, pauseButton);
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(graphicalMode)
                        .addComponent(pauseButton))
                .addGroup(layout.createParallelGroup()
                        .addComponent(startButton)
                        .addComponent(stopButton))
                .addComponent(status));
        status = new JLabel();
        status.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        buttonListenersConfig();
    }

    private void buttonListenersConfig() {
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.startServer((GraphicalMode) graphicalMode.getSelectedItem());
                graphicalMode.setEnabled(false);
                startButton.setEnabled(false);
                pauseButton.setEnabled(true);
                stopButton.setEnabled(true);
            }
        });
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pauseButton.isSelected()) {
                    controller.pauseServer();
                } else {
                    controller.continueServer();
                }
            }
        });
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.stopServer();
                graphicalMode.setEnabled(true);
                startButton.setEnabled(true);
                pauseButton.setEnabled(false);
                stopButton.setEnabled(false);
            }
        });
    }

    @Override
    public void update() {

    }
}
