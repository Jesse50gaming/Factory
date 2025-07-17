package com.factory.Handlers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean wPressed;
    public boolean sPressed;
    public boolean dPressed;
    public boolean aPressed;
    public boolean ePressed;
    public boolean escPressed;
    public boolean zPressed;
    public boolean fPressed;
    public boolean rPressed;

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            wPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            sPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            aPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            dPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_E) {
            ePressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            escPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_Z) {
            zPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_F) {
            fPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_R) {
            rPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            wPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            sPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            aPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            dPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_E) {
            ePressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            escPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_Z) {
            zPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_F) {
            fPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_R) {
            rPressed = false;
        }
    }
}
