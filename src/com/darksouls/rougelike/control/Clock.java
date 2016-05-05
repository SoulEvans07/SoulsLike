package com.darksouls.rougelike.control;

import com.darksouls.rougelike.view.GamePanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Clock {
    private static int counter;
    private static Timer timer;

    public static void tick() {
        counter++;
        // tick game elements
        GamePanel.getInstance().tick();
    }

    // only for animation
    public static void canvasTick(){
        if(GamePanel.getInstance().getCanvas().getGraphics() != null) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            GamePanel.getInstance().getCanvas().paint(GamePanel.getInstance().getCanvas().getGraphics());
        }
    }

    public static void animationTick(){
        if(GamePanel.getInstance().getCanvas().getGraphics() != null) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            GamePanel.getInstance().getCanvas().paint(GamePanel.getInstance().getCanvas().getGraphics());
        }
    }

    // TODO: no need for it yet
    //public static void set(){
    //    counter = 0;
    //    timer = new Timer(Reference.TIMER_TICK, new action());
    //}

    public static void pauseClock(){
        timer.stop();
    }

    public static void startClock(){
        timer.start();
    }

    public static void stopClock(){
        timer.stop();
        counter = 0;
    }

    public static int getTime(){
        return counter;
    }

    private static class action implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            tick();
        }
    }
}