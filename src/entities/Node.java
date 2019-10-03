package entities;

import java.util.ArrayList;

class Node {

    private static int current_Num = 0;
    private int num;
    private double amplitude;
    private ArrayList<Node> neighbours;

    public Node(){
        neighbours = new ArrayList<>(5);
        num = current_Num++;
    }


    public int getNum(){
        return num;
    }

    public ArrayList<Node> getNeighbours(){
        return neighbours;
    }

    public void addNeighbour(Node neighbour){
        neighbours.add(neighbour);
    }

    public void removeNeighbour(Node n){
        neighbours.remove(n);
    }


}
