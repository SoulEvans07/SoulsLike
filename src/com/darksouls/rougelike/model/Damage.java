package com.darksouls.rougelike.model;

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
}
