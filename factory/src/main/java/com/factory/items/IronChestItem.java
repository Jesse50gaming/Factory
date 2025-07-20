package com.factory.items;

import java.io.IOException;

import javax.imageio.ImageIO;

import com.factory.GamePanel;

public class IronChestItem extends Item {

    public IronChestItem(GamePanel gamePanel, int numberOfItems) {
        super(gamePanel, numberOfItems);
        setIndividualDefaults();
    }

    private void setIndividualDefaults() {
        placeable = true;
        rotateable = false;
        name = "Iron Chest";
        tileHeight = 1;
        tileWidth = 1;
        
        buildingType = com.factory.buildings.IronChest.class;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/items/ironChest.png"));
            ghostPath = "/ghostBuildings/ironChest/ironChest.png";
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update() {
        
        updateTile();
        checkAndAddToBelt();
    }

    @Override
    public Item cloneItem(int amount) {
        return new IronChestItem(gamePanel, amount);
    }



}
