package object;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

public class OBJ_Rock extends Projectile {

    GamePanel gp;
    
    public OBJ_Rock(GamePanel gp) {
        super(gp);
        this.gp = gp;
        
        name = "Rock";
        speed = 8;
        maxLife = 80; //NA VERDADE AQUI O TEMPO DE DURACAO NA TELA,ATE QUE O PROJECTIL "MORRA"
        life = maxLife;
        attack = 2;
        useCost = 1;
        alive = false;
        getImage();
    }
    
    public void getImage() {
        up1 = setup("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
        up2 = setup("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
        down1 = setup("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
        left1 = setup("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
        left2 = setup("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
        right1 = setup("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
        right2 = setup("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
    }
    
        @Override
    public boolean haveResource(Entity user) {
        
        boolean haveResource = false;
        if(user.ammo >= useCost) {
            haveResource = true;
        }
        return haveResource;
    }
    
    @Override
    public void subtrackResource(Entity user) {
        user.ammo -=  useCost;
    }
}