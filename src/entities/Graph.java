package entities;

import java.util.ArrayList;

public class Graph {
    private ArrayList<Node> nodes = new ArrayList<Node>(20);
    private static Graph instance;


    public static Graph getInstance(){
        if (instance == null){
            instance = new Graph();
        }
        return instance;
    }

    public void addNode(){
        instance.nodes.add(new Node());
        //...
    }

    public void removeNode(Node node){
        for(Node n : node.getNeighbours()){
            n.removeNeighbour(node);
        }
        instance.nodes.remove(node);
        //...
    }
}
