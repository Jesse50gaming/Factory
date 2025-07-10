package com.factory.items;

import java.io.IOException;

import javax.imageio.ImageIO;

import com.factory.GamePanel;

public class IronOre extends Item {

    public IronOre(GamePanel gamePanel, int x, int y, int numberOfItems) {
        super(gamePanel, x, y, numberOfItems);
        
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
        
    }

    




}
