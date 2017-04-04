package com.trophonix.slowclicker;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * Created by Lucas on 4/3/17.
 */
public class SlowClicker extends Canvas implements Runnable {

    private JFrame frame;

    private double full = 0.0;

    public SlowClicker() {
        frame = new JFrame("Slow Clicker");
        setPreferredSize(new Dimension(640, 480));
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        createBufferStrategy(3);
    }

    public synchronized void start() {
        new Thread(this).start();
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

    private void tick() {
        full += 1.0 / 60.0;
        if (full >= 100.0) full = 100.0;
    }

    private void render() {
        BufferStrategy bs = getBufferStrategy();
        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.GREEN);
        int height = (int)((getHeight() / 100.0) * full);
        g.fillRect(0, getHeight() - height, getWidth(), height);
        g.dispose();
        bs.show();
    }

    public static void main(String[] args) {
        new SlowClicker().start();
    }

}
