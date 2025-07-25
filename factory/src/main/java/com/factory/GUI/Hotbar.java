package com.factory.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.factory.GamePanel;
import com.factory.Player;
import com.factory.items.Item;

public class Hotbar extends GUI {


    public Player player;

    int width;
    
    
    int grabCooldown = 30;
    int height;

    BufferedImage middleImage, bottomImage;
    String[] hotbarSlots;

    public Hotbar(GamePanel gamePanel, int height, Player player) {
        super(gamePanel);
        
        this.player = player;
        this.width = 8;
        this.height = height;
        open = true;

        hotbarSlots = new String[width * height];
        pixelWidth = 138 * gamePanel.scale;
        screenX = gamePanel.screenWidth / 2 - pixelWidth / 2;
        screenY = gamePanel.screenHeight - (1 * 17 * gamePanel.scale) - (1 * 18 * gamePanel.scale);

        try {
            middleImage = ImageIO.read(getClass().getResourceAsStream("/GUI/containerMiddle.png"));
            bottomImage = ImageIO.read(getClass().getResourceAsStream("/GUI/containerBottom.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        pixelHeight = (middleImage.getHeight() * (height - 1) + bottomImage.getHeight()) * gamePanel.scale;
    }

    @Override
    public boolean mouseIsOver() {
        boolean mouseOver;
        int mouseX = gamePanel.mouseHandler.mouseScreenX;
        int mouseY = gamePanel.mouseHandler.mouseScreenY;

        mouseOver = mouseX >= screenX && mouseX <= screenX + pixelWidth && mouseY >= screenY && mouseY <= screenY + pixelHeight;
        return mouseOver;
    }

    public void draw(Graphics2D g2) {
        // bottom
        g2.drawImage(bottomImage, screenX, screenY + 17 * (height - 1) * gamePanel.scale ,bottomImage.getWidth() * gamePanel.scale ,bottomImage.getHeight() * gamePanel.scale, null);

        // middle
        for (int i = 0; i < height - 1; i++) {
            g2.drawImage(middleImage, screenX, screenY + i * middleImage.getHeight() * gamePanel.scale,middleImage.getWidth() * gamePanel.scale ,middleImage.getHeight() * gamePanel.scale, null);
        }

        for (int x = 0; x < width; x++) {
            String itemName = hotbarSlots[x];
            if (itemName == null) continue;

            Item item = player.inventory.pickUpStack(itemName);
            if (item == null) continue;

            //item
            g2.drawImage(item.image,screenX + gamePanel.scale + (17 * x) * gamePanel.scale,screenY + gamePanel.scale ,item.containerWidth * gamePanel.scale,item.containerHeight * gamePanel.scale,null);

            //stack size
            g2.setFont(new Font("TIMES NEW ROMAN", Font.BOLD, 10 * gamePanel.scale));
            g2.setColor(Color.WHITE);
            g2.drawString(String.valueOf(item.stackSize),screenX + gamePanel.scale + (17 * x) * gamePanel.scale,screenY + item.containerHeight * gamePanel.scale - 4);
        }

        for (int x = 0; x < width; x++) {
            String itemName = hotbarSlots[x+width];
            if (itemName == null) continue;

            Item item = player.inventory.pickUpStack(itemName);
            if (item == null) continue;

            //item
            g2.drawImage(item.image,screenX + gamePanel.scale + (17 * x) * gamePanel.scale,screenY + 17 * gamePanel.scale ,item.containerWidth * gamePanel.scale,item.containerHeight * gamePanel.scale,null);

            //stack size
            g2.setFont(new Font("TIMES NEW ROMAN", Font.BOLD, 10 * gamePanel.scale));
            g2.setColor(Color.WHITE);
            g2.drawString(String.valueOf(item.stackSize),screenX + gamePanel.scale + (17 * x) * gamePanel.scale,screenY + 17 * gamePanel.scale + item.containerHeight * gamePanel.scale - 4);
        }

        /* 
        for (int x = 0; x < width; x++) {
            g2.setFont(new Font("TIMES NEW ROMAN", Font.BOLD, 10 * gamePanel.scale));
            g2.setColor(Color.WHITE);
            g2.drawString(Integer.toString(x),screenX + gamePanel.scale + (17 * x) * gamePanel.scale,screenY + 17 * gamePanel.scale - 4);
        }

        for (int x = 0; x < width; x++) {

            g2.setFont(new Font("TIMES NEW ROMAN", Font.BOLD, 10 * gamePanel.scale));
            g2.setColor(Color.WHITE);
            g2.drawString(Integer.toString(x + width),screenX + gamePanel.scale + (17 * x) * gamePanel.scale,screenY + 17 * gamePanel.scale + 17 * gamePanel.scale - 4);
        }
        */
    }

    public void update() {
        
        if (grabCooldown > 0) grabCooldown--;

        if (gamePanel.mouseHandler.touchingMouse(screenX, screenY, pixelWidth, 17 * gamePanel.scale * height) && gamePanel.mouseHandler.leftClick && grabCooldown == 0 && !gamePanel.mouseHandler.leftClickUsed) {

            int col = findCol();
            int row = findRow();
            int slot = col + row * width;;

            if (gamePanel.mouseHandler.itemInHand && gamePanel.mouseHandler.inHand != null) {
                // assign hotbar slot
                setHotbarSlot(slot, gamePanel.mouseHandler.inHand.name);
            } else {
                // pick up an item from inventory
                Item item = getItemFromInventory(slot);
                if (item != null) {
                    gamePanel.mouseHandler.itemInHand = true;
                    gamePanel.mouseHandler.inHand = item;
                    player.inventory.remove(item);
                }
            }

            grabCooldown = 30;
            gamePanel.mouseHandler.useLeft();
        }
    }

    public void setHotbarSlot(int slot, String itemName) {
        
        if (slot >= 0 && slot < hotbarSlots.length) {
            
            hotbarSlots[slot] = itemName;
        }
    }

    public String getHotbarSlotName(int slot) {
        if (slot >= 0 && slot < hotbarSlots.length) {
            return hotbarSlots[slot];
        }
        return null;
    }

    public Item getItemFromInventory(int slot) {
        String name = getHotbarSlotName(slot);
        if (name == null) return null;
        return player.inventory.pickUpStack(name);
    }

    private int findCol() {
        int mouseX = gamePanel.mouseHandler.mouseScreenX;
        double col = (mouseX - screenX) / 17.0 / gamePanel.scale;
        return (int) Math.floor(col);
    }
    private int findRow() {
        int mouseY = gamePanel.mouseHandler.mouseScreenY;
        double row = (mouseY - screenY) / 17.0 / gamePanel.scale;
        return (int) Math.floor(row);
    }
}
