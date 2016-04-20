package com.darksouls.rougelike.model;

public abstract class Living extends Entity {
    private VPoint direction;

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

        return true;
    }
}
