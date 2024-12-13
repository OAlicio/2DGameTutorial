package entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.UtilityTool;

public class Entity {
    
    GamePanel gp;
    
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2; //BufferedImage basicamente descreve a imagem com um buffer acessivel para dados de imagem
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, 
            attackLeft1, attackLeft2, attackRight1, attackRight2;
    public BufferedImage image, image2, image3;
    
    //CRIANDO O COLISOR DAS ENTIDADES, UM RECTANGULO
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48); //AREA SOLIDA DEFAULT
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collision = false;
    String dialogue[] = new String[20]; //PARA OS DIALOGOS COM NPC's
    
    //ESTADOS
    public int worldX, worldY; //POSICAO DA ENTIDADE
    public String direction = "down";
    public int spriteNum = 1;
    int dialogueIndex = 0; 
    public boolean collisionOn = false;
    public boolean invincible = false;
    boolean attacking = false;
    
    //CONTADORES
    public int spriteCounter = 0;
    public int actionLockCounter = 0;
    public int invincibleCounter;
    
    //CHARACTER STATUS
    public String name;
    public int speed;
    public int maxLife;
    public int life;
    public int type; // ex: 0 = player, 1 = npc, 2 = monster, etc
    
    public Entity(GamePanel gp) {
        this.gp = gp;
    }
    
    public void setAction() {
        
    }
    
    public void speak() {
        
        if(dialogue[dialogueIndex] == null) {
            
            dialogueIndex = 0;
        }
                
        gp.ui.currentDialogue = dialogue[dialogueIndex];
        dialogueIndex += 1;
        
        switch(gp.player.direction) {
            
            case "up":
                direction = "down";
                break;
                
            case "down":
                direction = "up";
                break;
                
            case "left":
                direction = "right";
                break;
                
            case "right":
                direction = "left";
                break;
        }
    }
    
    public void update() {
        
        setAction();
        
        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);
        boolean contactPlayer = gp.cChecker.checkPlayer(this);
        
        //MONSTROS DAREM DANO AO PLAYER QUANDO ENCOSTAM NELE
        if (this.type == 2 && contactPlayer == true) {
            
            if(gp.player.invincible == false) {
                //DAR DANO AO PLAYER
                gp.player.life -= 1;
                gp.player.invincible = true;
            }
        }
        
         // SE A COLISAO FOR FALSA, O PLAYER NAO SE MOVE
        if(collisionOn == false) {

            switch(direction) {

                case "up":
                     worldY -= speed; // QUANTO MAIS PRA CIMA MENOS O Y VALE MENOS EM 4 PIXELS(SPEED)
                    break;
                case "down":
                    worldY += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;
            }
        }
        spriteCounter++;
        if(spriteCounter >= 14) { //A CADA 14 FRAMES TROCA A IMAGEM PELO SPRITENUM
            if(spriteNum == 1){
                spriteNum = 2;
            }
            else if(spriteNum == 2){
                spriteNum = 1;
            }
            spriteCounter = 0;
        }

        if(invincible == true) {
            invincibleCounter++;
            if(invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }
    
    public void draw(Graphics2D g2) {
        
        BufferedImage image = null;
        
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
            
        //EVITANDO RENDERIZAR EM TILES NAO VISIVEIS
        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && 
           worldX - gp.tileSize < gp.player.worldX + gp.player.screenX && 
           worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && 
           worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                
            switch(direction) {
        case "up":
            if(spriteNum == 1) {
                image = up1;
            }
            if(spriteNum == 2) {
                image = up2;
            }  
            break;
        case "down":
            if(spriteNum == 1) {
                image = down1;
            }
            if(spriteNum == 2) {
                image = down2;
            }  
            break;
        case "left":
            if(spriteNum == 1) {
                image = left1;
            }
            if(spriteNum == 2) {
                image = left2;
            }  
            break;
        case "right":
            if(spriteNum == 1) {
                image = right1;
            }
            if(spriteNum == 2) {
                image = right2;
            }
            break; 
        }
            
        if(invincible == true) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f /*O QUAO TRANSPARENTE FICARA A IMAGEM*/));
        }
        
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            //RESET ALPHA 
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    }
   
    
    public BufferedImage setup(String imagePath, int width, int height) {
        
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        
        try {
            
            image = ImageIO.read(getClass().getResourceAsStream(imagePath +".png"));
            image = uTool.scaledImage(image, width, height);
        }catch(IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    
}
