package com.factory.items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.factory.GamePanel;
import com.factory.buildings.Building;
import com.factory.buildings.ConveyorBelt;
import com.factory.util.Direction;


public abstract class Item {
    GamePanel gamePanel;

    public BufferedImage image;
    public boolean placeable = false;
    public int stackSize;
    int maxStackSize;
    public boolean onFloor;
    public int containerHeight;
    public int containerX,containerY;
    public String name;

    public int worldX,worldY;
    public int worldGroundCol,worldGroundRow;

    public int containerWidth;
    public int groundHeight;

    public int groundWidth;
    public int tileWidth;

    public int tileHeight;

    Class<? extends Building> buildingType;

    public String ghostPath;
    

    public boolean rotateable;

    public Direction moveDirection;
    

    public Item(GamePanel gamePanel, int numberOfItems) {
        this.gamePanel = gamePanel;

        this.stackSize = numberOfItems;
        setDefaults();
    }

    public void setDefaults() {
        maxStackSize = 100;
        containerHeight = 16;
        containerWidth = 16;
        groundHeight = gamePanel.tileSize / 2;
        groundWidth = gamePanel.tileSize / 2;
    }

    public void place(Direction direction) {

        if(placeable) {
            boolean place = true;

            int mouseTileWorldX = (int) Math.floor(gamePanel.mouseHandler.mouseWorldX/gamePanel.tileSize);
            int mouseTileWorldY = (int) Math.floor(gamePanel.mouseHandler.mouseWorldY/gamePanel.tileSize);

            for (int y = 0; y < tileHeight; y++) {
                for (int x = 0; x < tileWidth; x++) {
                    if(!gamePanel.hasObject[x + mouseTileWorldX][y + mouseTileWorldY]) {
                        continue;
                    }
                    place = false;
                }
            }
            if (place) {
                stackSize--;

                int placeX = mouseTileWorldX * gamePanel.tileSize;
                int placeY = mouseTileWorldY * gamePanel.tileSize;

                try {
                    Building newBuilding = buildingType.getConstructor(GamePanel.class, int.class, int.class, Direction.class).newInstance(gamePanel, placeX, placeY,direction);

                    gamePanel.buildings.add(newBuilding);
                    checkIfGone();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void checkAndAddToBelt() {
        if (!onFloor) return;

        
        int tileX = worldX / gamePanel.tileSize;
        int tileY = worldY / gamePanel.tileSize;

        ConveyorBelt belt = gamePanel.getConveyorBeltAt(tileX, tileY);

        if (belt != null) {
            // no dupes
            if (!belt.itemsOnBelt.contains(this)) {
                belt.itemsOnBelt.add(this);
                this.moveDirection = belt.direction; 
            }
        }
    }

    public boolean merge(Item item) {
        
        
        if (item.getClass() == this.getClass() && this.name.equals(item.name)) {
            if(stackSize == maxStackSize || item.stackSize == maxStackSize || stackSize + item.stackSize > maxStackSize) return false;
            this.stackSize += item.stackSize;
            return true;
        }
        return false;
    }

    public abstract void update();

    public boolean checkIfGone() {
        if (stackSize < 1) {
            return true;
        }
        return false;
    }

    public void updateTile() {
        if (onFloor) {
            worldGroundCol = (int) Math.floor(worldX/(gamePanel.tileSize/2));
            worldGroundRow = (int) Math.floor(worldY/(gamePanel.tileSize/2));
            
        }
        
    }

    public void uniquePlace(Direction placeDirection, Direction turnDirection) {
        System.out.println("no unique place");
    }

    public void paint (Graphics2D g2) {
        
        if (onFloor) {
            int screenX = worldX - gamePanel.player.cameraX;
            int screenY = worldY - gamePanel.player.cameraY;

            g2.drawImage(image,screenX,screenY,groundWidth,groundHeight, null);
            


        }

    }

    public boolean onFloor() {
        if (onFloor) return true;
        return false;
    }

    public abstract Item cloneItem(int amount);

    public Item splitItem(int remove) {
        int splitAmount = Math.min(remove, stackSize);
        stackSize -= splitAmount;
        return cloneItem(splitAmount);
    }

    public void putOnFloor(int x, int y) {

        for (int i = 0; i < stackSize; i++) {
            Item item = cloneItem(1);
            item.worldX = x;
            item.worldY = y;
            item.onFloor = true;
            gamePanel.addFloorItem(item);
        }
        stackSize = 0;
    }

    public BufferedImage getGhostImage() {

        BufferedImage ghost;
        try {
            ghost = ImageIO.read(getClass().getResourceAsStream(ghostPath));
            return ghost;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;

    }

    public void pickUp() {
        onFloor = false;
        gamePanel.removeItemFromAllBelts(this);
    }


    public void move(Direction direction, int magnitude) {
        switch (direction) {
            case UP -> worldY -= magnitude;
            case DOWN -> worldY += magnitude;
            case LEFT -> worldX -= magnitude;
            case RIGHT -> worldX += magnitude;
        }

        
    }

    public void rotate(int worldX, int worldY, Direction placeDirection, Direction turnDirection) {
        throw new UnsupportedOperationException("Unimplemented method 'rotate'");
    }

    

}
