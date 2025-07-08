package com.factory.Handlers;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.factory.GamePanel;
import com.factory.Hotbar;
import com.factory.ItemContainer;
import com.factory.items.Item;

public class MouseHandler implements MouseListener {

    public boolean leftDown = false;
    public boolean leftClick = false;
    public boolean rightDown = false;
    public boolean rightClick = false;
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
        if (e.getButton() == MouseEvent.BUTTON3) {
            rightDown = true;
            
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftDown = false;
            leftClick = true;
            dragging = false;
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            rightDown = false;
            rightClick = true;
            
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
        if (rightClick) {
            rightClick = false;
        }
    }

    public void draw(Graphics2D g2) {
        if (itemInHand && inHand != null) {
            g2.drawImage(inHand.image, mouseScreenX, mouseScreenY,
                    inHand.containerWidth * gamePanel.scale, inHand.containerHeight * gamePanel.scale, null);

            Font font = new Font("TIMES NEW ROMAN", Font.BOLD, 10 * gamePanel.scale);
            g2.setFont(font);
            g2.setColor(Color.WHITE);
            g2.drawString(String.valueOf(inHand.stackSize),
                    mouseScreenX, mouseScreenY + inHand.containerHeight * gamePanel.scale);
        }
    }

    public boolean touchingMouse(int screenX, int screenY, int width, int height) {
        return mouseScreenX >= screenX && mouseScreenX < screenX + width
                && mouseScreenY >= screenY && mouseScreenY < screenY + height;
    }

    public void pickUpItem(Item item, ItemContainer container) {
        if (item == null) return;

        itemInHand = true;
        inHand = item;
        container.remove(item);
    }

    public void dropItem(ItemContainer container) {
        if (inHand == null) return;

        container.add(inHand);
        itemInHand = false;
        inHand = null;
    }

    public void pickUpItem(Hotbar hotbar, int slot) {
        String itemName = hotbar.getHotbarSlotName(slot);
        if (itemName == null) return;

        Item item = gamePanel.player.inventory.pickUpStack(itemName);
        if (item == null) return;

        itemInHand = true;
        inHand = item;
    }

    public void dropItem(Hotbar hotbar) {
        
        if (inHand == null) return;

        gamePanel.player.inventory.add(inHand);
        itemInHand = false;
        inHand = null;
    }

    
}
