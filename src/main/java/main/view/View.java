package main.view;

import main.controller.IController;
import main.model.GraphicalMode;
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
    private JFrame frame;
    private JPanel background;
    private JButton typeConfirmButton;
    private JButton startButton;
    private JRadioButton pauseButton;
    private JButton stopButton;
    private JComboBox<GraphicalMode> graphicalMode;
    private JLabel status;
    private static View instance;

    private View() {
        initGraphics();
    }

    public static View getInstance() {
        if (instance == null) {
            instance = new View();
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
        background = new JPanel();
        GroupLayout layout = new GroupLayout(background);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        background.setLayout(layout);
        graphicalMode = new JComboBox<>(GraphicalMode.values());
        typeConfirmButton = new JButton("Confirm painting mode");
        startButton = new JButton("Start server");
        pauseButton = new JRadioButton("Pause server");
        stopButton = new JButton("Stop server");
        startButton.setEnabled(false);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);
        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addComponent(graphicalMode)
                        .addComponent(typeConfirmButton))
                .addGroup(layout.createSequentialGroup()
                        .addComponent(startButton)
                        .addComponent(pauseButton)
                        .addComponent(stopButton)));
        layout.linkSize(SwingConstants.HORIZONTAL, graphicalMode, typeConfirmButton);
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(graphicalMode)
                        .addComponent(typeConfirmButton))
                .addGroup(layout.createParallelGroup()
                        .addComponent(startButton)
                        .addComponent(pauseButton)
                        .addComponent(stopButton)));
        status = new JLabel();
        status.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        frame.add(BorderLayout.CENTER, background);
        frame.add(BorderLayout.SOUTH, status);
        frame.setResizable(false);
        frame.setSize(380, 130);
        frame.setVisible(true);
        buttonListenersConfig();
    }

    private void buttonListenersConfig() {
        typeConfirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graphicalMode.setEnabled(false);
                typeConfirmButton.setEnabled(false);
                startButton.setEnabled(true);
                pauseButton.setEnabled(true);
                stopButton.setEnabled(true);
            }
        });
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.startServer((GraphicalMode) graphicalMode.getSelectedItem());
                startButton.setEnabled(false);
            }
        });
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JRadioButton source = (JRadioButton) e.getSource();
                if (source.isSelected()) {
                    controller.pauseServer();
                    status.setText("Server paused");
                } else {
                    controller.continueServer();
                    status.setText("Server continue working");
                }
            }
        });
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.stopServer();
                graphicalMode.setEnabled(true);
                typeConfirmButton.setEnabled(true);
                startButton.setEnabled(false);
                pauseButton.setEnabled(false);
                stopButton.setEnabled(false);
            }
        });
    }

    @Override
    public void update() {
    }
}
