package main;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

//MOSTRA COISAS NA TELA
public class UI {
    
    GamePanel gp;
    Graphics2D g2;
    Font maruMonica, purisaB;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public boolean gameFinished = false;
    public String currentDialogue = "";
    
    public UI(GamePanel gp) {
    
        this.gp = gp;
        
        try {
            InputStream is = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf"); //PEGAR FONTES PERSONALIZADAS NOD FICHEIROS DO JOGO
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
            
            is = getClass().getResourceAsStream("/font/Purisa Bold.ttf");
            purisaB = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showMessage(String text) {
    
        message = text;
        messageOn = true;
    }
    
    public void draw(Graphics2D g2) {
        
        this.g2 = g2;
        
        g2.setFont(maruMonica);
//        g2.setFont(purisaB);
//        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); //DEIXA O TEXTO MAIS LISO
        g2.setColor(Color.white);
        
        
        //PLAY STATE
        if(gp.gameState == gp.playState) {
            //Playstate stuff implement later
            
        }
        
        //PAUSE STATE
        if(gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }
        
        //DIALOGUE STATE
        if(gp.gameState == gp.dialogueState) {
            drawDialogueScreen();
        }
    }

    public void drawPauseScreen() {
        
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,80F));
        String text = "PAUSED";
        int x = getXCenteredText(text);
        int y = gp.screenHeight/2;
        
        g2.drawString(text, x, y);
    }
    
    public void drawDialogueScreen() { //DISPLAY TEXTS
        
        //WINDOW
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 4;
        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));
        x += gp.tileSize;
        y += gp.tileSize;
        
        for(String line : currentDialogue.split("\n")) { //QUANDO TIVER A KEYWORD(\n ou qualquer outra) O TEXTO SERA QUEBRADO E SERA IMPRIMIDO A OUTRA PARTE COM UMA DISTANCIA DE 40(y)
            
            g2.drawString(line, x, y);
            y += 40;
        }
    }
    
    public void drawSubWindow(int x, int y, int width, int height) {
        
        Color c = new Color(0, 0, 0, 210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35); // fillRoundRect(w, y, width, height, arcwidth(arredondamento em width), acrheight(arredondamento em height)
   
        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5)); // -> Define o width/expessura das linhas de fora do grafico renderizado com Graphics2D
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10 , 25, 25);
    }
    
    public int getXCenteredText(String text) { //PEGAR O X CENTRALIZADO NA TELA
       
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth/2 - length/2;
        return x;
    }
}

