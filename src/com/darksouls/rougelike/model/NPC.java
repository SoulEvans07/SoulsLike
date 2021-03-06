package com.darksouls.rougelike.model;

import com.darksouls.rougelike.references.Reference;

import java.awt.*;

public class NPC extends Living{
    public NPC(){
        placeholder = new Color(120, 255, 88);
        accuracy = 5;
    }

    @Override
    public Damage getDmg() {
        return new Damage(0, Reference.PHYS_DMG, accuracy);
    }

    @Override
    public String toString(){
        return "NPC 1";
    }

    @Override
    public int getLivingClass(){
        return Reference.NPC;
    }
}
