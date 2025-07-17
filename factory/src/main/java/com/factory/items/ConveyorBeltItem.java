package com.factory.items;

import java.io.IOException;

import javax.imageio.ImageIO;

import com.factory.GamePanel;

public class ConveyorBeltItem extends Item {

    public ConveyorBeltItem(GamePanel gamePanel, int numberOfItems) {
        super(gamePanel, numberOfItems);
        
        setIndividualDefaults();
    }

    private void setIndividualDefaults() {
        placeable = true;
        rotateable = true;
        name = "Conveyor Belt";
        tileHeight = 1;
        tileWidth = 1;
        maxStackSize = 100;
        buildingType = com.factory.buildings.ConveyorBelt.class;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/items/conveyorBelt.png"));
            ghostPath = "/ghostBuildings/conveyorBelt/conveyorBelt.png";
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public Item cloneItem(int amount) {
        return new ConveyorBeltItem(gamePanel, amount);
    }

    @Override
    public void update() {
        
    }

}
