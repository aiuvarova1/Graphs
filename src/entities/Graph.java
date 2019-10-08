package entities;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import main.Drawer;

import java.util.ArrayList;

/**
 * Represents the whole graph on the pane and stores
 * the list of all nodes
 */
public class Graph {
    public static final int MAX_SIZE = 50;
    private ArrayList<Node> nodes = new ArrayList<Node>(20);
    private static Graph instance;


    public static Graph getInstance() {
        if (instance == null) {
            instance = new Graph();
        }
        return instance;
    }

    public int getSize() {
        return nodes.size();
    }


    public void addNode(Node node) {

        instance.nodes.add(node);
        //...
    }

    /**
     * Removes node from the list and renews info
     *
     * @param num number of the node in list
     */
    public void removeNode(int num) {


        for(Node n : nodes.get(num).getNeighbours()){
            n.removeNeighbour(nodes.get(num));
        }
        nodes.remove(num);
        for (int i = num; i < nodes.size(); i++)
            nodes.get(i).renewNum(i + 1);

        //...
    }


    public boolean isOkToPlaceNode(Node node) {
        for (Node n : nodes) {
            if (!n.equals(node)) {
                if (checkIntersection(node, n))
                    return false;
            }
        }
        return true;
    }

    private boolean checkIntersection(Node n1, Node n2) {
        return Math.sqrt((n1.getX() - n2.getX()) * (n1.getX() - n2.getX()) +
                (n1.getY() - n2.getY()) * (n1.getY() - n2.getY())) <= 2 * Node.RADIUS;
    }

    /**
     * Connects two given nodes if there are no edge between them
     * @param n1 first node
     * @param n2 second node
     * @param edge edge to connect
     */
    public void connectNodes(Node n1, Node n2, Edge edge){
        //no multiple edges!!!!!!!!
        edge.setNodes(n1, n2);
        if(n1.addEdge(n2,edge))
            n2.addEdge(n1,edge);
        else
            Drawer.getInstance().removeElement(edge);

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
}
