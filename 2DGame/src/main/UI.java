package main;


import entity.Entity;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;

//MOSTRA COISAS NA TELA
public class UI {
    
    GamePanel gp;
    Graphics2D g2;
    Font maruMonica, purisaB;
    BufferedImage heart_full, heart_half, heart_blank, crystal_full, crystal_blank;
    public boolean messageOn = false;
//    public String message = "";
//    int messageCounter = 0;
    ArrayList<String> message= new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public boolean gameFinished = false;
    public String currentDialogue = "";
    public int commandNum = 0;
    public int titleScreenState = 0;
    public int slotCol = 0;
    public int slotRow = 0;
    
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
        
        //HUD OBJECTS
        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;
        
        Entity crystal = new OBJ_ManaCrystal(gp);
        crystal_full = crystal.image;
        crystal_blank = crystal.image2;
        
    }
    
    public void addMessage(String text) {
    
        message.add(text);
        messageCounter.add(0);
    }
    
    public void draw(Graphics2D g2) {
        
        this.g2 = g2;
        
        g2.setFont(maruMonica);
//        g2.setFont(purisaB);
//        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); //DEIXA O TEXTO MAIS LISO
        g2.setColor(Color.white);
        
        
        //TITLE STATE
        if(gp.gameState == gp.titleState) {
            drawTitleScreen();
        }
        
        //PLAY STATE
        if(gp.gameState == gp.playState) {
            //Playstate stuff implement later
            drawPlayerLife();
            drawMessage();
        }
        
        //PAUSE STATE
        if(gp.gameState == gp.pauseState) {
            drawPlayerLife();
            drawPauseScreen();
        }
        
        //DIALOGUE STATE
        if(gp.gameState == gp.dialogueState) {
            drawPlayerLife();
            drawDialogueScreen();
        }
        
        //CHARACTER STATE
        if(gp.gameState == gp.characterState) {
            drawCharacterScreen();
            drawInventory();
        }
    }

    public void drawPlayerLife() {
        
        int x = gp.tileSize/2;
        int y = gp.tileSize/2;
        int i = 0;
        
        //DESENHA O LIMITE DE CORACOES COMO BRANCOS/VAZIOS
        while(i < gp.player.maxLife/2) {
            g2.drawImage(heart_blank, x, y, null);
            i++;
            x += gp.tileSize;
        }
        
        //RESET
        x = gp.tileSize/2;
        y = gp.tileSize/2;
        i = 0;
        
        //DESENHA A VIDA ATUAL / COLORINDO OS CORACOES BRANCOS
        while(i < gp.player.life) {
            g2.drawImage(heart_half, x, y, null);
            i++;
            if(i < gp.player.life) {
                g2.drawImage(heart_full, x, y, null);
            }
            i++;
            x += gp.tileSize;
        }
        
        //DESENHAR A MANA VAZIA
        x = (gp.tileSize/2) - 5;
        y = (int)(gp.tileSize * 1.5);
        i = 0;
        while(i < gp.player.maxMana) {
            g2.drawImage(crystal_blank, x, y, null);
            i++;
            x += 35;
        }
        
        //DESENHAR A MANA CHEIA
        x = (gp.tileSize/2) - 5;
        y = (int)(gp.tileSize * 1.5);
        i = 0;
        while(i < gp.player.mana) {
            g2.drawImage(crystal_full, x, y, null);
            i++;
            x += 35;
        }
    }
    
    public void drawMessage() {
        
        int messageX = gp.tileSize;
        int messageY = gp.tileSize * 4;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));
        
        for(int i= 0; i < message.size(); i++) {
            
            if(message.get(i) != null) {
                
                g2.setColor(new Color(0, 0, 0, 220));
                g2.drawString(message.get(i), messageX + 2, messageY + 2);
                g2.setColor(Color.white);
                g2.drawString(message.get(i), messageX, messageY);
                
                int counter = messageCounter.get(i) + 1; //MESSAGECOUNTER++ (ARRAY LIST)
                messageCounter.set(i, counter); // COLOCAR O CONTADOR AO ARRAY
                messageY += 50;
                
                if(messageCounter.get(i) > 180) {
                    message.remove(i);
                    messageCounter.remove(i);
                }
            }
        }
    }
    
    public void drawTitleScreen() {
        
        if(titleScreenState == 0) {
            
            //BACKGROUND COLOR
            g2.setColor(new Color(0, 0, 0));
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

            //TITLE NAME
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
            String text = "Blue Boy Adventure";
            int x = getXCenteredText(text);
            int y = gp.tileSize * 3;

            //TEXT SHADOW
            g2.setColor(new Color(210, 210, 210, 210));
            g2.drawString(text, x + 3, y + 5);

            //MAIN TEXT COLOR
            g2.setColor(Color.white);
            g2.drawString(text, x, y);

            //CHARACTER IMAGE
            x = gp.screenWidth / 2 - (gp.tileSize * 2)/2;
            y += gp.tileSize * 2;
            g2.drawImage(gp.player.down1, x, y, gp.tileSize * 2, gp.tileSize * 2, null);

            //MENU OPTIONS
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));

            text = "NEW GAME";
            x = getXCenteredText(text);
            y += gp.tileSize * 3.5;
            g2.drawString(text, x, y);
            if(commandNum == 0) {
                g2.drawString(">", x - gp.tileSize, y);
            }

            text = "LOAD GAME";
            x = getXCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if(commandNum == 1) {
                g2.drawString(">", x - gp.tileSize, y);
            }

            text = "QUIT";
            x = getXCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if(commandNum == 2) {
                g2.drawString(">", x - gp.tileSize, y);
            }
        }
        else if(titleScreenState == 1) {
            
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(42F));
            
            String text = "Select your class";
            int x = getXCenteredText(text);
            int y = gp.tileSize * 3;
            g2.drawString(text, x, y);
            
            text = "Fighter";
            x = getXCenteredText(text);
            y += gp.tileSize * 3;
            g2.drawString(text, x, y);
            if(commandNum == 0) {
                g2.drawString(">", x - gp.tileSize, y);
            }
            
            text = "Thief";
            x = getXCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if(commandNum == 1) {
                g2.drawString(">", x - gp.tileSize, y);
            }
            
            text = "Sorcerer";
            x = getXCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if(commandNum == 2) {
                g2.drawString(">", x - gp.tileSize, y);
            }
            
            text = "back";
            x = getXCenteredText(text);
            y += gp.tileSize * 2;
            g2.drawString(text, x, y);
            if(commandNum == 3) {
                g2.drawString(">", x - gp.tileSize, y);
            }
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
    
    public void drawCharacterScreen() {
         
        //CRIAR UM FRAME
        
        final int frameX = gp.tileSize;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.tileSize * 5;
        final int frameHeight = (gp.tileSize * 10) + gp.tileSize/2;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        
        //TEXTO
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));
        
        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;
        final int lineHeight = 36;
        
        // NAMES
        g2.drawString("Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Life", textX, textY);
        textY += lineHeight;
        g2.drawString("Mana", textX, textY);
        textY += lineHeight;
        g2.drawString("Strength", textX, textY);
        textY += lineHeight;
        g2.drawString("Dexterity", textX, textY);
        textY += lineHeight;
        g2.drawString("Attack", textX, textY);
        textY += lineHeight;
        g2.drawString("Defense", textX, textY);
        textY += lineHeight;
        g2.drawString("Exp", textX, textY);
        textY += lineHeight;
        g2.drawString("Next Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Coin", textX, textY);
        textY += lineHeight + 10;
        g2.drawString("Weapon", textX, textY);
        textY += lineHeight + 15;
        g2.drawString("Shield", textX, textY);
        textY += lineHeight;
        
        //VALORES
        int tailX = (frameX + frameWidth) - 30;
        
        //RESETAR TEXTY
        textY = frameY + gp.tileSize;
        String value;
        
        value = String.valueOf(gp.player.level);
        textX = getXAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        
        value = String.valueOf(gp.player.life + "/" + gp.player.maxLife);
        textX = getXAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        
        value = String.valueOf(gp.player.mana + "/" + gp.player.maxMana);
        textX = getXAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        
        value = String.valueOf(gp.player.strength);
        textX = getXAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        
        value = String.valueOf(gp.player.dexterity);
        textX = getXAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        
        value = String.valueOf(gp.player.attack);
        textX = getXAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        
        value = String.valueOf(gp.player.defense);
        textX = getXAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        
        value = String.valueOf(gp.player.exp);
        textX = getXAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        
        value = String.valueOf(gp.player.nextLevelExp);
        textX = getXAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        
        value = String.valueOf(gp.player.coin);
        textX = getXAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        
        g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tileSize, textY - 24, null);
        textY += gp.tileSize;
        
        g2.drawImage(gp.player.currentShield.down1, tailX - gp.tileSize, textY - 24, null);
        
    }
    
    public void drawInventory() {
        
        //TELA
        int frameX = gp.tileSize * 9;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 6;
        int frameHeight = gp.tileSize * 5;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        
        //SLOT
        final int slotXStart = frameX + 20;
        final int slotYStart = frameY + 20;
        int slotX = slotXStart;
        int slotY = slotYStart;
        int slotSize = gp.tileSize + 3;
        
        //DESENHAR OS ITENS DO PLAYER
        for(int i = 0; i < gp.player.inventory.size(); i++) {
            
            //CURSOR DE EQUIPAMENTO
            if(gp.player.inventory.get(i) == gp.player.currentWeapon || 
                    gp.player.inventory.get(i) == gp.player.currentShield) {
                
                g2.setColor(new Color(240, 190, 90));
                g2.fillRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);
            } 
            
            g2.drawImage(gp.player.inventory.get(i).down1, slotX, slotY, null);
            
            slotX += slotSize;
            
            if(i == 4 || i == 9 || i == 14) {
                slotX = slotXStart;
                slotY += gp.tileSize;
            }
        }
        
        
        //CURSOR
        int cursorX = slotXStart + (slotSize * slotCol);
        int cursorY = slotYStart + (slotSize * slotRow);
        int cursorWidth = gp.tileSize;
        int cursorHeight = gp.tileSize;
        
        //DESENHAR O CURSOR
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3)); //DIMINUI A GROSSURA DO OBJECTO DESENHADO
        g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);
        
        //TELA DE DESCRICAO DOS ITENS
        int dFrameX = frameX;
        int dFrameY = frameY + frameHeight;
        int dFrameWidth = frameWidth;
        int dFrameHeight = gp.tileSize * 3;
        
        
        //DESENHAR AS DESCRICOES DOS ITENS
        int textX = dFrameX + 20;
        int textY = dFrameY + gp.tileSize;
        g2.setFont(g2.getFont().deriveFont(28F));
        
        int itemIndex = getItemIndexSlot();
        
        if(itemIndex < gp.player.inventory.size()) {
            
            drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight); //TELA DE DESCRICAO SO APARECE QUANDO TEM UM ITEM
            
            for(String line : gp.player.inventory.get(itemIndex).description.split("\n")) {
                
                g2.drawString(line, textX, textY);
                textY += 32;
            }
        }
    }
    
    public int getItemIndexSlot() {
        
        int itemIndex = slotCol + (slotRow * 5);
        return itemIndex;
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

    public int getXAlignToRightText(String text, int tailX) { //PEGAR O X CENTRALIZADO NA TELA
       
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX - length;
        return x;
    }
}

