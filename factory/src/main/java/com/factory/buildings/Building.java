package com.factory.buildings;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.factory.GamePanel;

public class Building {

    public int worldX, worldY;
    public GamePanel gamePanel;
    public int screenX, screenY;
    public int width,height;
    public BufferedImage image;
    
    public Building(GamePanel gamePanel, int worldX, int worldY) {
        this.gamePanel = gamePanel;
        this.worldX = worldX;
        this.worldY = worldY;
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


    public void update() {
        
    }

    public void paint(Graphics2D g2) {

    }

}
