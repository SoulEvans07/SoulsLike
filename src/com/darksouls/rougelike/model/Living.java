package com.darksouls.rougelike.model;

import com.darksouls.rougelike.utility.LogHelper;

import java.util.ArrayList;
import java.util.Random;

public abstract class Living extends Entity {
    private VPoint direction;

    protected ArrayList<Action> plan;
    protected ViewMap viewed;

    protected int health;
    protected int maxHealth;      // HP from VIT

    // TODO: temporary:
    int accuracy;        // HIT

    public boolean step(VPoint dir){
        boolean ret = false;
        Tile neighbor = this.pos.getNeighbor(dir);

        LogHelper.writeLn("step: " + dir.toString());
        LogHelper.lift();

        if (neighbor != null) {
            if(neighbor.stepOn(this)) {
                LogHelper.writeLn("step on");
                this.pos.stepOff(this);
                LogHelper.writeLn("step off");
                this.pos = neighbor;
                ret = true;
            }
        }

        LogHelper.close();

        return ret;
    }

    public boolean attack(VPoint dir){
        boolean ret = false;

        LogHelper.mute();

        Tile target = this.pos.getNeighbor(dir);
        if(target != null && target.getLiving() != null ){
            LogHelper.writeLn(this.toString() + " attacked " + target.getLiving().toString());
            LogHelper.lift();
            if(target.getLiving().gotHit(this.getDmg())) {
                LogHelper.writeLn("dmg type:\t" + this.getDmg().getType());
                LogHelper.writeLn("dmg value:\t" + this.getDmg().getValue());
                ret = true;
            } else
                LogHelper.writeLn("missed");
            LogHelper.writeLn(target.getLiving().toString() + " health: " + target.getLiving().getHp());
            LogHelper.close();
        }

        LogHelper.unMute();

        return ret;
    }

    public boolean gotHit(Damage dmg){
        boolean hit = false;
        Random r = new Random();
        int random = Math.abs(r.nextInt());
        LogHelper.writeLn(random % 10);
        if(random % 10 < dmg.getHitRate()){
            this.health -= dmg.getValue();
            hit = true;
        }

        return hit;
    }

    public ArrayList<Action> getPlan() {
        return plan;
    }

    public int getHp(){
        return health;
    }

    public int getMaxHp() {
        return maxHealth;
    }

    public abstract Damage getDmg();

    public abstract int getLivingClass();
}
