package com.darksouls.rougelike.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NodeList {
    private ArrayList<Node> nodes;

    public NodeList(){
        nodes = new ArrayList<>();
    }

    public NodeList(ArrayList<Node> nodes){
        this.nodes = nodes;
        sort();
    }

    public void add(Node node){
        nodes.add(node);
        sort();
    }

    public Node get(int i){
        return nodes.get(i);
    }

    public Node get(VPoint pos){
        for(Node node : nodes){
            if(pos.equals(node.getTile().vect())) {
                return node;
            }
        }
        return null;
    }

    public int indexOf(Node node){
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getTile().vect().equals(node.getTile().vect())) {
                return i;
            }
        }
        return -1;
    }

    public void remove(Node node){
        int i = indexOf(node);
        if(i >= 0) {
            nodes.remove(i);
            sort();
        }
    }


    public boolean contains(VPoint pos){
        for(Node node : nodes)
            if(node.getTile().vect().equals(pos)) {
                return true;
            }
        return false;
    }

    public boolean contains(Node node){
        return this.contains(node.getTile().vect());
    }


    public int size() {
        return nodes.size();
    }

    public List<Node> getList(){
        return nodes;
    }

    public void sort(){
        Collections.sort(nodes);
    }
}