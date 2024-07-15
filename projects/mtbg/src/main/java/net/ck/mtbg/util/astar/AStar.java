package net.ck.mtbg.util.astar;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.map.Map;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.util.utils.MapUtils;

import java.util.*;


/**
 * A Star Algorithm
 *
 * @author Marcelo Surriabre
 * @version 2.1, 2017-02-23
 * <p>
 * adjusted claus 2023
 * <a href="https://github.com/marcelo-s/A-Star-Java-Implementation">https://github.com/marcelo-s/A-Star-Java-Implementation</a>
 */
@Log4j2
public class AStar
{
    private static final int DEFAULT_HV_COST = 1; // Horizontal - Vertical Cost
    private static final int DEFAULT_DIAGONAL_COST = 1000;

    @Getter
    @Setter
    private static int hvCost;

    @Getter
    @Setter
    private static int diagonalCost;

    @Getter
    @Setter
    private static MapTile[][] searchArea;

    @Getter
    @Setter
    private static PriorityQueue<MapTile> openList;

    @Getter
    @Setter
    private static Set<MapTile> closedSet;

    @Getter
    @Setter
    private static MapTile initialNode;

    @Getter
    @Setter
    private static MapTile finalNode;

    @Getter
    @Setter
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
            if (getPreviousMap() != null)
            {
                logger.info("map is not null");
                if (getPreviousMap().equals(ma))
                {
                    logger.info("same map, do not initialize");
                    //same map, do nothing
                }
                else
                {
                    logger.info("new map, do initialize");
                    setNodesOld(rows, cols);
                    setPreviousMap(ma);
                }
            }
            else
            {
                logger.info("first map, do initialize");
                //setPreviousMap(ma);
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

    private static void setNodesOld(int rows, int cols)
    {
        long start;
        if (GameConfiguration.debugASTAR == true)
        {
            start = System.nanoTime();
        }
        searchArea = new MapTile[rows][cols];
        for (int row = 0; row < searchArea.length; row++)
        {
            for (int col = 0; col < searchArea[0].length; col++)
            {
                /**
                 * why was this the other way round? when did I break this?
                 * how did I not recognize this?
                 */
                MapTile node = MapUtils.getMapTileByCoordinates(col, row);
                node.setParent(null);
                Objects.requireNonNull(node).calculateHeuristic(getFinalNode());
                searchArea[row][col] = node;
            }
        }
        if (GameConfiguration.debugASTAR == true)
        {
            logger.info("time taken for set nodesOLD: {}", System.nanoTime() - start);
        }
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
                    logger.debug("ASTAR find path: {}", System.nanoTime() - start);
                }
                return getPath(currentNode);
            }
            else
            {
                addAdjacentNodes(currentNode);
            }
        }
        if (GameConfiguration.debugASTAR == true)
        {
            logger.debug("ASTAR find no path: {}", System.nanoTime() - start);
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

    private static void addAdjacentMiddleRow(MapTile currentNode)
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

    private static void checkNode(MapTile currentNode, int col, int row, int cost)
    {
        if (GameConfiguration.debugASTAR == true)
        {
            //logger.debug("current node: {}, col: {}, row: {}", currentNode, col, row);
        }
        MapTile adjacentNode = getSearchArea()[row][col];

        if (!getClosedSet().contains(adjacentNode))
        {
            if (GameConfiguration.debugASTAR == true)
            {
                //logger.debug("adjecent node: {}", adjacentNode.getMapPosition());
            }
            if (!adjacentNode.isBlocked() || adjacentNode.isOpenable())
            {
                if (GameConfiguration.debugASTAR == true)
                {
                    //logger.debug("adjecent node: {} is either not blocked or openable", adjacentNode.getMapPosition());
                }
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
    }

    private static boolean isFinalNode(MapTile currentNode)
    {
        return currentNode.equals(finalNode);
    }

    private static boolean isEmpty(PriorityQueue<MapTile> openList)
    {
        return openList.isEmpty();
    }

    private static void setBlock(int row, int col)
    {
        searchArea[row][col].setBlocked(true);
    }

}

