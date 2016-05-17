package com.darksouls.rougelike.model;

import com.darksouls.rougelike.references.Config;
import com.darksouls.rougelike.references.Reference;
import com.darksouls.rougelike.utility.GuiMagic;
import com.darksouls.rougelike.utility.LogHelper;
import com.darksouls.rougelike.view.GamePanel;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class DungeonLevel {
    private int levelDepth;
    private int levelName;

    //VPoint playerPos;
    //ArrayList<VPoint> npcPos;
    ArrayList<Enemy> npcs;
    private ArrayList<Tile> fields;

    public VPoint getLevelSize() {
        return fieldSize;
    }

    private VPoint fieldSize;

    private ArrayList<String> map;
    private VPoint size;
    public void loadMap() throws IOException {
        ArrayList<String> temp = new ArrayList<>();
        String line;

        FileReader fr = null;
        BufferedReader br = null;

        try {
            InputStream is = this.getClass().getResourceAsStream(Reference.LVL_PATH + Reference.LVL_1);
            InputStreamReader ir = new InputStreamReader(is);
            br = new BufferedReader(ir);

            while ((line = br.readLine()) != null) {
                temp.add(new String(line));
                LogHelper.error(line.length());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) br.close();
            if (fr != null) fr.close();
        }

        this.map = temp;
        //LogHelper.inline("arenaLoaded src: " + fileName);
        fieldSize = new VPoint(this.map.get(0).length() , this.map.size());
        LogHelper.error(fieldSize.toString());
    }

    public void initMap(){
        Player.revive();

        try {
            this.loadMap();
        } catch (IOException e) {
            e.printStackTrace();
        }

        fields = new ArrayList<>();
        npcs = new ArrayList<>();

        for(int j = 0; j < fieldSize.y(); j++) {
            for (int i = 0; i < fieldSize.x(); i++) {
                if(map.get(j).charAt(i) == 'E')
                    npcs.add(new Enemy());
            }
        }
        Tile tmp = null;
        int atEnemy = 0;
        for(int j = 0; j < fieldSize.y(); j++){
            for(int i = 0; i < fieldSize.x(); i++){
                switch (map.get(j).charAt(i)){
                    case '#':
                        tmp = new Wall(i, j);
                        break;
                    case '_':
                        tmp = new Tile(i, j);
                        break;
                    case 'P':
                        tmp = new Tile(i, j);
                        Player.getInstance().setPos(tmp);
                        tmp.stepOn(Player.getInstance());
                        break;
                    case 'E':
                        tmp = new Tile(i, j);
                        npcs.get(atEnemy).setPos(tmp);
                        tmp.stepOn(npcs.get(atEnemy));
                        atEnemy++;
                        break;
                }
                if(tmp != null) {
                    Player.getInstance().addToView(tmp.vect(), Reference.TILE_HIDDEN);
                    for (int e = 0; e < npcs.size(); e++)
                        npcs.get(e).addToView(tmp.vect(), Reference.TILE_HIDDEN);
                    fields.add(tmp);
                }
            }
        }

        // neighbors
        for(int y = 0; y < fieldSize.y(); y++){
            for(int x = 0; x < fieldSize.x(); x++){
                this.getTile(x, y).addNeighbor(this.getTile(x  , y-1)); //up
                this.getTile(x, y).addNeighbor(this.getTile(x+1, y  )); //rigt
                this.getTile(x, y).addNeighbor(this.getTile(x  , y+1)); //down
                this.getTile(x, y).addNeighbor(this.getTile(x-1, y  )); //left
            }
        }

        // start the level with looking around
        Player.getInstance().lookAround();

        // npcs as well
        for(Enemy n : npcs){
            n.lookAround();
        }
    }

    public void tick(){
        for(int i = 0; i < npcs.size(); i++){
            Enemy n = npcs.get(i);

            if(n.getHp() <= 0){
                n.getPos().removeLiving();
                npcs.remove(i);
                continue;
            }

            if(n.plan()){
                //LogHelper.writeLn("NPC plan");
                ArrayList<Action> plan = n.getPlan();

                Action move = plan.get(plan.size() - 1); // get only the last
                move.exec(n);
                //LogHelper.writeLn(move.toString());
                plan.remove(plan.size() - 1);
            }

            if(Player.getInstance().getHp() <= 0){
                Player.getInstance().getPos().removeLiving();
                GamePanel.getInstance().getCanvas().repaint();
                GamePanel.getInstance().gameOver();
            }
        }
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
