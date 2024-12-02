package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import main.GamePanel;

public class TileManager {
    
    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][]; //ARMAZENA OS VALORES NUMERICOS DOS TILES QUE FOREM ENCONTRADOS
    
    public TileManager(GamePanel gp){
    
        this.gp = gp;
        
        tile = new Tile[10]; //Numero de tiles/blocos totais que poderao ser colocados
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        getTileImage();
        loadMap("/maps/world01.txt");
    }
    
    public void getTileImage() {
    
        try {
            
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));
            
            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));
            tile[1].collision = true;
            
            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/water.png"));
            tile[2].collision = true;
            
            tile[3] = new Tile();
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/tiles/earth.png"));
            
            tile[4] = new Tile();
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/tiles/tree.png"));
            tile[4].collision = true;
            
            tile[5] = new Tile();
            tile[5].image = ImageIO.read(getClass().getResourceAsStream("/tiles/sand.png"));
       
        }catch(IOException e){
            e.printStackTrace();
        }
        
    }
    
    public void loadMap(String filePath) {
    
        try {
        
            InputStream is = getClass().getResourceAsStream(filePath); //CARREGA O FICHEIRO DO MAPA
            BufferedReader br = new BufferedReader(new InputStreamReader(is)); //LE O CONTEUDO DO FICHEIRO
            
            int col = 0;
            int row = 0;
            
            while(col < gp.maxWorldCol && row < gp.maxWorldCol){  //Queremos ler ate o ultimo tile 
                String line = br.readLine(); // Read line: le uma linha do texto, e line recebera essa linha
                
                while(col < gp.maxWorldCol) {
                
                    String numbers[] = line.split(" "); // Divide o "texto" a cada espaco encontrado
                    
                    int num = Integer.parseInt(numbers[col]); //STring para Integer
                    
                    mapTileNum[col][row] = num;
                    col++;
                }
                if(col == gp.maxWorldCol) { //QUANDO A LINHA TERMINA
                
                    col = 0;
                    row++;
                }
            }
            br.close(); //FECHA O ARQUIVO QUE FOI ABERTO
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void draw(Graphics2D g2) {
    
        //DESENHAR OS tILES automaticamente
        int worldCol = 0;
        int worldRow = 0;
        
        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldCol) {
        
            int tileNum = mapTileNum[worldCol][worldRow]; //PEGAR O VALOR DO TILE NA POSICAO ex: 0x0, O MAPA JA FOI GUARDADO
            
            //IMPLEMENTANDO CAMERA
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;
            
            //EVITANDO RENDERIZAR TILES NAO VISIVEIS
            if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && 
               worldX - gp.tileSize < gp.player.worldX + gp.player.screenX && 
               worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && 
               worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                
                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
            
            worldCol++;
            
            if(worldCol == gp.maxWorldCol) {
            
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
