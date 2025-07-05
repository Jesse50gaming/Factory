package com.factory;

import java.io.IOException;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import com.factory.items.Item;

public class Hotbar {

    GamePanel gamePanel;
    int height,width;
    BufferedImage topImage, middleImage,bottomImage;
    Item[][] items;
    int screenX, screenY;
    int pixelWidth;
    int grabCooldown = 30;
    public Player player;

    public Hotbar(GamePanel gamePanel, int height, Player player) {
        this.player = player;
        this.gamePanel = gamePanel;
        this.height = height;
        setDefaults();
    }

    private void setDefaults() {
        width = 8;
        pixelWidth = 138 * gamePanel.scale;
        screenX =  gamePanel.screenWidth/2 - pixelWidth/2;
        screenY = gamePanel.screenHeight - ((height - 1) * 17 * gamePanel.scale) - (1 * 18 * gamePanel.scale);
        
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


        // middle
        for (int i = 1; i < height; i++) {
            g2.drawImage(middleImage, screenX, screenY + (i - 1) * middleImage.getHeight() * gamePanel.scale,middleImage.getWidth() * gamePanel.scale ,middleImage.getHeight() * gamePanel.scale, null);
        }

        // bottom
        g2.drawImage(bottomImage, screenX, screenY + (height - 1) * middleImage.getHeight() * gamePanel.scale ,bottomImage.getWidth() * gamePanel.scale ,bottomImage.getHeight() * gamePanel.scale, null);

        //items 

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (items[x][y] == null) continue;

                if (items[x][y].image == null) {
                    System.out.println("Image is null at (" + x + "," + y + ")");
                } else {

                    g2.drawImage(items[x][y].image,screenX + gamePanel.scale + (17 * x) * gamePanel.scale,screenY +(17 * y) * gamePanel.scale,items[x][y].containerWidth * gamePanel.scale,items[x][y].containerHeight * gamePanel.scale,null);

                    Font font = new Font("TIMES NEW ROMAN", 1, 10 * gamePanel.scale);
                    g2.setFont(font);
                    g2.setColor(Color.WHITE);
                    g2.drawString(String.valueOf(items[x][y].stackSize),screenX + gamePanel.scale + (17 * x) * gamePanel.scale,screenY + (17 * y) * gamePanel.scale + items[x][y].containerHeight * gamePanel.scale - 4);
                }
            }
        }
    }

    public void update() {
        if (grabCooldown > 0) {
            grabCooldown--;
        }
        

        

        if (gamePanel.mouseHandler.touchingMouse(screenX,screenY,pixelWidth,height * 17 * gamePanel.scale) && gamePanel.mouseHandler.leftDown && grabCooldown == 0) {
            

            if (gamePanel.mouseHandler.itemInHand == false && findItem() != null && grabCooldown == 0) {
                gamePanel.mouseHandler.pickUpItem(findItem(), this);
               
                grabCooldown = 30;
            }

            if (gamePanel.mouseHandler.itemInHand == true && grabCooldown == 0) {
                gamePanel.mouseHandler.dropItem(this);
                grabCooldown = 30;
            }
           
        }

    }

    public void add(Item item) {
        if(item == null) {
            return;
        }
        
        int x = findCol();
        int y = findRow();

        if (items[x][y] != null) {

            gamePanel.mouseHandler.pickUpItem(items[x][y], this);

        }
        
        items[x][y] = item;
        items[x][y].containerX = x;
        items[x][y].containerY = y;
                
    }

    public boolean nullCheck() {
        int x = findCol();
        int y = findRow();

        return items[x][y] == null;
    }

    public void remove(Item item) {
        items[item.containerX][item.containerY] = null;
        
    }

    

    public Item findItem() {
        int mouseX = gamePanel.mouseHandler.mouseScreenX;
        int mouseY = gamePanel.mouseHandler.mouseScreenY;

        double col = (mouseX - screenX) / 17.0 / gamePanel.scale;
        double row = (mouseY - screenY) / 17.0 / gamePanel.scale;

        int colRound = (int) Math.floor(col);
        int rowRound = (int) Math.floor(row);

        return items[colRound][rowRound];

    }

    private int findCol() {
        int mouseX = gamePanel.mouseHandler.mouseScreenX;
        double col = (mouseX - screenX) / 17.0 / gamePanel.scale;
        int colRound = (int) Math.floor(col);
        return colRound;
    }

    private int findRow() {
        int mouseY = gamePanel.mouseHandler.mouseScreenY;
        double row = (mouseY - screenY) / 17.0 / gamePanel.scale;
        int rowRound = (int) Math.floor(row);
        return rowRound;

    }

}
