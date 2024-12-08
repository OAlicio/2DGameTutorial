package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {
    
    KeyHandler keyH;
    
    public final int screenX; //ONDE DESENHAREMOS O PLAYER? QUEREMOS ELE NO CENTRO DA TELA
    public final int screenY;
    int standCounter = 0; //AJUSTAR O TIMING DO RELEASE BUTTON
    boolean moving = false;
    int pixelCounter = 0;
    
    public Player(GamePanel gp, KeyHandler keyH) {
    
        super(gp); //ATRIBUI GP AO CONSTRUTOR DA CLASSE MAE
        
        this.keyH = keyH;
        
        // CENTRALIZANDO O PLAYER
        screenX = gp.screenWidth/2 - gp.tileSize/2;
        screenY = gp.screenHeight/2 - gp.tileSize/2;
        //-----------------------//
        
        //COLISAO
        solidArea = new Rectangle(); //Rectangle(x, y, width, height)
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x; //SALVANDO OS VALORES DE SOLIDAREAX E Y, CASO SEJAM MODIFICADAS NO FUTURO
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;
        //------------//
        
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
    
        //POSICOES INICIAIS DO PLAYER
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        // ------------------------ //
        speed = 4;
        direction = "down"; //DIRECAO PADRAO PODE SER QUALQUER UMA
    }
    
        
    public void getPlayerImage() {
    
        up1 = setup("/player/boy_up_1");
        up2 = setup("/player/boy_up_2");
        down1 = setup("/player/boy_down_1");
        down2 = setup("/player/boy_down_2");
        left1 = setup("/player/boy_left_1");
        left2 = setup("/player/boy_left_2");
        right1 = setup("/player/boy_right_1");
        right2 = setup("/player/boy_right_2");
        
    }
        
    @Override
    public void update() {
        
        //ATUALIZAR AS COORDENADAS DO JOGADOR
        if(keyH.upPressed == true || keyH.downPressed == true 
                || keyH.leftPressed == true || keyH.rightPressed == true) { // Previne da imagem se mexer sem dar nenhum comando
            if (keyH.upPressed == true) {
                direction = "up";
            }
            else if(keyH.downPressed == true) {
                direction = "down";
            }
            else if(keyH.leftPressed == true) {
                direction = "left";
            }
            else if(keyH.rightPressed == true) {
                direction = "right";
            }
            
            //VERIFICAR COLISAO COM OS TILES
            collisionOn = false;
            gp.cChecker.checkTile(this);
            // ---------------------------- //
            
            //VERIFICAR COLISAO COM OS OBJECTOS
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);
            // ------------------------------ //
            
            //VERIFICAR COLISAO COM NPC
            
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);
            
            // ---------------------- //
            
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
        }
        else { //COLOCANDO O PLAYER NUMA POSICAO NORMAL
        
            standCounter++;
            
            if(standCounter == 20) { //A CADA x FRAMES O PLAYER VOLTA A POSICAO INICIAL APOS O BOTAO DEIXAR DE SER PRESSIONADO
               standCounter = 0;
               spriteNum = 1; 
            }
        }
       
    }
    
    public void pickUpObject (int i) {
        
        if(i != 999) {
        
        }
    }
    
    public void interactNPC(int i) {
        
        if(i != 999) {
            
            if(gp.keyH.dialogueKey == true) {
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            }
        }
        gp.keyH.dialogueKey = false;
    }

    @Override
    public void draw(Graphics2D g2) {
        
        BufferedImage image = null;
        //CENTRALIZANDO O PLAYER
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
        g2.drawImage(image, screenX, screenY, null); //drawImage(imagem, x, y, width, height, ImageObserver) DESENHA UMA IMAGEM
//        g2.setColor(Color.red);
//        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        // --------------- //
    }
}
