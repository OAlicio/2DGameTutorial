package object;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

public final class OBJ_Fireball extends Projectile {

    
    GamePanel gp;
    
    public OBJ_Fireball(GamePanel gp) {
        super(gp);
        this.gp = gp;
        
        name = "Fireball";
        speed = 5;
        maxLife = 80; //NA VERDADE AQUI O TEMPO DE DURACAO NA TELA,ATE QUE O PROJECTIL "MORRA"
        life = maxLife;
        attack = 2;
        useCost = 1;
        alive = false;
        getImage();
    }
    
    public void getImage() {
        up1 = setup("/projectile/fireball_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/projectile/fireball_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/projectile/fireball_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/projectile/fireball_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/projectile/fireball_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/projectile/fireball_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("/projectile/fireball_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/projectile/fireball_right_2", gp.tileSize, gp.tileSize);
    }

    @Override
    public boolean haveResource(Entity user) {
        
        boolean haveResource = false;
        if(user.mana >= useCost) {
            haveResource = true;
        }
        return haveResource;
    }
    
    @Override
    public void subtrackResource(Entity user) {
        user.mana -=  useCost;
    }
}
