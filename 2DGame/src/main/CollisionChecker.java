package main;

import entity.Entity;

//VERIFICADOR DE COLISOES DAS ENTIDADES
public class CollisionChecker {
    
    GamePanel gp;
    public CollisionChecker(GamePanel gp) {
    
        this.gp = gp;
    }
    
    public void checkTile(Entity entity) {
    
        //ENCONTRANDO COL E ROW PRA COLISAO
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;
        
        int entityLeftCol = entityLeftWorldX/gp.tileSize;
        int entityRightCol = entityRightWorldX/gp.tileSize;
        int entityTopRow = entityTopWorldY/gp.tileSize;
        int entityBottomRow = entityBottomWorldY/gp.tileSize;
        
        int tileNum1, tileNum2;
        
        switch(entity.direction) {
        
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
                
                if(gp.tileM.tile[tileNum1].collision == true || 
                   gp.tileM.tile[tileNum2].collision == true) {
                    
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];
                
                if(gp.tileM.tile[tileNum1].collision == true || 
                   gp.tileM.tile[tileNum2].collision == true) {
                    
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];
                
                if(gp.tileM.tile[tileNum1].collision == true || 
                   gp.tileM.tile[tileNum2].collision == true) {
                    
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];
                
                if(gp.tileM.tile[tileNum1].collision == true || 
                   gp.tileM.tile[tileNum2].collision == true) {
                    
                    entity.collisionOn = true;
                }
                break;
        }
    }
    
    public int checkObject(Entity entity, boolean player) { //VERIFICADOR DE COLISAO OBJECT - ENTITY
    
        int index = 999; //PODE SER QUALQUER NUMERO DESDE QUE NAO ESTEJA NO INTERVALO DO ARRAY DO OBJECTO
        
        for(int i = 0; i < gp.obj[1].length; i++) {
            
            if(gp.obj[gp.currentMap][i] != null) {
            
                //PEGAR A POSICAO DA AREA SOLIDA DA ENTIDADE
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                
                //PEGAR A POSICAO DA AREA SOLIDA DA ENTIDADE
                gp.obj[gp.currentMap][i].solidArea.x = gp.obj[gp.currentMap][i].worldX + gp.obj[gp.currentMap][i].solidArea.x;
                gp.obj[gp.currentMap][i].solidArea.y = gp.obj[gp.currentMap][i].worldY + gp.obj[gp.currentMap][i].solidArea.y;
                
                switch(entity.direction) { //VERIFICANDO ONDE A ENTIDADE ESTARA
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        break;
                }
                
                if(entity.solidArea.intersects(gp.obj[gp.currentMap][i].solidArea)){ //(.intersects)VERIFICA AUTOMATICAMENTE SE OS OBJECTOS ESTAO SE TOCANDO
                    if(gp.obj[gp.currentMap][i].collision == true) {
                        entity.collisionOn = true;
                    }
                    if(player == true) {
                        index = i;
                    }
                }
                
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.obj[gp.currentMap][i].solidArea.x = gp.obj[gp.currentMap][i].solidAreaDefaultX;
                gp.obj[gp.currentMap][i].solidArea.y = gp.obj[gp.currentMap][i].solidAreaDefaultY;
            }
        }
        return index;
    }
    
    //COLISAO DE NPC OU MONSTROS
    public int checkEntity(Entity entity, Entity[][] target) {
        
        int index = 999; //PODE SER QUALQUER NUMERO DESDE QUE NAO ESTEJA NO INTERVALO DO ARRAY DO OBJECTO
        
        for(int i = 0; i < target[1].length; i++) {
            
            if(target[gp.currentMap][i] != null) {
            
                //PEGAR A POSICAO DA AREA SOLIDA DA ENTIDADE
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                
                //PEGAR A POSICAO DA AREA SOLIDA DA ENTIDADE
                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].worldX + target[gp.currentMap][i].solidArea.x;
                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].worldY + target[gp.currentMap][i].solidArea.y;
                
                switch(entity.direction) { //VERIFICANDO ONDE A ENTIDADE ESTARA
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        break;
                }
                
                if(entity.solidArea.intersects(target[gp.currentMap][i].solidArea)){ //(.intersects)VERIFICA AUTOMATICAMENTE SE OS OBJECTOS ESTAO SE TOCANDO
                    if(target[gp.currentMap][i] != entity) {
                    entity.collisionOn = true;
                    index = i;
                    }
                }
                
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].solidAreaDefaultX;
                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].solidAreaDefaultY;
            }
        }
        return index;
    }
    
    public boolean checkPlayer(Entity entity) {
        
        boolean contactPlayer = false;
        
        //PEGAR A POSICAO DA AREA SOLIDA DA ENTIDADE
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                
                //PEGAR A POSICAO DA AREA SOLIDA DA ENTIDADE
                gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
                gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
                
                switch(entity.direction) { //VERIFICANDO ONDE A ENTIDADE ESTARA
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        break;
                }
                
                if(entity.solidArea.intersects(gp.player.solidArea)){ //(.intersects)VERIFICA AUTOMATICAMENTE SE OS OBJECTOS ESTAO SE TOCANDO
                    entity.collisionOn = true;
                    contactPlayer = true;
                }
                
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.player.solidArea.x = gp.player.solidAreaDefaultX;
                gp.player.solidArea.y = gp.player.solidAreaDefaultY;
                
                return contactPlayer;
    }
}
