package com.darksouls.rougelike.model;

import java.awt.*;

public class Wall extends Tile {

    public Wall(int x, int y){
        this.tileColor = new Color(0, 0, 0);
        this.pos = new VPoint(x, y);
        transparent = false;
    }

    @Override
    public boolean stepOn(Living living){
        return false;
    }

    @Override
    public void stepOff(Living living){
        // TODO: do nothing/ throw error
    }
}
