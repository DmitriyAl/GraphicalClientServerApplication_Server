package main.view;

import main.controller.IController;
import main.model.GraphicalMode;
import main.model.IModel;
import main.model.Observer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
    private JButton startSendingButton;
    private JRadioButton pauseButton;
    private JComboBox<GraphicalMode> graphicalMode;
    private JTextField port;
    private JLabel status;
    private JLabel sendingDelay;
    private JSlider delaySlider;
    private static View instance;

    private View(IModel model) {
        this.model = model;
        this.model.addObserver(this);
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
        startSendingButton = new JButton("Start server");
        pauseButton = new JRadioButton("Pause");
        pauseButton.setEnabled(false);
        delaySlider = new JSlider(SwingConstants.HORIZONTAL, 0, 500, 50);
        delaySlider.setPreferredSize(new Dimension(100, 20));
        delaySlider.setPaintTicks(true);
        delaySlider.setPaintTrack(true);
        delaySlider.setSnapToTicks(true);
        port = new JTextField("29288");
        status = new JLabel();
        sendingDelay = new JLabel("delay " + delaySlider.getValue() + " ms");
        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addComponent(graphicalMode)
                        .addComponent(sendingDelay)
                        .addComponent(delaySlider))
                .addGroup(layout.createSequentialGroup()
                        .addComponent(port)
                        .addComponent(startSendingButton)
                        .addComponent(pauseButton))
                .addComponent(status));
        layout.linkSize(SwingConstants.HORIZONTAL, startSendingButton);
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(graphicalMode)
                        .addComponent(sendingDelay)
                        .addComponent(delaySlider))
                .addGroup(layout.createParallelGroup()
                        .addComponent(port)
                        .addComponent(startSendingButton)
                        .addComponent(pauseButton))
                .addComponent(status));
        status.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 5));
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        buttonListenersConfig();
    }

    private void buttonListenersConfig() {
        startSendingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSettings();
            }
        });
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pauseButton.isSelected()) {
                    controller.pauseServer();
                } else {
                    controller.resumeServer();
                }
            }
        });
        delaySlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int delay = delaySlider.getValue();
                sendingDelay.setText("delay " + delay + " ms");
                controller.setSendingSpeed(delay);
            }
        });
    }

    private void startSettings() {
        String portNumber = port.getText();
        boolean isCorrect = controller.setPort(portNumber);
        if (!isCorrect) {
            status.setText("Incorrect port");
            return;
        }
        controller.setSendingSpeed(delaySlider.getValue());
        controller.startServer((GraphicalMode) graphicalMode.getSelectedItem());
        graphicalMode.setEnabled(false);
        startSendingButton.setEnabled(false);
        pauseButton.setEnabled(true);
        port.setEnabled(false);
    }

    @Override
    public void update() {
        switch (model.getStatus()) {
            case FINISHED:
                graphicalMode.setEnabled(true);
                startSendingButton.setEnabled(true);
                pauseButton.setEnabled(false);
                port.setEnabled(true);
            default:
                status.setText("Server status: " + model.getStatus().toString());
                break;
        }
    }
}
