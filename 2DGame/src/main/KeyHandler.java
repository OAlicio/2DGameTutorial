package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener { //KeyListner "escuta" as teclas que serao pressionadas

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, shotKeyPressed;
    
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
                titleState(code);
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
        
        //OPTIONS STATE
        else if(gp.gameState == gp.optionsState) {
            optionsState(code);
        }
        
        //GAME OVER STATE
        else if(gp.gameState == gp.gameOverState) {
            gameOverState(code);
        }
        
        //TRADE STATE
        else if(gp.gameState == gp.tradeState) {
            tradeState(code);
        }
        
        //MAP STATE
        else if(gp.gameState == gp.mapState) {
            mapState(code);
        }
    }
    
    public void titleState(int code) {
        
        if(gp.ui.titleScreenState == 0) {
            
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

        if(code == KeyEvent.VK_F){ // Tecla P
            shotKeyPressed = true;
        }
        
        if(code == KeyEvent.VK_P){ // Tecla P
            gp.gameState = gp.pauseState;
        }
         
        if(code == KeyEvent.VK_ESCAPE){ // Tecla P
            gp.gameState = gp.optionsState;
        }
        
        if(code == KeyEvent.VK_M){ // Tecla P
            gp.gameState = gp.mapState;
        }
        
        if(code == KeyEvent.VK_X){ // Tecla P
            if(gp.map.miniMapOn == false) {
                gp.map.miniMapOn = true;
            }
            else{
                gp.map.miniMapOn = false;
            }
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
             
             switch(gp.currentMap) {
                 
                 case 0:
                     gp.tileM.loadMap("/maps/worldV2.txt", 0);
                     break;
                 case 1:
                     gp.tileM.loadMap("/maps/Interior01.txt", 1);
                     break;
             }
             
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
        
        if(code == KeyEvent.VK_ENTER) {
            gp.player.selectItem();
        }
        
        playerInventory(code);
    }

    public void optionsState(int code) {
        
        if(code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.playState;
        }
        
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        
        int maxCommandNum = 0;
        
        switch(gp.ui.subState) {
            case 0:
                maxCommandNum = 5;
                break;
            case 3:
                maxCommandNum = 1;
                break;
        }
        
        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            gp.ui.commandNum--;
            gp.playSE(9);
            if(gp.ui.commandNum < 0) {
                gp.ui.commandNum = maxCommandNum;
            }
        }
        
        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            gp.ui.commandNum++;
            gp.playSE(9);
            if(gp.ui.commandNum > maxCommandNum) {
                gp.ui.commandNum = 0;
            }
        }
        
        if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            if(gp.ui.subState == 0) {
                if(gp.ui.commandNum == 1 && gp.music.volumeScale > 0) { //VOLUME
                    gp.music.volumeScale--;
                    gp.music.checkVolume();
                    gp.playSE(9);
                }
                if(gp.ui.commandNum == 2 && gp.se.volumeScale > 0) { //SE
                    gp.se.volumeScale--;
                    gp.playSE(9);
                }
            }
        }
        
        if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            if(gp.ui.subState == 0) {
                if(gp.ui.commandNum == 1 && gp.music.volumeScale < 5) {
                    gp.music.volumeScale++;
                    gp.music.checkVolume();
                    gp.playSE(9);
                }
                if(gp.ui.commandNum == 2 && gp.se.volumeScale < 5) { //SE
                    gp.se.volumeScale++;
                    gp.playSE(9);
                }
            }
        }
    }
    
    public void gameOverState(int code) {
        
        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP){
            
            gp.ui.commandNum--;
            if(gp.ui.commandNum < 0) {
                gp.ui.commandNum = 1;
            }
            gp.playSE(9);
        }

        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN){
                
           gp.ui.commandNum++;
            if(gp.ui.commandNum > 1) {
                gp.ui.commandNum = 0;
            }
            gp.playSE(9);
        }
        
        if(code == KeyEvent.VK_ENTER) {
            
            if(gp.ui.commandNum == 0) {
                gp.gameState = gp.playState;
                gp.retry();
                gp.playMusic(0);
            }
            
            else if(gp.ui.commandNum == 1) {
                gp.ui.titleScreenState = 0;
                gp.gameState = gp.titleState;
                gp.restart();
            }
        }
    }
    
    public void tradeState(int code) {
        
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        
        if(gp.ui.subState == 0) {
            
            if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 2;
                }
                gp.playSE(9);
            }
            
            if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                gp.ui.commandNum++;
                if(gp.ui.commandNum > 2) {
                    gp.ui.commandNum = 0;
                }
                gp.playSE(9);
            }
        }
        
        if(gp.ui.subState == 1) {
            npcInventory(code);
            if(code == KeyEvent.VK_ESCAPE) {
                gp.ui.subState = 0;
            }
        }
        
        if(gp.ui.subState == 2) {
            playerInventory(code);
            if(code == KeyEvent.VK_ESCAPE) {
                gp.ui.subState = 0;
            }
        }
    }
    
    public void mapState(int code) {
        
        if(code == KeyEvent.VK_M) {
            gp.gameState = gp.playState;
        }
    }
    
    public void playerInventory(int code) {
        
        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP){ // Tecla W
            
            if(gp.ui.playerSlotRow != 0) {
                gp.ui.playerSlotRow--;
                gp.playSE(9);  
            }
        }

        if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT){ // Tecla A
                
            if(gp.ui.playerSlotCol != 0) {
                gp.ui.playerSlotCol--;
                gp.playSE(9);
            }
        }
        
        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN){ // Tecla S
            
            if(gp.ui.playerSlotRow != 3) {
                gp.ui.playerSlotRow++;
                gp.playSE(9);
            }
        }

        if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT){ // Tecla D
            
            if(gp.ui.playerSlotCol != 4) {
                gp.ui.playerSlotCol++;
                gp.playSE(9);
            }
        }
    }
    
    public void npcInventory(int code) {
        
        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP){ // Tecla W
            
            if(gp.ui.npcSlotRow != 0) {
                gp.ui.npcSlotRow--;
                gp.playSE(9);  
            }
        }

        if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT){ // Tecla A
                
            if(gp.ui.npcSlotCol != 0) {
                gp.ui.npcSlotCol--;
                gp.playSE(9);
            }
        }
        
        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN){ // Tecla S
            
            if(gp.ui.npcSlotRow != 3) {
                gp.ui.npcSlotRow++;
                gp.playSE(9);
            }
        }

        if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT){ // Tecla D
            
            if(gp.ui.npcSlotCol != 4) {
                gp.ui.npcSlotCol++;
                gp.playSE(9);
            }
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
        
        if(code == KeyEvent.VK_F){ // Tecla P

            shotKeyPressed = false;
        }
    }
}
