package entity;

import java.util.Random;
import main.GamePanel;

public class NPC_OldMan extends Entity {
    
    public NPC_OldMan(GamePanel gp) {
        
        super(gp);
        
        direction = "up";
        speed = 1;
        
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        getImage();
        setDialogue();
    }
    
    public void getImage() {
    
        up1 = setup("/npc/oldman_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/npc/oldman_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/npc/oldman_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/npc/oldman_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/npc/oldman_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/npc/oldman_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("/npc/oldman_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/npc/oldman_right_2", gp.tileSize, gp.tileSize);
    }
    
    public void setDialogue() {
        
        dialogue[0][0] = "Hello, lad.";
        dialogue[0][1] = "So you've come to this island to \nfind the treasure?";  //GRAPHICS2D IGNORA O \n
        dialogue[0][2] = "I used to be a great wizard but now... \nI'm a bit too old for taking an adventure.";
        dialogue[0][3] = "Well, good luck on you.";

        dialogue[1][0] = "If you become d, rest at the water.";
        dialogue[1][1] = "However, the monsters reapper if you rest.\nI don't know why but that's how it works.";
        dialogue[1][2] = "In any case, don't push yourself too hard.";
        
        dialogue[2][0] = "I wonder how to open that door...";

    }
    
    @Override
    public void setAction() {
        
        if(onPath == true) {
            
//            int goalCol = 10;
//            int goalRow = 9;
            int goalCol = (gp.player.worldX + gp.player.solidArea.x)/gp.tileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y)/gp.tileSize;
            
            searchPath(goalCol, goalRow);
        }
        else {
             
            actionLockCounter++;

            //if(actionLockCounter == 120 || gp.npc[0].collisionOn == true) {
            if(actionLockCounter == 120) {
                //ALEATORIZAR UM NUMERO
                Random random = new Random();
                int i = random.nextInt(100) + 1; //NUMEROS DE 1 A 100 (SERIA DE 0 A 99 SEM O +1)
                // -------------------------- //

                // COLOCAR AS ANIMACOES DE ACORDO AO NUMERO ALEATORIO
                if(i <= 25) {
                    direction = "up";
                }

                if(i > 25 && i <= 50) {
                    direction = "down";
                }

                if(i > 50 && i <= 75) {
                    direction = "left";
                }

                if(i > 75 && i <= 100) {
                    direction = "right";
                }
                // ---------------------------------------------------- //

                actionLockCounter = 0;
            }
        }
    }
    
    public void speak() {
        
        facePlayer();
        startDialogue(this, dialogueSet);
        
        onPath = true;
    }
}
