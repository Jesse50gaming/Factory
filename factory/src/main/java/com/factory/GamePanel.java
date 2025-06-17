package com.factory;

import java.awt.Dimension;

import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.Color;


import javax.swing.JPanel;



import com.factory.Handlers.CollisionChecker;
import com.factory.Handlers.KeyHandler;
import com.factory.Handlers.MouseHandler;
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
    
    //Player
    public Player player = new Player(this, keyHandler, mouseHandler);
    

    //Other
    
    public TileManager tileManager = new TileManager(this);
    CollisionChecker collisionChecker = new CollisionChecker(this);
    
    

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
        player.move();
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

        player.paint(g2);
        
        g2.dispose();
    }



    public static void fullScreen() {
        
    }

}