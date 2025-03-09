package environment;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.image.BufferedImage;
import main.GamePanel;

public class Lightning {

    GamePanel gp;
    BufferedImage darknessFilter;

    public Lightning(GamePanel gp) {
        
        this.gp = gp;
        
        setLightSource();
       
    }
    
    public void setLightSource() {
        //CRIAR UMA BUFFEREDIMAGE
        darknessFilter = new BufferedImage(gp.screenWidth,
                gp.screenHeight, BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2 = (Graphics2D)darknessFilter.getGraphics();
        
        if(gp.player.currentLight == null) {
            g2.setColor(new Color(0,0,0,0.98f));
        }
        else {
        //PEGAR O CENTRO X E Y DA AREA DO CIRCULO
        int centerX = gp.player.screenX + gp.tileSize/2;
        int centerY = gp.player.screenY + gp.tileSize/2;
        
        //COR DA LUZ//
//         Color color2[] =  new Color[2];
//			 float fraction2[] = new float[2];
//	 
//			 float fR = 253 / 255.0F;
//			 float fG = 120 / 255.0F;
//			 float fB = 9 / 255.0F;
//			 
//			 color2[0] = new Color(fR,fG,fB,.40f);
//			 color2[1] = new Color(fR,fG,fB,.12f);
//		
//			 fraction2[0] = 0.10f;
//			 fraction2[1] = 0.96f;
//
//			 RadialGradientPaint gPaint2 = new RadialGradientPaint(centerX,centerY,(float) (gp.player.currentLight.lightRadius - 0.15),fraction2,color2);
//			 
//			 g2.setPaint(gPaint2);
//			 g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        //CRIAR EFEITO DE GRADIACAO
        Color color[] = new Color[12]; //QUANTO MAIOR FOR O NUMERO VARIAVEIS DENTRO DO ARRAY, MAIS DETALHES TERÁ
        float fraction[] = new float[12]; // *DISTANCIA ENTRE AS CORES, DO CENTRO A BORDA(MENOR AO MAIOR)
        
        color[0] = new Color(0, 0, 0, 0.1f);
        color[1] = new Color(0, 0, 0, 0.42f);
        color[2] = new Color(0, 0, 0, 0.52f);
        color[3] = new Color(0, 0, 0, 0.61f);
        color[4] = new Color(0, 0, 0, 0.69f);
        color[5] = new Color(0, 0, 0, 0.76f);
        color[6] = new Color(0, 0, 0, 0.82f);
        color[7] = new Color(0, 0, 0, 0.87f);
        color[8] = new Color(0, 0, 0, 0.91f);
        color[9] = new Color(0, 0, 0, 0.94f);
        color[10] = new Color(0, 0, 0, 0.96f);
        color[11] = new Color(0, 0, 0, 0.98f);
        
        fraction[0] = 0f;
        fraction[1] = 0.4f;
        fraction[2] = 0.5f;
        fraction[3] = 0.6f;
        fraction[4] = 0.65f;
        fraction[5] = 0.7f;
        fraction[6] = 0.75f;
        fraction[7] = 0.8f;
        fraction[8] = 0.85f;
        fraction[9] = 0.9f;
        fraction[10] = 0.95f;
        fraction[11] = 1f;
        
        //CRIAR OPCOES DE DESENHO DO GRADIENTE
        RadialGradientPaint gPaint = new RadialGradientPaint(centerX, centerY, gp.player.currentLight.lightRadius,
                                                    fraction, color);
        //COLOCAR OS DADOS DO GRADIENTE NO G2
        g2.setPaint(gPaint);
        }
        
        //DESENHA UM RECTANGULO SEM A AREA DE LUZ
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        g2.dispose();
    }
    
    public void update() {
        
        if(gp.player.lightUpdated == true) {
            setLightSource();
            gp.player.lightUpdated = false;
        }
    }
    
    public void draw(Graphics2D g2) {
        
        g2.drawImage(darknessFilter, 0, 0, null);
    }
}
