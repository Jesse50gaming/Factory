package com.factory.items;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import com.factory.GamePanel;
import com.factory.buildings.Building;
import com.factory.buildings.ConveyorBelt;
import com.factory.util.Direction;

public class ConveyorBeltItem extends Item {

    public ConveyorBeltItem(GamePanel gamePanel, int numberOfItems) {
        super(gamePanel, numberOfItems);
        
        setIndividualDefaults();
    }

    private void setIndividualDefaults() {
        placeable = true;
        rotateable = true;
        name = "Conveyor Belt";
        tileHeight = 1;
        tileWidth = 1;
        maxStackSize = 100;
        buildingType = com.factory.buildings.ConveyorBelt.class;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/items/conveyorBelt.png"));
            ghostPath = "/ghostBuildings/conveyorBelt/conveyorBelt.png";
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public Item cloneItem(int amount) {
        return new ConveyorBeltItem(gamePanel, amount);
    }

    @Override
    public void update() {
        updateTile();
        checkAndAddToBelt();
    }


    @Override
    public Building uniquePlace(Direction placeDirection, Direction turnDirection) {
        

        if(placeable) {
            boolean place = true;

            int mouseTileWorldX = (int) Math.floor(gamePanel.mouseHandler.mouseWorldX/gamePanel.tileSize);
            int mouseTileWorldY = (int) Math.floor(gamePanel.mouseHandler.mouseWorldY/gamePanel.tileSize);

            for (int y = 0; y < tileHeight; y++) {
                for (int x = 0; x < tileWidth; x++) {
                    if(!gamePanel.hasObject[x + mouseTileWorldX][y + mouseTileWorldY]) {
                        continue;
                    }
                    place = false;
                }
            }
            if (place) {
                
                stackSize--;

                int placeX = mouseTileWorldX * gamePanel.tileSize;
                int placeY = mouseTileWorldY * gamePanel.tileSize;

                try {
                    
                    ConveyorBelt newBuilding = new ConveyorBelt(gamePanel, placeX, placeY, placeDirection,turnDirection);
                    
                    gamePanel.buildings.add(newBuilding);
                    checkIfGone();

                    
                    return newBuilding;
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            }
            

        }
        
        return null;
    
    }

    @Override
    public void rotate(int worldX, int worldY, Direction placeDirection, Direction turnDirection) {

       
        Direction oppositeCheckDir = placeDirection.opposite();

        if (gamePanel.getConveyorBeltAtNextPosition(worldX * gamePanel.tileSize, worldY * gamePanel.tileSize, oppositeCheckDir) != null) {

            if (turnDirection == Direction.DOWN) {
                turnDirection = Direction.LEFT;
            } else if (turnDirection == Direction.LEFT) {
                turnDirection = Direction.RIGHT;
            } else if (turnDirection == Direction.RIGHT) {
                turnDirection = Direction.DOWN;
                placeDirection = placeDirection.rotate();
            } 

        } else {
            turnDirection = Direction.DOWN;
            placeDirection = placeDirection.rotate();
        }

        gamePanel.mouseHandler.placeDirection = placeDirection;
        gamePanel.mouseHandler.turnDirection = turnDirection;
    }

    @Override
    public BufferedImage getGhostImage() {
        BufferedImage ghost;

        try {
            ghost = ImageIO.read(getClass().getResourceAsStream(ghostPath));

            if (gamePanel.mouseHandler.turnDirection == Direction.RIGHT) {
                ghost = ImageIO.read(getClass().getResourceAsStream("/ghostBuildings/conveyorBelt/conveyorBeltRightTurn.png"));
            } else if (gamePanel.mouseHandler.turnDirection == Direction.LEFT) {
                ghost = ImageIO.read(getClass().getResourceAsStream("/ghostBuildings/conveyorBelt/conveyorBeltLeftTurn.png"));
            } else {
                ghost = ImageIO.read(getClass().getResourceAsStream(ghostPath));
            }
            
            return ghost;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    

}
