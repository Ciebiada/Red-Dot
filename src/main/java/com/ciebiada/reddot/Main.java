/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot;

import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

	private JFrame frame;
	private JLabel label;
    private JScrollPane scrollPane;
    private Timer timer;
    private JPanel statusBar;
    private JLabel status;
    private long timestamp = System.currentTimeMillis();
    private long samplestamp;
    private String pathToSave;
    private Scene scene;

    public Main(String pathToOpen) throws IOException, SAXException, ParserConfigurationException {
        if (!pathToOpen.endsWith(".xml")) {
            pathToOpen = pathToOpen.substring(0, pathToOpen.lastIndexOf('/'));
            pathToOpen += pathToOpen.substring(pathToOpen.lastIndexOf('/'), pathToOpen.length()) + ".xml";
        }

        scene = new Scene(pathToOpen);
        setupThePathToSave(pathToOpen);

		setupTheWindow();
        updateImage();
        frame.pack();
        setupTimer();

        scene.render();
    }

    private void setupThePathToSave(String pathToOpen) {
        String filename = pathToOpen.substring(pathToOpen.lastIndexOf('/') + 1, pathToOpen.lastIndexOf('.')) + ".png";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmm");
        pathToSave = "output/" + sdf.format(new Date()) + "_" + filename;
    }

    private void setupTimer() {
        ActionListener repaint = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateImage();
                updateStatus();
            }
        };

        timer = new Timer(7000, repaint);
        timer.setInitialDelay(5000);
        timer.start();
    }

    private void setupTheWindow() {
        frame = new JFrame("Red Dot");
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        label = new JLabel(new ImageIcon());
        scrollPane = new JScrollPane(label);

        statusBar = new JPanel(new FlowLayout(FlowLayout.LEADING));

        status = new JLabel();
        status.setText("Waiting for update...");
        statusBar.add(status);


        frame.getContentPane().add(scrollPane);
        frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
    }    private void updateStatus() {
        long time = System.currentTimeMillis();
        long samples = 0;//scene.film.getSamples();

        long elapsedTime = (time - timestamp) / 1000;
        status.setText("Samples per second: " + ((samples - samplestamp) / elapsedTime));

        timestamp = time;
        samplestamp = samples;
    }

	public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main(args[0]);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
    }

    private void updateImage() {
        BufferedImage image = scene.film.getImage();
        label.setIcon(new ImageIcon(image));

        try {
            ImageIO.write(image, "png", new File(pathToSave));
        } catch (IOException e) {
            System.out.println("Unable to save the file output.png");
        }
    }




}
