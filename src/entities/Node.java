package entities;

import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Node {

    public static final double RADIUS = 30;
    private static int current_Num = 0;

    private Circle circle;
    private int num;
    private double amplitude;
    private ArrayList<Node> neighbours;

    Node(Circle c){
        neighbours = new ArrayList<>(5);
        num = current_Num++;
        circle = c;
    }


    public int getNum(){
        return num;
    }

    ArrayList<Node> getNeighbours(){
        return neighbours;
    }

    public void addNeighbour(Node neighbour){
        neighbours.add(neighbour);
    }

    void removeNeighbour(Node n){
        neighbours.remove(n);
    }


}
