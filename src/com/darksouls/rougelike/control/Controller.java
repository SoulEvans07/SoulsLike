package com.darksouls.rougelike.control;

import com.darksouls.rougelike.model.Player;
import com.darksouls.rougelike.model.Tile;
import com.darksouls.rougelike.model.VPoint;
import com.darksouls.rougelike.references.Reference;
import com.darksouls.rougelike.utility.GuiMagic;
import com.darksouls.rougelike.utility.LogHelper;
import com.darksouls.rougelike.view.GamePanel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Controller {
    private static Controller instance;

    public Controller(){}

    public static Controller getInstance(){
        if(instance == null){
            instance = new Controller();
            instance.init();
        }
        return instance;
    }

    private void init(){
        this.keyAdapter = new KeyTrack();
        this.mouseAdapter = new MouseTrack();
    }

    private KeyAdapter keyAdapter;
    private MouseAdapter mouseAdapter;

    public KeyAdapter getKeyAdapter(){
        return keyAdapter;
    }

    public MouseAdapter getMouseAdapter(){
        return mouseAdapter;
    }

    public class MouseTrack extends MouseAdapter{
        @Override
        public void mouseReleased(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1) {
                VPoint tmp = new VPoint(e.getPoint()).addX(-2).addY(-2);    // -2 is correction, so its on the tip of the cursor
                GamePanel.getInstance().getCanvas().point = tmp;
                GamePanel.getInstance().getCanvas().repaint();
                GamePanel.getInstance().requestFocus();

                VPoint field = GuiMagic.getFieldCenter(tmp);

                Tile tile;
                if (field != null) {
                    tile = GamePanel.getInstance().getDungeonLevel().getTile(field.x(), field.y());
                    if (tile != null)
                        LogHelper.writeLn(tile.toString());
                }
            } else {
                GamePanel.getInstance().getCanvas().point = null;
                GamePanel.getInstance().requestFocus();
            }
        }

    }

    public class KeyTrack extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            boolean validAction = false;
            VPoint dir = new VPoint();
            switch (e.getKeyCode()){
                case Reference.MOVE_UP:
                    validAction = Player.getInstance().step(dir.set(0, -1));
                    break;
                case Reference.MOVE_RIGHT:
                    validAction = Player.getInstance().step(dir.set(1, 0));
                    break;
                case Reference.MOVE_DOWN:
                    validAction = Player.getInstance().step(dir.set(0, 1));
                    break;
                case Reference.MOVE_LEFT:
                    validAction = Player.getInstance().step(dir.set(-1, 0));
                    break;
                case Reference.WAIT:
                    // Wait is just a tick without action
                    validAction = true;
                    break;
            }

            if(validAction)
                Clock.tick();
        }
    }
}
