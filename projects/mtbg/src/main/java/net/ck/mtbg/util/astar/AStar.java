
package net.ck.mtbg.util.astar;

import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.map.Map;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.util.utils.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;


/**
 * A Star Algorithm
 *
 * @author Marcelo Surriabre
 * @version 2.1, 2017-02-23
 */

public class AStar
{
    private static final Logger logger = LogManager.getLogger(AStar.class);
    private static final int DEFAULT_HV_COST = 1; // Horizontal - Vertical Cost
    private static final int DEFAULT_DIAGONAL_COST = 1000;

    private static int hvCost;
    private static int diagonalCost;
    private static MapTile[][] searchArea;
    private static PriorityQueue<MapTile> openList;
    private static Set<MapTile> closedSet;
    private static MapTile initialNode;
    private static MapTile finalNode;

    private static Map previousMap;

    public static void initialize(int rows, int cols, MapTile initialNode, MapTile finalNode, Map map)
    {
        long start;
        if (GameConfiguration.debugASTAR == true)
        {
            start = System.nanoTime();
            logger.info("start initialize: ASTAR");
        }
        realInitialize(rows, cols, initialNode, finalNode, DEFAULT_HV_COST, DEFAULT_DIAGONAL_COST, map);
        if (GameConfiguration.debugASTAR == true)
        {
            logger.info("end initialize: ASTAR {}", System.nanoTime() - start);
        }
    }

    private static void realInitialize(int rows, int cols, MapTile initialNode, MapTile finalNode, int defaultHvCost, int defaultDiagonalCost, Map ma)
    {
        hvCost = hvCost;
        diagonalCost = diagonalCost;
        setInitialNode(initialNode);
        setFinalNode(finalNode);
        openList = new PriorityQueue<>(new Comparator<>()
        {
            @Override
            public int compare(MapTile node0, MapTile node1)
            {
                return Integer.compare(node0.getFinalCost(), node1.getFinalCost());
            }
        });

        if (GameConfiguration.debugASTAR == true)
        {
            if (previousMap != null)
            {
                if (previousMap.equals(ma))
                {
                    logger.info("same map, do not initialize");
                    //same map, do nothing
                }
                else
                {
                    logger.info("new map, do initialize");
                    //previousMap = ma;
                    setNodesOld(rows, cols);
                }
            }
            else
            {
                logger.info("first map, do initialize");
                //previousMap = ma;
                //TODO check implementation again to distinguish initialization and use
                setNodesOld(rows, cols);
            }
        }
        else
        {
            setNodesOld(rows, cols);
        }
        closedSet = new HashSet<>();
    }

    private static void setNodes(int rows, int cols)
    {
        long start = System.nanoTime();
        searchArea = Game.getCurrent().getCurrentMap().mapTiles.clone();
        for (int i = 0; i < searchArea.length; i++)
        {
            for (int j = 0; j < searchArea[0].length; j++)
            {
                searchArea[i][j].setParent(null);
                searchArea[i][j].calculateHeuristic(getFinalNode());
            }
        }
       // logger.info("time taken for set nodes: {}", System.nanoTime() - start);
    }


    private static void setNodesOld(int rows, int cols)
    {
        long start = System.nanoTime();
        searchArea = new MapTile[rows][cols];
        for (int i = 0; i < searchArea.length; i++)
        {
            for (int j = 0; j < searchArea[0].length; j++)
            {
                MapTile node = MapUtils.getMapTileByCoordinates(j, i);
                node.setParent(null);
                Objects.requireNonNull(node).calculateHeuristic(getFinalNode());
                searchArea[i][j] = node;
            }
        }
        //logger.info("time taken for set nodes: {}", System.nanoTime() - start);
    }

    public static void setBlocks(int[][] blocksArray)
    {
        for (int[] ints : blocksArray)
        {
            int row = ints[0];
            int col = ints[1];
            setBlock(row, col);
        }
    }

    public static List<MapTile> findPath()
    {
        long start = System.nanoTime();
        openList.add(initialNode);
        while (!isEmpty(openList))
        {
            MapTile currentNode = openList.poll();
            closedSet.add(currentNode);
            if (isFinalNode(currentNode))
            {
                if (GameConfiguration.debugASTAR == true)
                {
                    logger.info("ASTAR find path: {}", System.nanoTime() - start);
                }
                return getPath(currentNode);
            }
            else
            {
                addAdjacentNodes(currentNode);
            }
        }
        return new ArrayList<>();
    }

    private static List<MapTile> getPath(MapTile currentNode)
    {
        List<MapTile> path = new ArrayList<>();
        path.add(currentNode);
        MapTile parent;
        while ((parent = currentNode.getParent()) != null)
        {
            //logger.info("moving through list");
            path.add(0, parent);
            currentNode = parent;
        }
        return path;
    }

    private static void addAdjacentNodes(MapTile currentNode)
    {
        addAdjacentUpperRow(currentNode);
        addAdjacentMiddleRow(currentNode);
        addAdjacentLowerRow(currentNode);
    }

    private static void addAdjacentLowerRow(MapTile currentNode)
    {
        int row = currentNode.getY();
        int col = currentNode.getX();
        int lowerRow = row + 1;
        if (lowerRow < getSearchArea().length)
        {
            if (col - 1 >= 0)
            {
                //checkNode(currentNode, col - 1, lowerRow, getDiagonalCost()); // Comment this line if diagonal movements are not allowed
            }
            if (col + 1 < getSearchArea()[0].length)
            {
                //checkNode(currentNode, col + 1, lowerRow, getDiagonalCost()); // Comment this line if diagonal movements are not allowed
            }
            checkNode(currentNode, col, lowerRow, getHvCost());
        }
    }

    private  static void addAdjacentMiddleRow(MapTile currentNode)
    {
        int row = currentNode.getY();
        int col = currentNode.getX();
        if (col - 1 >= 0)
        {
            checkNode(currentNode, col - 1, row, getHvCost());
        }
        if (col + 1 < getSearchArea()[0].length)
        {
            checkNode(currentNode, col + 1, row, getHvCost());
        }
    }

    private static void addAdjacentUpperRow(MapTile currentNode)
    {
        int row = currentNode.getY();
        int col = currentNode.getX();
        int upperRow = row - 1;
        if (upperRow >= 0)
        {
            if (col - 1 >= 0)
            {
                //checkNode(currentNode, col - 1, upperRow, getDiagonalCost()); // Comment this if diagonal movements are not allowed
            }
            if (col + 1 < getSearchArea()[0].length)
            {
                //checkNode(currentNode, col + 1, upperRow, getDiagonalCost()); // Comment this if diagonal movements are not allowed
            }
            checkNode(currentNode, col, upperRow, getHvCost());
        }
    }

    private  static void checkNode(MapTile currentNode, int col, int row, int cost)
    {
        //logger.info("current node: {}, col: {}, row: {}", currentNode, col, row);
        MapTile adjacentNode = getSearchArea()[row][col];
        if (!adjacentNode.isBlocked() && !getClosedSet().contains(adjacentNode))
        {
            if (!getOpenList().contains(adjacentNode))
            {
                adjacentNode.setNodeData(currentNode, cost);
                getOpenList().add(adjacentNode);
            }
            else
            {
                boolean changed = adjacentNode.checkBetterPath(currentNode, cost);
                if (changed)
                {
                    // Remove and Add the changed node, so that the PriorityQueue can sort again its
                    // contents with the modified "finalCost" value of the modified node
                    getOpenList().remove(adjacentNode);
                    getOpenList().add(adjacentNode);
                }
            }
        }
    }

    private  static boolean isFinalNode(MapTile currentNode)
    {
        return currentNode.equals(finalNode);
    }

    private  static boolean isEmpty(PriorityQueue<MapTile> openList)
    {
        return openList.size() == 0;
    }

    private static void setBlock(int row, int col)
    {
        searchArea[row][col].setBlocked(true);
    }

    public static MapTile getInitialNode()
    {
        return initialNode;
    }

    public  static void setInitialNode(MapTile iN)
    {
        initialNode = iN;
    }

    private static MapTile getFinalNode()
    {
        return finalNode;
    }

    private static void setFinalNode(MapTile fN)
    {
        finalNode = fN;
    }

    private static MapTile[][] getSearchArea()
    {
        return searchArea;
    }

    private static void setSearchArea(MapTile[][] sA)
    {
        searchArea = sA;
    }

    private static PriorityQueue<MapTile> getOpenList()
    {
        return openList;
    }

    private static void setOpenList(PriorityQueue<MapTile> oL)
    {
        openList = oL;
    }

    private static Set<MapTile> getClosedSet()
    {
        return closedSet;
    }

    public static void setClosedSet(Set<MapTile> cS)
    {
        closedSet = cS;
    }

    public static int getHvCost()
    {
        return hvCost;
    }

    public static void setHvCost(int hC)
    {
        hvCost = hC;
    }

    private static int getDiagonalCost()
    {
        return diagonalCost;
    }

    private static void setDiagonalCost(int dC)
    {
        diagonalCost = dC;
    }
}

