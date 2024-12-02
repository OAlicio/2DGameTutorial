/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package object;

import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Mavi da Vintch
 */
public class OBJ_Chest extends SuperObject{
    
    public OBJ_Chest() {
    
        name = "Chest";
        
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/chest.png"));   
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
}
