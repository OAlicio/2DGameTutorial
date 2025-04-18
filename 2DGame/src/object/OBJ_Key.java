package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Key extends Entity {
    
    GamePanel gp;
    
    public OBJ_Key(GamePanel gp) {

        super(gp);
        this.gp = gp;
        
        type = type_consumable;
        name = "Key";
        down1 = setup("/objects/key", gp.tileSize, gp.tileSize);
        description = "[" + name + "]\nIt open the door.";
        price = 12;
        stackable = true;

        setDialogue();
    }

    public void setDialogue() {

        dialogue[0][0] = "Door Opened.";
        dialogue[1][0] = "This isn't a Door.";
    }
    
    @Override
    public boolean use(Entity entity) {
        
        
        int objIndex = getDetected(entity, gp.obj, "Door");
        
        if(objIndex != 999) {
            startDialogue(this, 0);
            gp.playSE(3);
            gp.obj[gp.currentMap][objIndex] = null;
            return true;
        } 
        else {
            startDialogue(this, 1);
            return false;
        }
    }
}
