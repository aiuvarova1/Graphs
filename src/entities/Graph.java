package entities;

import javafx.animation.PathTransition;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import main.Drawer;
import main.PopupMessage;
import main.Visualizer;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Stack;
import java.util.function.Consumer;

/**
 * Represents the whole graph on the pane and stores
 * the list of all nodes
 */
public class Graph {
    public static final int MAX_SIZE = 50;
    private ArrayList<Node> nodes = new ArrayList<Node>(20);
    private static Graph instance;
    private Stack<Node> dfsStack = new Stack<Node>();

    private boolean showDistances = false;

    private Edge startEdge;
    private Node startNode;

    public Edge getStartEdge(){
        return startEdge;
    }

    public Node getStartNode(){
        return startNode;
    }

    public Node getNode(int i){
        return nodes.get(i);
    }

    public void setStartEdge(Edge start) {
        startEdge = start;
    }

    public void setStartNode(Node start) {
        startNode = start;
    }


    /**
     * Singleton
     * @return graph's instance
     */
    public static Graph getInstance() {
        if (instance == null) {
            instance = new Graph();
        }
        return instance;
    }

    public int getSize() {
        return nodes.size();
    }

    public boolean areDistancesShown() {
        return showDistances;
    }

    /**
     * Adds the given node to the graph
     * @param node node to add
     */
    public void addNode(Node node) {

        int num = Integer.parseInt(node.getId()) - 1;
        instance.nodes.add(num, node);
        Drawer.getInstance().addElem(node);
    }

    /**
     * Removes node from the list and renews info
     *
     * @param circle node to remove
     */
    public void removeNode(Node circle) {

        ArrayList<Edge> edges = circle.getEdges();
        while (edges.size() != 0)
            edges.get(0).remove();

        nodes.remove(circle);
        refreshLabels(circle);

        if (startNode == circle)
            startNode = null;
        Drawer.getInstance().removeElement(circle);
    }

    public void refreshLabels(Node circle) {
        int num = Integer.parseInt(circle.getId()) - 1;
        // nodes.
        for (int i = num; i < nodes.size(); i++)
            nodes.get(i).renewNum(i + 1);
    }


    /**
     * Removes all nodes from the list
     */
    public void clearGraph() {
        nodes.clear();
    }

    /**
     * Rescales all nodes in graph
     *
     * @param axis   by which axis relocate the nodes
     * @param oldVal old value of width/height
     * @param newVal new value of width/height
     */
    public void rescale(char axis, double oldVal, double newVal) {
        double scale = newVal / oldVal;

        for (Node node : instance.nodes) {
            if (axis == 'x') {
                node.rescaleX(scale);
            }
            if (axis == 'y')
                node.rescaleY(scale);
        }
    }

    /**
     * Resets nodes' visualization info
     */
    public void resetNodes(){
        for(Node n: nodes){
            n.resetNode();
        }
    }

    /**
     * Sets lengths for all edges in graph
     */
    public void setLengths() {
        showDistances = true;
        runDFS(Node::showLengths);
    }

    /**
     * Hides lengths for all edges in graph
     */
    public void hideLengths() {
        showDistances = false;
        runDFS(Node::hideLengths);
    }

    /**
     * Sets all distances to \\infty
     */
    public void resetDistances() {
        runDFS(Node::resetLengths);
    }

    /**
     * Checks data correctness and starts the distribution
     */
    public void visualizeAmplitudes() {
        if (runDFS(null) > 1) {
            PopupMessage.showMessage("The graph is not connected");
            return;
        }

        if(startNode == null)
        {
            PopupMessage.showMessage("The beginning node is not selected");
            return;
        }

        if(startEdge == null)
        {
            PopupMessage.showMessage("The beginning edge is not selected");
            return;
        }

        for (Node n : nodes)
            n.restartTimer();

        Visualizer.startVisualization(startEdge, startNode);
    }


    /**
     * Runs DFS for one node
     *
     * @param handler method to handle with each node
     */
    private void DFS(Consumer<Node> handler) {
        Node curNode;
        while (!dfsStack.isEmpty()) {
            curNode = dfsStack.pop();
            if (!curNode.isVisited()) {
                curNode.visit();
                if (handler != null)
                    handler.accept(curNode);
                for (Node n : curNode.getNeighbours()) {
                    if (!n.isVisited())
                        dfsStack.push(n);
                }
            }
        }
    }

    /**
     * Runs dfs for each node and counts components
     *
     * @param handler method to handle depending on what we need
     * @return num of components
     */
    private int runDFS(Consumer<Node> handler) {

        if (nodes.size() == 0) return 0;

        int components = 0;
        for (Node n : nodes) {
            if (!n.isVisited()) {
                components++;
                dfsStack.push(n);
                DFS(handler);
            }
        }

        resetDFS();

        return components;
    }

    /**
     * Marks all nodes unvisited after dfs
     */
    private void resetDFS() {
        for (Node n : nodes)
            n.unvisit();
    }

}
