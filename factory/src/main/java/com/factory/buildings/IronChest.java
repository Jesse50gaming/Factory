package com.factory.buildings;
import java.awt.Graphics2D;

import java.io.IOException;

import javax.imageio.ImageIO;

import com.factory.GamePanel;
import com.factory.GUI.ItemContainer;

public class IronChest extends Building {

    
    
    ItemContainer container;
    

    public IronChest(GamePanel gamePanel, int x, int y) {
        super(gamePanel,x,y);
        setIndividualDefaults();
    }

    public void setIndividualDefaults() {
        

        container = new ItemContainer(gamePanel, 5);
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/buildings/ironChest.png"));
        } catch (IOException e) {
           
            e.printStackTrace();
        }
        item = com.factory.items.IronChestItem.class;
        width = image.getWidth() * gamePanel.scale;
        height = image.getHeight() * gamePanel.scale;

        markTiles();
    }

    
    @Override
    public void update() {
        container.update();

        screenX = worldX - gamePanel.player.cameraX;
        screenY = worldY - gamePanel.player.cameraY;


        
        if (gamePanel.mouseHandler.touchingMouse(screenX, screenY, width, height) && gamePanel.mouseHandler.leftClick && !gamePanel.mouseHandler.leftClickUsed) {
            container.toggle();
            gamePanel.mouseHandler.useLeft();
        }
        checkForDestruction();
    }

    
    @Override
    public void paint(Graphics2D g2) {
        

        width = image.getWidth() * gamePanel.scale;
        height = image.getHeight() * gamePanel.scale;

        
        
        if (screenX + width >= 0 && screenX <= gamePanel.screenWidth && screenY + height >= 0 && screenY <= gamePanel.screenHeight) {
            // chest
            g2.drawImage(image, screenX, screenY, width, height, null);

        }
        
    }

    public void remove() {
        gamePanel.removeBuilding(this);
        container.delete();
    }

}
