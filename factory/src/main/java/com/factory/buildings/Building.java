package com.factory.buildings;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;

import com.factory.GamePanel;
import com.factory.GUI.ItemContainer;
import com.factory.items.Item;
import com.factory.util.Direction;

public abstract class Building {

    public int worldX, worldY;
    public GamePanel gamePanel;
    public int screenX, screenY;
    public int width,height;
    public BufferedImage image;
    Direction direction;
    Class<? extends Item> item;
    boolean destroyed = false;
    
    public Building(GamePanel gamePanel, int worldX, int worldY, Direction direction) {
        this.gamePanel = gamePanel;
        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        setDefaults();
    }

    
    private void setDefaults() {
        gamePanel.buildings.add(this);
    }

    public void markTiles() {
        int tileHeight = height / gamePanel.tileSize;
        int tileWidth = width / gamePanel.tileSize;
        for (int y = 0; y < tileHeight; y++) {
            for (int x = 0; x < tileWidth; x++) {
                gamePanel.hasObject[x+worldX/gamePanel.tileSize][y + worldY /gamePanel.tileSize] = true;
            }
        }
    }


    public abstract void update();

    public void checkForDestruction() {
        if (gamePanel.mouseHandler.rightDown && !gamePanel.mouseHandler.rightClickUsed && !destroyed) {

            int mouseTileX = (int) Math.floor(gamePanel.mouseHandler.mouseWorldX / gamePanel.tileSize);
            int mouseTileY = (int) Math.floor(gamePanel.mouseHandler.mouseWorldY / gamePanel.tileSize);

            int buildingTileX = (int) Math.floor(worldX / gamePanel.tileSize);
            int buildingTileY = (int) Math.floor(worldY / gamePanel.tileSize);

            int tilesWide = width / gamePanel.tileSize;
            int tilesHigh = height / gamePanel.tileSize;
            if (mouseTileX >= buildingTileX && mouseTileX < buildingTileX + tilesWide && mouseTileY >= buildingTileY && mouseTileY < buildingTileY + tilesHigh) {
                gamePanel.mouseHandler.useRight();
                destroy(gamePanel.player.inventory);
                
            }
            
        }
        
    }

    

    public abstract void paint(Graphics2D g2);

    public boolean destroy(ItemContainer inventory) {
        destroyed = true;
        Item newItem;
        try {
            newItem = item.getConstructor(GamePanel.class, int.class).newInstance(gamePanel,1);
            inventory.add(newItem);
            gamePanel.destroyBuilding(this);
            return true;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void updateTexture() {
        
    }

}
