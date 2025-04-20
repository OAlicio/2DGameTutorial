package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Axe;
import object.OBJ_Boots;
import object.OBJ_Chest;
import object.OBJ_Door;
import object.OBJ_Key;
import object.OBJ_Lantern;
import object.OBJ_Potion_Red;
import object.OBJ_Shield_Blue;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword_Normal;
import object.OBJ_Tent;

public class SaveLoad {

    GamePanel gp;

    public SaveLoad(GamePanel gp) {
        this.gp = gp;
    }

    public void save() {
        
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("save.dat")));
        
            DataStorage ds = new DataStorage();

            // STATUS DO JOGADOR
            ds.level = gp.player.level;
            ds.maxLife = gp.player.maxLife;
            ds.life = gp.player.life;
            ds.maxMana = gp.player.maxMana;
            ds.mana = gp.player.mana;
            ds.strength = gp.player.strength;
            ds.dexterity = gp.player.dexterity;
            ds.exp = gp.player.exp;
            ds.nextLevelExp = gp.player.nextLevelExp;
            ds.coin = gp.player.coin;

            // INVENTARIO DO JOGADOR
            for(int i = 0; i < gp.player.inventory.size(); i++) {
                ds.itemNames.add(gp.player.inventory.get(i).name);
                ds.itemAmounts.add(gp.player.inventory.get(i).amount);
            }

            // ITEM EQUIPADO
            ds.currentWeaponSlot = gp.player.getCurrentWeaponSlot();
            ds.currentShieldSlot = gp.player.getCurrentShieldSlot();

            // OBJECTOS NO MAPA
            ds.mapObjectNames = new String[gp.maxMap][gp.obj[1].length];
            ds.mapObjectWorldX = new int[gp.maxMap][gp.obj[1].length];
            ds.mapObjectWorldY = new int[gp.maxMap][gp.obj[1].length];
            ds.mapObjectLootNames = new String[gp.maxMap][gp.obj[1].length];
            ds.mapObjectOpened = new boolean[gp.maxMap][gp.obj[1].length];

            for(int mapNum = 0; mapNum < gp.maxMap; mapNum++) {
                for(int i = 0; i < gp.obj[1].length; i++) {

                    if(gp.obj[mapNum][i] == null) {
                        ds.mapObjectNames[mapNum][i] = "NA";
                    }
                    else {
                        ds.mapObjectNames[mapNum][i] = gp.obj[mapNum][i].name;
                        ds.mapObjectWorldX[mapNum][i] = gp.obj[mapNum][i].worldX;
                        ds.mapObjectWorldY[mapNum][i] = gp.obj[mapNum][i].worldY;
                        if(gp.obj[mapNum][i].loot != null) {
                            ds.mapObjectLootNames[mapNum][i] = gp.obj[mapNum][i].loot.name;
                        }
                        ds.mapObjectOpened[mapNum][i] = gp.obj[mapNum][i].opened;
                    }
                }
            }

            // Salva o objecto DataStorage
            oos.writeObject(ds);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Save Exception!");
        }
    }

    public void load() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("save.dat")));

            // Ler o Objecto DataStorage
            DataStorage ds = (DataStorage)ois.readObject();

            // STATUS DO JOGADOR
            gp.player.level = ds.level;
            gp.player.maxLife = ds.maxLife;
            gp.player.life = ds.life;
            gp.player.maxMana = ds.maxMana;
            gp.player.mana = ds.mana;
            gp.player.strength = ds.strength;
            gp.player.dexterity = ds.dexterity;
            gp.player.exp = ds.exp;
            gp.player.nextLevelExp = ds.nextLevelExp;
            gp.player.coin = ds.coin;

            // INVENTARIO DO JOGADOR
            gp.player.inventory.clear();
            for(int i = 0; i < ds.itemNames.size(); i++) {
                gp.player.inventory.add(gp.eGenerator.getObject(ds.itemNames.get(i)));
                gp.player.inventory.get(i).amount = ds.itemAmounts.get(i);
            }

            // ITEM EQUIPADO
            gp.player.currentWeapon = gp.player.inventory.get(ds.currentWeaponSlot);
            gp.player.currentShield = gp.player.inventory.get(ds.currentShieldSlot);
            gp.player.getAttack();
            gp.player.getDefense();
            gp.player.getAttackImage();

            // OBJECTOS NO MAPA
            for(int mapNum = 0; mapNum < gp.maxMap; mapNum++) {

                for(int i = 0; i < gp.obj[1].length; i++) {
                    
                    if(ds.mapObjectNames[mapNum][i].equals("NA")) {
                        gp.obj[mapNum][i] = null;
                    }
                    else {
                        gp.obj[mapNum][i] = gp.eGenerator.getObject(ds.mapObjectNames[mapNum][i]);
                        gp.obj[mapNum][i].worldX = ds.mapObjectWorldX[mapNum][i];
                        gp.obj[mapNum][i].worldY = ds.mapObjectWorldY[mapNum][i];
                        if(ds.mapObjectLootNames[mapNum][i] != null) {
                            gp.obj[mapNum][i].loot = gp.eGenerator.getObject(ds.mapObjectLootNames[mapNum][i]);
                        }
                        gp.obj[mapNum][i].opened = ds.mapObjectOpened[mapNum][i];
                        if(gp.obj[mapNum][i].opened == true) {
                            gp.obj[mapNum][i].image = gp.obj[mapNum][i].image2;
                        }
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Load Exception!");
        }
    }
}
