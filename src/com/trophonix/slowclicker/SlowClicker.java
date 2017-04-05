package com.trophonix.slowclicker;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Lucas on 4/3/17.
 */
public class SlowClicker extends Canvas implements Runnable {

    private JFrame frame;

    private double full = 0.0;
    private int points = 0;

    private BufferedImage clickHere;

    private boolean click = false;

    public SlowClicker() {
        frame = new JFrame("Slow Clicker | POINTS: " + points);
        setPreferredSize(new Dimension(640, 480));
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        createBufferStrategy(3);
        try {
            clickHere = ImageIO.read(getClass().getResource("clickHere.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        MouseListener listener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {

            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                click = true;
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                click = false;
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        };
        addMouseListener(listener);
        frame.addMouseListener(listener);
    }

    public synchronized void start() {
        new Thread(this).start();
    }

    private void tick() {
        full += 1.0 / 60.0;
        if (full >= 100.0) {
            full = 100.0;
            if (click) {
                full = 0;
                points ++;
                frame.setTitle("Slow Clicker | POINTS: " + points);
            }
        }
    }

    private void render() {
        BufferStrategy bs = getBufferStrategy();
        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.GREEN);
        int height = (int)((getHeight() / 100.0) * full);
        g.fillRect(0, getHeight() - height, getWidth(), height);
        if (full >= 100.0) {
            g.drawImage(clickHere, (getWidth() / 2) - (clickHere.getWidth() / 2), (getHeight() / 2) - (clickHere.getHeight() / 2), null);
        }
        g.dispose();
        bs.show();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double delay = 1000000000.0 / 60.0;
        double delta = 0.0;
        long lastPrint = System.nanoTime();
        int tps = 0, fps = 0;
        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / delay;
            lastTime = now;
            while (delta >= 1) {
                tick();
                render();
                tps++;
                fps++;
                delta--;
            }
            if (now > lastPrint + 1000000000) {
                System.out.println("TPS: " + tps + "\nFPS: " + fps);
                tps = 0;
                fps = 0;
                lastPrint = now;
            }
        }
    }

    public static void main(String[] args) {
        new SlowClicker().start();
    }

}
