package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {
    
    GamePanel gp;
    KeyHandler keyH;
    
    public final int screenX; //ONDE DESENHAREMOS O PLAYER? QUEREMOS ELE NO CENTRO DA TELA
    public final int screenY;
    
    public Player(GamePanel gp, KeyHandler keyH) {
    
        this.gp = gp;
        this.keyH = keyH;
        
        // CENTRALIZANDO O PLAYER
        screenX = gp.screenWidth/2 - gp.tileSize/2;
        screenY = gp.screenHeight/2 - gp.tileSize/2;
        //-----------------------//
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
    
        try {
        
            up1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_2.png"));
            
        }catch(IOException e) { 
            e.printStackTrace();
        }
    }
    
    public void update() {

        //ATUALIZAR AS COORDENADAS DO JOGADOR
        if(keyH.upPressed == true || keyH.downPressed == true 
                || keyH.leftPressed == true || keyH.rightPressed == true) { // Previne da imagem se mexer sem dar nenhum comando
            if (keyH.upPressed == true) {
                direction = "up";
                worldY -= speed; // QUANTO MAIS PRA CIMA MENOS O Y VALE MENOS EM 4 PIXELS(SPEED)
            }
            else if(keyH.downPressed == true) {
                direction = "down";
                worldY += speed;
            }
            else if(keyH.leftPressed == true) {
                direction = "left";
                worldX -= speed;
            }
            else if(keyH.rightPressed == true) {
                direction = "right";
                worldX += speed;
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
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null); //drawImage(imagem, x, y, width, height, ImageObserver) DESENHA UMA IMAGEM
    }
}
