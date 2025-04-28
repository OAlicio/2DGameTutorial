package main;

import entity.Entity;

public class EventHandler {
    
    GamePanel gp;
    EventRect[][][] eventRect;
    Entity eventMaster;
    
    int previousEventX, previousEventY;
    boolean canTouchEvent = true;
    int tempMap, tempCol, tempRow;
    
    
    public EventHandler(GamePanel gp) {
        
        this.gp = gp;
        
        eventMaster = new Entity(gp);

        eventRect = new EventRect[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];
        
        int map = 0;
        int col = 0;
        int row = 0;
        while(map < gp.maxMap && col < gp.maxWorldCol && row < gp.maxWorldRow) {
           
            eventRect[map][col][row] = new EventRect();
            eventRect[map][col][row].x = 23; //QUEREMOS QUE O EVENTO OCORRA MAIS PRA DENTRO DE UM TILE
            eventRect[map][col][row].y = 23;
            eventRect[map][col][row].width = 2;
            eventRect[map][col][row].height = 2;

            eventRect[map][col][row].eventRectDefaultX = eventRect[map][col][row].x;
            eventRect[map][col][row].eventRectDefaultY = eventRect[map][col][row].y;

            col++;
            if(col == gp.maxWorldCol) {
                col = 0;
                row++;
                
                if(row == gp.maxWorldRow) {
                    row = 0;
                    map++;
                }
            }
        }

        setDialogue();
    }
    
    public void setDialogue() {

        eventMaster.dialogue[0][0] = "You fall into a pit!";

        eventMaster.dialogue[1][0] = "You drink the water.\nYour life and mana have been recovered "
                    + "\n And Monster's has been replaced. " + 
                    "\n(Game Saved)";
        eventMaster.dialogue[1][1] = "Damn, this is good water";

    }

    public void checkEvent() {
        
        // VERIFICA SE O JOGADOR ESTA UM TILE DE DISTANCIA DO ULTIMO EVENTO, SO ACONTECE SE O PLAYER SE MOVER UM TILE E VOLTAR
        
        int xDistance = Math.abs(gp.player.worldX - previousEventX);
        int yDistance = Math.abs(gp.player.worldY - previousEventY);
        int distance = Math.max(xDistance, yDistance);
        
        /*if(distance > gp.tileSize/2) {
            canTouchEvent = true;
        } THIS IS BETTER!*/
        
        if(distance > gp.tileSize/2) {
            canTouchEvent = true;
        }
        // ----------------------------------------------------------------- //
        
        if(canTouchEvent == true) {

            if(hit(0, 27, 16, "right")) {
                damagePit(gp.dialogueState);
            }

            else if(hit(0, 23, 12, "up")) {
                healingPool(gp.dialogueState);
            }

            // ----- Merchant --------

            //Pra dentro
            else if(hit(0, 10, 39, "any")) {
                teleport(1, 12, 13);
            }

            //Pra fora
            else if(hit(1, 12, 13, "any")) {
                teleport(0, 10, 39);
            }
            // ------------------------ //

            else if(hit(1, 12, 9, "up")) {
                speak(gp.npc[1][0]);
            }

            // ---------- Dungeon ----------

            //Pra dentro
            else if(hit(0, 12, 9, "up")) {
                teleport(2, 9, 41);
            }

            //Pra fora
            else if(hit(2, 9, 41, "up")) {
                teleport(0, 12, 9);
            }

            //B1
            else if(hit(2, 8, 7, "up")) {
                teleport(3, 26, 41);
            }

            //B2
            else if(hit(3, 26, 41, "up")) {
                teleport(2, 8, 7);
            }
            // -----------------------------
        }
    }
    
    public boolean hit(int map, int col, int row, String reqDirection) {
        
        boolean hit = false;
        
        if(map == gp.currentMap) {
            
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
        eventRect[map][col][row].x = col * gp.tileSize + eventRect[map][col][row].x;
        eventRect[map][col][row].y = row * gp.tileSize + eventRect[map][col][row].y;
        
         if(gp.player.solidArea.intersects(eventRect[map][col][row]) && eventRect[map][col][row].eventDone == false) {
            
            if(gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
                
                hit = true;
                
                previousEventX = gp.player.worldX;
                previousEventY = gp.player.worldY;
            }
        }
        
        //REDEFININDO PROS VALORES PADROES
        
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        eventRect[map][col][row].x = eventRect[map][col][row].eventRectDefaultX;
        eventRect[map][col][row].y = eventRect[map][col][row].eventRectDefaultY;
        
        }
        
        return hit;
    }
    
    public void teleport(int map, int col, int row) {
        
        gp.gameState = gp.transitionState;
        
        tempMap = map;
        tempCol = col;
        tempRow = row;
        
        canTouchEvent = false;
        gp.playSE(13);
    }
    
    public void damagePit(int gameState) {
        
        gp.gameState = gameState;
        gp.playSE(6);
        eventMaster.startDialogue(eventMaster, 0);
        gp.player.life -= 1;
        canTouchEvent = false;
    }
    
    public void healingPool(int gameState) {
        
        if(gp.keyH.enterPressed == true) {
            
            gp.gameState = gameState;
            gp.player.attackCanceled = true;
            gp.playSE(2);
            eventMaster.startDialogue(eventMaster, 1);
            gp.player.life = gp.player.maxLife;
            gp.player.mana = gp.player.maxMana;
            canTouchEvent = false;
            gp.aSetter.setMonster();
            gp.saveLoad.save();
        }
    }
    
    public void speak(Entity entity) {
        
        if(gp.keyH.enterPressed == true) {
            gp.gameState = gp.dialogueState;
            gp.player.attackCanceled = true;
            entity.speak();
        }
    }
}
