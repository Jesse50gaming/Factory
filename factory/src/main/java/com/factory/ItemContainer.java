package com.factory;

public class ItemContainer {
    GamePanel gamePanel;
    int height,width;

    public ItemContainer(GamePanel gamePanel, int height, int width) {
        this.gamePanel = gamePanel;
        this.width = width;
        this.height = height;
    }

}
