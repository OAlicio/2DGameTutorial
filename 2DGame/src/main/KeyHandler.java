package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener { //KeyListner "escuta" as teclas que serao pressionadas

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, dialogueKey;
    
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
        
        // PLAY STATE
        if(gp.gameState == gp.playState) {
            
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP){ // Tecla W
            upPressed = true;
            }
            
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN){ // Tecla S
                downPressed = true;
            }
            
            if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT){ // Tecla A
                leftPressed = true;
            }
            
            if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT){ // Tecla D
                rightPressed = true;
            }
            
            if (code == KeyEvent.VK_ENTER) {

                dialogueKey = true;
            }
            
             if (code == KeyEvent.VK_P){ // Tecla P
                
                gp.gameState = gp.pauseState;
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
        
        //PAUSE STATE
        else if(gp.gameState == gp.pauseState) {
            
            if (code == KeyEvent.VK_P){
                
                gp.gameState = gp.playState;
            }
        }
        
        // DIALOGUE STATE
        else if(gp.gameState == gp.dialogueState) {
            
            if(code == KeyEvent.VK_ENTER) {
                gp.gameState = gp.playState;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
        int code = e.getKeyCode();
        
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP){
            upPressed = false;
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN){
            downPressed = false;
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT){
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT){
            rightPressed = false;
        }
    }
    
    
}
