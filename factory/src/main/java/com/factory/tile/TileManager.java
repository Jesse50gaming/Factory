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
    public Tile[] backgroundTile;
    public Tile[] oreTile;
    public byte[][] mapTileNumber;
    public byte[][] oreMapNumber;
    public String currentMap;

    // chunks
    public static final int CHUNK_SIZE = 50;
    public static final int CHUNK_COLS = 1000 / CHUNK_SIZE;//up to 10000
    public static final int CHUNK_ROWS = 1000 / CHUNK_SIZE;
    public Chunk[][] chunks = new Chunk[CHUNK_COLS][CHUNK_ROWS];

    boolean[][] hasOre = new boolean[CHUNK_SIZE * CHUNK_COLS][CHUNK_SIZE * CHUNK_ROWS];

    public TileManager(GamePanel gp) {
        this.gp = gp;
        backgroundTile = new Tile[25];
        oreTile = new Tile[25];
        mapTileNumber = new byte[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        createMap();
    }

    public void getTileImage() {

        //Background
        backgroundTile[10] = new Tile(); // unasigned
        backgroundTile[10].image = loadImage("/tiles/unasigned.png");

        backgroundTile[0] = new Tile(); // grass
        backgroundTile[0].image = loadImage("/tiles/grass.png");

        backgroundTile[1] = new Tile(); // sand
        backgroundTile[1].image = loadImage("/tiles/sand.png");

        backgroundTile[2] = new Tile(); // snow
        backgroundTile[2].image = loadImage("/tiles/snow.png");

        //Ore
        oreTile[1] = new Tile(); // iron
        oreTile[1].image = loadImage("/tiles/ironOre.png");

        oreTile[2] = new Tile(); // copper
        oreTile[2].image = loadImage("/tiles/copperOre.png");

    }

    public void createMap() {
    final int MAP_WIDTH = CHUNK_SIZE * CHUNK_COLS;
    final int MAP_HEIGHT = CHUNK_SIZE * CHUNK_ROWS;
    final int BIOME_SIZE = 1000;
    final int BIOME_TYPE_COUNT = 3; // 0 = grass, 1 = sand 2 = snow
    final int ORE_TYPE_COUNT = 2; // 1 = iron, 2 = copper
    final int TOTAL_TILES = MAP_WIDTH * MAP_HEIGHT;
    final int MAX_BIOMES = TOTAL_TILES / BIOME_SIZE;
    final int MIN_ORE_PATCH = 20;
    final int MAX_ORE_PATCH = 120;
    final int MAX_ORE_PATCHES = MAP_HEIGHT * MAP_WIDTH / 4000; 

    byte[][] map = new byte[MAP_WIDTH][MAP_HEIGHT];
    boolean[][] filled = new boolean[MAP_WIDTH][MAP_HEIGHT];
    byte[][] oreMap = new byte[MAP_WIDTH][MAP_HEIGHT];
    

    // fill with unassigned
    for (int x = 0; x < MAP_WIDTH; x++) {
        Arrays.fill(map[x], (byte) 10);
        
    }

    Random rand = new Random();
    int createdBiomes = 0;
    int unassignedCount = TOTAL_TILES;

    while (createdBiomes < MAX_BIOMES && unassignedCount > 0) {
        int startX, startY;
        do {
            startX = rand.nextInt(MAP_WIDTH);
            startY = rand.nextInt(MAP_HEIGHT);
        } while (filled[startX][startY]);

        byte biomeType =  (byte) ((createdBiomes % BIOME_TYPE_COUNT));
        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(startX, startY));

        int count = 0;

        while (!queue.isEmpty() && count < BIOME_SIZE) {
            Point point = queue.poll();
            int x = point.x;
            int y = point.y;

            if (x < 0 || y < 0 || x >= MAP_WIDTH || y >= MAP_HEIGHT || filled[x][y]) continue;

            map[x][y] = biomeType;
            filled[x][y] = true;
            count++;
            unassignedCount--;

            // shuffles neighbors for random spread
            List<Point> neighbors = Arrays.asList(
                new Point(x + 1, y),
                new Point(x - 1, y),
                new Point(x, y + 1),
                new Point(x, y - 1)
            );
            Collections.shuffle(neighbors, rand);
            queue.addAll(neighbors);
        }

        createdBiomes++;
    }

    


    // fill remaining unassigned with nearby biome
    for (int x = 0; x < MAP_WIDTH; x++) {
        for (int y = 0; y < MAP_HEIGHT; y++) {
            if (!filled[x][y]) {
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        int nx = x + dx, ny = y + dy;
                        if (nx >= 0 && ny >= 0 && nx < MAP_WIDTH && ny < MAP_HEIGHT && filled[nx][ny]) {
                            map[x][y] = map[nx][ny];
                            filled[x][y] = true;
                            break;
                        }
                    }
                    if (filled[x][y]) break;
                }
            }
        }
    }

    this.mapTileNumber = map;
    
    //make Ore patches
    int orePatches = 0;
    while (orePatches < MAX_ORE_PATCHES) {

        int startX, startY;
        do {
            startX = rand.nextInt(MAP_WIDTH);
            startY = rand.nextInt(MAP_HEIGHT);
        } while (hasOre[startX][startY]);

        byte oreType = (byte) ((byte) (orePatches % ORE_TYPE_COUNT) + 1);
        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(startX, startY));
        int count = 0;
        int patchSize = rand.nextInt(MIN_ORE_PATCH, MAX_ORE_PATCH);

        while (!queue.isEmpty() && count < patchSize) {
            Point point = queue.poll();
            int x = point.x;
            int y = point.y;

            if (x < 0 || y < 0 || x >= MAP_WIDTH || y >= MAP_HEIGHT || hasOre[x][y]) continue;

            oreMap[x][y] = oreType;
            hasOre[x][y] = true;
            count++;
            

            // shuffles neighbors for random spread
            List<Point> neighbors = Arrays.asList(
                new Point(x + 1, y),
                new Point(x - 1, y),
                new Point(x, y + 1),
                new Point(x, y - 1)
            );
            Collections.shuffle(neighbors, rand);
            queue.addAll(neighbors);
            
        }

        orePatches++;
    }
    this.oreMapNumber = oreMap;


    // Create chunks
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
                        chunks[chunkX][chunkY].oreIDs[tx][ty] = oreMap[mapX][mapY];
                    }
                }
            }
        }
    }

    /* tile counter
    int grass = 0, sand = 0;
    for (int x = 0; x < MAP_WIDTH; x++) {
        for (int y = 0; y < MAP_HEIGHT; y++) {
            if (map[x][y] == 0) grass++;
            else if (map[x][y] == 1) sand++;
        }
    }
     
    System.out.println("Final biome tile counts:");
    System.out.println("Grass tiles: " + grass);
    System.out.println("Sand tiles: " + sand);
    */
}

    

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

                        if ( worldX + gp.tileSize > gp.player.cameraX && worldX < gp.player.cameraX + gp.screenWidth && worldY + gp.tileSize > gp.player.cameraY && worldY < gp.player.cameraY + gp.screenHeight) {
                            int tileNum = chunk.tileIDs[tx][ty];
                            int oreNum = chunk.oreIDs[tx][ty];
                            int worldTileX = cx * CHUNK_SIZE + tx;
                            int worldTileY = cy * CHUNK_SIZE + ty;
                            g2.drawImage(backgroundTile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                            if (hasOre[worldTileX][worldTileY] && oreNum != 0) {
                                g2.drawImage(oreTile[oreNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                            }
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
