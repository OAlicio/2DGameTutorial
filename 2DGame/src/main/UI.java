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
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;

//MOSTRA COISAS NA TELA
public class UI {
    
    GamePanel gp;
    Graphics2D g2;
    Font maruMonica, purisaB;
    BufferedImage heart_full, heart_half, heart_blank, crystal_full, crystal_blank, coin;
    public boolean messageOn = false;
    ArrayList<String> message= new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public boolean gameFinished = false;
    public String currentDialogue = "";
    public int commandNum = 0;
    public int titleScreenState = 0;
    public int playerSlotCol = 0;
    public int playerSlotRow = 0;
    public int npcSlotCol = 0;
    public int npcSlotRow = 0;
    int subState = 0;
    int counter = 0;
    public Entity npc;
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
        
        Entity bronzeCoin = new OBJ_Coin_Bronze(gp);
        coin = bronzeCoin.down1;
        
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
            //drawPlayerLife();
            drawDialogueScreen();
        }
        
        //CHARACTER STATE
        if(gp.gameState == gp.characterState) {
            drawCharacterScreen();
            drawInventory(gp.player, true);
        }
        
        //OPTIONS STATE
        if(gp.gameState == gp.optionsState) {
            drawOptionsScreen();
        }
        
        //GAME OVER STATE
        if(gp.gameState == gp.gameOverState) {
            drawGameOverState();
        }
        
        //TRANSITION STATE
        if(gp.gameState == gp.transitionState) {
            drawTransition();
        }
        
        //TRADE STATE
        if(gp.gameState == gp.tradeState) {
            drawTradeScreen();
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
        
            //BACKGROUND COLOR
            g2.setColor(new Color(0, 0, 0));
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
            
        if(titleScreenState == 0) {
            
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
        int x = gp.tileSize * 3;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 6);
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
        
        final int frameX = gp.tileSize * 2;
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
    
    public void drawInventory(Entity entity, boolean cursor) {
        
        int frameX = 0;
        int frameY = 0;
        int frameWidth = 0;
        int frameHeight = 0;
        int slotCol = 0;
        int slotRow = 0;
        
        if(entity == gp.player) {
            
            frameX = gp.tileSize * 12;
            frameY = gp.tileSize;
            frameWidth = gp.tileSize * 6;
            frameHeight = gp.tileSize * 5;
            slotCol = playerSlotCol;
            slotRow = playerSlotRow;
        } 
        else {
            frameX = gp.tileSize * 2;
            frameY = gp.tileSize;
            frameWidth = gp.tileSize * 6;
            frameHeight = gp.tileSize * 5;
            slotCol = npcSlotCol;
            slotRow = npcSlotRow;
        }
        
        //TELA
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        
        //SLOT
        final int slotXStart = frameX + 20;
        final int slotYStart = frameY + 20;
        int slotX = slotXStart;
        int slotY = slotYStart;
        int slotSize = gp.tileSize + 3;
        
        //DESENHAR OS ITENS DO PLAYER
        for(int i = 0; i < entity.inventory.size(); i++) {
            
            //CURSOR DE EQUIPAMENTO
            if(entity.inventory.get(i) == entity.currentWeapon || 
                    entity.inventory.get(i) == entity.currentShield) {
                
                g2.setColor(new Color(240, 190, 90));
                g2.fillRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);
            } 
            
            g2.drawImage(entity.inventory.get(i).down1, slotX, slotY, null);
            
            slotX += slotSize;
            
            if(i == 4 || i == 9 || i == 14) {
                slotX = slotXStart;
                slotY += gp.tileSize;
            }
        }
        
        if(cursor == true) {
            
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

            int itemIndex = getItemIndexSlot(slotCol, slotRow);

            if(itemIndex < entity.inventory.size()) {

                drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight); //TELA DE DESCRICAO SO APARECE QUANDO TEM UM ITEM

                for(String line : entity.inventory.get(itemIndex).description.split("\n")) {

                    g2.drawString(line, textX, textY);
                    textY += 32;
                }
            }
        }
        
        
    }
    
    public void drawGameOverState() {
        
        g2.setColor(new Color(0, 0, 0, 190));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        int x;
        int y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110f));
        
        text = "Game Over";
        //SOMBRA DO TEXTO
        g2.setColor(Color.black);
        x = getXCenteredText(text);
        y =gp.tileSize * 4;
        g2.drawString(text, x, y);
        // --------- //
        
        //TEXTO
        g2.setColor(Color.white);
        g2.drawString(text, x - 4, y - 4);
        // ---------- //
        
        // RETRY
        g2.setFont(g2.getFont().deriveFont(50f));
        text = "Retry";
        x = getXCenteredText(text);
        y += gp.tileSize * 4;
        g2.drawString(text, x, y);
        if(commandNum == 0) {
            g2.drawString(">", x - 40, y);
        }
        // ---------- //
        
        // VOLTAR A TELA INICIAL
        text = "Quit";
        x = getXCenteredText(text);
        y += 55;
        g2.drawString(text, x, y);
        if(commandNum == 1) {
            g2.drawString(">", x - 40, y);
        }
        // ---------------------- //
    }
    
    public void drawOptionsScreen() {
        
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(32F));
        
        //SUB WINDOW
        int frameX = gp.tileSize * 6;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 8;
        int frameHeight = gp.tileSize * 10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        
        switch(subState) {
            
            case 0:
                options_top(frameX, frameY);
                break;
                
            case 1:
                options_fullScreenNotification(frameX, frameY);
                break;
                
            case 2:
                options_control(frameX, frameY);
                break;
           
            case 3:
                options_endgameConfirmation(frameX, frameY);
                break;
        }
        
        gp.keyH.enterPressed = false;
    }
    
    public void options_top(int frameX, int frameY) {
        
        int textX;
        int textY;
        
        //TITULO
        String text = "Options";
        textX = getXCenteredText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);
        
        //FULL SCREEN ON/OF
        textX = frameX + gp.tileSize;
        textY += gp.tileSize * 2;
        g2.drawString("Full Screen", textX, textY);
        if(commandNum == 0) {
            g2.drawString(">", textX - 25, textY);
            if(gp.keyH.enterPressed == true) {
                if(gp.fullScreenOn == false) {
                    gp.fullScreenOn = true;
                }
                else if(gp.fullScreenOn == true) {
                    gp.fullScreenOn = false;
                }
                subState = 1;
            }
        }
        
        //MUSIC
        textY += gp.tileSize;
        g2.drawString("Music", textX, textY);
        if(commandNum == 1) {
            g2.drawString(">", textX - 25, textY);
        }
        
        //SE
        textY += gp.tileSize;
        g2.drawString("SE", textX, textY);
        if(commandNum == 2) {
            g2.drawString(">", textX - 25, textY);
        }
        
        //CONTROL
        textY += gp.tileSize;
        g2.drawString("Control", textX, textY);
        if(commandNum == 3) {
            g2.drawString(">", textX - 25, textY);
            if(gp.keyH.enterPressed == true) {
                subState = 2;
                commandNum = 0;
            }
        }
        
        //END GAME
        textY += gp.tileSize;
        g2.drawString("End Game", textX, textY);
        if(commandNum == 4) {
            g2.drawString(">", textX - 25, textY);
            if(gp.keyH.enterPressed == true) {
                subState = 3;
                commandNum = 0;
            }
        }
        
        // BACK
        textY += gp.tileSize * 2;
        g2.drawString("Back", textX, textY);
        if(commandNum == 5) {
            g2.drawString(">", textX - 25, textY);
            if(gp.keyH.enterPressed == true) {
                gp.gameState = gp.playState;
                commandNum = 0;
            }
        }
        
        //FULL SCREEN CHECK BOX
        textX = frameX + (int)(gp.tileSize * 4.5);
        textY = frameY + gp.tileSize * 2 + 24;
        g2.setStroke(new BasicStroke(3)); // AJUSTAR A LARGURA DA CHECKBOX
        g2.drawRect(textX, textY, 24, 24);
        if(gp.fullScreenOn == true) {
            g2.fillRect(textX, textY, 24, 24);
        }
        
        //VOLUME DA MUSICA
        textY += gp.tileSize;
        g2.drawRect(textX, textY, 120, 24); //120 /5 -> 24p
        int volumeWidth = 24 * gp.music.volumeScale;
        g2.fillRect(textX, textY, volumeWidth, 24);
        
        //SE
        textY += gp.tileSize;
        g2.drawRect(textX, textY, 120, 24);
        volumeWidth = 24 * gp.se.volumeScale;
        g2.fillRect(textX, textY, volumeWidth, 24);
        
        gp.config.saveConfig();
    }
    
    public void options_fullScreenNotification(int frameX, int frameY) {
        
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize * 3;
        
        currentDialogue = "Restart the game  \nto apply changes";
        
        for(String line: currentDialogue.split("\n")) {
            g2.drawString(line, textX, textY);
            textY += 40;
        }
        
        //BACK
        textY = frameY + gp.tileSize * 9;
        g2.drawString("Back", textX, textY);
        if(commandNum == 0) {
            g2.drawString(">", textX - 25, textY);
            if(gp.keyH.enterPressed == true) {
                subState = 0;
            }
        }
    }
    
    public void options_control(int frameX, int frameY) {
        int textX;
        int textY;
        
        //TITULO
        String text = "Control";
        textX = getXCenteredText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);
        
        //FULL SCREEN ON/OF
        textX = frameX + gp.tileSize;
        textY += gp.tileSize;
        g2.drawString("Move", textX, textY+=gp.tileSize);
        g2.drawString("Confirm/Attack", textX, textY+=gp.tileSize);
        g2.drawString("Shoot/Cast", textX, textY+=gp.tileSize);
        g2.drawString("Character Screen", textX, textY+=gp.tileSize);
        g2.drawString("Pause", textX, textY+=gp.tileSize);
        g2.drawString("Options", textX, textY+=gp.tileSize);
        
        textX = frameX + gp.tileSize * 6;
        textY = frameY + gp.tileSize * 3;
        g2.drawString("WASD", textX, textY);
        textY += gp.tileSize;
        g2.drawString("ENTER", textX, textY);
        textY += gp.tileSize;
        g2.drawString("F", textX, textY);
        textY += gp.tileSize;
        g2.drawString("C", textX, textY);
        textY += gp.tileSize;
        g2.drawString("P", textX, textY);
        textY += gp.tileSize;
        g2.drawString("ESC", textX, textY);
        textY += gp.tileSize;
        
        //BACK
        textX = frameX + gp.tileSize;
        textY = frameY + gp.tileSize * 9;
        g2.drawString("Back", textX, textY);
        if(commandNum == 0) {
            g2.drawString(">", textX - 25, textY);
            if(gp.keyH.enterPressed == true) {
                subState = 0;
                commandNum = 3;
            }
        }
    }
    
    public void options_endgameConfirmation(int frameX, int frameY) {
        
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize;
        
        currentDialogue = "Do You Want To Go Back\n To The Title Screen?";
        
        for(String line: currentDialogue.split("\n")) {
            g2.drawString(line, textX, textY);
            textY += 40;
        }
        
        //YES
        String text = "Yes";
        textX = getXCenteredText(text);
        textY += gp.tileSize * 3;
        g2.drawString(text, textX, textY);
        if(commandNum == 0) {
            g2.drawString(">", textX - 25, textY);
            if(gp.keyH.enterPressed == true) {
                subState = 0;
                titleScreenState = 0;
                gp.gameState = gp.titleState;
            }
        }
        
        //NO
        text = "No";
        textX = getXCenteredText(text);
        textY += gp.tileSize;
        g2.drawString(text, textX, textY);
        if(commandNum == 1) {
            g2.drawString(">", textX - 25, textY);
            if(gp.keyH.enterPressed == true) {
                subState = 0;
                commandNum = 4;
            }
        }
        
    }
    
    public void drawTransition() {
        
        counter++;
        g2.setColor(new Color(0, 0, 0, counter * 5));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        if(counter == 50) { //TEMPO DE TRANSICAO/INTENSIDADE DA TELA PRETA
            counter = 0;
            gp.gameState = gp.playState;
            gp.currentMap = gp.eHandler.tempMap;
            gp.player.worldX = gp.tileSize * gp.eHandler.tempCol;
            gp.player.worldY = gp.tileSize * gp.eHandler.tempRow;
            gp.eHandler.previousEventX = gp.player.worldX;
            gp.eHandler.previousEventY = gp.player.worldY;
        }
    }
    
    public void drawTradeScreen() {
        
        switch(subState) {
            
            case 0:
                trade_select();
                break;
            case 1:
                trade_buy();
                break;
            case 2:
                trade_sell();
                break;
        }
        gp.keyH.enterPressed = false;
    }
    
    public void trade_select() {
        
        drawDialogueScreen();
        
        //DESENHAR A JANELA DE TRADE
        int x = gp.tileSize * 15;
        int y = gp.tileSize * 4;
        int width = gp.tileSize * 3;
        int height = (int)(gp.tileSize * 3.5);
        drawSubWindow(x, y, width, height);
        
        //DESENHAR OS TEXTOS
        x += gp.tileSize;
        y += gp.tileSize;
        g2.drawString("Buy", x, y);
        if(commandNum == 0) {
            g2.drawString(">", x - 24, y);
            if(gp.keyH.enterPressed == true) {
                subState = 1;
            }
        }
        y += gp.tileSize;
        
        g2.drawString("Sell", x, y);
        if(commandNum == 1) {
            g2.drawString(">", x - 24, y);
            if(gp.keyH.enterPressed == true) {
                subState = 2;
            }
        }
        y += gp.tileSize;
        
        g2.drawString("Leave", x, y);
        if(commandNum == 2) {
            g2.drawString(">", x - 24, y);
            if(gp.keyH.enterPressed == true) {
                commandNum = 0;
                gp.gameState = gp.dialogueState;
                currentDialogue = "I'm Waiting for you! he he he!";
            }
        }
    }
    
    public void trade_buy() {
        
        //DESENHAR INVENTARIO DO PLAYER
        drawInventory(gp.player, false);
        
        //INVENTARIO DO NPC
        drawInventory(npc, true);
        
        //CAIXA DE DICA
        int x = gp.tileSize * 2;
        int y = gp.tileSize * 9;
        int width = gp.tileSize * 6;
        int height = gp.tileSize * 2;
        drawSubWindow(x, y, width, height);
        g2.drawString("[ESC] Back", x + 24, y + 60);
        
        //QUANT. DE MOEDAS DO PLAYER
        x = gp.tileSize * 12;
        y = gp.tileSize * 9;
        width = gp.tileSize * 6;
        height = gp.tileSize * 2;
        drawSubWindow(x, y, width, height);
        g2.drawString("Your Coins: " + gp.player.coin, x + 24, y + 60);
        
        //TABELA DE PRECOS
        int itemIndex = getItemIndexSlot(npcSlotCol, npcSlotRow);
        if(itemIndex < npc.inventory.size()) {
            
            x = (int)(gp.tileSize * 5.5);
            y = (int)(gp.tileSize * 5.5);
            width = (int)(gp.tileSize * 2.5);
            height = gp.tileSize;
            drawSubWindow(x, y, width, height);
            g2.drawImage(coin, x + 10, y + 8, 32, 32, null);
            
            int price = npc.inventory.get(itemIndex).price;
            String text = "" + price;
            x = getXAlignToRightText(text, gp.tileSize * 8 - 20);
            g2.drawString(text, x, y + 34);
            
            //BUY
            if(gp.keyH.enterPressed == true) {
                if(npc.inventory.get(itemIndex).price > gp.player.coin) {
                    subState = 0;
                    gp.gameState = gp.dialogueState;
                    currentDialogue = "You need More coint to buy that";
                    drawDialogueScreen();
                }
                else if(gp.player.inventory.size() == gp.player.maxInventorySize) {
                    subState = 0;
                    gp.gameState = gp.dialogueState;
                    currentDialogue = "You need more space in your inventory";
                }
                else {
                    gp.player.coin -= npc.inventory.get(itemIndex).price;
                    gp.player.inventory.add(npc.inventory.get(itemIndex));
                }
            }
        }
    }
    
    public void trade_sell() {
        
        //INVENTARIO DO PLAYER
        drawInventory(gp.player, true);
        
        int x;
        int y;
        int width;
        int height;
        
        //CAIXA DE DICA
        x = gp.tileSize * 2;
        y = gp.tileSize * 9;
        width = gp.tileSize * 6;
        height = gp.tileSize * 2;
        drawSubWindow(x, y, width, height);
        g2.drawString("[ESC] Back", x + 24, y + 60);
        
        //QUANT. DE MOEDAS DO PLAYER
        x = gp.tileSize * 12;
        y = gp.tileSize * 9;
        width = gp.tileSize * 6;
        height = gp.tileSize * 2;
        drawSubWindow(x, y, width, height);
        g2.drawString("Your Coins: " + gp.player.coin, x + 24, y + 60);
        
        //TABELA DE PRECOS
        int itemIndex = getItemIndexSlot(playerSlotCol, playerSlotRow);
        if(itemIndex < gp.player.inventory.size()) {
            
            x = (int)(gp.tileSize * 15.5);
            y = (int)(gp.tileSize * 5.5);
            width = (int)(gp.tileSize * 2.5);
            height = gp.tileSize;
            drawSubWindow(x, y, width, height);
            g2.drawImage(coin, x + 10, y + 8, 32, 32, null);
            
            int price = gp.player.inventory.get(itemIndex).price / 2;
            String text = "" + price;
            x = getXAlignToRightText(text, gp.tileSize * 18 - 20);
            g2.drawString(text, x, y + 34);
            
            //SELL
            if(gp.keyH.enterPressed == true) {
                
                if(gp.player.inventory.get(itemIndex) == gp.player.currentWeapon ||
                        gp.player.inventory.get(itemIndex) == gp.player.currentShield) { //ITENS EQUIPADOS NAOPODEM SER VENDIDIOS
                    subState = 0;
                    commandNum = 0;
                    gp.gameState = gp.dialogueState;
                    currentDialogue = "You cannot sell equipped item!";
                }
                else {
                    gp.player.inventory.remove(itemIndex);
                    gp.player.coin += price;
                }
            }
        }
        
        
    }
    
    public int getItemIndexSlot(int slotCol, int slotRow) {
        
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

