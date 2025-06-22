package com.factory.items;

import java.awt.image.BufferedImage;

import com.factory.GamePanel;

public class Item {
    GamePanel gamePanel;

    public BufferedImage image;

    int x,y;
    int stackSize;
    int maxStackSize;
    boolean inContainer = true;
    public int containerHeight;

    public int containerWidth;
    int groundHeight, groundWidth;

    public Item(GamePanel gamePanel,int x, int y, int numberOfItems) {
        this.gamePanel = gamePanel;
        this.x = x;
        this.y = y;
        this.stackSize = numberOfItems;
        setDefaults();
    }

    public void setDefaults() {
        containerHeight = 16 * gamePanel.scale;
        containerWidth = 16 * gamePanel.scale;
        groundHeight = gamePanel.tileSize / 4;
        groundWidth = gamePanel.tileSize / 4;
    }



}
