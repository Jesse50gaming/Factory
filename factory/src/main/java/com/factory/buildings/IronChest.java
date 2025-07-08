package com.factory.buildings;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.factory.GamePanel;
import com.factory.ItemContainer;

public class IronChest extends Building {

    BufferedImage image;
    
    ItemContainer container;
    

    public IronChest(GamePanel gamePanel, int x, int y) {
        super(gamePanel,x,y);
        setIndividualDefaults();
    }

    public void setIndividualDefaults() {
        width = 16 * gamePanel.scale;
        height = 16 * gamePanel.scale;

        container = new ItemContainer(gamePanel, 5);
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/buildings/ironChest.png"));
        } catch (IOException e) {
           
            e.printStackTrace();
        }
    }

    
    @Override
    public void update() {
        container.update();

        screenX = worldX - gamePanel.player.cameraX;
        screenY = worldY - gamePanel.player.cameraY;

        if (gamePanel.mouseHandler.touchingMouse(screenX, screenY, width, height) && gamePanel.mouseHandler.rightClick) {
            container.toggle();
        }
    }

    
    @Override
    public void paint(Graphics2D g2) {
        

        width = image.getWidth() * gamePanel.scale;
        height = image.getHeight() * gamePanel.scale;

        // container
        if (container.open) {
            container.draw(g2);
        }
        
        if (screenX + width < 0 || screenX > gamePanel.screenWidth || screenY + height < 0 || screenY > gamePanel.screenHeight) {
            return;
        }

        // chest
        g2.drawImage(image, screenX, screenY, width, height, null);

        
    }

}
