package com.factory.GUI;
import java.awt.Graphics2D;

import com.factory.GamePanel;


public class GUI {

    GamePanel gamePanel;
    public boolean open;
    int screenX, screenY;
    int pixelWidth, pixelHeight;

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


    public boolean mouseIsOver() {
        if (open) {
            boolean mouseOver;
            int mouseX = gamePanel.mouseHandler.mouseScreenX;
            int mouseY = gamePanel.mouseHandler.mouseScreenY;

            mouseOver = mouseX >= screenX && mouseX <= screenX + pixelWidth && mouseY >= screenY && mouseY <= screenY + pixelHeight;

            
            return mouseOver;
        }
        return false;
    }

}
