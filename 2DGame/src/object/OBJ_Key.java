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
public class OBJ_Key extends SuperObject {
    
    public OBJ_Key() {
    
        name = "Key";
        
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/key.png"));
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
}
