package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

public class Player extends Entity {
    
    GamePanel gp;
    KeyHandler keyH;
    
    public final int screenX; //ONDE DESENHAREMOS O PLAYER? QUEREMOS ELE NO CENTRO DA TELA
    public final int screenY;
    public int hasKey = 0; //Quantidade de chaves pegas
    int standCounter = 0; //AJUSTAR O TIMING DO RELEASE BUTTON
    boolean moving = false;
    int pixelCounter = 0;
    
    public Player(GamePanel gp, KeyHandler keyH) {
    
        this.gp = gp;
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
        
        //MOVIMENTACAO TILE/GRID "COLISAO"
        /*solidArea = new Rectangle(); //Rectangle(x, y, width, height)
        solidArea.x = 1;
        solidArea.y = 1;
        solidAreaDefaultX = solidArea.x; //SALVANDO OS VALORES DE SOLIDAREAX E Y, CASO SEJAM MODIFICADAS NO FUTURO
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 46;
        solidArea.height = 46;*/
        
        //------//
        
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
    
        up1 = setup("boy_up_1");
        up2 = setup("boy_up_2");
        down1 = setup("boy_down_1");
        down2 = setup("boy_down_2");
        left1 = setup("boy_left_1");
        left2 = setup("boy_left_2");
        right1 = setup("boy_right_1");
        right2 = setup("boy_right_2");
        
    }
    
    public BufferedImage setup(String imageName) {
        
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        
        try {
            
            image = ImageIO.read(getClass().getResourceAsStream("/player/" + imageName +".png"));
            image = uTool.scaledImage(image, gp.tileSize, gp.tileSize);
        }catch(IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    
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
            
            //VERIFICAR COLISAO COM OS OBJECTOS
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);
            
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
        
            String objectName = gp.obj[i].name;
            switch(objectName) {
                case "Key": 
                    hasKey++;
                    gp.playSE(1);
                    gp.obj[i] = null; //DELETA O OBJECTO TOCADO
                    gp.ui.showMessage("Chave coletada");
                    break;
                case "Door":
                    if(hasKey > 0) {
                        gp.playSE(3);
                        gp.obj[i] = null;
                        hasKey--;
                        gp.ui.showMessage("Abriste mais uma porta");
                    }
                    else {
                        gp.ui.showMessage("Precisas de uma chave Mingous");
                    }
                    break;
                case "Boots":
                    gp.playSE(2);
                    speed += 2;
                    gp.obj[i] = null;
                    gp.ui.showMessage("Velocidade aumentada");
                    break;
                case "Chest": //Quando o player chegar no bau precisamos parar o jogo
                    gp.ui.gameFinished = true;
                    gp.stopMusic();
                    gp.playSE(4);
                    break;
            }
        }
    }

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
        
        //COLLISION VIEWER
        g2.setColor(Color.red);
        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        // --------------- //
    }
}
