package com.factory.tile;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

import com.factory.GamePanel;

public class TileManager {

    private HashMap<String, BufferedImage> cache = new HashMap<>();
    GamePanel gp;
    public Tile[] tile;
    public byte[][] mapTileNumber;
    public String currentMap;

    // chunks
    public static final int CHUNK_SIZE = 50;
    public static final int CHUNK_COLS = 1000 / CHUNK_SIZE;
    public static final int CHUNK_ROWS = 1000 / CHUNK_SIZE;
    public Chunk[][] chunks = new Chunk[CHUNK_COLS][CHUNK_ROWS];

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[10];
        mapTileNumber = new byte[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        createMap();
    }

    public void getTileImage() {
        tile[0] = new Tile(); // Grass
        tile[0].image = loadImage("/tiles/grass.png");

        tile[1] = new Tile(); // Sand
        tile[1].image = loadImage("/tiles/sand.png");

        tile[2] = new Tile(); // Unassigned
        tile[2].image = loadImage("/tiles/unasigned.png");
    }

    public void createMap() {
        final int MAP_WIDTH = CHUNK_SIZE * CHUNK_COLS;// tiles
        final int MAP_HEIGHT = CHUNK_SIZE * CHUNK_ROWS;
        final int BIOME_SIZE = 10000; //each biome if uninterupted will have 1,0000 tiles
        final int BIOME_TYPE_COUNT = 2;

        byte[][] map = new byte[MAP_WIDTH][MAP_HEIGHT];
        boolean[][] filled = new boolean[MAP_WIDTH][MAP_HEIGHT];
        boolean[][] partOfBiome = new boolean[MAP_WIDTH][MAP_HEIGHT];
        Random rand = new Random();
        

        // Set everything to unassigned
        for (int x = 0; x < MAP_WIDTH; x++) {
            Arrays.fill(map[x], (byte) 2);
        }

        int unasignedCount = MAP_WIDTH * MAP_HEIGHT;
        int unasignedBiomeCount = MAP_WIDTH * MAP_HEIGHT;
        int biomeID = 0;

        while (unasignedCount > 0) {
            //System.out.println("Unassigned: " + unasignedCount);

            int startX, startY;
            do {
                startX = rand.nextInt(MAP_WIDTH);
                startY = rand.nextInt(MAP_HEIGHT);
            } while (filled[startX][startY]); //keeps picking if filled

            Queue<Point> frontier = new LinkedList<>();
            frontier.add(new Point(startX, startY));

            int count = 0;
            byte biomeType = (byte) (biomeID % BIOME_TYPE_COUNT);

            while (!frontier.isEmpty() && count < BIOME_SIZE) {
                Point point = frontier.poll();
                int x = point.x;
                int y = point.y;

                if (x < 0 || y < 0 || x >= MAP_WIDTH || y >= MAP_HEIGHT || filled[x][y]) {  // skips if outside borders
                    continue; 
                }
                    

                map[x][y] = biomeType;
                filled[x][y] = true;
                unasignedCount--;
                count++;

                // random spreading
                if (rand.nextBoolean()) {
                    frontier.add(new Point(x + 1, y));
                }
                if (rand.nextBoolean()) {
                    frontier.add(new Point(x - 1, y));
                }
                if (rand.nextBoolean()) {
                    frontier.add(new Point(x, y + 1));
                }
                if (rand.nextBoolean()) {
                    frontier.add(new Point(x, y - 1));
                }
            }

            biomeID++;
        } // done with assigning biomes and tiles

        this.mapTileNumber = map;

        while (unasignedBiomeCount > 0) {

            int startX, startY;
            do {
                startX = rand.nextInt(MAP_WIDTH);
                startY = rand.nextInt(MAP_HEIGHT);
            } while (partOfBiome[startX][startY]); //keeps picking if filled

            List<Point> biomePoints = new LinkedList<>();
            byte biomeType = mapTileNumber[startX][startY];
            Queue<Point> frontier = new LinkedList<>();
            frontier.add(new Point(startX, startY));

            int count = 0;
            

            while (!frontier.isEmpty()) {
                Point point = frontier.poll();
                int x = point.x;
                int y = point.y;
                

                if (x < 0 || y < 0 || x >= MAP_WIDTH || y >= MAP_HEIGHT || partOfBiome[x][y]) {  // skips if outside borders
                    continue; 
                }

                
                biomePoints.add(point);
                count++;

                // random spreading
                if (x + 1 < MAP_WIDTH && rand.nextBoolean() && mapTileNumber[x + 1][y] == biomeType) {
                    frontier.add(new Point(x + 1, y));
                }
                if (x - 1 > MAP_WIDTH && rand.nextBoolean() && mapTileNumber[x-1][y] == biomeType) {
                    frontier.add(new Point(x - 1, y));
                }
                if (y + 1 < MAP_HEIGHT && rand.nextBoolean() && mapTileNumber[x][y+1] == biomeType) {
                    frontier.add(new Point(x, y + 1));
                }
                if (y - 1 > MAP_HEIGHT && rand.nextBoolean() && mapTileNumber[x][y - 1] == biomeType) {
                    frontier.add(new Point(x, y - 1));
                }

            }

            if (count >= BIOME_SIZE) {
                for (Point point : biomePoints) {
                    mapTileNumber[point.x][point.y] = biomeType;
                    unasignedBiomeCount--;
                    partOfBiome[point.x][point.y] = true;
                }
            } else {
                for (Point point : biomePoints) {
                    mapTileNumber[point.x][point.y] = (byte) (biomeID % BIOME_TYPE_COUNT); ;
                }
            }
            

        }


        // Create chunks (same as before)
        for (int chunkX = 0; chunkX < CHUNK_COLS; chunkX++) {
            for (int chunkY = 0; chunkY < CHUNK_ROWS; chunkY++) {
                chunks[chunkX][chunkY] = new Chunk(chunkX, chunkY);
                int worldX = chunkX * CHUNK_SIZE;
                int worldY = chunkY * CHUNK_SIZE;

                for (int tx = 0; tx < CHUNK_SIZE; tx++) {
                    for (int ty = 0; ty < CHUNK_SIZE; ty++) {
                        int mapX = worldX + tx;
                        int mapY = worldY + ty;
                        if (mapX < MAP_WIDTH && mapY < MAP_HEIGHT) {
                            chunks[chunkX][chunkY].tileIDs[tx][ty] = map[mapX][mapY];
                        }
                    }
                }
            }
        }
    }

    /* 
    public boolean unasignedTilesLeft() {
        int sandCount = 0;
        int grassCount = 0;
        int unasignedCount = 0;

        for (int x = 0; x < gp.maxWorldCol; x++) {
            for (int y = 0; y < gp.maxWorldRow; y++) {
                byte tile = mapTileNumber[x][y];
                if (tile == 1) {
                    sandCount++;
                } else if (tile == 0) {
                    grassCount++;
                } else if (tile == 2) {
                    unasignedCount++;
                }
            }
        }

        System.out.println("Total sand tiles: " + sandCount);
        System.out.println("Total grass tiles: " + grassCount);
        System.out.println("Total unasigned tiles: " + unasignedCount);

        return unasignedCount > 0;
    }
    */

    public void drawBackground(Graphics2D g2) {
        int startCol = gp.player.cameraX / gp.tileSize / CHUNK_SIZE;
        int startRow = gp.player.cameraY / gp.tileSize / CHUNK_SIZE;

        for (int cx = Math.max(0, startCol - 1); cx <= Math.min(CHUNK_COLS - 1, startCol + 1); cx++) {
            for (int cy = Math.max(0, startRow - 1); cy <= Math.min(CHUNK_ROWS - 1, startRow + 1); cy++) {

                Chunk chunk = chunks[cx][cy];

                for (int tx = 0; tx < CHUNK_SIZE; tx++) {
                    for (int ty = 0; ty < CHUNK_SIZE; ty++) {
                        int worldX = (cx * CHUNK_SIZE + tx) * gp.tileSize;
                        int worldY = (cy * CHUNK_SIZE + ty) * gp.tileSize;

                        int screenX = worldX - gp.player.cameraX;
                        int screenY = worldY - gp.player.cameraY;

                        if (
                            worldX + gp.tileSize > gp.player.cameraX &&
                            worldX < gp.player.cameraX + gp.screenWidth &&
                            worldY + gp.tileSize > gp.player.cameraY &&
                            worldY < gp.player.cameraY + gp.screenHeight
                        ) {
                            int tileNum = chunk.tileIDs[tx][ty];
                            g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                        }
                    }
                }
            }
        }
    }

    private BufferedImage loadImage(String path) {
        if (cache.containsKey(path))
            return cache.get(path);
        try {
            BufferedImage img = ImageIO.read(getClass().getResourceAsStream(path));
            cache.put(path, img);
            return img;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
