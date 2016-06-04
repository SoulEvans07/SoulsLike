package com.darksouls.rougelike.model;


import com.darksouls.rougelike.utility.LogHelper;

import java.awt.*;
import java.util.Random;

public class VPoint {
    private double x;
    private double y;

    public VPoint(){
        x = 0;
        y = 0;
    }

    public VPoint(double px, double py){
        this.x = px;
        this.y = py;
    }

    public VPoint(int px, int py){
        this((double)px, (double)py);
    }

    public VPoint(VPoint p){
        this(p.x(), p.y());
    }

    public VPoint(Point p){
        this(p.x, p.y);
    }

    public double x(){
        return this.x;
    }

    public int getX(){
        return (int)this.x;
    }

    public void x(double p){
        this.x = p;
    }

      public double y() {
        return this.y;
    }

    public int getY() {
        return (int)this.y;
    }

    public void y(double p){
        this.y = p;
    }

    public VPoint set(double px, double py){
        this.x = px;
        this.y = py;
        return this;
    }

    public VPoint set(VPoint p){
        return this.set(p.x(), p.y());
    }

    public void printPoint(String end){
        LogHelper.write("[" + x + ", " + y + "]" + end);
    }

    public void printPoint(){
        printPoint("\n");
    }

    public VPoint add(VPoint a){
        return new VPoint(this.x + a.x(), this.y + a.y());
    }

    public VPoint subtract(VPoint a) {
        return new VPoint(this.x - a.x(), this.y - a.y());
    }

    public VPoint multiply(double m){
        return new VPoint(this.x * m, this.y * m);
    }

    public double length(){
        return Math.sqrt(x*x + y*y);
    }

    public VPoint addX(int x){
        return new VPoint(this.x + x, this.y);
    }

    public VPoint addY(int y){
        return new VPoint(this.x, this.y + y);
    }

    public double getDist(VPoint p){
        return Math.sqrt(Math.pow(this.x - p.x(), 2) + Math.pow(this.y - p.y(), 2));
    }

    @Override
    public String toString(){
        return "[" + x + ", " + y + "]";
    }

    @Override
    public boolean equals(Object object){
        boolean same = false;
        if (object != null && object instanceof VPoint) {
            same = (this.x == ((VPoint) object).x &&
                    this.y == ((VPoint) object).y );
        }
        return same;
    }

    public static VPoint randPos(VPoint size){
        VPoint rand = new VPoint();
        Random random = new Random();
        rand.set((int) Math.floor(random.nextDouble() * size.x()),
                 (int) Math.floor(random.nextDouble() * size.y()));
        return rand;
    }


}
