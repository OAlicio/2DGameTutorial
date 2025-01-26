package monster;

import entity.Entity;
import java.util.Random;
import main.GamePanel;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;
import object.OBJ_Rock;

public class MON_GreenSlime extends Entity {
    
    GamePanel gp;
    
    public MON_GreenSlime(GamePanel gp) {
        
        super(gp);
        
        this.gp = gp;
        
        type = type_monster;
        name = "Green Slime";
        speed = 1;
        maxLife = 4;
        life = maxLife;
        attack = 4;
        defense = 0;
        exp = 2;
        
        projectile = new OBJ_Rock(gp);
        
        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        getImage();
    }
    
    public void getImage() {
        
        up1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        up2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
        down1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        left2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
        right1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        right2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
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
        
        int i = new Random().nextInt(100) + 1;
        if(i > 99 && projectile.alive == false && shotAvailableCounter == 30) {
            projectile.set(worldX, worldY, direction, true, this);
            gp.projectileList.add(projectile);
            shotAvailableCounter = 0;
        }
    }
    
    @Override
    public void damageReaction() {
        
        actionLockCounter = 0;
        direction = gp.player.direction;
    }
    
    @Override
    public void checkDrop() {
        
        //RANDOMIZA O DROP
        int i = new Random().nextInt(100) + 1;
        
        //COLOCAR O ITEM DROPADO
        if(i < 50) {
            
            dropItem(new OBJ_Coin_Bronze(gp));
        }
        
        if(i >= 50 && i < 75) {
            
            dropItem(new OBJ_Heart(gp));
        }
        
        if(i >= 75 && i < 100) {
            
            dropItem(new OBJ_ManaCrystal(gp));
        }
    }
}
