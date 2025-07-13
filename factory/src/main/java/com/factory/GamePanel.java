package com.factory;

import java.awt.Dimension;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;


import javax.swing.JPanel; 



import com.factory.Handlers.CollisionChecker;
import com.factory.Handlers.GUIHandler;
import com.factory.Handlers.KeyHandler;
import com.factory.Handlers.MouseHandler;
import com.factory.buildings.Building;
import com.factory.tile.TileManager;



public class GamePanel extends JPanel implements Runnable {

    //Screen Settings
    static int originalTileSize = 16;
    public int scale = 4;
    final public int tileSize = originalTileSize * scale;

    final public int maxScreenCol = 24;
    final public int maxScreenRow = 18;

    final public int screenWidth = tileSize*maxScreenCol;
    final public int screenHeight = tileSize*maxScreenRow;
    public final int FPS = 60;

    //World Settings

    public int maxWorldCol = 10000;  
    public int maxWorldRow = 10000; 
    public int worldWidth = tileSize * maxWorldCol;
    public int worldHeight = tileSize * maxWorldRow;

    int cooldown = 60; // temporary

    //Other
    Thread gameThread;
    public KeyHandler keyHandler = new KeyHandler();
    public MouseHandler mouseHandler = new MouseHandler(this);
    public GUIHandler GUIHandler = new GUIHandler(this);
    
    //Player
    public Player player = new Player(this, keyHandler, mouseHandler);
    

    //Other
    
    public TileManager tileManager = new TileManager(this);
    CollisionChecker collisionChecker = new CollisionChecker(this);
    

    //objects
    public ArrayList<Building> buildings = new ArrayList<>();
    public boolean[][] hasObject = new boolean[maxWorldCol][maxWorldRow];

    List<Building> buildingsToRemove = new ArrayList<>();
    

    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(keyHandler);
        this.addMouseListener(mouseHandler);
        
        
    }   

    




    @Override
    public void run() { 

        double drawInterval = 1000000000/FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while(gameThread != null) {

            
             update();
            
             repaint();
             
             try {
                
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime/1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);
                
                nextDrawTime += drawInterval;
                

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void update() {
        player.update();
        
        for (Building building : buildings) {
            building.update();
        }

        for (Building building: buildingsToRemove) {
            buildings.remove(building);
        }
        
        
        GUIHandler.update();
        mouseHandler.updateMouse();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        tileManager.drawBackground(g2);

        for (Building building : buildings) {
            building.paint(g2);
        }

        player.paint(g2);
        mouseHandler.draw(g2);
        GUIHandler.draw(g2);
        g2.dispose();
    }

    public void removeBuilding(Building building) {
        buildings.remove(building);
        hasObject[building.worldX /tileSize][building.worldY / tileSize] = false;
    }

    public void destroyBuilding(Building building) {
        buildingsToRemove.add(building);

        int startX = building.worldX / tileSize;
        int startY = building.worldY / tileSize;
        int tileWidth = building.width / tileSize;
        int tileHeight = building.height / tileSize;

        for (int y = 0; y < tileHeight; y++) {
            for (int x = 0; x < tileWidth; x++) {
                hasObject[startX + x][startY + y] = false;
            }
        }

       
        building = null;
    }



    public static void fullScreen() {
        
    }

}