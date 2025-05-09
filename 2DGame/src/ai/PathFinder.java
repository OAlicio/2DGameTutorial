package ai;

import entity.Entity;
import java.util.ArrayList;
import main.GamePanel;

public class PathFinder {

    GamePanel gp;
    Node[][] node;
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    boolean goalReached = false;
    int step = 0;
    
    public PathFinder(GamePanel gp) {
        
        this.gp = gp;
        
        instanciateNodes();
    }
    
    public void instanciateNodes() {
       
        node = new Node[gp.maxWorldCol][gp.maxWorldRow];
        
        int col = 0;
        int row = 0;
        
        while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
            
            node[col][row] = new Node(col, row);
            col++;
            if(col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }
    
    public void resetNodes() { //*RESETAR OS VALORES ANTERIOS DO PATHFIND
        
        int col = 0;
        int row = 0;
        
        while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
            
            //RESETA OPEN, CHECKED E SOLID STATE
            node[col][row].open = false;
            node[col][row].checked = false;
            node[col][row].solid = false;
            
            col++;
            if(col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
        
        //RESETA OUTRAS CONFIGURACOES
        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }
    
    //public void setNodes(int startCol, int startRow, int goalCol, int goalRow, Entity entity); -> Entity nao foi usado, mas pode
    
    public void seeiTiles() {
        
        for(int i = 0; i < gp.iTile[1].length; i++) {
                if(gp.iTile[gp.currentMap][i] != null && gp.iTile[gp.currentMap][i].destructible == true) {
                    int itCol = gp.iTile[gp.currentMap][i].worldX/gp.tileSize;
                    int itRow = gp.iTile[gp.currentMap][i].worldY/gp.tileSize;
                    node[itCol][itRow].solid = true;
                }
            }
    }
    
    public void setNodes(int startCol, int startRow, int goalCol, int goalRow) {
        
        resetNodes();
        
        // COLOCAR OS NODES DE INICIO E O DE FIM
        startNode = node[startCol][startRow];
        currentNode = startNode;
        goalNode = node[goalCol][goalRow];
        openList.add(currentNode);
        
        int col = 0;
        int row = 0;
        
        while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
            
            //NODES SOLIDOS
            //VERIFICA OS TILES
            int tileNum = gp.tileM.mapTileNum[gp.currentMap][col][row];
            if(gp.tileM.tile[tileNum].collision == true) {
                node[col][row].solid = true;
            }
            //VERIFICA OS TILES INTERACTIVOS
            //seeiTiles();
//            for(int i = 0; i < gp.iTile[1].length; i++) {
//                if(gp.iTile[gp.currentMap][i] != null && gp.iTile[gp.currentMap][i].destructible == true) {
//                    int itCol = gp.iTile[gp.currentMap][i].worldX/gp.tileSize;
//                    int itRow = gp.iTile[gp.currentMap][i].worldY/gp.tileSize;
//                    node[itCol][itRow].solid = true;
//                }
//            }
            
            //DEFINIR OS CUSTOS
            getCost(node[col][row]);
            col++;
            if(col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }
    
    public void getCost(Node node) {
        
        //G COST
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;
        
        //H COST
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;
        
        //F COST
        node.fCost = node.gCost + node.hCost;
    }
    
    public boolean search() {
        
        while(goalReached == false && step < 500) {
            int col = currentNode.col;
            int row = currentNode.row;
            
            //VERIFICAR O NODE ATUAL
            currentNode.checked = true;
            openList.remove(currentNode);
            
            //ABRIR O NODE ACIMA
            if(row - 1 >= 0) {
                openNode(node[col][row-1]);
            }
            //ABRIR O NODE A ESQUERDA
            if(col - 1 >= 0) {
                openNode(node[col-1][row]);
            }
            //ABRIR O NODE A BAIXO
            if(row + 1 < gp.maxWorldRow) {
                openNode(node[col][row+1]);
            }
            //ABRIR O NODE A DIREITA
            if(col + 1 < gp.maxWorldCol) {
                openNode(node[col+1][row]);
            }
            
            //ENCONTRA O MELHOR NODE
            int bestNodeIndex = 0;
            int bestNodefCost = 999;
            
            for(int i = 0; i < openList.size(); i++) {
                
                //VERIFiCA SE O F COST DO NODE E* O MELHOR
                if(openList.get(i).fCost < bestNodefCost) {
                    
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                }
                
                //SE O F COST SAO IGUAIS, VERIFICA O G COST
                else if(openList.get(i).fCost == bestNodefCost) {
                    if(openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }
            
            //SE NAO HA NODE NA OPENLIST, O LOOP TERMINA
            if(openList.isEmpty()) {
                break;
            }
            
            //APOS O LOOP
            currentNode = openList.get(bestNodeIndex);
            
            if(currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }
            step++;
        }
        
        return goalReached;
    }
    
    public void openNode(Node node) {
        
        if(node.open == false && node.checked == false && node.solid == false) {
            
            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }
    
    public void trackThePath() {
        
        Node current = goalNode;
        
        while(current != startNode) {
            
            pathList.add(0,current);
            current = current.parent;
        }
    }
}
