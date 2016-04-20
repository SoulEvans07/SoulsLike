package com.darksouls.rougelike.model;

import java.awt.*;

public abstract class Entity {
    //protected VPoint pos;
    protected Tile pos;

    protected Color placeholder;

    public Color getColor(){
        return placeholder;
    }

    public void setPos(Tile pos) {
        this.pos = pos;
    }

    public Tile getPos() {
        return this.pos;
    }
}
