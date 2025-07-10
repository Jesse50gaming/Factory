package com.factory.items;

import java.io.IOException;

import javax.imageio.ImageIO;

import com.factory.GamePanel;

public class IronChestItem extends Item {

    public IronChestItem(GamePanel gamePanel,int x, int y, int numberOfItems) {
        super(gamePanel, x, y, numberOfItems);
        setIndividualDefaults();
    }

    private void setIndividualDefaults() {
        placeable = true;
        name = "Iron Chest";
        tileHeight = 1;
        tileWidth = 1;
        
        buildingType = com.factory.buildings.IronChest.class;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/items/ironChest.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
