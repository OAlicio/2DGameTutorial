package entity;

import java.util.Random;
import main.GamePanel;

public class NPC_OldMan extends Entity {
    
    public NPC_OldMan(GamePanel gp) {
        
        super(gp);
        
        direction = "up";
        speed = 1;
        
        getImage();
        setDialogue();
        solidArea.x = 0;
        solidArea.y = 16;
        solidArea.width = 48;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
    
    public void getImage() {
    
        up1 = setup("/npc/oldman_up_1");
        up2 = setup("/npc/oldman_up_2");
        down1 = setup("/npc/oldman_down_1");
        down2 = setup("/npc/oldman_down_2");
        left1 = setup("/npc/oldman_left_1");
        left2 = setup("/npc/oldman_left_2");
        right1 = setup("/npc/oldman_right_1");
        right2 = setup("/npc/oldman_right_2");
    }
    
    public void setDialogue() {
        
        dialogue[0] = "Hello, lad.";
        dialogue[1] = "So you've come to this island to \nfind the treasure?";  //GRAPHICS2D IGNORA O \n
        dialogue[2] = "I used to be a great wizard but now... \nI'm a bit too old for taking an adventure.";
        dialogue[3] = "Well, good luck on you.";
    }
    
    @Override
    public void setAction() {
        
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
    
    public void speak() {
        
        super.speak();
    }
}
