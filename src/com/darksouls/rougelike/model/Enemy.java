package com.darksouls.rougelike.model;

import com.darksouls.rougelike.references.Colors;
import com.darksouls.rougelike.references.Reference;
import com.darksouls.rougelike.utility.LogHelper;
import com.darksouls.rougelike.view.GamePanel;

import java.util.ArrayList;

public class Enemy extends Living{
    private ViewMap viewed;

    private Tile target;

    public Enemy(){
        health = 10;
        placeholder = Colors.enemy;
    }

    public boolean plan(){
        if(plan != null)
            plan.clear();

        if(seekPlayer()){
            Node start = new Node(pos, target);

            NodeList open = new NodeList();
            NodeList closed = new NodeList();

            open.add(start);

            while(open.size() > 0){
                Node at = open.get(0);
                if(at.getTile().vect().equals(target.vect())){
                    plan = new ArrayList<>();

                    VPoint dir;
                    int type;

                    VPoint prev = at.getTile().vect();

                    if(at.getTile().getLiving() != null && at.getTile().getLiving() != this)
                        type = Reference.ATTACK_ACT;
                    else
                        type = Reference.MOVE_ACT;

                    at = at.getParent();

                    // TODO: make it more efficient by only saving the first action into the plan
                    while(at != null){
                        dir = prev.subtract(at.getTile().vect());

                        Action temp = new Action(type, dir);
                        plan.add(temp);

                        prev = at.getTile().vect();
                        at = at.getParent();

                        if(at != null && at.getTile().getLiving() != null && at.getTile().getLiving() != this)
                            type = Reference.ATTACK_ACT;
                        else
                            type = Reference.MOVE_ACT;
                    }

                    if(plan.size() == 0)
                        return false;

                    return true;
                }

                open.remove(at);
                closed.add(at);

                NodeList neighbours = new NodeList();
                for (Tile n : at.getTile().getNeighbors()){
                    if(n.isTransparent()) {
                        Node tmp = new Node(n, target);

                        if (closed.contains(tmp)) {
                            tmp.setSteps(closed.get(tmp.getTile().vect()).getSteps());
                        } else if (open.contains(tmp)) {
                            tmp.setSteps(open.get(tmp.getTile().vect()).getSteps());
                        } else
                            tmp.setSteps(GamePanel.getInstance().getDungeonLevel().getInfinity());

                        neighbours.add(tmp);
                    }
                }

                for(Node n : neighbours.getList()){
                    boolean temp = (!open.contains(n) && !closed.contains(n)) ||  (at.getDist() + 1 < n.getSteps());
                    if(temp){
                        n.setSteps(at.getSteps() + 1);
                        n.setParent(at);

                        open.add(n);
                        closed.remove(n);
                    }
                }
            }
            LogHelper.error("Enemy cannot find Path");
        }

        return false;
    }

    public void addToView(VPoint pos, int flag){
        if(viewed == null)
            viewed = new ViewMap();

        viewed.set(pos, flag);
    }

    public int getVisibilityLevel(VPoint pos){
        int v = Reference.TILE_HIDDEN;
        if(viewed != null && viewed.containsKey(pos))
            v = viewed.get(pos);
        return v;
    }

    public void lookAround(){
        if(viewed != null) {
            // set SEEN
            for(int i = 0; i < viewed.size(); i++){
                VPoint p = viewed.getKeys().get(i);
                if (viewed.containsKey(p) && viewed.get(p) == Reference.TILE_VISIBLE) {
                    viewed.set(p, Reference.TILE_SEEN);
                }
            }

            // set VISIBLE
            int start = (Reference.FOV - 1) / 2;
            VPoint tmp;
            for (int y = -start; y <= start; y++)
                for (int x = -start; x <= start; x++) {
                    tmp = pos.vect().addX(x).addY(y);
                    if (viewed.containsKey(tmp) &&
                            GamePanel.getInstance().getDungeonLevel().rayTrace(tmp, pos.vect())) {
                        viewed.set(tmp, Reference.TILE_VISIBLE);
                    }
                }
        }
    }

    public boolean seekPlayer(){
        target = null;

        for(int i = 0; i < viewed.getKeys().size(); i++){
            VPoint p = viewed.getKeys().get(i);
            if(viewed.get(p) == Reference.TILE_VISIBLE) {
                Living living = GamePanel.getInstance().getDungeonLevel().getTile(p).getLiving();
                if (living != null && living != this && living.getLivingClass() == Reference.PLAYER) {
                    target = living.getPos();
                    LogHelper.writeLn("found: " + target.toString());
                }
            }
        }

        return target != null;
    }

    @Override
    public boolean step(VPoint dir){
        boolean ret = super.step(dir);
        if(ret) {
            this.lookAround();
        }
        return ret;
    }

    @Override
    public Damage getDmg() {
        return new Damage(1, Reference.PHYS_DMG, accuracy);
    }

    @Override
    public String toString(){
        return "Enemy 1";
    }

    @Override
    public int getLivingClass(){
        return Reference.MOB;
    }
}
