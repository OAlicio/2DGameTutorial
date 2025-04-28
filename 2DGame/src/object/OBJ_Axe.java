package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Axe extends Entity{

    public static final String objName = "Woodcutter's Axe";

    public OBJ_Axe(GamePanel gp) {
        super(gp);
        
        type = type_axe;
        name = objName;
        down1 = setup("/objects/axe", gp.tileSize, gp.tileSize);
        attackValue = 6;
        attackArea.width = 30;
        attackArea.height = 30;
        description = "[" + name + "]\nA bit rusty but can \ncut some trees.";
        price = 75;
        knockBackPower = 2;
        motion1_duration = 20;
        motion2_duration = 40;
    }

}
