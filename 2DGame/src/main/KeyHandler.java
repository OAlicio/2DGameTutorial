package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener { //KeyListner "escuta" as teclas que serao pressionadas

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed;
    
    //DEBUG
    boolean showDebugText = false;
    
    public KeyHandler(GamePanel gp) {
        
        this.gp = gp;
    }
    
    @Override
    public void keyTyped(KeyEvent e) { //NAO USAREMOS O KEYTYPED
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
        int code = e.getKeyCode();
        
        //TITLE STATE
        if(gp.gameState == gp.titleState) {
            
            if(gp.ui.titleScreenState == 0) {
                
                titleState(code);
            }
            
            else if(gp.ui.titleScreenState == 1) {
                
                if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP){ // Tecla W
                    gp.ui.commandNum--;
                    if(gp.ui.commandNum < 0) {
                        gp.ui.commandNum = 0;
                        //gp.ui.commandNum = 2; -> *Se quisermosum menu que ao chegar ao fim volta ao inicio
                    }
                }

                if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN){ // Tecla S
                    gp.ui.commandNum++;
                    if(gp.ui.commandNum > 3) {
                        gp.ui.commandNum = 3;
                        //gp.ui.commandNum = 0; -> *
                    }
                }

                if(code == KeyEvent.VK_ENTER) {
                    
                    if(gp.ui.commandNum == 0) {
                        System.out.println("Fighter stuff here");
                        gp.gameState = gp.playState;
                        gp.playMusic(0);
                    }

                    if(gp.ui.commandNum == 1) {
                        System.out.println("Thief stuff here");
                        gp.gameState = gp.playState;
                        gp.playMusic(0);

                    }

                    if(gp.ui.commandNum == 2) {
                        System.out.println("Sorcerer stuff here");
                        gp.gameState = gp.playState;
                        gp.playMusic(0);
                    }
                    
                     if(gp.ui.commandNum == 3) {
                        //BACK
                        gp.ui.titleScreenState = 0; //FECHAR O PROGRAMA
                    }
                }
            }
        }
        
        // PLAY STATE
        else if(gp.gameState == gp.playState) {
            
            playState(code);
        }
        
        //PAUSE STATE
        else if(gp.gameState == gp.pauseState) {
            
            pauseState(code);
        }
        
        // DIALOGUE STATE
        else if(gp.gameState == gp.dialogueState) {
            
            dialogueState(code);
        }
        //CHARACTER STATE
        else if(gp.gameState == gp.characterState) {
            
            characterState(code);
        }
    }
    
    public void titleState(int code) {
        
        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP){ // Tecla W
            gp.ui.commandNum--;
            if(gp.ui.commandNum < 0) {
                gp.ui.commandNum = 0;
                //gp.ui.commandNum = 2; -> *Se quisermos um menu que ao chegar ao fim volta ao inicio
            }
        }

        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN){ // Tecla S
            gp.ui.commandNum++;
            if(gp.ui.commandNum > 2) {
                gp.ui.commandNum = 2;
                //gp.ui.commandNum = 0; -> *
            }
        }

        if(code == KeyEvent.VK_ENTER) {
            if(gp.ui.commandNum == 0) {
                gp.ui.titleScreenState = 1;
            }

            if(gp.ui.commandNum == 1) {
                //NEW GAME

            }

            if(gp.ui.commandNum == 2) {
                //QUIT
                System.exit(0); //FECHAR O PROGRAMA
            }
        }
    }
    
    public void playState(int code) {
        
        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP){ // Tecla W
            upPressed = true;
        }

        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN){ // Tecla S
            downPressed = true;
        }

        if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT){ // Tecla A
            leftPressed = true;
        }

        if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT){ // Tecla D
            rightPressed = true;
        }

        if(code == KeyEvent.VK_C) {
            gp.gameState = gp.characterState; 
        }

        if(code == KeyEvent.VK_ENTER) {

            enterPressed = true;
        }

         if(code == KeyEvent.VK_P){ // Tecla P

            gp.gameState = gp.pauseState;
        }

        //DEBUG
        if(code == KeyEvent.VK_T ){

            if(showDebugText == false) {
                showDebugText = true;
            }

            else if(showDebugText == true) {
                showDebugText = false;
            }
        }
        
        //MAP CHANGE's RELOADER
         if(code == KeyEvent.VK_R ) {
             
             gp.tileM.loadMap("/maps/worldV2.txt");
         }
    }
    
    public void pauseState(int code) {
        
        if (code == KeyEvent.VK_P){

            gp.gameState = gp.playState;
        }
    }
    
    public void dialogueState(int code) {
        
        if(code == KeyEvent.VK_ENTER) {
            
            gp.gameState = gp.playState;
        }
    }
    
    public void characterState(int code) {
        
        if(code == KeyEvent.VK_C) {
            gp.gameState = gp.playState;
        }
        
        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP){ // Tecla W
            
            if(gp.ui.slotRow != 0) {
                gp.ui.slotRow--;
                gp.playSE(9);  
            }
        }

        if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT){ // Tecla A
                
            if(gp.ui.slotCol != 0) {
                gp.ui.slotCol--;
                gp.playSE(9);
            }
        }
        
        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN){ // Tecla S
            
            if(gp.ui.slotRow != 3) {
                gp.ui.slotRow++;
                gp.playSE(9);
            }
        }

        if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT){ // Tecla D
            
            if(gp.ui.slotCol != 4) {
                gp.ui.slotCol++;
                gp.playSE(9);
            }
        }
        
        if(code == KeyEvent.VK_ENTER) {
            gp.player.selectItem();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
        int code = e.getKeyCode();
        
        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP){
            upPressed = false;
        }
        
        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN){
            downPressed = false;
        }
        
        if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT){
            leftPressed = false;
        }
        
        if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT){
            rightPressed = false;
        }
        
        //if)
    }
    
    
}
