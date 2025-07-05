package com.factory;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class IronChest {

    BufferedImage image;
    int x, y;
    ItemContainer container;
    GamePanel gamePanel;

    public IronChest(GamePanel gamePanel, int x, int y) {
        this.gamePanel = gamePanel;
        this.x = x;
        this.y = y;
        setIndividualDefaults();
    }

    public void setIndividualDefaults() {
        container = new ItemContainer(gamePanel, x);
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/buildings/ironChest.png"));
        } catch (IOException e) {
           
            e.printStackTrace();
        }
    }

}
