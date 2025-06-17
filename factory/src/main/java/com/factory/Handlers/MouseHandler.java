package com.factory.Handlers;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.factory.GamePanel;

public class MouseHandler implements MouseListener  {

    public boolean leftClick = false;
    boolean leftDown = false;
    GamePanel gamePanel;

    int mouseScreenX, mouseScreenY;
    int mouseWorldX, mouseWorldY;

    public MouseHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        leftClick = true;
        leftClick = false;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == 1) { // left click
            leftDown = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getButton() == 1) { // left click
            leftDown = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {}

    public void updateMouse() {
        Point mousePoint = MouseInfo.getPointerInfo().getLocation();
        Point windowLocation = gamePanel.getLocationOnScreen();

        mouseScreenX = mousePoint.x - windowLocation.x;
        mouseScreenY = mousePoint.y - windowLocation.y;

        mouseWorldX = mouseScreenX + gamePanel.player.cameraX;
        mouseWorldY = mouseScreenY + gamePanel.player.cameraY;

    }
    
}
