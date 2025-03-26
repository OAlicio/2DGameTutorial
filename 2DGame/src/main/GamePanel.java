package main;

import ai.PathFinder;
import entity.Entity;
import entity.Player;
import environment.EnvironmentManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JPanel;
import tile.Map;
import tile.TileManager;
import tile_interactive.InteractiveTile;

public class GamePanel extends JPanel implements Runnable { //RUNNABLE -> THREAD
    
    //OPCOES DE TELA
    public final int originalTileSize = 16; // TAMANHO DOS TILES (16 X 16)
    final int scale = 3;
    public final int tileSize = originalTileSize * scale; //Coloca numa escala mais visivel nas telas atuais (48 X 48)
    public final int maxScreenCol = 20; // TAMANHO/LIMITE MAXIMO NA VERTICAL
    public final int maxScreenRow = 12; // TAMANHO/LIMITE MAXIMO NA HORIZONTAL
    public final int screenWidth = tileSize * maxScreenCol; // COMPRIMENTO TOTAL 960 pixels
    public final int screenHeight = tileSize * maxScreenRow; // ALTURA TOTAL 576 pixels
    
    //OPCOES DE MUNDO
    public int maxWorldCol; //MAIOR VALOR DE "x" DO MAPA
    public int maxWorldRow; //MAIOR VALOR DE "y" DO MAPA   
    public final int maxMap = 10; //MAXIMO NUMERO DE MAPAS/MUNDOS
    public int currentMap = 0; //MAPA ACTUAL
    
    //FULL SCREEN
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;
    public boolean fullScreenOn = false;
    
    //FPS
    int FPS = 60; //FPS DESEJADO
    int frameCount = 0;
    
    //SYSTEM -------------------------------//
    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound se = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetsSetter aSetter = new AssetsSetter(this);
    public UI ui = new UI(this);
    Config config = new Config(this);
    public PathFinder pFinder = new PathFinder(this);
    public EventHandler eHandler = new EventHandler(this);
    EnvironmentManager  eManager = new EnvironmentManager(this);
    Map map = new Map(this);
    Thread gameThread; //Mantem o programa rodando até que seja fechado
    // ---------------------------------------//
    
    //ENTITY AND OBJECT
    public Player player = new Player(this, keyH); //INSTANCIANDO O PLAYER
    public Entity obj[][] = new Entity[maxMap][30]; //QUANTIDADE DE OBJECTOS/ENTIDADES A SEREM MOSTRADOS NA TELA
    public Entity npc[][] = new Entity[maxMap][20];
    public Entity monster[][] = new Entity[maxMap][20];
    public Entity projectile[][] = new Entity[maxMap][20];
    public InteractiveTile iTile[][] = new InteractiveTile[maxMap][50]; //TILES INTERACTIVOS/ QUE QUEBRAM
//  public ArrayList<Entity> projectileList = new ArrayList<>();
    public ArrayList<Entity> particleList = new ArrayList<>();
    ArrayList<Entity> entityList = new ArrayList<>(); //REORGANIZANDO AS LAYERS DAS ENTIDADE(PLAYER & OBJECTOS)
    // --------------------------------------------- //
    
    //GAMESTATE
    
    public int gameState;
    public final int titleState = 0;  
    public final int playState = 1; //PODEM SER QUAISQUER NUMEROS
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;
    public final int optionsState = 5;
    public final int gameOverState = 6;
    public final int transitionState = 7;
    public final int tradeState = 8;
    public final int sleepState = 9;
    public final int mapState = 10;
    
    //---------//
    
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); //Coloca o tamanho da classe JPanel(Gamepanel)
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH); // ADICIONA O KEY LISTNER CRIADO AO GAMEPANEL
        this.setFocusable(true); //Gamepanel será "focado" para receber inputs
    }
    
    public void setupGame() {
    
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        aSetter.setInteractiveTile();
        eManager.setup();
        gameState = titleState;
        
        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB); //TAMANHO MAXIMO DA TELA COLOCADO DENTRO DE UMA BUFFERED IMAGE VAZIA
        g2 = (Graphics2D)tempScreen.getGraphics(); //TUDO QUE O G2 DESENHAR SERA FEITO APARTIR DO TEMPSCREEN
        
        if(fullScreenOn == true) {
            setFullScreen();
        }
    }

    public void retry() {
        
        player.setDefaultPositions();
        player.restoreLifeAndMana();
        aSetter.setNPC();
        aSetter.setMonster();
        playMusic(0);
    }
    
    public void restart() {
        
        player.invincible = false;
        player.setDefaultValues();
        player.setItems();
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        aSetter.setInteractiveTile();
    }
    
    public void setFullScreen() {
        
        //PEGAR A TELA DO APARELHO
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(Main.window);
        // ---------------------- //
        
        //PEGAR O COMPRIMENTO E ALTURA MAXIMA
        screenWidth2 = Main.window.getWidth();
        screenHeight2 = Main.window.getHeight();
        // ----------------------------------- //
    }
    
    public void startGameThread() {
    
        gameThread = new Thread(this); //INSTANCIA O THREAD
        gameThread.start(); // COMECA O THREAD
    }

    //Metodo Delta/Acumulator
    @Override
    public void run() { // FUNCAO CHAMADA PELO THREAD
        
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;
        
        while (gameThread != null) { //ENQUANTO O GAME THREAD EXISTIR, REPITA
            
            currentTime = System.nanoTime(); // PEGA O TEMPO DE EXECUCAO ATUAL DO SISTEMA EM NANO SEGUNDOS (1s = 1Bns)
            
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;
            if(delta >= 1) {
                update();
                drawToTempScreen(); //DESENHA TUDO AO BUFFERED IMAGE
                drawToScreen(); //DESENHA A BUFFERED IMAGE NA TELA
                delta--;
                drawCount++;
            }
            // Calcula FPS a cada segundo
            if (timer >= 1000000000) {
                frameCount = drawCount; // Reseta o contador
                drawCount = 0;
                timer = 0; // Reinicia o timer
            }
        }
    }
    
    public void update() { // METODO PADRAO DO JPANEL
            
            if(gameState == playState) {
                
                //PLAYER
                player.update();
                
                //NPC
                for(int i = 0; i < npc[1].length; i++) {
                    if(npc[currentMap][i] != null) {
                        npc[currentMap][i].update();
                    }
                }
                for(int i = 0; i < monster[1].length; i++) {
                    if(monster[currentMap][i] != null) {
                        if(monster[currentMap][i].alive == true && monster[currentMap][i].dying == false) {
                            monster[currentMap][i].update();
                        }
                        if(monster[currentMap][i].alive == false) {
                            monster[currentMap][i].checkDrop(); //verificar drop
                            monster[currentMap][i] = null;
                        }
                    }
                }
                for(int i = 0; i < projectile[1].length; i++) {
                    if(projectile[currentMap][i] != null) {
                        if(projectile[currentMap][i].alive == true) {
                            projectile[currentMap][i].update();
                        }
                        if(projectile[currentMap][i].alive == false) {
                            projectile[currentMap][i] = null;
                        }
                    }
                }
                
                for(int i = 0; i < particleList.size(); i++) {
                    if(particleList.get(i) != null) {
                        if(particleList.get(i).alive == true) {
                            particleList.get(i).update();
                        }
                        if(particleList.get(i).alive == false) {
                            particleList.remove(i);
                        }
                    }
                }
                
                for(int i = 0; i < iTile[1].length; i++) {
                    if(iTile[currentMap][i] != null) {
                        iTile[currentMap][i].update();
                    }
                }
                
                //LUZ
                eManager.update();
            }
            
            if(gameState == pauseState) {
                //NOTHING
            }
            
        }

    //REDESENHAR NAO ATRAVES DO JPANEL, PARA DAR UM RESIZE PRO FULL SCREEN
    public void drawToTempScreen() {
        
            //DEBUG
            long drawStart = 0;
            if(keyH.showDebugText == true) {
                drawStart = System.nanoTime();
            }
   
            //TITLE SCREEN
            if(gameState == titleState) {
               ui.draw(g2); 
            }
            
            // MOSTRAR O MAPA NA TELA
            else if(gameState == mapState) {
                map.drawFullMapScreen(g2);
            }
            
            // OUTROS
            else {
            
                //TILE
                tileM.draw(g2); // AQUI TEMOS LAYERS, INDO DE CIMA PRA BAIXO (O MAIS ABAIXO TEM MAIOR PRIORIDADE)

                //TILE INTERACTIVO
                for(int i = 0; i < iTile[1].length; i++) {
                    if(iTile[currentMap][i] != null) {
                        iTile[currentMap][i].draw(g2);
                    }
                }
                
                // ADICIONA ENTIDADES A LISTA DE ENTIDADES
                
                    //PLAYER
                entityList.add(player);
                
                    //NPC
                for(int i = 0; i < npc[1].length; i++) {
                    if(npc[currentMap][i] != null) {
                        entityList.add(npc[currentMap][i]);
                    }
                }
                
                    //OBJECTOS
                for(int i = 0; i < obj[1].length; i++) {
                    if(obj[currentMap][i] != null) {
                        entityList.add(obj[currentMap][i]);
                    }
                }
               
                //MONSTROS
                for (int i = 0; i < monster[1].length; i++) {
                    if (monster[currentMap][i] != null) {
                        entityList.add(monster[currentMap][i]);
                    }
                }
                
                //PROJECTIL
                for(int i = 0; i < projectile[1].length; i++) {
                    if(projectile[currentMap][i] != null) {
                        entityList.add(projectile[currentMap][i]);
                    }
                }
                
                //PARTICULAS
                for(int i = 0; i < particleList.size(); i++) {
                    if(particleList.get(i) != null) {
                        entityList.add(particleList.get(i));
                    }
                }
                
                //SORT
                Collections.sort(entityList, new Comparator<Entity>() {
                   
                    @Override
                    public int compare(Entity e1, Entity e2) {
                        int result = Integer.compare(e1.worldY, e2.worldY);
                        return result;
                    }
                });
                
                //DESENHAR AS ENTIDADES
                for(int i = 0; i < entityList.size(); i++) {
                
                    entityList.get(i).draw(g2);
                }
                
                //ESVAZIAR A LISTA DE ENTIDADES
                entityList.clear();
                // -------------------------------------- //
                
                //ENVIRONMENT
                eManager.draw(g2);
                
                // MINI MAPA
                map.drawMiniMap(g2);
                
                // UI
                ui.draw(g2);
            }
            
                //DEBUG
                if(keyH.showDebugText == true) {
                    long drawEnd = System.nanoTime();
                    long passed = drawEnd - drawStart;
                    
                    g2.setFont(new Font("Arial", Font.PLAIN, 20));
                    g2.setColor(Color.white);
                    int x = 10;
                    int y = 400;
                    int lineHeight = 20;
                    
                    g2.drawString("WorldX: " + player.worldX, x, y);
                    y += lineHeight;
                    g2.drawString("WorldY: " + player.worldY, x, y);
                    y += lineHeight;
                    g2.drawString("Col: " + (player.worldX + player.solidArea.x)/tileSize, x, y);
                    y += lineHeight;
                    g2.drawString("Row: " + (player.worldY + player.solidArea.y)/tileSize, x, y);
                    y += lineHeight;
                    g2.drawString("FPS: " + frameCount, x, y);
                    y += lineHeight;
                    
                    g2.drawString("Draw Time: " + passed, x, y);
                }
       
    }
    
    // DESENHA AS BUFFERED IMAGE DO DRAWTOTEMPSCREEN
    public void drawToScreen() {
        
        Graphics g = getGraphics();
        g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
        g.dispose();
    }
    
    public void playMusic(int i) {
    
        music.setFile(i);
        music.play();
        music.loop();
    }
         
    public void stopMusic() {
    
        music.stop();
    }
    
    public void playSE(int i) {
    
        se.setFile(i);
        se.play();
    }
} 
