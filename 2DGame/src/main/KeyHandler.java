package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener { //KeyListner "escuta" as teclas que serao pressionadas

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed;
    
    //DEBUG
    boolean checkDrawTime = false;
    
    public KeyHandler(GamePanel gp) {
        
        this.gp = gp;
    }
    
    @Override
    public void keyTyped(KeyEvent e) { //NAO USAREMOS O KEYTYPED
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
        int code = e.getKeyCode();
        
        if (code == KeyEvent.VK_W){ // Tecla W
            upPressed = true;
        }
        if (code == KeyEvent.VK_S){ // Tecla S
            downPressed = true;
        }
        if (code == KeyEvent.VK_A){ // Tecla A
            leftPressed = true;
        }
        if (code == KeyEvent.VK_D){ // Tecla D
            rightPressed = true;
        }
        if (code == KeyEvent.VK_P){ // Tecla D
            if(gp.gameState == gp.playState) {
                gp.gameState = gp.pauseState;
            }
            else if(gp.gameState == gp.pauseState) {
                gp.gameState = gp.playState;
            }
        }
        
        //DEBUG
        if (code == KeyEvent.VK_T ){
            if(checkDrawTime == false) {
                checkDrawTime = true;
            }
            else if(checkDrawTime == true) {
                checkDrawTime = false;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
        int code = e.getKeyCode();
        
        if (code == KeyEvent.VK_W){
            upPressed = false;
        }
        if (code == KeyEvent.VK_S){
            downPressed = false;
        }
        if (code == KeyEvent.VK_A){
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D){
            rightPressed = false;
        }
    }
    
    
}
