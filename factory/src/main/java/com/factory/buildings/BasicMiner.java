package com.factory.buildings;

import java.awt.Graphics2D;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import javax.imageio.ImageIO;

import com.factory.GamePanel;
import com.factory.GUI.ItemContainer;
import com.factory.items.Item;

public class BasicMiner extends Building {

    int miningSpeed = 5 * gamePanel.FPS;  //how many seconds to mine once
    int miningCooldown = miningSpeed;
    int miningProductivity = 1; // how many ores are produced

    ItemContainer container;

    public BasicMiner(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        
        setIndividualDefaults();
    }

    private void setIndividualDefaults() {
    
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/buildings/basicMiner.png"));
        } catch (IOException e) {
            e.printStackTrace();
        } 

        width = image.getWidth() * gamePanel.scale;
        height = image.getHeight() * gamePanel.scale;
        item = com.factory.items.BasicMinerItem.class;
        container = new ItemContainer(gamePanel, 1);
        markTiles();
    }

    @Override
    public void update() {

        screenX = worldX - gamePanel.player.cameraX;
        screenY = worldY - gamePanel.player.cameraY;

        if(miningCooldown == 0) {
            mine();
        }

        if (gamePanel.mouseHandler.leftClick && !gamePanel.mouseHandler.leftClickUsed && gamePanel.mouseHandler.touchingMouse(screenX, screenY, width, height)) {
            container.toggle();
        }

        checkForDestruction();
        miningCooldown--;
    }

    @Override
    public void paint(Graphics2D g2) {

        width = image.getWidth() * gamePanel.scale;
        height = image.getHeight() * gamePanel.scale;


        if (screenX + width >= 0 && screenX <= gamePanel.screenWidth && screenY + height >= 0 && screenY <= gamePanel.screenHeight) { 
            g2.drawImage(image,screenX,screenY,width, height,null);
        }
        
    }

    public void mine() {
        miningCooldown = miningSpeed;
        int col;
        int row;

        Random rand = new Random();

        col = rand.nextInt(worldX / gamePanel.tileSize, worldX / gamePanel.tileSize + width / gamePanel.tileSize);
        row = rand.nextInt(worldY / gamePanel.tileSize, worldY / gamePanel.tileSize + height / gamePanel.tileSize);

        if (!gamePanel.tileManager.hasOre[col][row]) {
            return;
        }
        gamePanel.tileManager.oreCount[col][row] -= miningProductivity;
        
        if (gamePanel.tileManager.oreCount[col][row] < 1) {
            gamePanel.tileManager.hasOre[col][row] = false;
        }

        try {
            Item newItem = gamePanel.tileManager.oreTypeClass[col][row].getConstructor(GamePanel.class, int.class).newInstance(gamePanel, miningProductivity);

            container.add(newItem);

        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException |InvocationTargetException | NoSuchMethodException | SecurityException e) {

            e.printStackTrace();
        }
    }


}
