package main;

import entity.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable { //RUNNABLE » THREAD
    
    //OPCOES DE TELA
    public final int originalTileSize = 16; // TAMANHO DOS TILES (16 X 16)
    final int scale = 3;
    
    public final int tileSize = originalTileSize * scale; //Coloca numa escala mais visivel nas telas atuais (48 X 48)
    public final int maxScreenCol = 16; // TAMANHO MAXIMO NA VERTICAL
    public final int maxScreenRow = 12; // TAMANHO MAXIMO NA HORIZONTAL
    final int screenWidth = tileSize * maxScreenCol; // COMPRIMENTO TOTAL
    final int screenHeight = tileSize * maxScreenRow; // ALTURA TOTAL
    
    //FPS
    int FPS = 60; //FPS DESEJADO
    
    KeyHandler keyH = new KeyHandler();
    Thread gameThread; //Mantem o programa rodando até que seja fechado
    Player player = new Player(this, keyH); //INSTANCIANDO O PLAYER
    
    //POSICAO INICIALDO JOGADOR
    
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;
    
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); //Coloca o tamanho da classe JPanel(Gamepanel)
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH); // ADICIONA O KEY LISTNER CRIADO AO GAMEPANEL
        this.setFocusable(true); //Gamepanel será "focado" para receber inputs
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

            player.draw(g2);
            
            g2.dispose(); // DESCARTA E RELANCA COISAS QUE O SISTEMA ESTARA USANDO
        }
           
} 
