package com.factory;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ItemContainer {
    GamePanel gamePanel;
    int height,width;
    BufferedImage topImage, middleImage,bottomImage;
    Item[][] items;
    int screenX, screenY;
    boolean open;
    int closeTimer;
    int pixelWidth;


    public ItemContainer(GamePanel gamePanel, int height) {
        this.gamePanel = gamePanel;
        this.height = height; 
        setDefaults();
    }

    private void setDefaults() {
        width = 8;
        screenX = 100;
        screenY = 100;
        pixelWidth = 138 * gamePanel.scale;
        open = false;
        items = new Item[width][height];
        try {
            topImage = ImageIO.read(getClass().getResourceAsStream("/GUI/containerTop.png"));
            middleImage = ImageIO.read(getClass().getResourceAsStream("/GUI/containerMiddle.png"));
            bottomImage = ImageIO.read(getClass().getResourceAsStream("/GUI/containerBottom.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void draw(Graphics2D g2) {
        if (!open) return;

        // top
        g2.drawImage(topImage, screenX, screenY,topImage.getWidth() * gamePanel.scale ,topImage.getHeight() * gamePanel.scale, null);

        // middle
        for (int i = 1; i < height; i++) {
            g2.drawImage(middleImage, screenX, screenY + 12 * gamePanel.scale + (i - 1) * middleImage.getHeight() * gamePanel.scale,middleImage.getWidth() * gamePanel.scale ,middleImage.getHeight() * gamePanel.scale, null);
        }

        // bottom
        g2.drawImage(bottomImage, screenX, screenY + 12 * gamePanel.scale + (height - 1) * middleImage.getHeight() * gamePanel.scale ,bottomImage.getWidth() * gamePanel.scale ,bottomImage.getHeight() * gamePanel.scale, null);
    }

    public void toggle() {
        
        if(open == false && closeTimer == 0) {
            open = true;
            
        }else if(open == true && closeTimer == 0) {
            open = false;
        }
        closeTimer = 30;
    }

    public void update() {
        if (closeTimer > 0) {
            closeTimer--;
        }

        if (open && gamePanel.mouseHandler.touchingMouse(screenX,screenY,pixelWidth,12 * gamePanel.scale) && gamePanel.mouseHandler.leftDown) {
            drag();
        }
    }

    int mouseStartX,mouseStartY;
    public void drag() {
        if (!gamePanel.mouseHandler.dragging) {
            mouseStartX = gamePanel.mouseHandler.mouseScreenX;
            mouseStartY = gamePanel.mouseHandler.mouseScreenY;
        }
        gamePanel.mouseHandler.dragging = true;

        screenX += gamePanel.mouseHandler.mouseScreenX - mouseStartX;
        screenY += gamePanel.mouseHandler.mouseScreenY - mouseStartY;

        mouseStartX = gamePanel.mouseHandler.mouseScreenX;
        mouseStartY = gamePanel.mouseHandler.mouseScreenY;
    }



    

}
