package com.darksouls.rougelike.model;

import com.darksouls.rougelike.references.Colors;
import com.darksouls.rougelike.references.Config;
import com.darksouls.rougelike.references.Reference;

import java.awt.*;
import java.util.ArrayList;

public class Tile {
    protected boolean transparent;
    protected VPoint pos;
    protected Color tileColor;
    protected Living livingEntity;
    protected ArrayList<Entity> entities;

    ArrayList<Tile> neighbors;  // max 4. up, down, left, right; can step, can attack
    ArrayList<Tile> corners;    // max 4. 4 corners; only attack

    protected Tile(){}

    public Tile(int x, int y){
        pos = new VPoint(x, y);
        tileColor = Colors.freeCell;
        transparent = true;
    }

    public void addNeighbor(Tile t){
        if(neighbors == null)
            neighbors = new ArrayList<>();
        neighbors.add(t);
    }

    public void addCorner(Tile t){
        corners.add(t);
    }

    public boolean stepOn(Living on){
        boolean step = false;
        if(this.livingEntity == null) {
            this.livingEntity = on;
            step = true;
        }
        return step;
    }

    public Living getLiving(){
        return livingEntity;
    }

    public void removeLiving(){
        if(livingEntity.getHp() <= 0)
            livingEntity = null;
    }

    public void stepOff(Living off){
        this.livingEntity = null;
    }

    public Tile getNeighbor(VPoint dir){
        Tile ret = null;
        if(neighbors != null){
            for(int i = 0; i < neighbors.size(); i++)
                if(neighbors.get(i).vect().equals(pos.add(dir)))
                    ret = neighbors.get(i);
        }

        return ret;
    }

    public ArrayList<Tile> getNeighbors() {
        return neighbors;
    }

    // gets you the screen coordinate of the tile center
    public VPoint mVect(){
        return pos.multiply(Config.FIELD_SIZE).addX(Config.FIELD_SIZE / 2).addY(Config.FIELD_SIZE / 2);
    }

    public VPoint vect(){
        return pos;
    }

    public boolean isTransparent(){
        return transparent;
    }

    public boolean isValid(){ // isValid place to step
        return true;
    }


    //------------------------------------------------------------------------------------------------------------------

    @Override
    public String toString(){
        return "Tile: " + pos.toString();
    }

    // TODO: temporary render
    public Color getColor(int visibility){
        Color ret = tileColor;
        if( !Config.DEBUG && visibility == Reference.TILE_HIDDEN)
            ret = new Color(0, 0, 0);
        else {
            if (entities != null && entities.size() != 0)
                ret = entities.get(0).getColor();

            if (visibility == Reference.TILE_VISIBLE && livingEntity != null)
                ret = livingEntity.getColor();

            if(visibility == Reference.TILE_SEEN)
                ret = ret.darker().darker();

            // TODO: temporary; only for testing
            if(Config.DEBUG && visibility != Reference.TILE_VISIBLE) {
                if (livingEntity != null)
                    ret = livingEntity.getColor().darker();
                if (visibility == Reference.TILE_SEEN)
                    ret = ret.darker().darker();
                if (visibility == Reference.TILE_HIDDEN)
                    ret = ret.darker().darker().darker().darker().darker().darker().darker();
            }
        }

        return ret;
    }
}
