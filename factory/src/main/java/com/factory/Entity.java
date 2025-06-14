package com.factory;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Entity {

    BufferedImage up1, down1, left1, right1;
    BufferedImage up2, down2, left2, right2;

    public int worldX, worldY;
    public int width;
    public int height;
    public int maxHealth;
    public int speed;
    public int health;
    public int damage;

    public Rectangle hitBox = new Rectangle();
    public boolean collisionOn = false;
    public String direction = "down";

}