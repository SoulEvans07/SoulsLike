package com.darksouls.rougelike.model;

import com.darksouls.rougelike.references.Config;
import com.darksouls.rougelike.references.Reference;
import com.darksouls.rougelike.utility.GuiMagic;

import java.util.ArrayList;
import java.util.Random;

public class DungeonLevel {
    private int levelDepth;
    private int levelName;

    //VPoint playerPos;
    //ArrayList<VPoint> npcPos;
    private ArrayList<Tile> fields;

    public VPoint getLevelSize() {
        return fieldSize;
    }

    private VPoint fieldSize;

    public void loadMap(){
        fields = new ArrayList<>();
        fieldSize = new VPoint(33, 23);

        // TODO: get map entrance
        VPoint entrance = new VPoint(1, 1);
        Random r = new Random();

        VPoint npc = new VPoint( 1 + Math.abs(r.nextInt()) % (fieldSize.x() - 2),
                1 + Math.abs(r.nextInt()) % (fieldSize.y() - 2));

        Tile tmp;

        for(int j = 0; j < fieldSize.y(); j++){
            for(int i = 0; i < fieldSize.x(); i++){
                // Procedural map
                if(i == 0 || i == fieldSize.x()-1 || j == 0 || j == fieldSize.y()-1)
                    tmp = new Wall(i, j);
                else {
                    if (!(i == entrance.x() && j == entrance.y()) && r.nextInt(10) > 8)
                        tmp = new Wall(i, j);
                    else
                        tmp = new Tile(i, j);
                }
                // Player placement
                if(i == entrance.x() && j == entrance.y()) {
                    Player.getInstance().setPos(tmp);
                    tmp.stepOn(Player.getInstance());
                }

                // Random NPC
                if(i == npc.x() && j == npc.y()){
                    NPC one = new NPC();
                    one.setPos(tmp);
                    tmp.stepOn(one);
                }

                // setting up Player view
                Player.getInstance().addToView(tmp.vect(), Reference.TILE_HIDDEN);

                fields.add(tmp);
            }
        }

        // start the level with looking around
        Player.getInstance().lookAround();

        // neighbors
        for(int y = 0; y < fieldSize.y(); y++){
            for(int x = 0; x < fieldSize.x(); x++){
                this.getTile(x, y).addNeighbor(this.getTile(x  , y-1)); //up
                this.getTile(x, y).addNeighbor(this.getTile(x+1, y  )); //rigt
                this.getTile(x, y).addNeighbor(this.getTile(x  , y+1)); //down
                this.getTile(x, y).addNeighbor(this.getTile(x-1, y  )); //left
            }
        }
    }

    public void tick(){

    }


    public int getInfinity(){
        return fieldSize.getX() * fieldSize.getY() + 1;
    }

    public boolean rayTrace(VPoint tile, VPoint at){
        VPoint from = new VPoint(at.x() * Config.FIELD_SIZE + Config.FIELD_SIZE / 2,
                at.y() * Config.FIELD_SIZE + Config.FIELD_SIZE / 2);
        VPoint to = new VPoint(tile.x() * Config.FIELD_SIZE + Config.FIELD_SIZE / 2,
                tile.y() * Config.FIELD_SIZE + Config.FIELD_SIZE / 2);

        VPoint vector = to.subtract(from);
        vector = vector.multiply(2 / vector.length());

        from = from.add(vector);
        while(inBound(from) && from.getDist(to) > (Config.FIELD_SIZE / 8)){
            if(!GuiMagic.getFieldCenter(from).equals(at) && !GuiMagic.getFieldCenter(from).equals(tile) &&
                    !getTile(GuiMagic.getFieldCenter(from)).isTransparent()) {
                return false;
            }
            from = from.add(vector);
        }
        return true;
    }

    public boolean inBound(VPoint p){
        return !(p.x() < 0 || p.y() < 0 || p.x() > fieldSize.x()*Config.FIELD_SIZE || p.y() > fieldSize.y()*Config.FIELD_SIZE);
    }

    public boolean inBoundTile(VPoint p){
        return !(p.x() < 0 || p.y() < 0 || p.x() > fieldSize.x() || p.y() > fieldSize.y());
    }

    public Tile getTile(int x, int y){
        Tile ret = null;
        int id = y * fieldSize.getX() + x;
        if(0 <= id && id < fieldSize.x() * fieldSize.y())
            ret = fields.get(id);
        return ret;
    }

    public Tile getTile(VPoint v){
        return this.getTile(v.getX(), v.getY());
    }
}
