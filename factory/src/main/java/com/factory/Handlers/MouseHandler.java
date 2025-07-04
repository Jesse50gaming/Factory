package com.factory.Handlers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.factory.GamePanel;
import com.factory.Hotbar;
import com.factory.ItemContainer;
import com.factory.items.Item;

public class MouseHandler implements MouseListener {

    public boolean leftDown = false;
    public boolean leftClick = false; // Set to true for one frame after a click
    GamePanel gamePanel;

    public int mouseScreenX, mouseScreenY;
    public int mouseWorldX, mouseWorldY;
    public boolean dragging = false;

    public boolean itemInHand = false;
    public Item inHand;

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

    public void draw(Graphics2D g2) {
        if(itemInHand) {
            g2.drawImage(inHand.image, mouseScreenX, mouseScreenY, inHand.containerWidth * gamePanel.scale, inHand.containerHeight * gamePanel.scale, null);

            Font font = new Font("TIMES NEW ROMAN", 1, 10 * gamePanel.scale);
            g2.setFont(font);
            g2.setColor(Color.WHITE);
            g2.drawString(String.valueOf(inHand.stackSize),mouseScreenX, mouseScreenY + gamePanel.scale + 17 * gamePanel.scale);
        }
        
    }

    public boolean touchingMouse(int screenX, int screenY, int width, int height) {
        return mouseScreenX >= screenX && mouseScreenX < screenX + width && mouseScreenY >= screenY && mouseScreenY < screenY + height;
    }

    public void pickUpItem(Item item, ItemContainer container) {
        itemInHand = true;
        inHand = item;
        container.remove(item);
    }

    public void dropItem(ItemContainer container) {
        itemInHand = false;
        container.add(inHand);
        inHand = null;
    }

    public void pickUpItem(Item item, Hotbar hotbar) {
        itemInHand = true;
        pickUpItem(hotbar.player.inventory.pickUpStack(item.name),hotbar.player.inventory); 
        
        
    }

    public void dropItem(Hotbar hotbar) {
        if (hotbar.nullCheck()) {
            hotbar.add(inHand);
            return;
        }
        Item item = hotbar.findItem();
        hotbar.add(inHand);
        hotbar.player.inventory.add(item);
        
    }
}

