package com.factory.buildings;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.factory.GamePanel;
import com.factory.items.Item;
import com.factory.util.Direction;

public class ConveyorBelt extends Building {

    public Direction direction;
    int movingSpeed = 1; // how many pixels an item moves per frame
    BufferedImage image2;
    public List<Item> itemsOnBelt;  // Make sure this is initialized somewhere!

    public ConveyorBelt(GamePanel gamePanel, int worldX, int worldY, Direction direction) {
        super(gamePanel, worldX, worldY, direction);
        this.direction = direction;

        itemsOnBelt = new ArrayList<>();  // initialize list to avoid NullPointerException
        setIndividualDefaults();
    }

    @Override
    public void update() {
        screenX = worldX - gamePanel.player.cameraX;
        screenY = worldY - gamePanel.player.cameraY;
        moveItems();
        checkForDestruction();
    }

    @Override
    public void paint(Graphics2D g2) {
        if (image == null || image2 == null) return;

        screenX = worldX - gamePanel.player.cameraX;
        screenY = worldY - gamePanel.player.cameraY;

        BufferedImage imgToDraw = null;

        if (gamePanel.animationCount < 15) {
            imgToDraw = image;
        } else if (gamePanel.animationCount < 30) {
            imgToDraw = image2;
        } else if (gamePanel.animationCount < 45) {
            imgToDraw = image;
        } else {
            imgToDraw = image2;
        }

        g2.rotate(Math.toRadians(direction.rotateAngle()), screenX + width / 2, screenY + height / 2);
        g2.drawImage(imgToDraw, screenX, screenY, width, height, null);
        g2.rotate(-Math.toRadians(direction.rotateAngle()), screenX + width / 2, screenY + height / 2);
    }

    private void setIndividualDefaults() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/buildings/conveyorBelt/conveyorBelt_1.png"));
            image2 = ImageIO.read(getClass().getResourceAsStream("/buildings/conveyorBelt/conveyorBelt_2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        width = image.getWidth() * gamePanel.scale;
        height = image.getHeight() * gamePanel.scale;
        item = com.factory.items.ConveyorBeltItem.class;
        markTiles();
    }

    public void moveItems() {
        if (itemsOnBelt == null) return;

        // Make a copy to avoid concurrent modification while removing items
        for (Item item : new ArrayList<>(itemsOnBelt)) {
            // Move item based on conveyor direction
            switch (direction) {
                case LEFT -> item.worldX -= movingSpeed;
                case RIGHT -> item.worldX += movingSpeed;
                case UP -> item.worldY -= movingSpeed;
                case DOWN -> item.worldY += movingSpeed;
            }

            // Check if item reached the end of the belt
            boolean reachedEnd = switch (direction) {
                case LEFT -> item.worldX <= worldX;
                case RIGHT -> item.worldX + item.groundWidth >= worldX + width;
                case UP -> item.worldY <= worldY;
                case DOWN -> item.worldY + item.groundHeight >= worldY + height;
            };

            if (reachedEnd) {
                ConveyorBelt nextBelt = gamePanel.getConveyorBeltAtNextPosition(worldX, worldY, direction);
                if (nextBelt != null) {
                    // Remove item from current belt
                    itemsOnBelt.remove(item);

                    // Place item at start of next belt depending on direction
                    switch (direction) {
                        case LEFT -> item.worldX = nextBelt.worldX + nextBelt.width - item.groundWidth;
                        case RIGHT -> item.worldX = nextBelt.worldX;
                        case UP -> item.worldY = nextBelt.worldY + nextBelt.height - item.groundHeight;
                        case DOWN -> item.worldY = nextBelt.worldY;
                    }

                    // Add item to next belt
                    nextBelt.itemsOnBelt.add(item);
                } else {
                    // clamp if no other belt
                    clampItemToBelt(item);
                }
            } else {
                //clamp otherwise
                clampItemToBelt(item);
            }
        }
    }

    private void clampItemToBelt(Item item) {
        if (item.worldX < worldX) item.worldX = worldX;
        if (item.worldX + item.groundWidth > worldX + width) item.worldX = worldX + width - item.groundWidth;
        if (item.worldY < worldY) item.worldY = worldY;
        if (item.worldY + item.groundHeight > worldY + height) item.worldY = worldY + height - item.groundHeight;
    }

    public void addItem(Item item) {
        if (!itemsOnBelt.contains(item)) {
            itemsOnBelt.add(item);
        }
    }
}
