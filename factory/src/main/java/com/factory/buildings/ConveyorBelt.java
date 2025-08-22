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
    public List<Item> itemsOnBelt; 
    public Direction turnDirection; 

    public ConveyorBelt(GamePanel gamePanel, int worldX, int worldY, Direction direction,Direction turnDirection) {
        super(gamePanel, worldX, worldY, direction);
        this.direction = direction;
        this.turnDirection = turnDirection;

        itemsOnBelt = new ArrayList<>();  
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
            if (turnDirection == Direction.LEFT) {
                image = ImageIO.read(getClass().getResourceAsStream("/buildings/conveyorBelt/conveyorBeltLeftTurn_1.png"));
                image2 = ImageIO.read(getClass().getResourceAsStream("/buildings/conveyorBelt/conveyorBeltLeftTurn_2.png"));
            }
            else if(turnDirection == Direction.RIGHT) {
                image = ImageIO.read(getClass().getResourceAsStream("/buildings/conveyorBelt/conveyorBeltRightTurn_1.png"));
                image2 = ImageIO.read(getClass().getResourceAsStream("/buildings/conveyorBelt/conveyorBeltRightTurn_2.png"));
            } else {
                image = ImageIO.read(getClass().getResourceAsStream("/buildings/conveyorBelt/conveyorBelt_1.png"));
                image2 = ImageIO.read(getClass().getResourceAsStream("/buildings/conveyorBelt/conveyorBelt_2.png"));
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        width = image.getWidth() * gamePanel.scale;
        height = image.getHeight() * gamePanel.scale;
        item = com.factory.items.ConveyorBeltItem.class;
        markTiles();
    }

    public void updateTexture() {
        try {
            if (turnDirection == Direction.LEFT) {
                image = ImageIO.read(getClass().getResourceAsStream("/buildings/conveyorBelt/conveyorBeltLeftTurn_1.png"));
                image2 = ImageIO.read(getClass().getResourceAsStream("/buildings/conveyorBelt/conveyorBeltLeftTurn_2.png"));
            }
            else if(turnDirection == Direction.RIGHT) {
                image = ImageIO.read(getClass().getResourceAsStream("/buildings/conveyorBelt/conveyorBeltRightTurn_1.png"));
                image2 = ImageIO.read(getClass().getResourceAsStream("/buildings/conveyorBelt/conveyorBeltRightTurn_2.png"));
            } else {
                image = ImageIO.read(getClass().getResourceAsStream("/buildings/conveyorBelt/conveyorBelt_1.png"));
                image2 = ImageIO.read(getClass().getResourceAsStream("/buildings/conveyorBelt/conveyorBelt_2.png"));
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void moveItems() {
        if (itemsOnBelt == null) return;

        for (Item item : new ArrayList<>(itemsOnBelt)) {

            if (turnDirection == Direction.DOWN) {
                // straight belt
                moveInDirection(item, direction);

                if (hasReachedEnd(item, direction)) {
                    handleBeltTransfer(item, direction);
                } else {
                    clampItemToBelt(item, direction);
                }

            } else if (turnDirection == Direction.RIGHT || turnDirection == Direction.LEFT) {
                // curved belt

                boolean insideTrack;
                //works
                if (turnDirection == Direction.LEFT) {
                    insideTrack = switch (direction) {
                        case LEFT  -> item.worldY >= worldY + height / 2;
                        case RIGHT -> item.worldY <= worldY + height / 2;
                        case UP    -> item.worldX <= worldX + width / 2;
                        case DOWN  -> item.worldX >= worldX + width / 2;
                    };
                } else {
                    insideTrack = switch (direction) {
                        case LEFT  -> item.worldY <= worldY + height / 2;
                        case RIGHT -> item.worldY >= worldY + height / 2;
                        case UP    -> item.worldX >= worldX + width / 2;
                        case DOWN  -> item.worldX <= worldX + width / 2;
                    };
                }
                
                
                boolean inFirstHalf = isInFirstHalf(item, direction,insideTrack);
                
                Direction turnDir = (turnDirection == Direction.RIGHT) ? direction.rotate() : direction.counterRotate();

                if (inFirstHalf) {
                    moveInDirection(item, direction);
                } else {
                    
                    moveInDirection(item, turnDir);
                }

                if (hasReachedEnd(item, turnDir)) {
                    handleBeltTransfer(item, turnDir);
                } else {
                    if (inFirstHalf) clampItemToBelt(item,direction);
                    if (!inFirstHalf) clampItemToBelt(item, turnDir);
                    
                }
            }
        }
    }

    private void clampItemToBelt(Item item, Direction direction) {
        ConveyorBelt nextBelt = gamePanel.getConveyorBeltAtNextPosition(worldX, worldY, direction);

        if (nextBelt != null) {
            switch (direction) {
                case UP:
                    if (item.worldX < worldX) {
                        item.worldX = worldX;
                    } 
                    if (item.worldX + item.groundWidth > worldX + width) {
                        item.worldX = worldX + width - item.groundWidth;
                    }
                break;
                case DOWN:
                    if (item.worldX < worldX) {
                        item.worldX = worldX;
                    } 
                    if (item.worldX + item.groundWidth > worldX + width) {
                        item.worldX = worldX + width - item.groundWidth;
                    }
                break;
                case RIGHT:
                    if (item.worldY < worldY) {
                        item.worldY = worldY;
                    }
                    if (item.worldY + item.groundHeight > worldY + height) {
                        item.worldY = worldY + height - item.groundHeight;
                    }
                break;
                case LEFT:
                    if (item.worldY < worldY) {
                        item.worldY = worldY;
                    }
                    if (item.worldY + item.groundHeight > worldY + height) {
                        item.worldY = worldY + height - item.groundHeight;
                    }
                break;
            }
        } else{
            if (item.worldX < worldX) {
                item.worldX = worldX;
            } 
            if (item.worldX + item.groundWidth > worldX + width) {
                item.worldX = worldX + width - item.groundWidth;
            }
            if (item.worldY < worldY) {
                item.worldY = worldY;
            }
            if (item.worldY + item.groundHeight > worldY + height) {
                item.worldY = worldY + height - item.groundHeight;
            }
        }
        
    }

    public void addItem(Item item) {
        if (!itemsOnBelt.contains(item)) {
            itemsOnBelt.add(item);
        }
    }
    private void moveInDirection(Item item, Direction dir) {
        switch (dir) {
            case LEFT  -> item.worldX -= movingSpeed;
            case RIGHT -> item.worldX += movingSpeed;
            case UP    -> item.worldY -= movingSpeed;
            case DOWN  -> item.worldY += movingSpeed;
        }
    }

    private boolean isInFirstHalf(Item item, Direction dir,boolean insideTrack) {

        boolean inFirstHalf;

        if (insideTrack) {
            inFirstHalf = switch (dir) {
                case LEFT  -> item.worldX >= worldX + width / 2;
                case RIGHT -> item.worldX + item.groundWidth <= worldX + width / 2;
                case UP -> item.worldY >= worldY + height / 2;
                case DOWN  -> item.worldY + item.groundHeight <= worldY + height / 2;
            };
        } else {
            inFirstHalf = switch (dir) {
                case LEFT  -> item.worldX >= worldX;
                case RIGHT -> item.worldX + item.groundWidth <= worldX + width;
                case UP -> item.worldY >= worldY;
                case DOWN  -> item.worldY + item.groundHeight <= worldY + height;
            };
        }

        return inFirstHalf;
    }

    private boolean hasReachedEnd(Item item, Direction dir) {
        return switch (dir) {
            case LEFT  -> item.worldX + item.groundWidth <= worldX;
            case RIGHT -> item.worldX >= worldX + width;
            case UP    -> item.worldY + item.groundHeight <= worldY;  
            case DOWN  -> item.worldY >= worldY + height;
        };
    }

    private void handleBeltTransfer(Item item, Direction exitDir) {
        int tileAlignedX = (worldX / gamePanel.tileSize) * gamePanel.tileSize;
        int tileAlignedY = (worldY / gamePanel.tileSize) * gamePanel.tileSize;

        ConveyorBelt nextBelt = gamePanel.getConveyorBeltAtNextPosition(tileAlignedX, tileAlignedY, exitDir);

        if (nextBelt != null) {
            gamePanel.removeItemFromAllBelts(item);
            snapToStartOfNextBelt(item, nextBelt, exitDir);
            nextBelt.itemsOnBelt.add(item);
        } else {
            clampItemToBelt(item, exitDir);
        }
    }

    private void snapToStartOfNextBelt(Item item, ConveyorBelt nextBelt, Direction dir) {
        switch (dir) {
            case LEFT  -> item.worldX = nextBelt.worldX + nextBelt.width - item.groundWidth;
            case RIGHT -> item.worldX = nextBelt.worldX;
            case UP -> item.worldY = nextBelt.worldY + nextBelt.height - item.groundHeight;
            case DOWN  -> item.worldY = nextBelt.worldY;
        }
    }
}
