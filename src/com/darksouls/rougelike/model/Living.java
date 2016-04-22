package com.darksouls.rougelike.model;

import java.util.ArrayList;
import java.util.Random;

public abstract class Living extends Entity {
    private VPoint direction;

    protected ArrayList<Action> plan;

    protected int health;         // HP from VIT

    // TODO: temporary:
    int accuracy;        // HIT

    public boolean step(VPoint dir){
        boolean ret = false;
        Tile neighbor = this.pos.getNeighbor(dir);
        if (neighbor != null) {
            if(neighbor.stepOn(this)) {
                this.pos.stepOff(this);
                this.pos = neighbor;
                ret = true;
            }
        }

        return ret;
    }

    public boolean attack(VPoint dir){
        boolean ret = false;

        Tile target = this.pos.getNeighbor(dir);
        if(target != null){
            if(target.getLiving() != null && target.getLiving().gotHit(this.getDmg()))
                ret = true;
        }

        return ret;
    }

    public boolean gotHit(Damage dmg){
        boolean hit = false;
        Random r = new Random();
        if(Math.abs(r.nextInt()) % 10 < dmg.getHitRate()){
            this.health -= dmg.getValue();
            hit = true;
        }

        return hit;
    }

    public ArrayList<Action> getPlan() {
        return plan;
    }

    public abstract Damage getDmg();
}
