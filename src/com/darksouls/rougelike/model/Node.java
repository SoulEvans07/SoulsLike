package com.darksouls.rougelike.model;

public class Node implements Comparable {
    private Tile ref;

    private int steps;
    private int dist;
    private Node parent;

    public Node(Tile tile, Tile goal){
        ref = tile;
        steps = 0;
        tile.toString();
        goal.toString();
        dist = Math.abs(goal.vect().getX() - tile.vect().getX()) + Math.abs(goal.vect().getY() - tile.vect().getY());
        parent = null;
    }

    public Node(Tile tile, Tile goal, Node _parent){
        this(tile, goal);
        parent = _parent;
    }

    public Node(Node n){
        ref = n.ref;
        steps = n.steps;
        dist = n.dist;
        parent = n.parent;
    }

    public Tile getTile() {
        return ref;
    }

    public int getDist() {
        return dist;
    }

    public int getSteps() {
        return steps;
    }

    public Node getParent() {
        return parent;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getF(){
        return dist + steps;
    }

    @Override
    public int compareTo(Object o) {
        int compareF = ((Node)o).getF();

        return this.getF() - compareF;
    }
}
