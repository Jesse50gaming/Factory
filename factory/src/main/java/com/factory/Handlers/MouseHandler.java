package com.factory.Handlers;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.factory.GamePanel;

public class MouseHandler implements MouseListener {

    public boolean leftDown = false;
    public boolean leftClick = false; // Set to true for one frame after a click
    GamePanel gamePanel;

    public int mouseScreenX, mouseScreenY;
    public int mouseWorldX, mouseWorldY;
    public boolean dragging = false;

    public MouseHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftDown = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftDown = false;
            leftClick = true;
            dragging = false; 
        }
    }

    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    public void updateMouse() {
        if (gamePanel.isShowing()) {
            Point mousePoint = MouseInfo.getPointerInfo().getLocation();
            Point windowLocation = gamePanel.getLocationOnScreen();

            mouseScreenX = mousePoint.x - windowLocation.x;
            mouseScreenY = mousePoint.y - windowLocation.y;

            mouseWorldX = mouseScreenX + gamePanel.player.cameraX;
            mouseWorldY = mouseScreenY + gamePanel.player.cameraY;
        }

        
        if (leftClick) {
            leftClick = false;
        }
    }

    public boolean touchingMouse(int screenX, int screenY, int width, int height) {
        return mouseScreenX >= screenX && mouseScreenX < screenX + width &&
               mouseScreenY >= screenY && mouseScreenY < screenY + height;
    }
}

