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
    Font arial_40, arial_80B;
    BufferedImage keyImage;
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
        OBJ_Key key = new OBJ_Key();
        keyImage = key.image;
    }
    
    public void showMessage(String text) {
    
        message = text;
        messageOn = true;
    }
    
    public void draw(Graphics2D g2) {
        
        if(gameFinished == true) {
        
            g2.setFont(arial_40);
            g2.setColor(Color.white);
            String text;
            int textLength;
            int x;
            int y;
            
            text = "Encontraste o tesouro";
            textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth(); //RETORNA O TAMANHO DO TEXTO
            x = gp.screenWidth/2 - textLength/2;
            y = gp.screenHeight/2 - (gp.tileSize*3);
            g2.drawString(text, x, y);
            
            text = "Tempo de conclusâo :" + dFormat.format(playTime) + "!";
            textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth(); //RETORNA O TAMANHO DO TEXTO
            x = gp.screenWidth/2 - textLength/2;
            y = gp.screenHeight/2 + (gp.tileSize*4);
            g2.drawString(text, x, y);
            
            g2.setFont(arial_80B);
            g2.setColor(Color.yellow);
            text = "Parabéns! Ganhaste em um jogo claramente feito pra criança";
            textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth(); //RETORNA O TAMANHO DO TEXTO
            
            x = gp.screenWidth/2 - textLength/2;
            y = gp.screenHeight/2 + (gp.tileSize*2);
            g2.drawString(text, x, y);
            
            gp.gameThread = null; //PARA O THREAD/JOGO
            
        }
        else {
            //NAO É RECOMENDADO INSTANCIAR OBJECTOS NO GAMELOOP, DIMINUIRA O FPS POIS AUMENTA O TEMPO ATE FINALIZAR O LOOP
            //g2.setFont(new Font("Arial", Font.PLAIN, 40)); //Font(nome, tipo, tamanho)
            g2.setFont(arial_40);
            g2.setColor(Color.white);
            g2.drawImage(keyImage, gp.tileSize/2, gp.tileSize/2, gp.tileSize, gp.tileSize, null);
            //MOSTRAR QUE O JOGADOR PEGOU A CHAVE
            g2.drawString("x = " + gp.player.hasKey, 74, 65); //dwawString(String, x, y) NORMALMENTE O Y SIGNIFICA O TOPO DA IMAGEM OU OBJECTO, POREM NO DRAWSTRING SIGNIFICA A LINHA BASE DO TEXTO

            //TEMPO DE JOGO
            playTime += (double)1/60;
            g2.drawString("Tempo: " + dFormat.format(playTime), gp.tileSize*11, 65);
            
            //MENSAGEM
            if(messageOn == true) {

                g2.setFont(g2.getFont().deriveFont(30F)); //ALTERA O TAMANHODA FONTE ATUAL
                g2.drawString(message, gp.tileSize/2, gp.tileSize*5);

                messageCounter++;

                if(messageCounter > 120) { //APOS 2s(60FPS x 2) A MENSAGEM DESAPARECE
                    messageCounter = 0;
                    messageOn = false;
                }
            }
        }
    }
}
