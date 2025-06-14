package com.factory.tile;

public class Chunk {
    public int[][] tileIDs = new int[TileManager.CHUNK_SIZE][TileManager.CHUNK_SIZE];
    public int chunkX, chunkY;

    public Chunk(int x, int y) {
        this.chunkX = x;
        this.chunkY = y;
    }
}

