package tile;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import main.GamePanel;

public class Map extends TileManager{

    GamePanel gp;
    BufferedImage worldMap[];
    public boolean miniMapOn = false;
    
    public Map(GamePanel gp) {
        super(gp);
        this.gp = gp;
        createWorldMap();
    }

    public void createWorldMap() {
        
        worldMap = new BufferedImage[gp.maxMap];
        int worldMapWidth = gp.tileSize * gp.maxWorldCol;
        int worldMapHeight = gp.tileSize * gp.maxWorldRow;
        
        for(int i = 0; i < gp.maxMap; i++) {
            
            worldMap[i] = new BufferedImage(worldMapWidth, worldMapHeight, BufferedImage.TYPE_INT_ARGB);
            //TUDO QUE O G2 DESENHAR ESTARA LIGADO AO worldMap
            Graphics2D g2 = (Graphics2D)worldMap[i].createGraphics();
            
            int col = 0;
            int row = 0;
            while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
                
                int tileNum = mapTileNum[i][col][row];
                int x = gp.tileSize * col;
                int y = gp.tileSize * row;
                g2.drawImage(tile[tileNum].image, x, y, null);
                
                col++;
                if(col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            g2.dispose();
        }
    }
    
    public void drawFullMapScreen(Graphics2D g2) {
        
        //COR DE FUNDO
        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        // DESENHA O MAPA
        int width = 500;
        int height = 500; // -> PODEM SER QUASQUEIR VALORES, DESDE QUE SEJAM MENORES QUE O TAMANHO DA TELA 
        
        int x = gp.screenWidth/2 - width/2;
        int y = gp.screenHeight/2 - height/2;
        g2.drawImage(worldMap[gp.currentMap], x, y, width, height, null);
        
        //DESENHA O PLAYER
        double scale = (double)(gp.tileSize * gp.maxWorldCol)/width;
        int playerX = (int)(x + gp.player.worldX/scale);
        int playerY = (int )(y + gp.player.worldY/scale);
        int playerSize = (int)(gp.tileSize/scale);
        g2.drawImage(gp.player.down1, playerX, playerY, playerSize, playerSize, null);
        
        
        //Hint
        g2.setFont(gp.ui.purisaB.deriveFont(18f));
        g2.setColor(Color.white);
        g2.drawString("PRESS M TO CLOSE", 750, 550);
    }
    
    public void drawMiniMap(Graphics2D g2) {
        
        if(miniMapOn == true) {
            
            // DESENHA O MAPA
            int width = 300;
            int height = 300; // -> PODEM SER QUASQUEIR VALORES, DESDE QUE SEJAM MENORES QUE O TAMANHO DA TELA 

            int x = gp.screenWidth - width - 50;
            int y = 50;

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
            g2.drawImage(worldMap[gp.currentMap], x, y, width, height, null);
            
            //DESENHA O PLAYER
            double scale = (double)(gp.tileSize * gp.maxWorldCol)/width;
            int playerX = (int)(x + gp.player.worldX/scale);
            int playerY = (int )(y + gp.player.worldY/scale);
            int playerSize = (int)(gp.tileSize/3);
            g2.drawImage(gp.player.down1, playerX-6, playerY-6, playerSize, playerSize, null);
            
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    }
}
