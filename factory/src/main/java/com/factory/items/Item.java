package com.factory.items;

import java.awt.image.BufferedImage;

import com.factory.GamePanel;
import com.factory.buildings.Building;


public class Item {
    GamePanel gamePanel;

    public BufferedImage image;
    boolean placeable = false;
    int x,y;
    public int stackSize;
    int maxStackSize;
    boolean inContainer = true;
    public int containerHeight;
    public int containerX,containerY;
    public String name;

    public int containerWidth;
    int groundHeight, groundWidth;
    int tileWidth,tileHeight;

    Class<? extends Building> buildingType;
    

    public Item(GamePanel gamePanel,int x, int y, int numberOfItems) {
        this.gamePanel = gamePanel;
        this.x = x;
        this.y = y;
        this.stackSize = numberOfItems;
        setDefaults();
    }

    public void setDefaults() {
        containerHeight = 16;
        containerWidth = 16;
        groundHeight = gamePanel.tileSize / 4;
        groundWidth = gamePanel.tileSize / 4;
    }

    public void place() {

        if(placeable) {
            boolean place = true;

            int mouseTileWorldX = (int) Math.floor(gamePanel.mouseHandler.mouseWorldX/gamePanel.tileSize);
            int mouseTileWorldY = (int) Math.floor(gamePanel.mouseHandler.mouseWorldY/gamePanel.tileSize);

            for (int y = 0; y < tileHeight; y++) {
                for (int x = 0; x < tileWidth; x++) {
                    if(!gamePanel.hasObject[x + mouseTileWorldX][y + mouseTileWorldY]) {
                        continue;
                    }
                    place = false;
                }
            }
            if (place) {
                stackSize--;

                int placeX = mouseTileWorldX * gamePanel.tileSize;
                int placeY = mouseTileWorldY * gamePanel.tileSize;

                try {
                    Building newBuilding = buildingType.getConstructor(GamePanel.class, int.class, int.class).newInstance(gamePanel, placeX, placeY);

                    gamePanel.buildings.add(newBuilding);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }



}
