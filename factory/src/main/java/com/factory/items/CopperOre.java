package com.factory.items;

import java.io.IOException;

import javax.imageio.ImageIO;

import com.factory.GamePanel;

public class CopperOre extends Item {

    public CopperOre(GamePanel gamePanel, int x, int y, int numberOfItems) {
        super(gamePanel, x, y, numberOfItems);
        setDefaults();
        setIndividualDefaults();
    }

    public void setIndividualDefaults() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/items/copperOre.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        name = "Copper Ore";

        maxStackSize = 100;
        
    }



}
