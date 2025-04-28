package tile_interactive;

import entity.Entity;
import java.awt.Color;
import main.GamePanel;

public class IT_DryTree extends InteractiveTile{

    GamePanel gp;
    
    public IT_DryTree(GamePanel gp, int col, int row) {
        super(gp, col, row);
        this.gp = gp;
        
        this.worldX = gp.tileSize * col;
        this.worldY = gp.tileSize * row;
        
        down1 = setup("/tiles_interactive/drytree", gp.tileSize, gp.tileSize);
        destructible = true;
        life = 1;
    }

    @Override
    public boolean isCorrectItem(Entity entity) {
        
        boolean isCorrectItem = false;
        
        if(entity.currentWeapon.type == type_axe) {
            isCorrectItem = true;
        }
        
        return isCorrectItem;
    }
    
    @Override
    public void playSE() {
        gp.playSE(11);
    }
    
    @Override
    public InteractiveTile getDestroyedForm() {
        
        InteractiveTile tile = new IT_Trunk(gp, worldX / gp.tileSize, worldY / gp.tileSize);
        return tile;
    }
    
    @Override
    public Color getParticleColor() {
        
        Color color = new Color(65, 50, 30);
        return color;
    }
    
    @Override
    public int getParticleSize() {
        
        int size = 6; //6 pixeis
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
