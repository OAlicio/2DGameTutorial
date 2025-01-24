package main;

import java.awt.Color;
import javax.swing.JFrame;

public class Main {

    public static JFrame window;
    
    public static void main(String[] args) {
        
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Octavio's 2D Game");
        window.setUndecorated(true); //TIRAR AS BORDAS PADRAO
        
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        
        window.pack();
        
        window.setBackground(Color.black);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        
        gamePanel.setupGame();
        gamePanel.startGameThread();
    }
    
}
