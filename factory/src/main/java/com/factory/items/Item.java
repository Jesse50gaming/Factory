package com.factory.items;

import java.awt.image.BufferedImage;

import com.factory.GamePanel;
import com.factory.buildings.Building;


public class Item {
    GamePanel gamePanel;

    public BufferedImage image;
    boolean placeable = false;
    public int stackSize;
    int maxStackSize;
    public boolean onFloor;
    public int containerHeight;
    public int containerX,containerY;
    public String name;

    public int worldX,worldY;

    public int containerWidth;
    int groundHeight, groundWidth;
    int tileWidth,tileHeight;

    Class<? extends Building> buildingType;
    

    public Item(GamePanel gamePanel, int numberOfItems) {
        this.gamePanel = gamePanel;

        this.stackSize = numberOfItems;
        setDefaults();
    }

    public void setDefaults() {
        maxStackSize = 100;
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
                    checkIfGone();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean merge(Item item) {
        
        
        if (item.getClass() == this.getClass() && this.name.equals(item.name)) {
            if(stackSize == maxStackSize || item.stackSize == maxStackSize || stackSize + item.stackSize > maxStackSize) return false;
            this.stackSize += item.stackSize;
            return true;
        }
        return false;
    }

    public void update() {
        
    }

    public boolean checkIfGone() {
        if (stackSize < 1) {
            return true;
        }
        return false;
    }

    

    

}
