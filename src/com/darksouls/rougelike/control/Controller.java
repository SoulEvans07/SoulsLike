package com.darksouls.rougelike.control;

import com.darksouls.rougelike.model.Action;
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
import java.util.ArrayList;
import java.util.Locale;

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
                GamePanel.getInstance().getCanvas().point = null;
                GamePanel.getInstance().requestFocus();

                VPoint tmp = new VPoint(e.getPoint()).addX(-2).addY(-2);    // -2 is correction, so its on the tip of the cursor
                VPoint field = GuiMagic.getFieldCenter(tmp);

                Tile tile;
                if (field != null) {
                    if(Player.getInstance().getVisibilityLevel(field) != Reference.TILE_HIDDEN) {
                        tile = GamePanel.getInstance().getDungeonLevel().getTile(field.getX(), field.getY());

                        if (tile != null && GamePanel.getInstance().getDungeonLevel().getTile(field).isValid()) {
                            if (Player.getInstance().plan(tile)) {
                                /*ArrayList<Action> plan = Player.getInstance().getPlan();

                                int startHp = Player.getInstance().getHp();
                                while (plan.size() > 0) {
                                    if (!Player.getInstance().seeDanger() && startHp == Player.getInstance().getHp()) {
                                        Action move = plan.get(plan.size() - 1); // get last
                                        move.exec(Player.getInstance());
                                        //LogHelper.writeLn(move.toString());
                                        plan.remove(plan.size() - 1);
                                        Clock.tick();
                                    } else
                                        plan.clear(); // drop plan if danger seen
                                }*/
                                Player.getInstance().execPlan();
                            }
                        }
                    }
                }
            } else {
                VPoint tmp = new VPoint(e.getPoint()).addX(-2).addY(-2);    // -2 is correction, so its on the tip of the cursor
                GamePanel.getInstance().getCanvas().point = tmp;
                GamePanel.getInstance().getCanvas().repaint();
                GamePanel.getInstance().requestFocus();
            }
        }

    }

    public class KeyTrack extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (Player.getCurrInstance() != null && Player.getCurrInstance().getHp() > 0) {
                boolean validAction = false;
                VPoint dir = new VPoint();

                switch (e.getKeyCode()) {
                    case Reference.MOVE_UP:
                        dir.set(0, -1);
                        break;
                    case Reference.MOVE_RIGHT:
                        dir.set(1, 0);
                        break;
                    case Reference.MOVE_DOWN:
                        dir.set(0, 1);
                        break;
                    case Reference.MOVE_LEFT:
                        dir.set(-1, 0);
                        break;
                    case Reference.WAIT:
                        // Wait is just a tick without action
                        validAction = true;
                        Clock.tick();
                        break;
                }

                Tile target = Player.getInstance().getPos().getNeighbor(dir);
                if(target != null && target.isTransparent()) {
                    if (target.getLiving() != null) {
                        Player.getInstance().plan(new Action(Reference.ATTACK_ACT, dir));
                    } else {
                        Player.getInstance().plan(new Action(Reference.MOVE_ACT, dir));
                    }
                    Player.getInstance().execPlan();
                }
                /*int dmg = Player.getInstance().attack(dir);
                if (dmg <= -2) {
                    validAction = Player.getInstance().step(dir);
                    if (validAction) {
                        Player.getInstance().seeDanger(); // clears unseen npc from ignore list
                        Clock.tick();
                    }
                } else if (dmg == -1) {
                    //LogHelper.error("miss");
                    for (int i = 0; i < 10; i++) {
                        GamePanel.getInstance().getCanvas().drawDMG("miss", Player.getInstance().getPos().mVect(), dir, i, Player.getInstance());
                        Clock.animationTick();
                    }
                    Clock.tick();
                } else if (dmg >= 0) {
                    //LogHelper.error("dmg");
                    for (int i = 0; i < 10; i++) {
                        GamePanel.getInstance().getCanvas().drawDMG("" + dmg, Player.getInstance().getPos().mVect(), dir, i, Player.getInstance());
                        Clock.animationTick();
                    }
                    Clock.tick();
                }*/
            }
        }
    }
}
