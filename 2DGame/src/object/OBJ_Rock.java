package object;

import entity.Entity;
import entity.Projectile;
import java.awt.Color;
import main.GamePanel;

public class OBJ_Rock extends Projectile {

    GamePanel gp;
    
    public static final String objName = "Rock";

    public OBJ_Rock(GamePanel gp) {
        super(gp);
        this.gp = gp;
        
        name = objName;
        speed = 4;
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
    
    @Override
    public Color getParticleColor() {
        
        Color color = new Color(40, 50, 0);
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