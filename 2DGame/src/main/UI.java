package main;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import object.OBJ_Key;

//MOSTRA COISAS NA TELA
public class UI {
    
    GamePanel gp;
    Graphics2D g2;
    Font arial_40, arial_80B;
//    BufferedImage keyImage;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public boolean gameFinished = false;
    
    double playTime;
    DecimalFormat dFormat = new DecimalFormat("#0.00"); //FORMATAR O PLAYTIME OU UM DOUBLE QUALQUER
    
    public UI(GamePanel gp) {
    
        this.gp = gp;
        
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);
    }
    
    public void showMessage(String text) {
    
        message = text;
        messageOn = true;
    }
    
    public void draw(Graphics2D g2) {
        
        this.g2 = g2;
        
        g2.setFont(arial_40);
        g2.setColor(Color.white);
        
        if(gp.gameState == gp.playState) {
            //Playstate stuff implement later
            
        }
        if(gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }
    }
    
    public void drawPauseScreen() {
        
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,80F));
        String text = "PAUSED";
        int x = getXCenteredText(text);
        int y = gp.screenHeight/2;
        
        g2.drawString(text, x, y);
    }
    
    public int getXCenteredText(String text) { //CENTRALIZAR O X NA TELA
       
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth/2 - length/2;
        return x;
    }
}

