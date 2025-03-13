package tile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.UtilityTool;

public class TileManager {
    
    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][][]; //ARMAZENA O VALOR NUMERICO DO MAPA, E DOS TILES QUE FOREM ENCONTRADOS
    boolean drawPath = true;
    ArrayList<String> fileNames = new ArrayList<>();
    ArrayList<String> collisionStatus = new ArrayList<>();
    InputStream is;
    BufferedReader br;
    
    
    public TileManager(GamePanel gp) {
    
        this.gp = gp;
    
        //LER OS DADOS DO FICHEIRO COMOS DADOS DOS TILES
        
        is = getClass().getResourceAsStream("/maps/tiledata.txt");
        br = new BufferedReader(new InputStreamReader(is));
        
        String line;
        
        try {
            while((line = br.readLine()) != null) {
                fileNames.add(line);
                collisionStatus.add(br.readLine());
            }
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(TileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // ------------------------------ //
        
        //INICIALIZA O ARRAY DE TILES BASEADO NO TAMANHO DE fileNames
        tile = new Tile[fileNames.size()]; //Numero de tiles/blocos totais que poderao ser colocados
        getTileImage();
        
        //PEGAR O maxWorldCol e Row
        is = getClass().getResourceAsStream("/maps/worldmap.txt");
        br = new BufferedReader(new InputStreamReader(is));
        
        try{
            String line2 = br.readLine();
            String maxTile[] = line2.split(" ");
            
            //APENAS PARA MAPAS Y x Y(1x1, 50x50, 100x100, etc)
            gp.maxWorldCol = maxTile.length;
            gp.maxWorldRow = maxTile.length;
            mapTileNum = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];
            
            br.close();
        } catch(IOException e) {
            System.out.println("Exception");
        }
        
        
        loadMap("/maps/worldmap.txt", 0);
//        loadMap("/maps/indoor01.txt", 1);
        
//        loadMap("/maps/worldV2.txt", 0);
//        loadMap("/maps/Interior01.txt", 1);
    }

    
    public void getMapSize(InputStream is, BufferedReader br, String mapPath) {
        
        
    }
    
    public void getWorldColandRow() {
        
    }
    
    public void getTileImage() {
    
        for(int i = 0; i < fileNames.size(); i++) {
            
            String fileName;
            boolean collision;
            
            //PEGAR O NOME DO FICHEIRO
            fileName = fileNames.get(i);
            
            //PEGAR O ESTADO DE COLISAO
            if(collisionStatus.get(i).equals("true")) {
                collision = true;
            }
            else {
                collision = false;
            }
            
            setup(i, fileName, collision);
        }
    }
    
    public void setup(int index, String imagePath, boolean collision) {
        
        UtilityTool uTool = new UtilityTool();
        
        try {
            
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/"+ imagePath));
            tile[index].image = uTool.scaledImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public void loadMap(String filePath, int map) {
    
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
                    
                    mapTileNum[map][col][row] = num;
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
        
            int tileNum = mapTileNum[gp.currentMap][worldCol][worldRow]; //PEGAR O VALOR DO TILE NA POSICAO ex: 0x0, O MAPA JA FOI GUARDADO
            
            //IMPLEMENTANDO CAMERA
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;
            
            //EVITANDO RENDERIZAR TILES NAO VISIVEIS
            if(worldX + (gp.tileSize * 2) > gp.player.worldX - gp.player.screenX && 
               worldX - (gp.tileSize * 2) < gp.player.worldX + gp.player.screenX && 
               worldY + (gp.tileSize * 2) > gp.player.worldY - gp.player.screenY && 
               worldY - (gp.tileSize * 2)< gp.player.worldY + gp.player.screenY ) {
                
                g2.drawImage(tile[tileNum].image, screenX, screenY, null);
            }
            
            worldCol++;
            
            if(worldCol == gp.maxWorldCol) {
            
                worldCol = 0;
                worldRow++;
            }
        }
        if(drawPath == true) {
            g2.setColor(new Color(255, 0, 0, 70));
            
            for(int i = 0; i< gp.pFinder.pathList.size(); i++) {
                
            int worldX = gp.pFinder.pathList.get(i).col * gp.tileSize;
            int worldY = gp.pFinder.pathList.get(i).row * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;
            
            g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);
            }
        }
    }
}
