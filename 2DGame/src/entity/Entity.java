package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
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
    public boolean alive = true;
    public boolean dying = false;
    boolean hpBarOn = false;
    
    //CONTADORES
    public int spriteCounter = 0;
    public int actionLockCounter = 0;
    public int invincibleCounter;
    public int shotAvailableCounter = 0;
    int dyingCounter = 0;
    int hpBarCounter = 0;
    
    //ATRIBUTOS DO PERSONAGEM
    public String name;
    public int speed;
    public int maxLife;
    public int life;
    public int maxMana;
    public int mana;
    public int ammo;
    public int level;
    public int strength;
    public int dexterity;
    public int attack;
    public int defense;
    public int exp;
    public int nextLevelExp;
    public int coin;
    public Entity currentWeapon;
    public Entity currentShield;
    public Projectile projectile;
    
    //ATRIBUTOS DOS ITENS
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 20; 
    public int value;
    public int attackValue;
    public int defenseValue;
    public String description = "";
    public int useCost;
    public int price;
    
    //TIPOS
    public int type; // ex: 0 = player, 1 = npc, 2 = monster, etc
    public final int type_player = 0;
    public final int type_npc = 1;
    public final int type_monster = 2;
    public final int type_sword = 3;
    public final int type_axe = 4;
    public final int type_shield = 5;
    public final int type_consumable = 6;
    public final int type_pickupOnly = 7;
     
    public Entity(GamePanel gp) {
        this.gp = gp;
    }
    
    public void setAction() {
        
    }
    
    public void damageReaction() {
        
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
    
    public void use(Entity entity) {
        
    }
    
    public void checkDrop() {
        
    }
    
    public void dropItem(Entity droppedItem) {
        
        for(int i = 0; i < gp.obj[1].length; i++) {
            
            if(gp.obj[gp.currentMap][i] == null) {
                gp.obj[gp.currentMap][i] = droppedItem;
                gp.obj[gp.currentMap][i].worldX = worldX;
                gp.obj[gp.currentMap][i].worldY = worldY;
                break; //AO ENCONTRAR UMA ESPACO VAZIO O LOOP TERMINA
            }
        }
    }
    
    public Color getParticleColor() {
        
        Color color = null;
        return color;
    }
    
    public int getParticleSize() {
        
        int size = 0;
        return size;
    }
    
    public int getParticleSpeed() {
        
        int speed = 0; //Velocidade da particula
        return speed;
    }
    
    public int getParticleMaxLife() { //QUANTO TEMPO A PARTICULA DURA
        
        int maxLife = 0;
        return maxLife;
    }
    
    public void generateParticle(Entity generator, Entity target) {

        Color color = generator.getParticleColor();
        int size = generator.getParticleSize();
        int speed = generator.getParticleSpeed();
        int maxLife = generator.getParticleMaxLife();
        
        Particle p1 = new Particle(gp, target, color, size, speed, maxLife, -2, -1); //Colocando target a particula aparecera no objectofinal, colocando generator ela aparecera no objecto que geraria a particula
        Particle p2 = new Particle(gp, target, color, size, speed, maxLife, 2, -1);
        Particle p3 = new Particle(gp, target, color, size, speed, maxLife, -2, 1);
        Particle p4 = new Particle(gp, target, color, size, speed, maxLife, 2, 1);
        gp.particleList.add(p1);
        gp.particleList.add(p2);
        gp.particleList.add(p3);
        gp.particleList.add(p4);
        
    }
    
    public void update() {
        
        setAction();
        
        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);
        gp.cChecker.checkEntity(this, gp.iTile);
        boolean contactPlayer = gp.cChecker.checkPlayer(this);
        
        //MONSTROS DAREM DANO AO PLAYER QUANDO ENCOSTAM NELE
        if (this.type == type_monster && contactPlayer == true) {
            
            damagePlayer(attack);
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
        if(spriteCounter >= 24) { //A CADA 24 FRAMES TROCA A IMAGEM PELO SPRITENUM
            if(spriteNum == 1) {
                spriteNum = 2;
            }
            else if(spriteNum == 2) {
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
        if(shotAvailableCounter < 30) {
            
            shotAvailableCounter++;
        }
    }
    
    public void damagePlayer(int attack) {
        
        if(gp.player.invincible == false) {
                //DAR DANO AO PLAYER
                gp.playSE(6);
                
                int damage = attack - gp.player.defense;
                
                if(damage < 0) {
                    damage = 0;
                }
                
                gp.player.life -= damage;
                gp.player.invincible = true;
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
            
        //Health Bar dos monstros
        if(type == 2 && hpBarOn == true) {
            
            double oneScale = (double) gp.tileSize/maxLife; // |oneScale| |oneScale| |oneScale| |oneScale| -> hpBar
            double hpBarValue = oneScale * life;
            
            //Cria uma linha preta em volta do rectangulo vermelho
            g2.setColor(new Color(35, 35, 35));
            g2.fillRect(screenX - 1, screenY - 16, gp.tileSize + 2, 12);
            
            //Cria o rectangulo vermelho
            g2.setColor(new Color(255, 0 ,30));
            g2.fillRect(screenX, screenY - 15, (int) hpBarValue, 10);
            
            hpBarCounter++;
            
            if(hpBarCounter > 600) {
                hpBarCounter = 0;
                hpBarOn = false;
            }
        }
        
        if(invincible == true) {
            //g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f /*O QUAO TRANSPARENTE FICARA A IMAGEM*/));
            hpBarOn = true;
            hpBarCounter = 0;
            changeAlpha(g2, 0.4f);
        }
        
        if(dying == true) {
            
            dyingAnimation(g2);
        }
        
        g2.drawImage(image, screenX, screenY, null);
            //RESET ALPHA 
        //g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        changeAlpha(g2, 1f);
        }
    }
   
    public void dyingAnimation(Graphics2D g2) {
        
        dyingCounter++;
        
        int i = 5;
        
        if(dyingCounter <= i) {
            //g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f)); -> Ja temos funcao pra isso
            changeAlpha(g2, 0f);
        }
        
        if(dyingCounter > i && dyingCounter <= i * 2) {
            //g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            changeAlpha(g2, 1f);
        }
        
        if(dyingCounter > i * 2 && dyingCounter <= i * 3) {
            //g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f));
            changeAlpha(g2, 0f);
        }
        
        if(dyingCounter > i * 3 && dyingCounter <= i * 4) {
            //g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            changeAlpha(g2, 1f);
        }
        
        if(dyingCounter > i * 4 && dyingCounter <= i * 5) {
            //g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f));
            changeAlpha(g2, 0f);
        }
        
        if(dyingCounter > i * 5 && dyingCounter <= i * 6) {
            //g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            changeAlpha(g2, 1f);
        }
        
        if(dyingCounter > i * 6 && dyingCounter <= i * 7) {
            //g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f));
            changeAlpha(g2, 0f);
        }
        
        if(dyingCounter > i * 7 && dyingCounter <= i * 8) {
            //g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            changeAlpha(g2, 1f);
        }
        
        if(dyingCounter > 40) {
            alive = false;
        }
    }
    
    public void changeAlpha(Graphics2D g2, float alphaValue) {
        
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
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
