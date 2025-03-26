package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
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
    public boolean onPath = false;
    public boolean knockBack = false;
    
    //CONTADORES
    public int spriteCounter = 0;
    public int actionLockCounter = 0;
    public int invincibleCounter;
    public int shotAvailableCounter = 0;
    int dyingCounter = 0;
    int hpBarCounter = 0;
    int knockBarCounter = 0;
    
    //ATRIBUTOS DO PERSONAGEM
    public String name;
    public int defaultSpeed;
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
    public Entity currentLight;
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
    public int knockBackPower = 0;
    public boolean stackable = false;
    public int amount = 1;
    public int lightRadius;
    
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
    public final int type_obstacle = 8;
    public final int type_light = 9;
     
    public Entity(GamePanel gp) {
        this.gp = gp;
    }
    
    public int getLeftX() {
        return worldX + solidArea.x;
    }
    
    public int getRightX() {
        return worldX + solidArea.x + solidArea.width;
    }
    
    public int getTopY() {
        return worldY + solidArea.y;
    }
    
    public int getBottomY() {
        return worldY + solidArea.y + solidArea.height;
    }
    
    public int getCol() {
        return (worldX + solidArea.x)/gp.tileSize;
    }
    
    public int getRow() {
        return (worldY + solidArea.y)/gp.tileSize;
    }
    
    public int getXdistance(Entity target) {
        int xDistance = Math.abs(worldX - target.worldX);
        return xDistance;
    }
    
    public int getYdistance(Entity target) {
        int yDistance = Math.abs(worldY - target.worldY);
        return yDistance;
    }
    
    public int getTileDistance(Entity target) {
        int tileDistance = (getXdistance(target) + getYdistance(target))/gp.tileSize;
        return tileDistance;
    }
    
    public int getGoalCol(Entity target) {
        int goalCol = (target.worldX + target.solidArea.x)/gp.tileSize;
        return goalCol;
    }
    
    public int getGoalRow(Entity target) {
        int goalRow = (target.worldY + target.solidArea.y)/gp.tileSize;
        return goalRow;
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
    
    public void interact() {
        
    }
    
    public boolean use(Entity entity) {
        
        return false;
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
    
    public void checkCollision() {
        
        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);
        gp.cChecker.checkEntity(this, gp.iTile);
        boolean contactPlayer = gp.cChecker.checkPlayer(this);
        
        //MONSTROS DAREM DANO AO PLAYER QUANDO ENCOSTAM NELE
        if (type == type_monster && contactPlayer == true) {
            
            damagePlayer(attack);
        }
    }
    
    public void update() {
        
        if(knockBack == true) {
            
            checkCollision();
            
            if(collisionOn == true) {
                knockBarCounter = 0;
                knockBack = false;
                speed = defaultSpeed;
            }
            else if(collisionOn == false) {
                
                switch(gp.player.direction) {
                    
                    case "up":
                        worldY -= speed;
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
            knockBarCounter++;
            if(knockBarCounter == 10) {
                knockBarCounter = 0;
                knockBack = false;
                speed = defaultSpeed;
            }
        }
        else {
            
            setAction();
            checkCollision();

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
    
    public void checkShootOrNot(int rate, int shootInterval) {
        
        int i = new Random().nextInt(rate);
        if(i == 0 && projectile.alive == false && shotAvailableCounter == shootInterval) {

            projectile.set(worldX, worldY, direction, true, this);

            // VERIFICAR DISPONIBILIDADE
            for(int ii = 0; ii < gp.projectile[1].length; ii++) { //VERIFICA QUAL SLOT TEM DISPONIBILIDADE PARA O PROJECTIL
                if(gp.projectile[gp.currentMap][ii] == null) {
                    gp.projectile[gp.currentMap][ii] = projectile;
                    break;
                }
            }
            shotAvailableCounter = 0;
        }
    }
    
    public void checkStartChasingOrNot(Entity target, int distance, int rate) {
        
        if(getTileDistance(target) < distance) {
            int i = new Random().nextInt(rate); // -> 0 à (rate - 1) ** 60 vezes por segundo ocorre a verificacao
            if(i == 0) {
                onPath = true;
            }
        }
    }
    
    public void checkStopChasingOrNot(Entity target, int distance, int rate) {
        
        if(getTileDistance(target) > distance) {
            int i = new Random().nextInt(rate); // -> 0 à (rate - 1) ** 60 vezes por segundo ocorre a verificacao
            if(i == 0) {
                onPath = false;
            }
        }
    }
    
    public void getRandomDirection() {
        
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
    
    public void searchPath(int goalCol, int goalRow) {
        
        int startCol = (worldX + solidArea.x)/gp.tileSize;
        int startRow = (worldY + solidArea.y)/gp.tileSize;
        
        gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow);
        
        if(gp.pFinder.search() == true) {
            
            //PROXIMO WORLDX E WORLDY
            int nextX = gp.pFinder.pathList.get(0).col * gp.tileSize;
            int nextY = gp.pFinder.pathList.get(0).row * gp.tileSize;
            
            //POSICAO DA AREA SOLIDA DA ENTIDADE 
            int enLeftX = worldX + solidArea.x;
            int enRightX = worldX + solidArea.x + solidArea.width;
            int enTopY = worldY + solidArea.y;
            int enBottomY = worldY + solidArea.y + solidArea.height;
            
            if(enTopY > nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize) {
                direction = "up";
            }
            else if(enTopY < nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize) {
                direction = "down";
            }
            else if(enTopY >= nextY && enBottomY < nextY + gp.tileSize) {
                //LEFT OU RIGHT
                if(enLeftX > nextX) {
                    direction = "left";
                }
                if(enLeftX < nextX) {
                    direction = "right";
                }
            }
            else if(enTopY > nextY && enLeftX > nextX) {
                //UP OU LEFT
                direction = "up";
                checkCollision();
                if(collisionOn == true) {
                    direction = "left";
                }
            }
            else if(enTopY > nextY && enLeftX < nextX) {
                //UP OU RIGHT
                direction = "up";
                checkCollision();
                if(collisionOn == true) {
                    direction = "right";
                }
            }
            else if(enTopY < nextY && enLeftX > nextX) {
                //DOWN OU LEFT
                direction = "down";
                checkCollision();
                if(collisionOn == true) {
                    direction = "left";
                }
            }
            else if(enTopY < nextY && enLeftX < nextX) {
                //DOWN OU RIGHT
                direction = "down";
                checkCollision();
                if(collisionOn == true) {
                    direction = "right";
                }
            }
            
            //QUANDO ATINGIR O OBJECTIVO, PARAR A "PROCURA"
//            int nextCol = gp.pFinder.pathList.get(0).col;
//            int nextRow = gp.pFinder.pathList.get(0).row;
//            if(nextCol == goalCol && nextRow == goalRow) {
//                onPath = false;
//            }
        }
    }
    
    public int getDetected(Entity user, Entity target[][], String targetName) {
        
        int index = 999;
        
        //VERIFICAR OS OBJECTOS EM VOLTA
        int nextWorldX = user.getLeftX();
        int nextWorldY = user.getTopY();
        
        switch(user.direction) {
            
            case "up": 
                nextWorldY= user.getTopY() - 1;
                break;
            case "down":
                nextWorldY = user.getBottomY() + 1;
                break;
            case "left":
                nextWorldX = user.getLeftX() - 1;
                break;
            case "right":
                nextWorldX = user.getRightX() + 1;
                break;
        }
        
        int col = nextWorldX/gp.tileSize;
        int row = nextWorldY/gp.tileSize;
        
        for(int i = 0; i < target[1].length; i++) {
            if(target[gp.currentMap][i] != null) {
                if(target[gp.currentMap][i].getCol() == col &&
                        target[gp.currentMap][i].getRow() == row &&
                        target[gp.currentMap][i].name.equals(targetName)) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }
}
