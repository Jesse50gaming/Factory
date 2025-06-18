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


    public ItemContainer(GamePanel gamePanel, int height) {
        this.gamePanel = gamePanel;
        this.height = height; 
        setDefaults();
    }

    private void setDefaults() {
        width = 8;
        screenX = 100;
        screenY = 100;
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
        for (int i = 1; i < height - 1; i++) {
            g2.drawImage(middleImage, screenX, screenY + i * middleImage.getHeight() * gamePanel.scale,middleImage.getWidth() * gamePanel.scale ,middleImage.getHeight() * gamePanel.scale, null);
        }

        // bottom
        g2.drawImage(bottomImage, screenX, screenY + (height - 1) * middleImage.getHeight() * gamePanel.scale ,bottomImage.getWidth() * gamePanel.scale ,bottomImage.getHeight() * gamePanel.scale, null);
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
    }



    

}
