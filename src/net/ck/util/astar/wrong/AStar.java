//package net.ck.util.astar.wrong;
//
//import net.ck.game.map.MapTile;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import java.util.*;
//
//public class AStar
//{
//
//    private final Logger logger = LogManager.getLogger(getRealClass());
//
//    public Class<?> getRealClass()
//    {
//        Class<?> enclosingClass = getClass().getEnclosingClass();
//        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
//    }
//
//    public static MapTile aStar(MapTile start, MapTile target){
//        PriorityQueue<MapTile> closedList = new PriorityQueue<>();
//        PriorityQueue<MapTile> openList = new PriorityQueue<>();
//
//        start.finalCost = start.g + start.calculateHeuristic(target);
//        openList.add(start);
//
//        while(!openList.isEmpty()){
//            MapTile n = openList.peek();
//            if(n == target){
//                return n;
//            }
//
//            for(MapTile.Edge edge : n.neighbors){
//                MapTile m = edge.node;
//                double totalWeight = n.g + edge.weight;
//
//                if(!openList.contains(m) && !closedList.contains(m)){
//                    m.parent = n;
//                    m.g = totalWeight;
//                    m.finalCost = m.g + m.calculateHeuristic(target);
//                    openList.add(m);
//                } else {
//                    if(totalWeight < m.g){
//                        m.parent = n;
//                        m.g = totalWeight;
//                        m.finalCost = m.g + m.calculateHeuristic(target);
//
//                        if(closedList.contains(m)){
//                            closedList.remove(m);
//                            openList.add(m);
//                        }
//                    }
//                }
//            }
//
//            openList.remove(n);
//            closedList.add(n);
//        }
//        return null;
//    }
//
//    public static void printPath(MapTile target){
//        MapTile n = target;
//
//        if(n==null)
//            return;
//
//        List<Integer> ids = new ArrayList<>();
//
//        while(n.parent != null){
//            ids.add(n.getId());
//            n = n.parent;
//        }
//        ids.add(n.getId());
//        Collections.reverse(ids);
//
//        for(int id : ids){
//            System.out.print(id + " ");
//        }
//        System.out.println("");
//    }
//
///*    public static void main(String[] args) {
//        Node head = new Node(3);
//        head.g = 0;
//
//        Node n1 = new Node(2);
//        Node n2 = new Node(2);
//        Node n3 = new Node(2);
//
//        head.addBranch(1, n1);
//        head.addBranch(5, n2);
//        head.addBranch(2, n3);
//        n3.addBranch(1, n2);
//
//        Node n4 = new Node(1);
//        Node n5 = new Node(1);
//        Node target = new Node(0);
//
//        n1.addBranch(7, n4);
//        n2.addBranch(4, n5);
//        n3.addBranch(6, n4);
//
//        n4.addBranch(3, target);
//        n5.addBranch(1, n4);
//        n5.addBranch(3, target);
//
//        Node res = aStar(head, target);
//        printPath(res);
//    }*/
//}
