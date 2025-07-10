package com.factory.Handlers;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.factory.GamePanel;
import com.factory.GUI.GUI;


public class GUIHandler {

    public ArrayList<GUI> GUIList = new ArrayList<>();
    public GamePanel gamePanel;



    public GUIHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;


    }

    public void update() {
        for(GUI gui:GUIList) {
            gui.update();
        }
    }

    public void draw(Graphics2D g2) {
        for(GUI gui:GUIList) {
            gui.draw(g2);;
        }
    }

    public void add(GUI GUI) {
        GUIList.add(GUI);
    }

    public void remove(GUI GUI) {
        GUIList.remove(GUI);
    }


}
