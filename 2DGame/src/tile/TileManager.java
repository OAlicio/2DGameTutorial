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
    Tile[] tile;
    int mapTileNum[][]; //ARMAZENA OS VALORES NUMERICOS DOS TILES QUE FOREM ENCONTRADOS
    public TileManager(GamePanel gp){
    
        this.gp = gp;
        
        tile = new Tile[10]; //Numero de tiles/blocos totais que poderao ser colocados
        mapTileNum = new int[gp.maxScreenCol][gp.maxScreenRow];
        getTileImage();
        loadMap("/maps/map01.txt");
    }
    
    public void getTileImage(){
    
        try {
            
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));
            
            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));
            
            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/water.png"));
            
        }catch(IOException e){
            e.printStackTrace();
        }
        
    }
    
    public void loadMap(String filePath){
    
        try {
        
            InputStream is = getClass().getResourceAsStream(filePath); //CARREGA O FICHEIRO DO MAPA
            BufferedReader br = new BufferedReader(new InputStreamReader(is)); //LE O CONTEUDO DO FICHEIRO
            
            int col = 0;
            int row = 0;
            
            while(col < gp.maxScreenCol && row < gp.maxScreenRow){  //Queremos ler ate o ultimo tile 
                String line = br.readLine(); // Read line: le uma linha do texto, e line recebera essa linha
                
                while(col < gp.maxScreenCol) {
                
                    String numbers[] = line.split(" "); // Divide o "texto" a cada espaco encontrado
                    
                    int num = Integer.parseInt(numbers[col]); //STring para Integer
                    
                    mapTileNum[col][row] = num;
                    col++;
                }
                if(col == gp.maxScreenCol) { //QUANDO A LINHA TERMINA
                
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
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;
        
        while(col < gp.maxScreenCol && row < gp.maxScreenRow) {
        
            int tileNum = mapTileNum[col][row]; //PEGAR O VALOR DO TILE NA POSICAO ex: 0x0, O MAPA JA FOI GUARDADO
            
            g2.drawImage(tile[tileNum].image, x, y, gp.tileSize, gp.tileSize, null);
            col++;
            x += gp.tileSize;
            
            if(col == gp.maxScreenCol) {
            
                col = 0;
                x = 0;
                row++;
                y += gp.tileSize;
            }
        }
    }
}
