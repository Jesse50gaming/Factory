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

        if (turnDirection == Direction.DOWN) {
            // make copy to avoid error
            for (Item item : new ArrayList<>(itemsOnBelt)) {
                // move based on direciton
                switch (direction) {
                    case LEFT -> item.worldX -= movingSpeed;
                    case RIGHT -> item.worldX += movingSpeed;
                    case UP -> item.worldY -= movingSpeed;
                    case DOWN -> item.worldY += movingSpeed;
                }

                
                boolean reachedEnd = switch (direction) {
                    case LEFT -> item.worldX + item.groundWidth <= worldX;
                    case RIGHT -> item.worldX >= worldX + width;
                    case UP -> item.worldY + item.groundHeight <= worldY;
                    case DOWN -> item.worldY >= worldY + height;
                };
                
                if (reachedEnd) {
                    ConveyorBelt nextBelt = gamePanel.getConveyorBeltAtNextPosition(worldX, worldY, direction);
                    if (nextBelt != null) {
                        
                        gamePanel.removeItemFromAllBelts(item);

                        //put at start of next belt
                        
                        switch (direction) {
                            case LEFT -> item.worldX = nextBelt.worldX + nextBelt.width - item.groundWidth;
                            case RIGHT -> item.worldX = nextBelt.worldX;
                            case UP -> item.worldY = nextBelt.worldY + nextBelt.height - item.groundHeight;
                            case DOWN -> item.worldY = nextBelt.worldY;
                        }
                        
                        
                        nextBelt.itemsOnBelt.add(item);
                    } else {
                        
                        clampItemToBelt(item);
                    }
                } else {
                    clampItemToBelt(item);
                }
            }
        } else if (turnDirection == Direction.RIGHT) {
            
            for (Item item : new ArrayList<>(itemsOnBelt)) {

                //movement
                boolean insideTrack = switch (direction) {
                    case LEFT -> item.worldY <= worldY + height / 2;
                    case RIGHT -> item.worldY <= worldY + height / 2;
                    case UP -> item.worldX <= worldX + width / 2;
                    case DOWN -> item.worldX <= worldX + width / 2;
                };

                boolean reachedHalf;
                if (insideTrack) {
                    reachedHalf = switch (direction) {
                        case LEFT -> item.worldX == worldX + width / 2;
                        case RIGHT -> item.worldX == worldX;
                        case UP -> item.worldY == worldY + height / 2;
                        case DOWN -> item.worldY == worldY;
                    };
                } else {
                    reachedHalf = switch (direction) {
                        case LEFT -> item.worldX == worldX;
                        case RIGHT -> item.worldX == worldX - width / 2;
                        case UP -> item.worldY == worldY;
                        case DOWN -> item.worldY == worldY - height /2;
                    };
                }

                if (reachedHalf) {
                    switch (direction) {
                        case LEFT -> item.worldY -= movingSpeed;
                        case RIGHT -> item.worldY += movingSpeed;
                        case UP -> item.worldX += movingSpeed;
                        case DOWN -> item.worldX -= movingSpeed;
                    }

                } else {
                    switch (direction) {
                        case LEFT -> item.worldX -= movingSpeed;
                        case RIGHT -> item.worldX += movingSpeed;
                        case UP -> item.worldY -= movingSpeed;
                        case DOWN -> item.worldY += movingSpeed;
                    }
                }
                
                

                
                boolean reachedEnd = switch (direction) {
                    case LEFT -> item.worldY + item.groundHeight <= worldY;
                    case RIGHT -> item.worldY >= worldY + height;
                    case UP -> item.worldX >= worldX + width;
                    case DOWN -> item.worldX + item.groundWidth <= worldX;
                };
                
                if (reachedEnd) {
                    ConveyorBelt nextBelt = gamePanel.getConveyorBeltAtNextPosition(worldX, worldY, direction.rotate());
                    if (nextBelt != null) {
                        
                        gamePanel.removeItemFromAllBelts(item);

                        //put at start of next belt
                        
                        switch (direction) {
                            case LEFT -> item.worldY = nextBelt.worldY + nextBelt.height - item.groundHeight;
                            case RIGHT -> item.worldY = nextBelt.worldY;
                            case UP -> item.worldX = nextBelt.worldX;
                            case DOWN -> item.worldY = nextBelt.worldY;
                        }
                        
                        
                        nextBelt.itemsOnBelt.add(item);
                    } else {
                        
                        clampItemToBelt(item);
                    }
                } else {
                    clampItemToBelt(item);
                }
            }
        } else if (turnDirection == Direction.LEFT) {
            
        }
    }

    private void clampItemToBelt(Item item) {
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
}
