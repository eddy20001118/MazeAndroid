package com.android.yuhao_li.maze_android;

public class Position {
    private int x, y;
    private Position prev;


    public Position(int x, int y, Position prev) {
        this.x = x;
        this.y = y;
        this.prev = prev;
    }

    public Position(int x, int y) {
        this(x, y, null);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x =x;
    }

    public void setY(int y) {
        this.y =y;
    }

    public Position getPrev() {
        return prev;
    }

    public void PrintPosition(){
        System.out.println(x+ ","+y);
    }
}
