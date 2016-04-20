package com.darksouls.rougelike.model;

import com.darksouls.rougelike.references.Reference;
import com.darksouls.rougelike.view.GamePanel;

import java.awt.*;

// Singleton
public class Player extends Living{
    private static  Player instance;

    private Player(){}

    public static Player getInstance(){
        // TODO: Cant happen but throw error.
        if(instance == null) {
            instance = new Player();
            instance.placeholder = new Color(147, 101, 250);
        }

        return instance;
    }

    private String name;

    private Inventory inventory;

    private ViewMap viewed;

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
    public boolean step(VPoint dir){
        boolean ret = super.step(dir);
        if(ret) {
            //LogHelper.writeLn("------------------------------");
            this.lookAround();
        }

        return ret;
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
    int health;         // HP from VIT
    int mana;           // MP from INT
    int stamina;        // SP from ENR

    int equipLoad;      // from ENR

    int physicalDef;    // from ENR
    int magicDef;       // from INT, levelUp
    int flameDef;       // from levelUp
    int lightningDef;   // from levelUp

    int itemDisc;       // LCK - luck, itemDiscovery
    int poise;          // stun resistance

    int bleedRes;
    int poisonRes;
    int curseRes;



}
