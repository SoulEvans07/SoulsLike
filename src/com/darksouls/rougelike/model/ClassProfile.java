package com.darksouls.rougelike.model;

public class ClassProfile {
    String className;

    Inventory starterItems;

    int charLevel;      // soul-level

    // Starting Stats
    int vitality;       // VIT - health point
    int endurance;      // ENR - stamina
    int strength;       // STR - phyAttack, handling-weight-limit
    int dexterity;      // DEX - speed, cast-speed

    int intelligence;   // INT - magicRes, magicAttack
    int faith;          // FTH

    int resistance;     // RES
    int attunement;     // ATT
    int humanity;       // HUM

}
