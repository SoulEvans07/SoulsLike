package com.darksouls.rougelike.model;

import com.darksouls.rougelike.control.Clock;
import com.darksouls.rougelike.references.Reference;
import com.darksouls.rougelike.view.GamePanel;

public class Action {
    private int actionType;
    private VPoint dir;

    public Action(int _actionType, VPoint _dir){
        this.actionType = _actionType;
        this.dir = new VPoint(_dir);
    }

    public void exec(Living entity){
        if(this.actionType == Reference.MOVE_ACT)
            entity.step(this.dir);

        if(this.actionType == Reference.ATTACK_ACT) {
            int dmg = entity.attack(this.dir);
            // TODO draw dmg
            if(dmg == -1) {
                // TODO : draw miss
                //LogHelper.error("miss");
                for(int i = 0; i < 10; i++){
                    GamePanel.getInstance().getCanvas().drawDMG("miss", entity.getPos().mVect(), dir, i);
                    Clock.animationTick();
                }
            } else if(dmg >= 0) {
                // TODO : draw dmg
                //LogHelper.error("dmg");
                for(int i = 0; i < 10; i++){
                    GamePanel.getInstance().getCanvas().drawDMG("" + dmg, entity.getPos().mVect(), dir, i);
                    Clock.animationTick();
                }
            }
        }

        GamePanel.getInstance().getCanvas().repaint();
    }

    @Override
    public String toString(){
        return "Action: " + (actionType==Reference.ATTACK_ACT ? "attack" : "step") + dir.toString();
    }

}
