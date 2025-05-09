package main;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
    
    Clip clip; //ABRIR FICHEIROS DE AUDIO
    URL soundURL[] = new URL[30]; //GUARDA O CAMINHO DO SOM A SER USADO
    FloatControl fc;
    int volumeScale = 3; //ESCALAS POSSIVEIS A ALTERAR NO VOLUME DO JOGO
    float volume;
    
    public Sound() {
    
        soundURL[0] = getClass().getResource("/sound/BlueBoyAdventure.wav");
        soundURL[1] = getClass().getResource("/sound/coin.wav");
        soundURL[2] = getClass().getResource("/sound/powerup.wav");
        soundURL[3] = getClass().getResource("/sound/unlock.wav");
        soundURL[4] = getClass().getResource("/sound/fanfare.wav");
        soundURL[5] = getClass().getResource("/sound/hitmonster.wav");
        soundURL[6] = getClass().getResource("/sound/receivedamage.wav");
        soundURL[7] = getClass().getResource("/sound/swingweapon.wav");
        soundURL[8] = getClass().getResource("/sound/levelup.wav");
        soundURL[9] = getClass().getResource("/sound/cursor.wav");
        soundURL[10] = getClass().getResource("/sound/burning.wav");
        soundURL[11] = getClass().getResource("/sound/cuttree.wav");
        soundURL[12] = getClass().getResource("/sound/gameover.wav");
        soundURL[13] = getClass().getResource("/sound/stairs.wav");
        soundURL[14] = getClass().getResource("/sound/sleep.wav");
        soundURL[15] = getClass().getResource("/sound/blocked.wav");
        soundURL[16] = getClass().getResource("/sound/parry.wav");
        soundURL[17] = getClass().getResource("/sound/speak.wav");
        soundURL[18] = getClass().getResource("/sound/Merchant.wav");
        soundURL[19] = getClass().getResource("/sound/Dungeon.wav");

    }
    
    public void setFile(int i) {
    
        try{
            //ABRIR UM FICHEIRO DE AUDIO EM JAVA
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
            fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN); //CONTROLAR O VOLUME, ACEITA VALORES ENTRE -80f A 6f
            checkVolume();
            
        }catch(UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Erro ao carregar som: " + soundURL[i]);
            e.printStackTrace();
        }
    }
    
    public void play() {
        if(clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }
    
    public void loop() {
        if(clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    
    public void stop() {
    
        if(clip != null) {
            clip.stop();
        }
    }
    
    public void checkVolume() {
        
        switch(volumeScale) {
            
            case 0:
                volume = -80f;
                break;
            case 1:
                volume = -20;
                break;
            case 2:
                volume = -12;
                break;
            case 3:
                volume = -5f;
                break;
            case 4:
                volume = 1f;
                break;
            case 5:
                volume = 6f;
                break;
        }
        fc.setValue(volume);
    }
}
