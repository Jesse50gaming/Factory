package com.factory.GUI;
import java.awt.Graphics2D;

import com.factory.GamePanel;


public class GUI {

    GamePanel gamePanel;
    public boolean open;

    public GUI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        gamePanel.GUIHandler.add(this);
    }

    public void update(){

    }

    public void draw(Graphics2D g2) {

    }

    public void close() {
        open = false;
    }

}
