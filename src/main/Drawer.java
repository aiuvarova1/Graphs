package main;

import entities.Node;

import javafx.geometry.Bounds;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.input.MouseEvent;




public class Drawer {

    //private GraphicsContext graphics;
    private static Drawer instance;


    static Drawer getInstance(){
        if(instance == null) {
            instance = new Drawer();
        }
        return instance;
    }

     Circle drawNode(AnchorPane pane, MouseEvent ev){
        double xPos = ev.getX();
        double yPos = ev.getY();

        Bounds bounds = (pane.getBoundsInLocal());

        if(xPos - Node.RADIUS < bounds.getMinX())
            xPos = bounds.getMinX() + Node.RADIUS;
        else if(xPos + Node.RADIUS > bounds.getMaxX())
            xPos = bounds.getMaxX() - Node.RADIUS;

        if(yPos - Node.RADIUS < bounds.getMinY())
            yPos = bounds.getMinY() + Node.RADIUS + 5;
        else if (yPos + Node.RADIUS > bounds.getMaxY())
            yPos = bounds.getMaxY() - Node.RADIUS;

        Circle node = new Circle(xPos, yPos, Node.RADIUS, Color.WHITE);
        node.addEventHandler(MouseEvent.MOUSE_DRAGGED, ev1 -> System.out.println("drag"));
        node.addEventFilter(MouseEvent.MOUSE_DRAGGED, Handlers.dragFilter);

        node.setStroke(Color.BLACK);
        pane.getChildren().add(node);

        return node;
    }


}
