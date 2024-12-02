package main;

import entity.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import object.SuperObject;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable { //RUNNABLE » THREAD
    
    //OPCOES DE TELA
    public final int originalTileSize = 16; // TAMANHO DOS TILES (16 X 16)
    final int scale = 3;
    
    public final int tileSize = originalTileSize * scale; //Coloca numa escala mais visivel nas telas atuais (48 X 48)
    public final int maxScreenCol = 16; // TAMANHO/LIMITE MAXIMO NA VERTICAL
    public final int maxScreenRow = 12; // TAMANHO/LIMITE MAXIMO NA HORIZONTAL
    public final int screenWidth = tileSize * maxScreenCol; // COMPRIMENTO TOTAL 768 pixels
    public final int screenHeight = tileSize * maxScreenRow; // ALTURA TOTAL 576 pixels
    
    //OPCOES DE MUNDO
    public final int maxWorldCol = 50; //MAIOR VALOR DE "x" DO MAPA
    public final int maxWorldRow = 50; //MAIOR VALOR DE "y" DO MAPA
    /* AGORA O TAMANHO DO MAPA É O LIMITE, E NAO MAIS A TELA */
    public final int worldWidth = tileSize * maxWorldCol; //COMPRIMENTO MAXIMO DO MAPA
    public final int worldHeight = tileSize * maxWorldRow; //ALTURA MAXIMA DO MAPA
    
    
    //FPS
    int FPS = 60; //FPS DESEJADO
    
    //OBJECTOS
    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread; //Mantem o programa rodando até que seja fechado
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetsSetter aSetter = new AssetsSetter(this);
    public Player player = new Player(this, keyH); //INSTANCIANDO O PLAYER
    public SuperObject obj[] = new SuperObject[10]; //QUANTIDADE DE OBJECTOS A SEREM MOSTRADOS NA TELA
    // --------------------------------------------- //
    
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); //Coloca o tamanho da classe JPanel(Gamepanel)
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH); // ADICIONA O KEY LISTNER CRIADO AO GAMEPANEL
        this.setFocusable(true); //Gamepanel será "focado" para receber inputs
    }
    
    public void setupGame() {
    
        aSetter.setObject();
    }

    public void startGameThread() {
    
        gameThread = new Thread(this); //INSTANCIA O THREAD
        gameThread.start(); // COMECA O THREAD
    }

    //Metodo Delta/Acumulator
    public void run() { // FUNCAO CHAMADA PELO THREAD
        
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        long drawCount = 0;
        
        while (gameThread != null) { //ENQUANTO O GAME THREAD EXISTIR, REPITA
            
            currentTime = System.nanoTime(); // PEGA O TEMPO DE EXECUCAO ATUAL DO SISTEMA EM NANO SEGUNDOS (1s = 1Bns)
            
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;
            
            if(delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }
            
            if(timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }
    
    public void update() { // METODO PADRAO DO JPANEL
        
            player.update();
        }

    public void paintComponent(Graphics g) { //METODO PADRAO TAMBEM
           
            super.paintComponent(g);
            
            Graphics2D g2 = (Graphics2D)g; // MAIS SOFISTICADO QUE O GRAPHICS, POSSUI MAIS FUNCOES

            //TILE
            tileM.draw(g2); // AQUI TEMOS LAYERS, SE O TILE FOR DESENHADO APOS O PLAYER, O PLAYER FICARA POR BAIXO DOS TILES
            
            //OBJECT
            for(int i = 0; i < obj.length; i++) {
                if(obj[i] != null) {
                    obj[i].draw(g2, this);
                }
            }
            
            //PLAYER
            player.draw(g2);
            
            g2.dispose(); // DESCARTA E RELANCA COISAS QUE O SISTEMA ESTARA USANDO
        }
           
} 
