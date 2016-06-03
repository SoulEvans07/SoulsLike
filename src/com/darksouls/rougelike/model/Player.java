package com.darksouls.rougelike.model;

import com.darksouls.rougelike.control.Clock;
import com.darksouls.rougelike.references.Colors;
import com.darksouls.rougelike.references.Reference;
import com.darksouls.rougelike.utility.LogHelper;
import com.darksouls.rougelike.view.GamePanel;

import java.awt.*;
import java.util.ArrayList;

// Singleton
public class Player extends Living{
    private static  Player instance;

    private Player(){
        placeholder = Colors.player;
        name = "Soul";

        accuracy = 8; // max 9

        vitality = 10;
        health =  this.getMaxHp();

        endurance = 5;
        stamina = this.getMaxStamina();
    }

    public static void revive(){
        instance = new Player();
    }

    public static Player getInstance(){
        if(instance == null) {
            instance = new Player();
        }

        return instance;
    }

    public static Player getCurrInstance(){
        return instance;
    }

    private String name;

    private Inventory inventory;

    private ArrayList<Living> seen;
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
        if(ret) {   // step successful
            this.setStamina(stamina + 1);
            this.lookAround();
            Clock.canvasTick();
        }
        return ret;
    }

    @Override
    public int attack(VPoint dir){
        int ret = -2;

        Tile target = this.pos.getNeighbor(dir);
        if(target != null && target.getLiving() != null ){
            if(this.getStamina() > 0) {
                if (target.getLiving().gotHit(this.getDmg())) {
                    ret = this.getDmg().getValue();
                    this.setStamina(stamina - 1);
                } else {
                    ret = -1;
                    this.setStamina(stamina - 2);
                }
                Clock.canvasTick();
            }
        }

        return ret;
    }

    public boolean seeDanger(){
        boolean danger = false;
        seen = new ArrayList<>();

        if(ignore == null)
            ignore = new ArrayList<>();

        for(int i = 0; i < viewed.getKeys().size(); i++){
            VPoint p = viewed.getKeys().get(i);
            if(viewed.get(p) == Reference.TILE_VISIBLE) {
                Living living = GamePanel.getInstance().getDungeonLevel().getTile(p).getLiving();
                if(living != null && living != instance) {
                    seen.add(living);
                    if (!ignore.contains(living)) {
                        danger = true;
                        ignore.add(living);
                    }
                }
            }
        }

        for(int i = 0; i < ignore.size(); i++)
            if(!seen.contains(ignore.get(i)))
                ignore.remove(i);

        return danger;
    }

    public ArrayList<VPoint> getSeenPos(){
        ArrayList<VPoint> tiles = null;
        if(seen != null && seen.size() > 0) {
            tiles = new ArrayList<>();
            for (Living l : seen)
                tiles.add(l.getPos().vect());
        }
        return tiles;
    }

    public void plan(Action act){
        plan = new ArrayList<>();
        plan.add(act);
    }

    // A* algorithm
    public boolean plan(Tile goal){
        Node start = new Node(pos, goal);

        NodeList open = new NodeList();
        NodeList closed = new NodeList();

        //LogHelper.error("---------------------------");

        open.add(start);

        while(open.size() > 0){
            //LogHelper.error("open: " + open.size() + " closed: " + closed.size());

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

                    //if(at != null && at.getTile().getLiving() != null && at.getTile().getLiving() != instance)
                    if(at != null && at.getTile().getLiving() != null && at.getTile().getLiving() != instance)
                        if(at.getTile().equals(goal))
                            type = Reference.ATTACK_ACT;
                        else
                            return true;
                    else
                        type = Reference.MOVE_ACT;
                }

                if(plan.size() == 0) {
                    LogHelper.comment("0 plan");
                    return false;
                }

                return true;
            }

            open.remove(at);
            closed.add(at);

            int ne = 0;
            NodeList neighbours = new NodeList();
            for (Tile n : at.getTile().getNeighbors()){
                if(this.getVisibilityLevel(n.pos) != Reference.TILE_HIDDEN && n.isTransparent() &&
                        (n.pos.equals(goal.pos) || !n.isObscured())) {
                    Node tmp = new Node(n, goal);

                    if (closed.contains(n.pos)) {
                        tmp = closed.get(tmp.getTile().vect());
                    } else if (open.contains(n.pos)) {
                        tmp = open.get(tmp.getTile().vect());
                    } else{
                        tmp = new Node(n, goal);
                        tmp.setSteps(GamePanel.getInstance().getDungeonLevel().getInfinity());
                    }

                    neighbours.add(tmp);
                    ne++;
                }
            }

            //LogHelper.comment("neighb: " + ne);
            ne = 0;
            for(Node n : neighbours.getList()){
                boolean temp = !closed.contains(n) && (!open.contains(n) ||  at.getSteps() + 1 < n.getSteps());
                if(temp){
                    //LogHelper.error("-open " + n.getTile().toString() + " " + n.getF() + " c: " + closed.size());
                    open.remove(n);
                    closed.remove(n);

                    if(at.getSteps() + 1 < n.getSteps())
                        n.setSteps(at.getSteps() + 1);

                    if(n.getF() > GamePanel.getInstance().getDungeonLevel().getInfinity()) {
                        LogHelper.error("A* failed, but saved");
                        return false;
                    }

                    if(!open.contains(n)) {
                        n.setParent(at);
                        open.add(n);
                    }
                    //LogHelper.error("+open " + n.getTile().toString() + " " + n.getF() + " c: " + closed.size());
                    ne++;
                }
            }
            //LogHelper.comment("+ open: " + ne);
        }

        LogHelper.comment("No Path");
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

    public void execPlan(){
        int startHp = health;
        while (plan.size() > 0) {
            if (!seeDanger() && startHp == health) {
                Action move = plan.get(plan.size() - 1); // get last
                move.exec(this);
                //LogHelper.writeLn(move.toString());
                plan.remove(plan.size() - 1);
                Clock.tick();
            } else
                plan.clear(); // drop plan if danger seen
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
    public int getMaxHp() {
        return vitality * 2;
    }

    public int getStamina(){
        return stamina;
    }

    public int getMaxStamina(){
        return endurance;
    }

    private void setStamina(int value){
        stamina = value;
        if(stamina < 0)
            stamina = 0;
        if(stamina > this.getMaxStamina())
            stamina = this.getMaxStamina();
    }

    @Override
    public String toString(){
        return "Player named: " + name;
    }

    @Override
    public int getLivingClass(){
        return Reference.PLAYER;
    }

}
