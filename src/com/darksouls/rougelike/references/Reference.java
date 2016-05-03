package com.darksouls.rougelike.references;

import java.awt.event.KeyEvent;

public class Reference {
    //---------GAME-REFERENCES---------
    public static final String PROJ_NAME = "\"Souls Like\" - the Dark Souls rougeLike";
    public static final String PROJ_VER = "vDEBUG";

    public static final String START_DATE = "Day01 2016.04.07";
    public static final String TIME_STAMP = "Day05 2016.05.02";

    //----------GAME-DEFAULTS----------
    // Controls
    public static final int MOVE_UP    = KeyEvent.VK_W;
    public static final int MOVE_RIGHT = KeyEvent.VK_D;
    public static final int MOVE_DOWN  = KeyEvent.VK_S;
    public static final int MOVE_LEFT  = KeyEvent.VK_A;
    public static final int WAIT       = KeyEvent.VK_T;


    // Player values
    public static final int FOV = 7;    // always odd number

    // Tile Visibility Levels (percent
    public static final int TILE_HIDDEN = 10;
    public static final int TILE_SEEN = 2;
    public static final int TILE_VISIBLE = 0;

    // ActionTypes
    public static final int MOVE_ACT = 0;
    public static final int ATTACK_ACT = 1;

    // DamageTypes
    public static final int PHYS_DMG = 0;
    public static final int FIRE_DMG = 1;

    // LivingClasses
    public static final int PLAYER = 0;
    public static final int NPC = 1;
    public static final int MOB = 2;

    //---------CLASS-REFERENCES--------
    // Clock related
    public static final int TIMER_TICK = 40;


    //--------TEMPORARY-VARABLES-------
    public static final double WALL_RAND = 0.8;
    public static final double ENEMY_COUNT = 20;

    //---------UTILITY-DEFAULTS--------
    // GuiMagic related
    public static final int WIN_WBORDER = 10;
    public static final int WIN_HBORDER = 10;

    // LogHelper related
    public static final int TAB_SIZE = 4;
}
