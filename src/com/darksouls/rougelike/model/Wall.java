package com.darksouls.rougelike.model;

import com.darksouls.rougelike.references.Colors;

public class Wall extends Tile {

    public Wall(int x, int y){
        this.tileColor = Colors.wallCell;
        this.pos = new VPoint(x, y);
        transparent = false;
    }

    @Override
    public boolean isValid(){
        return false;
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
