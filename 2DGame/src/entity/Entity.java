package entity;

import java.awt.image.BufferedImage;

public class Entity {
    
    public int x, y;
    public int speed;
    
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2; //BufferedImage basicamente descreve a imagem com um buffer acessivel para dados de imagem
    public String direction;
    
    public int spriteCounter = 0;
    public int spriteNum = 1;
}
