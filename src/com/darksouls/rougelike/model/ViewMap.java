package com.darksouls.rougelike.model;

import java.util.ArrayList;

public class ViewMap {
    private ArrayList<VPoint> keys;
    private ArrayList<Integer> values;

    public ViewMap(){
        keys = new ArrayList<>();
        values = new ArrayList<>();
    }

    public void set(VPoint k, int v){
        if(!keys.contains(k)){
            keys.add(k);
            values.add(v);
        } else {
            values.set(keys.indexOf(k), v);
        }
    }

    public void remove(VPoint k){
        if(keys.contains(k)){
            int i = keys.indexOf(k);
            keys.remove(i);
            values.remove(i);
        }
    }

    public boolean containsKey(VPoint k){
        return keys.contains(k);
    }

    public int get(VPoint k){
        return values.get(keys.indexOf(k));
    }

    public int size(){
        return keys.size();
    }

    public ArrayList<VPoint> getKeys(){
        return keys;
    }

    public ArrayList<Integer> getValues(){
        return values;
    }
}
