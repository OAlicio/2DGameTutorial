package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import main.GamePanel;

public class Particle extends Entity{

    Entity generator;
    Color color;
    int size; //Tamanho da particula
    int xd; //direcao que a particula tera
    int yd; //direcao que a particula tera
    
    public Particle(GamePanel gp, Entity generator, Color color, int size, 
            int speed, int maxLife, int xd, int yd) {
        
        super(gp);
        
        this.generator = generator;
        this.color = color;
        this.size = size;
        this.speed = speed;
        this.maxLife = maxLife;
        this.xd = xd;
        this.yd = yd;
        
        life = maxLife;
        int offset = (gp.tileSize / 2) - (size / 2); //AJUSTA O POSICIONAMENTO INICIAL DAS PARTICULAS
        worldX = generator.worldX + offset;
        worldY = generator.worldY + offset;
    }

    @Override
    public void update() {
        
        life--;
        
        if(life < maxLife / 4) {
            yd++;
        }
        
        worldX += xd * speed;
        worldY += yd * speed;
        
        if(life == 0) {
            alive = false;
        }
    }
    
    public void draw(Graphics2D g2) {
        
        int screenX = worldX - gp.player.worldX + gp.player.screenX; //POSICIONAMENTO DAS PARTICULAS
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        
        g2.setColor(color);
        g2.fillRect(screenX, screenY, size, size);
    }
    
}
