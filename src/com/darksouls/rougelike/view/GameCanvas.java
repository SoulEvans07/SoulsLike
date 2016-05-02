package com.darksouls.rougelike.view;

import com.darksouls.rougelike.model.DungeonLevel;
import com.darksouls.rougelike.model.Player;
import com.darksouls.rougelike.model.VPoint;
import com.darksouls.rougelike.references.Colors;
import com.darksouls.rougelike.references.Config;
import com.darksouls.rougelike.utility.GuiMagic;
import com.darksouls.rougelike.utility.LogHelper;

import java.awt.*;
import java.awt.event.KeyListener;

public class GameCanvas extends Canvas {

    private double width, height;
    private int fieldSize;
    private int rows, cols;
    private int lineWidth;
    private int tileSize;

    private Image offscreen;
    private Graphics bufferGraphics;

    private DungeonLevel level;

    public GameCanvas(DungeonLevel l){
        super();

        level = l;

        for(KeyListener kL : GamePanel.getInstance().getKeyListeners()){
            this.addKeyListener(kL);
        }

        cols = (int) level.getLevelSize().x();
        rows = (int) level.getLevelSize().y();

        offscreen = createImage((int) Math.ceil(cols*fieldSize), (int) Math.ceil(rows*fieldSize));
        if (offscreen != null)
            bufferGraphics = offscreen.getGraphics();

        //lineWidth = 2;
    }

    @Override
    public void paint(Graphics g) {
        //super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        drawLevel(g2);
    }

    @Override
    public void repaint(){
        super.repaint();
    }

    @Override
    public void update(Graphics g){
        super.update(g);
    }

    // http://stackoverflow.com/questions/2845346/double-buffering-with-awt
    // http://docs.oracle.com/javase/tutorial/extra/fullscreen/doublebuf.html
    // http://stackoverflow.com/questions/27136250/awt-canvas-bufferstrategy-and-resize-flickering
    // https://community.oracle.com/thread/1263801?start=0&tstart=0

    // TODO: Temporary point
    public VPoint point;

    private void drawLevel(Graphics2D g) {
        if(bufferGraphics != null) {
            bufferGraphics.clearRect(0, 0, (int) Math.ceil(cols*Config.FIELD_SIZE),
                    (int) Math.ceil(rows*Config.FIELD_SIZE));
            bufferGraphics.setColor(Colors.line);

            VPoint temp = new VPoint();
            int visibility;
            for (int y = 0; y < rows; y++) {
                for (int x = 0; x < cols; x++) {
                    temp.set(x, y);

                    // Visibility setting
                    visibility = Player.getInstance().getVisibilityLevel(temp);
                    Color c = level.getTile(x, y).getColor(visibility);

                    bufferGraphics.setColor(c);
                    //bufferGraphics.fillRect((int) Math.floor(x * fieldSize), (int) Math.floor(y * fieldSize),
                    //        (int) Math.ceil(fieldSize), (int) Math.ceil(fieldSize));
                    bufferGraphics.fillRect(x*Config.FIELD_SIZE, y*Config.FIELD_SIZE,
                            Config.FIELD_SIZE, Config.FIELD_SIZE);
                }
            }

            // TODO: Temporary line
            if(Config.DEBUG && point != null) {
                bufferGraphics.setColor(Colors.spawnCell);
                point = GuiMagic.getFieldCoord(point);
                bufferGraphics.drawLine((int)point.x(), (int)point.y(),
                        (int)Player.getInstance().getPos().mVect().x(),
                        (int)Player.getInstance().getPos().mVect().y());
            }

            if(g != null)
                g.drawImage(offscreen, 0, 0, this);
        } else {
            offscreen = createImage((int) Math.ceil(cols*Config.FIELD_SIZE),
                    (int) Math.ceil(rows*Config.FIELD_SIZE));
            if (offscreen != null) {
                bufferGraphics = offscreen.getGraphics();
                repaint();
            }
        }
    }

    public boolean windowUpdate(VPoint size){
        if(Math.min(size.x() / cols, size.y() / rows) == Config.FIELD_SIZE)
            return false;

        width = size.x();
        height = size.y();
        //fieldSize = Math.min(width / cols, height / rows);

        offscreen = createImage((int) Math.ceil(width), (int) Math.ceil(height));
        if (offscreen != null)
            bufferGraphics = offscreen.getGraphics();

        return true;
    }
}
