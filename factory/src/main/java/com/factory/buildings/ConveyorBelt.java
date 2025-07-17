package com.factory.buildings;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.factory.GamePanel;
import com.factory.items.Item;
import com.factory.util.Direction;


public class ConveyorBelt extends Building{

    Direction direction;
    int movingSpeed = 1; //how many pixels an item moves per frame
    BufferedImage image2;


    public ConveyorBelt(GamePanel gamePanel, int worldX, int worldY, Direction direction) {
        super(gamePanel, worldX, worldY, direction);
        this.direction = direction;

        setIndividualDefaults();
    }

    @Override
    public void update() {
        screenX = worldX - gamePanel.player.cameraX;
        screenY = worldY - gamePanel.player.cameraY;
        moveItems();
        checkForDestruction();
    }

    @Override
    public void paint(Graphics2D g2) {
        if (image == null || image2 == null) return;

        screenX = worldX - gamePanel.player.cameraX;
        screenY = worldY - gamePanel.player.cameraY;

        BufferedImage imgToDraw = null;

        if (gamePanel.animationCount < 15) {
            imgToDraw = image;
        } else if (gamePanel.animationCount >= 15 && gamePanel.animationCount < 30) {
            imgToDraw = image2;
        } else if (gamePanel.animationCount >= 30 && gamePanel.animationCount < 45) {
            imgToDraw = image;
        } else if (gamePanel.animationCount >= 45) {
            imgToDraw = image2;
        }

        g2.rotate(Math.toRadians(direction.rotateAngle()), screenX + width / 2, screenY + height / 2);
        g2.drawImage(imgToDraw, screenX, screenY, width, height, null);
        g2.rotate(-Math.toRadians(direction.rotateAngle()), screenX + width / 2, screenY + height / 2);
    }

    private void setIndividualDefaults() {
    
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/buildings/conveyorBelt/conveyorBelt_1.png"));
            image2 = ImageIO.read(getClass().getResourceAsStream("/buildings/conveyorBelt/conveyorBelt_2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        } 

        width = image.getWidth() * gamePanel.scale;
        height = image.getHeight() * gamePanel.scale;
        item = com.factory.items.ConveyorBeltItem.class; 
        markTiles();
    }

    public void moveItems() {
        
        int beltX = worldX;
        int beltY = worldY;
        int beltWidth = gamePanel.tileSize;
        int beltHeight = gamePanel.tileSize;

        for (Item item : gamePanel.floorItems) {
            int itemX = item.worldX;
            int itemY = item.worldY;
            int itemWidth = item.tileWidth * gamePanel.tileSize;
            int itemHeight = item.tileHeight * gamePanel.tileSize;

            
            int overlapX = Math.max(0, Math.min(itemX + itemWidth, beltX + beltWidth) - Math.max(itemX, beltX));
            int overlapY = Math.max(0, Math.min(itemY + itemHeight, beltY + beltHeight) - Math.max(itemY, beltY));
            boolean onEnough = false;

            if (item.moveDirection == direction) {
                onEnough = overlapX > 0 && overlapY > 0;
            } else {
                onEnough = overlapX >= itemWidth && overlapY >= itemHeight;
            }   
            
            if (!(onEnough)) continue;
            
            item.moveDirection = direction;
            item.move(direction, movingSpeed);
        }
    }

}
