package com.darksouls.rougelike.model;

import com.darksouls.rougelike.control.Clock;
import com.darksouls.rougelike.references.Reference;
import com.darksouls.rougelike.utility.LogHelper;
import com.darksouls.rougelike.view.GamePanel;

import java.awt.*;
import java.util.ArrayList;

// Singleton
public class Player extends Living{
    private static  Player instance;

    private Player(){
        placeholder = new Color(147, 101, 250);
        health = 20;
        accuracy = 8; // max 9
        name = "Soul";
    }

    public static Player getInstance(){
        // TODO: Cant happen but throw error.
        if(instance == null) {
            instance = new Player();
        }

        return instance;
    }

    private String name;

    private Inventory inventory;

    private ViewMap viewed;

    private ArrayList<Living> ignore;

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

    @Override
    public Damage getDmg(){
        return new Damage(3, Reference.PHYS_DMG, accuracy);
    }

    @Override
    public boolean step(VPoint dir){
        boolean ret = super.step(dir);
        if(ret) {
            //LogHelper.writeLn("------------------------------");
            Clock.tick();
            this.lookAround();
        }
        return ret;
    }

    @Override
    public boolean attack(VPoint dir){
        boolean ret = super.attack(dir);
        if(ret){
            Clock.tick();
        }

        return ret;
    }

    public boolean seeDanger(){
        boolean danger = false;
        ArrayList<Living> seen = new ArrayList<>();

        if(ignore == null)
            ignore = new ArrayList<>();

        for(int i = 0; i < viewed.getKeys().size(); i++){
            VPoint p = viewed.getKeys().get(i);
            if(viewed.get(p) == Reference.TILE_VISIBLE) {
                Living living = GamePanel.getInstance().getDungeonLevel().getTile(p).getLiving();
                seen.add(living);
                if (living != null && living != instance && !ignore.contains(living)) {
                    danger = true;
                    ignore.add(living);
                }
            }
        }

        for(int i = 0; i < ignore.size(); i++)
            if(!seen.contains(ignore.get(i)))
                ignore.remove(i);

        return danger;
    }

    // A* algorithm
    public boolean plan(Tile goal){
        Node start = new Node(pos, goal);

        NodeList open = new NodeList();
        NodeList closed = new NodeList();

        open.add(start);

        while(open.size() > 0){
            Node at = open.get(0);
            if(at.getTile().vect().equals(goal.vect())){
                plan = new ArrayList<>();

                VPoint dir;
                int type;

                VPoint prev = at.getTile().vect();

                if(at.getTile().getLiving() != null && at.getTile().getLiving() != instance)
                    type = Reference.ATTACK_ACT;
                else
                    type = Reference.MOVE_ACT;

                at = at.getParent();

                while(at != null){
                    dir = prev.subtract(at.getTile().vect());

                    Action temp = new Action(type, dir);
                    plan.add(temp);

                    prev = at.getTile().vect();
                    at = at.getParent();

                    if(at != null && at.getTile().getLiving() != null && at.getTile().getLiving() != instance)
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
                    Node tmp = new Node(n, goal);

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

        LogHelper.error("No Path");
        return false;
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

    int souls;          // currency
    int charLevel;      // soul-level, All defence-Up

    // Stats
    int vitality;       // VIT - health point
    int endurance;      // ENR - stamina
    int strength;       // STR - phyAttack, handling-weight-limit
    int dexterity;      // DEX - speed, cast-speed

    int intelligence;   // INT - magicRes, magicAttack
    int faith;          // FTH

    int resistance;     // RES
    int attunement;     // ATT
    int humanity;       // HUM

    // Calculated stat
    // TODO: http://darksouls.wikidot.com/stats
    int mana;           // MP from INT
    int stamina;        // SP from ENR

    int equipLoad;      // from ENR

    int hitRate;        // from veapon Acuracy, LUCK

    int physicalDef;    // from ENR
    int magicDef;       // from INT, levelUp
    int flameDef;       // from levelUp
    int lightningDef;   // from levelUp

    int itemDisc;       // LCK - luck, itemDiscovery
    int poise;          // stun resistance

    int bleedRes;
    int poisonRes;
    int curseRes;



    @Override
    public String toString(){
        return "Player named: " + name;
    }

    @Override
    public int getLivingClass(){
        return Reference.PLAYER;
    }

}
