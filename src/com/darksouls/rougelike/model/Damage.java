package com.darksouls.rougelike.model;

import com.darksouls.rougelike.references.Reference;

public class Damage {
    private int value;
    private int damageType;
    private int hitRate;

    public Damage(int _value, int _type, int _hitRate){
        value = _value;
        damageType = _type;
        hitRate = _hitRate;
    }

    public int getHitRate() {
        return hitRate;
    }

    public int getValue() {
        return value;
    }

    public String getType(){
        String type = "unknown";

        switch (damageType){
            case Reference.PHYS_DMG:
                type = "Physical";
                break;
            case Reference.FIRE_DMG:
                type = "Fire";
                break;
        }

        return type;
    }
}
