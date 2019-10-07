package entities;

import javafx.scene.shape.Line;

/**
 * Represents an edge between 2 nodes
 */
public class Edge {

    private Node n1;
    private Node n2;
    private Line line;

    public Edge(Node n1, Node n2) {
        this.n1 = n1;
        this.n2 = n2;
    }


}
