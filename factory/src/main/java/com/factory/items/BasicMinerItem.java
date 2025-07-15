package com.factory.items;

import java.io.IOException;
import javax.imageio.ImageIO;
import com.factory.GamePanel;

public class BasicMinerItem extends Item {

    public BasicMinerItem(GamePanel gamePanel, int numberOfItems) {
        super(gamePanel, numberOfItems);
        
        setIndividualDefaults();
    }

    private void setIndividualDefaults() {
        placeable = true;
        name = "Basic Miner";
        tileHeight = 2;
        tileWidth = 2;
        maxStackSize = 100;
        buildingType = com.factory.buildings.BasicMiner.class;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/items/basicMinerItem.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public void update() {
    
    }
    
    @Override
    public Item cloneItem(int amount) {
        return new BasicMinerItem(gamePanel, amount);
    }

}
