package com.factory.Handlers;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import com.factory.GamePanel;
import com.factory.GUI.GUI;
import com.factory.GUI.Hotbar;
import com.factory.GUI.ItemContainer;
import com.factory.buildings.Building;
import com.factory.items.ConveyorBeltItem;
import com.factory.items.Item;
import com.factory.util.Direction;

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

    public boolean rightClickUsed = false;
    public boolean leftClickUsed = false;

    public int dropCooldown = 0;
    public int rotateCooldown = 0;

    public Direction placeDirection = Direction.UP;
    public Direction turnDirection = Direction.DOWN;
    public BufferedImage placeGhost;

    public Direction lastPlaceDirection = Direction.UP;

    public int mouseTileWorldX;
    public int mouseTileWorldY;

    int rotateNum = 0;
    

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

            mouseTileWorldX = (int) Math.floor(mouseWorldX / gamePanel.tileSize);
            mouseTileWorldY = (int) Math.floor(mouseWorldY / gamePanel.tileSize);
        }

        if (dropCooldown > 0) dropCooldown--;
        if (rotateCooldown > 0) rotateCooldown--;

        if (itemInHand && inHand.placeable) {


            // objects to compare to
            ConveyorBeltItem belt = new ConveyorBeltItem(gamePanel,1);

            //rotate
            if (gamePanel.keyHandler.rPressed && rotateCooldown == 0) {
                rotateNum++;

                
                if (inHand.getClass() == belt.getClass()) {

                    

                    inHand.rotate(mouseTileWorldX,mouseTileWorldY, placeDirection, turnDirection);
                    /* 
                    if (rotateNum % 12 == 0) {
                        turnDirection = Direction.UP;
                        placeDirection = Direction.UP;
                    } else if (rotateNum % 12 == 1) {
                        placeDirection = Direction.UP;
                        turnDirection = Direction.RIGHT;
                    }else if (rotateNum % 12 == 2) {
                        placeDirection = Direction.UP;
                        turnDirection = Direction.LEFT;
                    }else if (rotateNum % 12 == 3) {
                        placeDirection = Direction.RIGHT;
                        turnDirection = Direction.UP;
                    }else if (rotateNum % 12 == 4) {
                        placeDirection = Direction.RIGHT;
                        turnDirection = Direction.RIGHT;
                    }else if (rotateNum % 12 == 5) {
                        placeDirection = Direction.RIGHT;
                        turnDirection = Direction.LEFT;
                    }else if (rotateNum % 12 == 6) {
                        placeDirection = Direction.DOWN;
                        turnDirection = Direction.UP;
                    }else if (rotateNum % 12 == 7) {
                        placeDirection = Direction.DOWN;
                        turnDirection = Direction.RIGHT;
                    } else if (rotateNum % 12 == 8) {
                        placeDirection = Direction.DOWN;
                        turnDirection = Direction.LEFT;
                    }else if (rotateNum % 12 == 9) {
                        placeDirection = Direction.LEFT;
                        turnDirection = Direction.UP;
                    }else if (rotateNum % 12 == 10) {
                        placeDirection = Direction.LEFT;
                        turnDirection = Direction.RIGHT;
                    }else if (rotateNum % 12 == 11) {
                        placeDirection = Direction.LEFT;
                        turnDirection = Direction.LEFT;
                    }
                    */

                } else {
                    placeDirection = placeDirection.rotate();
                    
                }
                rotateCooldown = 15;
            }

            //should it be placed
            if (leftDown && !leftClickUsed) {
                boolean mouseOverGUI = false;
                for (GUI gui: gamePanel.GUIHandler.GUIList) {
                    if (gui.mouseIsOver()) {
                        mouseOverGUI = true;
                        
                        break;
                    }
                }
                
                if(!mouseOverGUI) {
                    

                    // place
                    if (inHand.getClass() == belt.getClass()) {
                        Building building = inHand.uniquePlace(placeDirection, turnDirection);  
                        if (building != null) {
                            building.updateTexture();
                        }
                        
                    } else {
                        inHand.place(placeDirection);
                    }
                    turnDirection = Direction.DOWN;
                    lastPlaceDirection = placeDirection;
                    
                    useLeft();
                }
            }
        }

        if (itemInHand && gamePanel.keyHandler.zPressed && dropCooldown == 0) {
            if (inHand.stackSize > 0) {
                Item dropItem = inHand.splitItem(1);
                if (dropItem != null) {
                    dropItem.putOnFloor(mouseWorldX, mouseWorldY);
                    dropCooldown = 10;

                    if (inHand.checkIfGone()) {
                        inHand = null;
                        itemInHand = false;
                    }
                }
            }
        }

        if (itemInHand) {
            inHand.update();
            if (inHand.checkIfGone()) {
                inHand = null;
                itemInHand = false;
            }
        }

        if (rightClick) rightClick = false;
        if (leftClick) leftClick = false;

        rightClickUsed = false;
        leftClickUsed = false;
        
    }



    public void draw(Graphics2D g2) {
        if (itemInHand && inHand != null) {

            g2.drawImage(inHand.image, mouseScreenX, mouseScreenY,inHand.containerWidth * gamePanel.scale,inHand.containerHeight * gamePanel.scale, null);

            Font font = new Font("TIMES NEW ROMAN", Font.BOLD, 10 * gamePanel.scale);
            g2.setFont(font);
            g2.setColor(Color.WHITE);
            g2.drawString(String.valueOf(inHand.stackSize),mouseScreenX, mouseScreenY + inHand.containerHeight * gamePanel.scale);

            if (inHand.placeable) {
                int worldTileX = (int) Math.floor(gamePanel.mouseHandler.mouseWorldX / gamePanel.tileSize);
                int worldTileY = (int) Math.floor(gamePanel.mouseHandler.mouseWorldY / gamePanel.tileSize);

                int ghostWorldX = worldTileX * gamePanel.tileSize;
                int ghostWorldY = worldTileY * gamePanel.tileSize;

                int screenX = ghostWorldX - gamePanel.player.cameraX;
                int screenY = ghostWorldY - gamePanel.player.cameraY;

                BufferedImage ghostImage = inHand.getGhostImage();

                AffineTransform old = g2.getTransform();
                if (ghostImage != null) {
                    int drawWidth = inHand.tileWidth * gamePanel.tileSize;
                    int drawHeight = inHand.tileHeight * gamePanel.tileSize;

                    double rotationAngleRadians = Math.toRadians(placeDirection.rotateAngle());
                    if(inHand.rotateable) {
                        g2.rotate(rotationAngleRadians, screenX + drawWidth / 2.0, screenY + drawHeight / 2.0);
                    }
                    
                    g2.drawImage(ghostImage, screenX, screenY, drawWidth, drawHeight, null);
                    g2.setTransform(old);
                }
            }
        }
    }

    public boolean touchingMouse(int screenX, int screenY, int width, int height) {
        return mouseScreenX >= screenX && mouseScreenX < screenX + width&& mouseScreenY >= screenY && mouseScreenY < screenY + height;
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

    public void useRight() {
        rightClickUsed = true;
    }

    public void useLeft() {
        leftClickUsed = true;
        
    }

    
}
