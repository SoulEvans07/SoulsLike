package com.darksouls.rougelike.model;

import java.util.ArrayList;

public class Inventory {
    ArrayList<ItemStack> armor;
    ArrayList<ItemStack> items;
    ArrayList<ItemStack> hotBar;

    int selected;   // selected inventory slot id

    boolean potionBag;
    int potionBagSize;
    ArrayList<ItemStack> potions;

    boolean scrollBook;
    int scrollBookSize;
    ArrayList<ItemStack> scrolls;

    boolean trapBox;
    int trapBoxSize;
    ArrayList<ItemStack> traps;

    boolean keyChain;
    int keyChainSize;
    ArrayList<ItemStack> keys;
}
