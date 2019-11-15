package entities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Point extends Circle {
    private double amplitude;
    private Node destination;

    public Point(){
        super(6,Color.DARKGREY);
        amplitude = 1;
       // setFill(Color.DARKBLUE);

    }

    public void setDestination(Node n){
        destination = n;
    }

    public int getNodesNum(){
        return destination.getNum()-1;
    }

    public void addChild(){

    }
}
