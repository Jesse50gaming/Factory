package com.factory;


import javax.swing.JFrame;



public class Main {
    public static void main(String[] args) {

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Factory");
        
        window.requestFocus();
        

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack();
        
        gamePanel.startGameThread();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

    }

}