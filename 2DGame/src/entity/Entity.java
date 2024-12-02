package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Entity {
    
    public int worldX, worldY; //POSICAO DA ENTIDADE
    public int speed;
    
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2; //BufferedImage basicamente descreve a imagem com um buffer acessivel para dados de imagem
    public String direction;
    
    public int spriteCounter = 0;
    public int spriteNum = 1;
    
    //CRIANDO O COLISOR DAS ENTIDADES, UM RECTANGULO
    public Rectangle solidArea;
    public boolean collisionOn = false;
}
