package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Chest extends Entity {
    
    GamePanel gp;
    
    public static final String objName = "Chest";

    public OBJ_Chest(GamePanel gp) {
    
        super(gp);
        this.gp = gp;
        
        type = type_obstacle;
        name = objName;
        image = setup("/objects/chest", gp.tileSize, gp.tileSize);
        image2 = setup("/objects/chest_opened", gp.tileSize, gp.tileSize);
        down1 = image;
        collision = true;
        
        solidArea.x = 4;
        solidArea.y = 16;
        solidArea.width = 40;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
    
    public void setLoot(Entity loot) {
        this.loot = loot;
        
        setDialogue();
    }

    public void setDialogue() {

        dialogue[0][0] = "Chest opened and find a " + loot.name + "!\n" + //
                        "...But you cannot carry any more\"";
        dialogue[1][0] = "Chest opened and find a " + loot.name + "!\n" + //
                        "You obtain the \" + loot.name + \"!\"";
        dialogue[2][0] = "This chest is already opened";
    }

    public void interact() {
        
        if(opened == false) {
            gp.playSE(3);
            
            if(gp.player.canObtainItem(loot) == false) {
                startDialogue(this, 0);
            }
            else {
                startDialogue(this, 1);
                down1 = image2;
                opened = true;
            }
            startDialogue(this, 0);
        }
        else {
            startDialogue(this, 2);
        }
    }
}
