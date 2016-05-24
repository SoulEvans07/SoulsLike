package com.darksouls.rougelike.view;

import com.darksouls.rougelike.model.*;
import com.darksouls.rougelike.references.Colors;
import com.darksouls.rougelike.references.Config;
import com.darksouls.rougelike.references.Reference;
import com.darksouls.rougelike.utility.GuiMagic;

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

    public GameCanvas(DungeonLevel l) {
        super();

        level = l;

        for (KeyListener kL : GamePanel.getInstance().getKeyListeners()) {
            this.addKeyListener(kL);
        }

        cols = (int) level.getLevelSize().x();
        rows = (int) level.getLevelSize().y();

        offscreen = createImage((int) Math.ceil(cols * fieldSize), (int) Math.ceil(rows * fieldSize));
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
    public void repaint() {
        super.repaint();
    }

    @Override
    public void update(Graphics g) {
        super.update(g);
    }

    // http://stackoverflow.com/questions/2845346/double-buffering-with-awt
    // http://docs.oracle.com/javase/tutorial/extra/fullscreen/doublebuf.html
    // http://stackoverflow.com/questions/27136250/awt-canvas-bufferstrategy-and-resize-flickering
    // https://community.oracle.com/thread/1263801?start=0&tstart=0

    // TODO: Temporary point for ruler
    public VPoint point;

    private void drawLevel(Graphics2D g) {
        if (bufferGraphics != null) {
            bufferGraphics.clearRect(0, 0, (int) Math.ceil(cols * Config.FIELD_SIZE),
                    (int) Math.ceil(rows * Config.FIELD_SIZE));
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
                    bufferGraphics.fillRect(x * Config.FIELD_SIZE, y * Config.FIELD_SIZE,
                            Config.FIELD_SIZE, Config.FIELD_SIZE);
                }
            }

            // TODO: Temporary ruler line
            if (Config.DEBUG && point != null) {
                bufferGraphics.setColor(Colors.spawnCell);
                point = GuiMagic.getFieldCoord(point);
                bufferGraphics.drawLine((int) point.x(), (int) point.y(),
                        (int) Player.getInstance().getPos().mVect().x(),
                        (int) Player.getInstance().getPos().mVect().y());
            }

            this.drawHUD();

            if (g != null)
                g.drawImage(offscreen, 0, 0, this);
        } else {
            offscreen = createImage((int) Math.ceil(cols * Config.FIELD_SIZE),
                    (int) Math.ceil(rows * Config.FIELD_SIZE));
            if (offscreen != null) {
                bufferGraphics = offscreen.getGraphics();
                repaint();
            }
        }
    }

    private void drawHUD() {
        bufferGraphics.setColor(new Color(164, 0, 0));
        bufferGraphics.fillRect(0, 0, Player.getInstance().getMaxHp() * 10, Config.FIELD_SIZE / 4);
        bufferGraphics.setColor(new Color(255, 0, 0));
        bufferGraphics.fillRect(0, 0, Player.getInstance().getHp() * 10, Config.FIELD_SIZE / 4);

        if (Player.getInstance().getSeenPos() != null && Player.getInstance().getSeenPos().size() > 0)
            for (VPoint vp : Player.getInstance().getSeenPos()) {
                if(vp != null) {
                    Tile tmp = GamePanel.getInstance().getDungeonLevel().getTile(vp);
                    if (tmp !=null && tmp.getLiving() != null && tmp.getLiving() != Player.getInstance() &&
                    tmp.getLiving().getHp() != tmp.getLiving().getMaxHp())
                    this.drawHP(tmp.vect(), tmp.getLiving().getHp(), tmp.getLiving().getMaxHp());
                }
            }
    }

    private static Font inline = new Font("Monospaced", Font.BOLD , 15);
    //private static Font outline = new Font(Reference.FONT_NAME, Font.BOLD , 16);

    public void drawDMG(String dmg, VPoint from, VPoint dir, int delta, Living src){
        if (GamePanel.getInstance().getCanvas().getGraphics() != null) {
            VPoint up = new VPoint(0, Config.FIELD_SIZE / 8 - delta);
            if (src == Player.getInstance())
                bufferGraphics.setColor(Colors.enemyDMG);
            else
                bufferGraphics.setColor(Colors.playerDMG);

            bufferGraphics.setFont(inline);
            FontMetrics fm = bufferGraphics.getFontMetrics();
            int w = fm.stringWidth(dmg);
            int h = fm.getAscent();
            bufferGraphics.drawString(dmg, from.getX() + dir.getX() * Config.FIELD_SIZE + up.getX() - (w / 2), from.getY() + dir.getY() * Config.FIELD_SIZE + up.getY() + (h / 4));
            GamePanel.getInstance().getCanvas().getGraphics().drawImage(offscreen, 0, 0, this);
        }
    }

    private void drawHP(VPoint pos, int hp, int maxHp) {

        int max = Config.FIELD_SIZE;
        float length = (float)max * ((float)hp / (float)maxHp);

        VPoint start = pos.multiply(Config.FIELD_SIZE)
                .addY(-Config.FIELD_SIZE / 4);

        bufferGraphics.setColor(new Color(164, 0, 0));
        bufferGraphics.fillRect(start.getX(), start.getY(), max, Config.FIELD_SIZE / 8);
        //bufferGraphics.setColor(new Color(255, 0, 0));
        bufferGraphics.setColor(new Color(55, 244, 48));
        bufferGraphics.fillRect(start.getX(), start.getY(), Math.round(length), Config.FIELD_SIZE / 8);
    }

    public boolean windowUpdate(VPoint size) {
        if (Math.min(size.x() / cols, size.y() / rows) == Config.FIELD_SIZE)
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
