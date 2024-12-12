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
        
        int entityLeftCol = (entityLeftWorldX/gp.tileSize);
        int entityRightCol = entityRightWorldX/gp.tileSize;
        int entityTopRow = entityTopWorldY/gp.tileSize;
        int entityBottomRow = entityBottomWorldY/gp.tileSize;
        
        int tileNum1, tileNum2;
        
        switch(entity.direction) {
        
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                
                if(gp.tileM.tile[tileNum1].collision == true || 
                   gp.tileM.tile[tileNum2].collision == true) {
                    
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = ((entityBottomWorldY + entity.speed)/gp.tileSize);
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                
                if(gp.tileM.tile[tileNum1].collision == true || 
                   gp.tileM.tile[tileNum2].collision == true) {
                    
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                
                if(gp.tileM.tile[tileNum1].collision == true || 
                   gp.tileM.tile[tileNum2].collision == true) {
                    
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                
                if(gp.tileM.tile[tileNum1].collision == true || 
                   gp.tileM.tile[tileNum2].collision == true) {
                    
                    entity.collisionOn = true;
                }
                break;
        }
    }
    
    public int checkObject(Entity entity, boolean player) { //VERIFICADOR DE COLISAO OBJECT - ENTITY
    
        int index = 999; //PODE SER QUALQUER NUMERO DESDE QUE NAO ESTEJA NO INTERVALO DO ARRAY DO OBJECTO
        
        for(int i = 0; i < gp.obj.length; i++) {
            
            if(gp.obj[i] != null) {
            
                //PEGAR A POSICAO DA AREA SOLIDA DA ENTIDADE
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                
                //PEGAR A POSICAO DA AREA SOLIDA DA ENTIDADE
                gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
                gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;
                
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
                
                if(entity.solidArea.intersects(gp.obj[i].solidArea)){ //(.intersects)VERIFICA AUTOMATICAMENTE SE OS OBJECTOS ESTAO SE TOCANDO
                    if(gp.obj[i].collision == true) {
                        entity.collisionOn = true;
                    }
                    if(player == true) {
                        index = i;
                    }
                }
                
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.obj[i].solidArea.x = gp.obj[i].solidAreaDefaultX;
                gp.obj[i].solidArea.y = gp.obj[i].solidAreaDefaultY;
            }
        }
        return index;
    }
    
    //COLISAO DE NPC OU MONSTROS
    public int checkEntity(Entity entity, Entity[] target) {
        
        int index = 999; //PODE SER QUALQUER NUMERO DESDE QUE NAO ESTEJA NO INTERVALO DO ARRAY DO OBJECTO
        
        for(int i = 0; i < target.length; i++) {
            
            if(target[i] != null) {
            
                //PEGAR A POSICAO DA AREA SOLIDA DA ENTIDADE
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                
                //PEGAR A POSICAO DA AREA SOLIDA DA ENTIDADE
                target[i].solidArea.x = target[i].worldX + target[i].solidArea.x;
                target[i].solidArea.y = target[i].worldY + target[i].solidArea.y;
                
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
                
                if(entity.solidArea.intersects(target[i].solidArea)){ //(.intersects)VERIFICA AUTOMATICAMENTE SE OS OBJECTOS ESTAO SE TOCANDO
                    if(target[i] != entity) {
                        
                        entity.collisionOn = true;
                    index = i;
                    }
                }
                
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[i].solidArea.x = target[i].solidAreaDefaultX;
                target[i].solidArea.y = target[i].solidAreaDefaultY;
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
