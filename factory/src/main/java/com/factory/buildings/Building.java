package com.factory.buildings;
import java.awt.Graphics2D;

import com.factory.GamePanel;

public class Building {

    public int worldX, worldY;
    public GamePanel gamePanel;
    public int screenX, screenY;
    public int width,height;

    public Building(GamePanel gamePanel, int worldX, int worldY) {
        this.gamePanel = gamePanel;
        this.worldX = worldX;
        this.worldY = worldY;
        
    }

    
    public void update() {
        
    }

    public void paint(Graphics2D g2) {

    }

}
