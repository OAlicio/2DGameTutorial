package monster;

import entity.Entity;
import java.util.Random;
import main.GamePanel;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;
import object.OBJ_Rock;

public class MON_Orc extends Entity{

    GamePanel gp;
    
    public MON_Orc(GamePanel gp) {
        
        super(gp);
        
        this.gp = gp;
        
        type = type_monster;
        name = "Orc";
        defaultSpeed = 1;
        speed = defaultSpeed;
        maxLife = 10;
        life = maxLife;
        attack = 8;
        defense = 2;
        exp = 10;
        knockBackPower = 5;
        
        solidArea.x = 4;
        solidArea.y = 4;
        solidArea.width = 40;
        solidArea.height = 44;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        attackArea.width = 48;
        attackArea.height = 48;
        motion1_duration = 40;
        motion2_duration = 85;
//        if(gp.player.defense < 2) {
//            gp.player.currentWeapon.knockBackPower = 0;
//        }
//        else {
//            gp.player.currentWeapon.knockBackPower = gp.player.defense/2;
//        }
        getImage();
        getAttackImage();
    }
    
    public void getImage() {
        
        up1 = setup("/monster/orc_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/monster/orc_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/monster/orc_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/monster/orc_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/monster/orc_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/monster/orc_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("/monster/orc_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/monster/orc_right_2", gp.tileSize, gp.tileSize);
    }
    
    public void getAttackImage() {
        
        attackUp1 = setup("/monster/orc_attack_up_1", gp.tileSize, gp.tileSize * 2);
        attackUp2 = setup("/monster/orc_attack_up_2", gp.tileSize, gp.tileSize * 2);
        attackDown1 = setup("/monster/orc_attack_down_1", gp.tileSize, gp.tileSize * 2);
        attackDown2 = setup("/monster/orc_attack_down_2", gp.tileSize, gp.tileSize * 2);
        attackLeft1 = setup("/monster/orc_attack_left_1", gp.tileSize * 2, gp.tileSize);
        attackLeft2 = setup("/monster/orc_attack_left_2", gp.tileSize * 2, gp.tileSize);
        attackRight1 = setup("/monster/orc_attack_right_1", gp.tileSize * 2, gp.tileSize);
        attackRight2 = setup("/monster/orc_attack_right_2", gp.tileSize * 2, gp.tileSize);
    }
    
    @Override
    public void setAction() {
        
        if(onPath == true) {
            
            // Verifica se para de seguir
            checkStopChasingOrNot(gp.player, 15, 100);
            
            // Verifica em que direcao deve ir
            searchPath(getGoalCol(gp.player), getGoalRow(gp.player));
        }
        else {
            
            //Verifica quando iniciar a perseguicao
            checkStartChasingOrNot(gp.player, 5, 100);
            
            //Pega uma direcao aleatoria se nao estiver perseguindo
            getRandomDirection();
        }
        
        /* Verifica se ataca */
        if(attacking == false) {
            checkAttackOrNot(30, gp.tileSize * 4, gp.tileSize);
        }
    }
    
    @Override
    public void damageReaction() {
        
        actionLockCounter = 0;
//        direction = gp.player.direction;
        onPath = true;
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