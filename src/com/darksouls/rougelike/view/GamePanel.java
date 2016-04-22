package com.darksouls.rougelike.view;

import com.darksouls.rougelike.control.Controller;
import com.darksouls.rougelike.model.DungeonLevel;
import com.darksouls.rougelike.model.VPoint;
import com.darksouls.rougelike.references.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// Singleton
public class GamePanel extends JPanel {
    private static GamePanel instance;

    private GameCanvas canvas;
    private DungeonLevel dungeonLevel;

    private GamePanel(){}

    public static GamePanel getInstance(){
        if(instance == null)
            instance = new GamePanel();

        return instance;
    }

    public static GamePanel getNewInstance(){
        instance = new GamePanel();
        instance.init();

        return instance;
    }

    private void init(){
        this.setBackground(Colors.line);
        //game = new Game();
        dungeonLevel = new DungeonLevel();
        dungeonLevel.loadMap();

        setLayout(new BorderLayout());
        canvas = new GameCanvas(dungeonLevel);
        this.add(canvas, BorderLayout.CENTER);
        canvas.paint(GUI.getInstance().getGraphics());

        this.addKeyListener(new escListener());
        this.addKeyListener(Controller.getInstance().getKeyAdapter());
        this.addMouseListener(Controller.getInstance().getMouseAdapter());
        canvas.addMouseListener(Controller.getInstance().getMouseAdapter());
    }

    public GameCanvas getCanvas(){
        return canvas;
    }

    public DungeonLevel getDungeonLevel(){
        return dungeonLevel;
    }

    public void tick(){
        dungeonLevel.tick();
        canvas.repaint();
    }

    public void windowUpdate(VPoint size){
        if(canvas.windowUpdate(size))
            canvas.repaint();
    }

    private class escListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){
            if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                GUI.getInstance().back2Menu();
            }
        }
    }
}
