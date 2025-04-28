package entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GamePanel;
import main.KeyHandler;
import object.OBJ_Axe;
import object.OBJ_Fireball;
import object.OBJ_Key;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword_Normal;
import object.OBJ_Tent;

public class Player extends Entity {
    
    KeyHandler keyH;
    
    public final int screenX; //ONDE DESENHAREMOS O PLAYER? QUEREMOS ELE NO CENTRO DA TELA
    public final int screenY;
    int standCounter = 0; //AJUSTAR O TIMING DO RELEASE BUTTON
    boolean moving = false;
    int pixelCounter = 0;
    public boolean attackCanceled = false;
    public boolean lightUpdated = false;
    
    public Player(GamePanel gp, KeyHandler keyH) {
    
        super(gp); //ATRIBUI GP AO CONSTRUTOR DA CLASSE MAE
        
        this.keyH = keyH;
        
        // CENTRALIZANDO O PLAYER
        screenX = gp.screenWidth/2 - gp.tileSize/2;
        screenY = gp.screenHeight/2 - gp.tileSize/2;
        //-----------------------//
        
        //COLISAO
        solidArea = new Rectangle(); //Rectangle(x, y, width, height)
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x; //SALVANDO OS VALORES DE SOLIDAREAX E Y, CASO SEJAM MODIFICADAS NO FUTURO
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;
        //------------//
        
        //AREA DE ATAQUE
//        attackArea.width = 36;
//        attackArea.height = 36;
        
        // ------------- //
        
        setDefaultValues();
    }

    public void setDefaultValues() {
    
        //POSICOES INICIAIS DO PLAYER
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
//        worldX = gp.tileSize * 34;
//        worldY = gp.tileSize * 43;
        defaultSpeed = 4;
        speed = defaultSpeed;
        direction = "down"; //DIRECAO PADRAO PODE SER QUALQUER UMA
        
        //PLAYER STATUS
        level = 1;
        maxLife = 6;
        life = maxLife;
        maxMana = 4;
        mana = maxMana;
        ammo = 10; //PARA ROCK
        strength = 1; //QUANTO MAIS FORTE FOR, MAIS DANO DARA
        dexterity = 1; //QUANTO MAIS DESTREZA TIVER, MENOS DANO TOMA
        exp = 0;
        nextLevelExp = 5;
        coin = 500;
        currentWeapon = new OBJ_Sword_Normal(gp);
        currentShield = new OBJ_Shield_Wood(gp);
        currentLight = null;
        projectile = new OBJ_Fireball(gp); // USA mana
        //projectile = new OBJ_Rock(gp); // USA ammo;
        attack = getAttack(); //O VALOR DO ATAQUE TOTAL E DECIDIDO ATRAVES DA FORCA E ARMA
        defense = getDefense(); //O VALOR DA DEFESA TOTAL VALE A DESTREZA E O ESCUDO
        
        getImage();
        getAttackImage();
        getGuardImage();
        setItems();
        setDialogue();
    }
    
    public void setDefaultPositions() {
        
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        direction = "down";
    }
    
    public void setDialogue() {

        dialogue[0][0] = "You're level " + level + " now!\n"
                    + "You feel Stronger!";
    }

    public void restoreStatus() {
        
        life = maxLife;
        mana = maxMana;
        invincible = false;
        transparent = false;
        attacking = false;
        guarding = false;
        knockBack = false;
        lightUpdated = true;
        speed = defaultSpeed;
    }
    
    public void setItems() {
        
        inventory.clear();
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(new OBJ_Axe(gp));
        inventory.add(new OBJ_Key(gp));
        inventory.add(new OBJ_Key(gp));
        inventory.add(new OBJ_Tent(gp));
    }
    
    public int getAttack() {
        
        attackArea = currentWeapon.attackArea;
        motion1_duration = currentWeapon.motion1_duration;
        motion2_duration = currentWeapon.motion2_duration;
        return attack = strength * currentWeapon.attackValue;
    }  
    
    public int getCurrentWeaponSlot() {
        int currentWeaponSlot = 0;
        for(int i = 0; i < inventory.size(); i++) {
            if(inventory.get(i) == currentWeapon) {
                currentWeaponSlot = i;
            }
        }
        return currentWeaponSlot;
    }

    public int getCurrentShieldSlot() {
        int currentShieldSlot = 0;
        for(int i = 0; i < inventory.size(); i++) {
            if(inventory.get(i) == currentShield) {
                currentShieldSlot = i;
            }
        }
        return currentShieldSlot;
    }


    public int getDefense() {
        
        return defense = dexterity * currentShield.defenseValue;
    } 
    
    public void getImage() {
    
        up1 = setup("/player/boy_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/player/boy_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/player/boy_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/player/boy_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/player/boy_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/player/boy_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("/player/boy_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/player/boy_right_2", gp.tileSize, gp.tileSize);
    }
    
    public void getSleepingImage(BufferedImage image) {
        up1 = image;
        up2 = image;
        down1 = image;
        down2 = image;
        left1 = image;
        left2 = image;
        right1 = image;
        right2 = image;
    }
    
    public void getAttackImage() {
        
        if(currentWeapon.type == type_sword) {
            attackUp1 = setup("/player/boy_attack_up_1", gp.tileSize, gp.tileSize * 2);
            attackUp2 = setup("/player/boy_attack_up_2", gp.tileSize, gp.tileSize * 2);
            attackDown1 = setup("/player/boy_attack_down_1", gp.tileSize, gp.tileSize * 2);
            attackDown2 = setup("/player/boy_attack_down_2", gp.tileSize, gp.tileSize * 2);
            attackLeft1 = setup("/player/boy_attack_left_1", gp.tileSize * 2, gp.tileSize);
            attackLeft2 = setup("/player/boy_attack_left_2", gp.tileSize * 2, gp.tileSize);
            attackRight1 = setup("/player/boy_attack_right_1", gp.tileSize * 2, gp.tileSize);
            attackRight2 = setup("/player/boy_attack_right_2", gp.tileSize * 2, gp.tileSize);
        }
        
        if(currentWeapon.type == type_axe) {
            attackUp1 = setup("/player/boy_axe_up_1", gp.tileSize, gp.tileSize * 2);
            attackUp2 = setup("/player/boy_axe_up_2", gp.tileSize, gp.tileSize * 2);
            attackDown1 = setup("/player/boy_axe_down_1", gp.tileSize, gp.tileSize * 2);
            attackDown2 = setup("/player/boy_axe_down_2", gp.tileSize, gp.tileSize * 2);
            attackLeft1 = setup("/player/boy_axe_left_1", gp.tileSize * 2, gp.tileSize);
            attackLeft2 = setup("/player/boy_axe_left_2", gp.tileSize * 2, gp.tileSize);
            attackRight1 = setup("/player/boy_axe_right_1", gp.tileSize * 2, gp.tileSize);
            attackRight2 = setup("/player/boy_axe_right_2", gp.tileSize * 2, gp.tileSize);
        }
    }
        
    public void getGuardImage() {
        
        guardUp = setup("/player/boy_guard_up", gp.tileSize, gp.tileSize);
        guardDown = setup("/player/boy_guard_down", gp.tileSize, gp.tileSize);
        guardLeft = setup("/player/boy_guard_left", gp.tileSize, gp.tileSize);
        guardRight = setup("/player/boy_guard_right", gp.tileSize, gp.tileSize);
    }
    
    @Override
    public void update() {
        
        if(knockBack == true) {
            
            collisionOn = false;
            gp.cChecker.checkTile(this);
            gp.cChecker.checkObject(this, true);
            gp.cChecker.checkEntity(this, gp.npc);
            gp.cChecker.checkEntity(this, gp.monster);
            gp.cChecker.checkEntity(this, gp.iTile);
            
            if(collisionOn == true) {
                knockBarCounter = 0;
                knockBack = false;
                speed = defaultSpeed;
            }
            else if(collisionOn == false) {
                switch(knockBackDirection) {
                    case "up":
                        worldY -= speed;
                        break;
                    case "down":
                        worldY += speed;
                        break;
                    case "left":
                        worldX -= speed;
                        break;
                    case "right":
                        worldX += speed;
                        break;
                }
            }
            knockBarCounter++;
            if(knockBarCounter == 10) {
                knockBarCounter = 0;
                knockBack = false;
                speed = defaultSpeed;
            }
        }
        else if(attacking == true) {
            attacking();
        }
        else if(gp.keyH.spacePressed == true) {
            guarding = true;
            guardCounter++;
        }
        //ATUALIZAR AS COORDENADAS DO JOGADOR
        else if(keyH.upPressed == true || keyH.downPressed == true 
                || keyH.leftPressed == true || keyH.rightPressed == true || keyH.enterPressed == true) { // Previne da imagem se mexer sem dar nenhum comando
            
            if (keyH.upPressed == true) {
                direction = "up";
            }
            
            else if(keyH.downPressed == true) {
                direction = "down";
            }
            
            else if(keyH.leftPressed == true) {
                direction = "left";
            }
            
            else if(keyH.rightPressed == true) {
                direction = "right";
            }
            
            //VERIFICAR COLISAO COM OS TILES
            collisionOn = false;
            gp.cChecker.checkTile(this);
            // ---------------------------- //
            
            //VERIFICAR COLISAO COM OS OBJECTOS
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);
            // ------------------------------ //
            
            //VERIFICAR COLISAO COM NPC
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);
            // ---------------------- //
            
            //VERIIFCAR COLISAO COM MONSTROS
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            monsterDamage(monsterIndex);
            // --------------------------- //
            
            //VERIIFCAR COLISAO COM TILES INTERACTIVOS
            int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile); //NAO NECESSARIO O INDEX, PODE SER FEITO SEM SER AQUI
            // --------------------------- //
            
            // VERIFICA OS EVENTOS
            gp.eHandler.checkEvent();
            // --------------------- //
            
            // SE A COLISAO FOR FALSA, O PLAYER NAO SE MOVE
            if(collisionOn == false && keyH.enterPressed == false) {
            
                switch(direction) {
                    
                    case "up":
                        worldY -= speed; // QUANTO MAIS PRA CIMA MENOS O Y VALE MENOS EM 4 PIXELS(SPEED)
                        break;
                    case "down":
                        worldY += speed;
                        break;
                    case "left":
                        worldX -= speed;
                        break;
                    case "right":
                        worldX += speed;
                        break;
                }
            }
            
            if(keyH.enterPressed == true && attackCanceled == false) {
                gp.playSE(7);
                attacking = true;
                spriteCounter = 0;
            }
            
            attackCanceled = false;
            keyH.enterPressed = false;
            guarding = false;
            guardCounter = 0;
                    
            spriteCounter++;
            if(spriteCounter >= 14) { //A CADA 14 FRAMES TROCA A IMAGEM PELO SPRITENUM
                if(spriteNum == 1){
                    spriteNum = 2;
                }
                else if(spriteNum == 2){
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }   
        }
        else { //COLOCANDO O PLAYER NUMA POSICAO NORMAL
        
            standCounter++;
            
            if(standCounter == 20) { //A CADA x FRAMES O PLAYER VOLTA AO FRAME INICIAL APOS O BOTAO DEIXAR DE SER PRESSIONADO
                spriteNum = 1;
                standCounter = 0;
            }
            guarding = false;
            guardCounter = 0;
        }
       
        //PROJECTEIS
        if(gp.keyH.shotKeyPressed == true && projectile.alive == false 
                && shotAvailableCounter == 30 && projectile.haveResource(this) == true) {
            
            //COLOCA OS VALORES PADRAO DA COORDENADA, DIRECAO,STATUS E USUARIO
            projectile.set(worldX, worldY, direction, true, this);
            
            //REDUZIR A MANA
            projectile.subtrackResource(this);
            
            //VERIFICAR DISPONIBILIDADE
            for(int i = 0; i < gp.projectile[1].length; i++) { //VERIFICA QUAL SLOT TEM DISPONIBILIDADE PARA O PROJECTIL
                if(gp.projectile[gp.currentMap][i] == null) {
                    gp.projectile[gp.currentMap][i] = projectile;
                    break;
                }
            }
    
            shotAvailableCounter = 0;
            
            gp.playSE(10);
        }
        
        // PRECISA ESTAR FORA DO IF STATEMENT DAS KEYBINDS DE MOVIMENTACAO
        if(invincible == true) {
            
            invincibleCounter++;
            
            if(invincibleCounter == 60) {
                invincible = false;
                transparent = false;
                invincibleCounter = 0;
            }
        }
        
        if(shotAvailableCounter < 30) {
            shotAvailableCounter++;
        }
        
        if(life > maxLife) {
            life = maxLife;
        }
        
        if(mana > maxMana) {
            mana = maxMana;
        }
        
        if(life <= 0) {
            gp.gameState = gp.gameOverState;
            gp.ui.commandNum = -1;
            gp.stopMusic();
            gp.playSE(12);
        }
    }
    
    public void pickUpObject (int i) {
        
        if(i != 999) {
        
            //APENAS PEGA ITENS
            if(gp.obj[gp.currentMap][i].type == type_pickupOnly) {
                
                gp.obj[gp.currentMap][i].use(this);
                gp.obj[gp.currentMap][i] = null;
            }
            //OBSTACULO
            else if(gp.obj[gp.currentMap][i].type == type_obstacle) {
                if(keyH.enterPressed == true) {
                    attackCanceled = true;
                    gp.obj[gp.currentMap][i].interact();
                }
            }
            //ITENS DO INVENTARIO
            else {
                
                String text;

                if(canObtainItem(gp.obj[gp.currentMap][i]) == true) {
                    gp.playSE(1);
                    text = "Got a " + gp.obj[gp.currentMap][i].name + "!";
                }
                else {
                    text = "You cannot carry any more item!";
                }
                gp.ui.addMessage(text);
                gp.obj[gp.currentMap][i] = null;
            }
        }
    }
    
    public void interactNPC(int i) {
        
        if(i != 999) {

            if(gp.keyH.enterPressed == true) {
                attackCanceled = true;
                gp.npc[gp.currentMap][i].speak();
            }
        }  
    }
    
    //MONSTRO PRA PLAYER
    public void monsterDamage(int i) {
        
        if(i != 999) {
            
            if(invincible == false && gp.monster[gp.currentMap][i].dying == false) {
                
                gp.playSE(6);
                
                int damage = gp.monster[gp.currentMap][i].attack - defense;
                
                if(damage < 1) {
                    damage = 1;
                }
                
                life -= damage;
                invincible = true;
                transparent = true;
            }
        }
    }
    
    //PLAYER PRA MONSTRO
    public void damageMonster(int i, Entity attacker, int attack, int knockBackPower) {
        
        if(i != 999) {
            
            if(gp.monster[gp.currentMap][i].invincible == false) {
                
                gp.playSE(5);
                
                if(knockBackPower > 0) {
                    setKnockBack(gp.monster[gp.currentMap][i], attacker, knockBackPower);
                }
                
                //Dano extra, caso o inimigo tomou parry
                if(gp.monster[gp.currentMap][i].offBalance == true) {
                    attack *= 5;
                }
                
                int damage = attack - gp.monster[gp.currentMap][i].defense;
                
                if(damage < 0) {
                    damage = 0;
                }
                
                gp.monster[gp.currentMap][i].life -= damage;
                gp.ui.addMessage(damage + " damage");
                
                gp.monster[gp.currentMap][i].invincible = true;
                gp.monster[gp.currentMap][i].damageReaction();
                
                if(gp.monster[gp.currentMap][i].life <= 0) {
                    gp.monster[gp.currentMap][i].dying = true;
                    gp.ui.addMessage("Killed the " + gp.monster[gp.currentMap][i].name + "!");
                    gp.ui.addMessage("Exp + " + gp.monster[gp.currentMap][i].exp + "!");
                    exp += gp.monster[gp.currentMap][i].exp;
                    checkLevelUp();
                }
            }
        }
    }
    
    public void damageInteractiveTile(int i) {
        
        if (i != 999 && gp.iTile[gp.currentMap][i].destructible == true 
                && gp.iTile[gp.currentMap][i].isCorrectItem(this) == true && gp.iTile[gp.currentMap][i].invincible == false) {
            
            gp.iTile[gp.currentMap][i].playSE();
            
            gp.iTile[gp.currentMap][i].life--;
            gp.iTile[gp.currentMap][i].invincible = true;
            
            //GERAR PARTICULAS
            generateParticle(gp.iTile[gp.currentMap][i], gp.iTile[gp.currentMap][i]);
            
            if(gp.iTile[gp.currentMap][i].life == 0) {
                
                gp.iTile[gp.currentMap][i] = gp.iTile[gp.currentMap][i].getDestroyedForm();
                gp.ui.addMessage("Arvore quebrada");
            }
        }
    }
    
    public void damageProjectile(int i) { //DESTRUIR PROJECTEIS
        
        if(i != 999) {
            
            Entity projectile = gp.projectile[gp.currentMap][i];
            projectile.alive = false;
            generateParticle(projectile, projectile);
        }
    }
    
    public void checkLevelUp() {
        
        if(exp >= nextLevelExp) {
            
            level++;
            nextLevelExp *= 2; 
            maxLife += 2;
            strength++;
            dexterity++;
            attack = getAttack();
            defense = getDefense();
            
            gp.playSE(8);
            setDialogue();
            startDialogue(this, 0);
        }
    }
    
    public void selectItem() {
        
        int itemIndex = gp.ui.getItemIndexSlot(gp.ui.playerSlotCol, gp.ui.playerSlotRow);
        
        if(itemIndex < inventory.size()) {
            
            Entity selectedItem = inventory.get(itemIndex);
            
            if(selectedItem.type == type_sword || selectedItem.type == type_axe) {
                
                currentWeapon = selectedItem;
                attack = getAttack();
                getAttackImage();
            }
            
            if(selectedItem.type == type_shield) {
                
                currentShield = selectedItem;
                defense = getDefense();
            }
            
            if(selectedItem.type == type_light) {
                
                if(currentLight == selectedItem) {
                    currentLight = null;
                }
                else {
                    currentLight = selectedItem;
                }
                lightUpdated = true;
            }
            
            if(selectedItem.type == type_consumable) {
                
                if(selectedItem.use(this) == true) {
                    if(selectedItem.amount > 1) {
                        selectedItem.amount--;
                    }
                    else {
                        inventory.remove(itemIndex);
                    }
                }
            }
        }
    }

    public int searchItemInInventory(String itemName) {
        
        int itemIndex = 999;
        
        for(int i = 0; i < inventory.size(); i++) {
            if(inventory.get(i).name.equals(itemName)) {
                itemIndex = i;
                break;
            }
        }
        return itemIndex;
    }
    
    public boolean canObtainItem(Entity item) {
        
        boolean canObtain = false;
        
        Entity newItem = gp.eGenerator.getObject(item.name);

        //VERIFICAR SE O ITEM E STACKAVEL
        if(newItem.stackable == true) {
            
            int index =  searchItemInInventory(newItem.name);
            
            if(index != 999) {
                inventory.get(index).amount++;
                canObtain = true;
            }
            else { //NOVO ITEM PRECISA VERIFICAR SE HA ESPACO NO INVENTARIO
                if(inventory.size() != maxInventorySize) {
                    inventory.add(newItem);
                    canObtain = true;
                }
            }
        }
        else { //NAO E STACKAVEL
            if(inventory.size() != maxInventorySize) {
                    inventory.add(newItem);
                    canObtain = true;
                }
        }
        return canObtain;
    }
    
    @Override
    public void draw(Graphics2D g2) {
        
        BufferedImage image = null;
        int tempScreenX = screenX; //RESOLVE O PROBLEMA DOS SPRITES ESTAREM EM LOCALIZACOES DIFERENTES APOS O ATAQUE
        int tempScreenY = screenY;
        
        //CENTRALIZANDO O PLAYER
        switch(direction) {
            
        case "up":
            
            if(attacking == false) {
                if(spriteNum == 1) {
                    image = up1;
                }
                if(spriteNum == 2) {
                    image = up2;
                } 
            }
            
            if(attacking == true) {
                tempScreenY = screenY - gp.tileSize;
                if(spriteNum == 1) {
                    image = attackUp1;
                }
                if(spriteNum == 2) {
                    image = attackUp2;
                } 
            }
            
            if(guarding == true) {
                image = guardUp;
            }
            break;
            
        case "down":
            
            if(attacking == false) {
                if(spriteNum == 1) {
                    image = down1;
                }
                if(spriteNum == 2) {
                    image = down2;
                } 
            }
            
            if(attacking == true) {
                if(spriteNum == 1) {
                    image = attackDown1;
                }
                if(spriteNum == 2) {
                    image = attackDown2;
                } 
            }
            if(guarding == true) {
                image = guardDown;
            }
            break;
            
        case "left":
            
            if(attacking == false) {
                if(spriteNum == 1) {
                image = left1;
                }
                if(spriteNum == 2) {
                    image = left2;
                }
            }
            
            if(attacking == true) {
                tempScreenX = screenX - gp.tileSize;
                if(spriteNum == 1) {
                    image = attackLeft1;
                }
                if(spriteNum == 2) {
                    image = attackLeft2;
                }
            }
            
            if(guarding == true) {
                image = guardLeft;
            }
            break;
            
        case "right":
            
            if(attacking == false) {
                if(spriteNum == 1) {
                    image = right1;
                }
                if(spriteNum == 2) {
                    image = right2;
                } 
            }
            
            if(attacking == true) {
                if(spriteNum == 1) {
                    image = attackRight1;
                }
                if(spriteNum == 2) {
                    image = attackRight2;
                }
            }
            
            if(guarding == true) {
                image = guardRight;
            }
                        
            break; 
        }
        
        if(transparent == true) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f /*O QUAO TRANSPARENTE FICARA A IMAGEM*/));
        }
        
        g2.drawImage(image, tempScreenX, tempScreenY, null); //drawImage(imagem, x, y, width, height, ImageObserver) DESENHA UMA IMAGEM

        //RESET ALPHA 
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
         
        //COLISAO
//        g2.setColor(Color.red);
//        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        // --------------- //
        
        //DEBUG PARA TEMPO DE INVENCIBILIDADE
//        g2.setFont(new Font("Arial", Font.PLAIN, 25));
//        g2.setColor(Color.white);
//        g2.drawString("Invincible: " + invincibleCounter, 10, 400);
    }
}
