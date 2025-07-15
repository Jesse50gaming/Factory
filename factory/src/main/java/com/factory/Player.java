package com.factory;

import java.awt.Graphics2D;

import java.awt.image.BufferedImage;
import java.io.IOException;


import javax.imageio.ImageIO;

import com.factory.GUI.Hotbar;
import com.factory.GUI.ItemContainer;
import com.factory.Handlers.KeyHandler;
import com.factory.Handlers.MouseHandler;
import com.factory.items.BasicMinerItem;
import com.factory.items.CopperOre;
import com.factory.items.IronChestItem;
import com.factory.items.IronOre;
import com.factory.items.Item;


public class Player extends Entity {

   

    
    public BufferedImage currentImage;
    public ItemContainer inventory;
    public Hotbar hotbar;

    int spriteCounter = 0;
    int spriteNum = 1;
    public double scale = 1;

    public int cameraX, cameraY;
    GamePanel gamePanel;
    KeyHandler keyHandler;
    MouseHandler mouseHandler;

    int cooldown = 0;

    public boolean controllable = true;

    int pickUpRange;

    
    

    public Player(GamePanel gamePanel, KeyHandler keyHandler, MouseHandler mouseHandler) {
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        this.mouseHandler = mouseHandler;
        loadPlayerImages();
        setDefaultValues();
    }

    private void setDefaultValues() {
        
        worldX = 200 * gamePanel.tileSize;
        worldY = 200 * gamePanel.tileSize;
        width = 1;
        height = 1;
        speed = 2 * gamePanel.scale;
        currentImage = down1; 
        health = 100;
        maxHealth = 100;
        damage = 10;
        pickUpRange = 2;
        inventory = new ItemContainer(gamePanel, 10);
        hotbar = new Hotbar(gamePanel, 2,this);
        

        inventory.add(new IronOre(gamePanel, 1));
        inventory.add(new CopperOre(gamePanel, 5));
        inventory.add(new IronChestItem(gamePanel,5));
        inventory.add(new BasicMinerItem(gamePanel, 5));
        updateHitBox();
        
    }

    public void update() {
        inventory.update();
        hotbar.update();
        move();
        keyBinds();
    }

    public void keyBinds() {
        if (keyHandler.ePressed) {
            inventory.toggle();
        }  
        if (keyHandler.fPressed) {
            pickUpItems();
        }
    }

    private void loadPlayerImages() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/player/up1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player/up2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/player/down1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player/down2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/left1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/left2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/right1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/right2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paint(Graphics2D g2) {
        int drawX = worldX - cameraX;
        int drawY = worldY - cameraY;
        g2.drawImage(currentImage, drawX, drawY, width * (int) (gamePanel.tileSize * scale), height * (int) (gamePanel.tileSize * scale), null);
        hotbar.draw(g2);
    }

    public void move() {
        boolean moving = false;

        if (keyHandler.aPressed) {
            
            moving = true;
            direction = "left";
            updateSprite(left1, left2);
        }  
        if (keyHandler.dPressed) {
            
            moving = true;
            direction = "right";
            updateSprite(right1, right2);
        } 
        if (keyHandler.wPressed) {
            
            moving = true;
            direction = "up";
            updateSprite(up1, up2);
        } 
        if (keyHandler.sPressed) {
            
            moving = true;
            
            direction = "down";
            updateSprite(down1, down2);
        }

        collisionOn = false;
        gamePanel.collisionChecker.checkTile(this);
        

        if (collisionOn == false && moving && controllable) {
            if (keyHandler.wPressed) {
                worldY -= speed;
            }
            if (keyHandler.sPressed) {
                worldY += speed;
            }
            if (keyHandler.aPressed) {
                worldX -= speed;
            }
            if (keyHandler.dPressed) {
                worldX += speed;
                
            }

        }

        if (!moving) spriteCounter = 0; 

        cameraX = worldX - gamePanel.screenWidth / 2 + gamePanel.tileSize / 2;
        cameraY = worldY - gamePanel.screenHeight / 2 + gamePanel.tileSize / 2;

        int maxCamX = gamePanel.maxWorldCol * gamePanel.tileSize - gamePanel.screenWidth;
        int maxCamY = gamePanel.maxWorldRow * gamePanel.tileSize - gamePanel.screenHeight;

        cameraX = Math.max(0, Math.min(cameraX, maxCamX));
        cameraY = Math.max(0, Math.min(cameraY, maxCamY));

        
        
        worldX = Math.max(0, Math.min(worldX, gamePanel.worldWidth - width * gamePanel.tileSize));
        worldY = Math.max(0, Math.min(worldY, gamePanel.worldHeight - height * gamePanel.tileSize));

    }

    private void updateSprite(BufferedImage img1, BufferedImage img2) {


        
        spriteCounter++;
        if (spriteCounter > 10) { //update every 10 frames
            if (spriteNum == 1) {
                spriteNum = 2;
            } else {
            spriteNum = 1;
            }
            spriteCounter = 0;
        }

        if (spriteNum == 1) {
            currentImage = img1;
        } else {
            currentImage = img2;
        }
        
        
    }

    public void pickUpItems() {

        int startCol = worldX / (gamePanel.tileSize / 2);
        int startRow = worldY / (gamePanel.tileSize / 2);

        for (int x = 0; x < pickUpRange; x++) {
            for (int y = 0; y < pickUpRange; y++) {
                if (gamePanel.hasItem[startCol + x][startRow + y]) {
                    for (Item item : gamePanel.floorItems) {
                        if (item.worldGroundCol == startCol + x && item.worldGroundRow == startRow + y) {
                            addItem(item);
                            gamePanel.removeFloorItem(item);
                        }
                    }
                }
            }
        }

    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public void updateHitBox() {
      
        hitBox.x = 4 * gamePanel.scale;
        hitBox.y = 4 * gamePanel.scale;
        hitBox.width = (int) (0.5 * width * gamePanel.tileSize * scale);
        hitBox.height = (int) (0.5 * height * gamePanel.tileSize * scale);  
    }

}