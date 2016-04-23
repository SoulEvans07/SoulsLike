package com.darksouls.rougelike.model;

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
            entity.attack(this.dir);
        }

        GamePanel.getInstance().getCanvas().repaint();
    }

    @Override
    public String toString(){
        return "Action: " + (actionType==Reference.ATTACK_ACT ? "attack" : "step") + dir.toString();
    }

}
