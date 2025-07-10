package com.factory.buildings;

import java.awt.Graphics2D;

import com.factory.GamePanel;

public class BasicMiner extends Building {

    int miningSpeed = 5;  //how many seconds to mine once
    int miningCooldown = 5;

    public BasicMiner(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        
        setIndividualDefaults();
    }

    private void setIndividualDefaults() {
        height = 2;
        width = 2;
    }

    @Override
    public void update() {

        if(miningCooldown == 0) {
            mine();
        }


        
        miningCooldown--;
    }

    @Override
    public void paint(Graphics2D g2) {
        
    }

    public void mine() {
        miningCooldown = miningSpeed;
    }

}
