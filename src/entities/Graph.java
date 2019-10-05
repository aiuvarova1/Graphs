package entities;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Graph {
    public static final int MAX_SIZE = 50;
    private ArrayList<Node> nodes = new ArrayList<Node>(20);
    private static Graph instance;


    public static Graph getInstance(){
        if (instance == null){
            instance = new Graph();
        }
        return instance;
    }

    public int getSize(){
        return nodes.size();
    }


    public void addNode(StackPane node){

        instance.nodes.add(new Node(node,instance.nodes.size() + 1));
        //...
    }

    public void removeNode(Node node){
        for(Node n : node.getNeighbours()){
            n.removeNeighbour(node);
        }
        instance.nodes.remove(node);
        //...
    }


    public boolean isOkToPlaceNode(Node node){
        for(Node n : nodes){
            if(!n.equals(node)){
                if(checkIntersection(node,n))
                    return false;
            }
        }
        return true;
    }

    private boolean checkIntersection(Node n1, Node n2){
        return Math.sqrt((n1.getX() - n2.getX())*(n1.getX() - n2.getX()) +
                (n1.getY() - n2.getY())*(n1.getY() - n2.getY())) <= 2* Node.RADIUS;
    }
}
