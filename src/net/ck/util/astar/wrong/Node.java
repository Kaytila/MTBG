package net.ck.util.astar.wrong;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Node implements Comparable<Node>
{

    // Id for readability of result purposes
    private static int idCounter = 0;
    private final Logger logger = LogManager.getLogger(getRealClass());
    public int id;
    // Parent in the path
    public Node parent = null;
    public List<Edge> neighbors;
    // Evaluation functions
    public double f = Double.MAX_VALUE;
    public double g = Double.MAX_VALUE;
    // Hardcoded heuristic
    public double h;
    Node(double h)
    {
        this.h = h;
        this.id = idCounter++;
        this.neighbors = new ArrayList<>();
    }

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }

    @Override
    public int compareTo(Node n)
    {
        return Double.compare(this.f, n.f);
    }

    public void addBranch(int weight, Node node)
    {
        Edge newEdge = new Edge(weight, node);
        neighbors.add(newEdge);
    }

    public double calculateHeuristic(Node target)
    {
        return this.h;
    }

    public static class Edge
    {
        public int weight;
        public Node node;
        Edge(int weight, Node node)
        {
            this.weight = weight;
            this.node = node;
        }
    }
}
