package com.factory;

import java.awt.image.BufferedImage;

public class Item {
    GamePanel gamePanel;

    BufferedImage image;

    int x,y;
    boolean inContainer = true;

    public Item(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }



}
