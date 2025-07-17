package com.factory.items;

import java.io.IOException;

import javax.imageio.ImageIO;

import com.factory.GamePanel;

public class IronOre extends Item {

    public IronOre(GamePanel gamePanel, int numberOfItems) {
        super(gamePanel, numberOfItems);
        
        setIndividualDefaults();
    }

    public void setIndividualDefaults() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/items/ironOre.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        name = "Iron Ore";

        maxStackSize = 100;
        placeable = false;
        rotateable = false;
        
    }

    @Override
    public void update() {
        updateTile();
    }


    @Override
    public Item cloneItem(int amount) {
        return new IronOre(gamePanel, amount);
    }
    




}
