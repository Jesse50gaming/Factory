package com.factory.util;

public enum Direction {

    UP,
    DOWN,
    LEFT,
    RIGHT;


    public String directionString() {
        switch (this) {
            case UP: return "_UP";
            case DOWN: return "_DOWN";     
            case LEFT: return "_LEFT";
            case RIGHT: return "_RIGHT";
            default: return "_UP"; 
        }
    }


    public Direction rotate() {
        switch (this) {
            case UP: return RIGHT;
            case DOWN: return LEFT;     
            case LEFT: return UP;
            case RIGHT: return DOWN;
            default: return this; 
        }
    }

    public int rotateAngle() {
        switch (this) {
            case UP: return 0;
            case DOWN: return 180;     
            case LEFT: return 270;
            case RIGHT: return 90;
            default: return 0; 
        }
    }

    public Direction opposite() {
        switch (this) {
            case UP: return DOWN;
            case DOWN: return UP;     
            case LEFT: return RIGHT;
            case RIGHT: return LEFT;
            default: return this; 
        }
    }

}
