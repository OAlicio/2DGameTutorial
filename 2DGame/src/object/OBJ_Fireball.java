package object;

import entity.Entity;
import entity.Projectile;
import java.awt.Color;
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
        knockBackPower = 5;
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
    
    @Override
    public Color getParticleColor() {
        
        Color color = new Color(240, 50, 0);
        return color;
    }
    
    @Override
    public int getParticleSize() {
        
        int size = 10;
        return size;
    }
    
    @Override
    public int getParticleSpeed() {
        
        int speed = 1; //Velocidade da particula
        return speed;
    }
    
    @Override
    public int getParticleMaxLife() { //QUANTO TEMPO A PARTICULA DURA
        
        int maxLife = 20;
        return maxLife;
    }
}
