/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Mavi da Vintch
 */
public class UtilityTool {
    
    public BufferedImage scaledImage(BufferedImage original, int width, int height) {
       
        // COLOCANDO ESCALA NOS TILES, EVITANDO QUE SEJA INSTANCIADO NO GAMELOOP
            BufferedImage scaledImage = new BufferedImage(width, height, original.getType()); //(int width, int height, int imageType)
            Graphics2D g2 = scaledImage.createGraphics(); //AQUILO QUE g2 DESENHAR SERA SALVO NO SCALEDIMAGE
            g2.drawImage(original, 0, 0, width, height, null);
            g2.dispose();

            return scaledImage;
    }
}
