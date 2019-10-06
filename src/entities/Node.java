package entities;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import main.Drawer;
import main.Handlers;


import java.util.ArrayList;

/**
 * Represents a vertice of the graph
 */
public class Node {

    public static final double RADIUS = 27;

    /**
     * Container with circle and text (representation of the vertice on the screen)
     */
    private StackPane circle;

    private int num;
    private double amplitude;
    private ArrayList<Node> neighbours;
    private double[] initialPosition;

    public double getX() {
        return circle.getLayoutX();
    }

    public double getY() {
        return circle.getLayoutY();
    }

    public int getNum() {
        return num;
    }

     ArrayList<Node> getNeighbours() {
        return neighbours;
    }


    Node(StackPane c, int num) {
        neighbours = new ArrayList<>(5);
        this.num = num;
        circle = c;
        initialPosition = new double[]{circle.getLayoutX(), circle.getLayoutY()};

        circle.setId("" + num);
        setHandlers();
    }

    /**
     * Renews number label
     * @param num new number
     */
    public void renewNum(int num){
        this.num = num;
        circle.setId("" + num);
        setText();
    }

    /**
     * Rescales the node bt x-axis
     * @param scale scale param
     */
    public void rescaleX(double scale){

        Bounds b = Drawer.getInstance().getBounds();


        if(circle.getLayoutX()* scale > b.getMaxX())
            circle.setLayoutX(b.getMaxX() - 2* RADIUS - 2 *Drawer.BOUNDS_GAP);
        else
            circle.setLayoutX(circle.getLayoutX() * scale );

        circle.relocate(circle.getLayoutX(), circle.getLayoutY());
    }

    /**
     * Rescales the node by y-axis
     * @param scale scale param
     */
    public void rescaleY(double scale){
        Bounds b = Drawer.getInstance().getBounds();

        //System.out.println("scale" + circle.getLayoutY() + " " + b.getMaxY());
        if(circle.getLayoutY()* scale > b.getMaxY())
            circle.setLayoutY(b.getMaxY() - 2* RADIUS - 2 *Drawer.BOUNDS_GAP);
        else
            circle.setLayoutY(circle.getLayoutY() * scale );

        circle.relocate(circle.getLayoutX(), circle.getLayoutY());
    }

    /**
     * Sets filters and handlers for mouse events
     * (dragging, clicking, etc)
     */
    private void setHandlers() {
        circle.addEventFilter(MouseEvent.MOUSE_CLICKED, Handlers.clickFilter);
        circle.addEventFilter(MouseEvent.MOUSE_DRAGGED, Handlers.dragFilter);

        circle.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                initialPosition[0] = circle.getLayoutX();
                initialPosition[1] = circle.getLayoutY();
                circle.getScene().setCursor(Cursor.MOVE);
                circle.toFront();

            }
        });

        circle.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                circle.getScene().setCursor(Cursor.HAND);

                //System.out.println(circle.getLayoutX() + " " + circle.getTranslateX());
                circle.setLayoutX(circle.getLayoutX() + circle.getTranslateX());
                circle.setTranslateX(0);

                circle.setLayoutY(circle.getLayoutY() + circle.getTranslateY());
                circle.setTranslateY(0);

            }
        });
        circle.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                boolean[] crossedBounds = checkBoundsCrossed(event);
                if(!crossedBounds[0])
                    circle.setTranslateX(circle.getTranslateX() + event.getX() - RADIUS);
                if(!crossedBounds[1])
                    circle.setTranslateY(circle.getTranslateY() + event.getY() - RADIUS);

            }
        });
        circle.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!mouseEvent.isPrimaryButtonDown()) {
                    circle.getScene().setCursor(Cursor.HAND);
                }
                Circle shape = (Circle)circle.getChildren().get(0);
                shape.setFill(Color.AZURE);

            }


        });
        circle.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!mouseEvent.isPrimaryButtonDown()) {
                    circle.getScene().setCursor(Cursor.DEFAULT);
                }
                Circle shape = (Circle)circle.getChildren().get(0);
                shape.setFill(Color.WHITE);
            }
        });

    }


    /**
     * Checks whether the node will cross the bounds of the drawing area after moving
     * on cursor position
     * @param event contains info about cursor
     * @return returns {xBound is crossed, yBound is crossed}
     */
    private boolean[] checkBoundsCrossed(MouseEvent event) {

        Bounds b = Drawer.getInstance().getBounds();

        boolean crossedBoundsX = false;
        boolean crossedBoundsY = false;
        if (circle.getTranslateX() + event.getX() - RADIUS + circle.getLayoutX() < 0) {
//            System.out.println("bounds " + b.getMinX() + " " + circle.getTranslateX() + " " + circle.getLayoutX());
            circle.setLayoutX(0);
            circle.setTranslateX(0);
            crossedBoundsX = true;

            //was 2.5
        } else if (circle.getTranslateX() + event.getX() + 2 * RADIUS + circle.getLayoutX() > b.getMaxX()) {
            circle.setLayoutX(b.getMaxX() - 2* RADIUS - Drawer.BOUNDS_GAP);
            circle.setTranslateX(0);
            //System.out.println("crossed " + b.getMaxX() + " " + circle.getLayoutX() + " " + circle.getTranslateX());
            crossedBoundsX = true;
        }

        if (circle.getTranslateY() + event.getY() - RADIUS + circle.getLayoutY() < b.getMinY()){
            circle.setLayoutY(10);
            circle.setTranslateY(0);
            crossedBoundsY = true;
        }else if(circle.getTranslateY() + event.getY() + 2*RADIUS + circle.getLayoutY() > b.getMaxY()){

            circle.setLayoutY(b.getMaxY() - 2*RADIUS - Drawer.BOUNDS_GAP);
            circle.setTranslateY(0);
           // System.out.println("crossed " + b.getMaxY() + " " + circle.getLayoutY() + " " + circle.getTranslateY());
            crossedBoundsY = true;
        }

        return new boolean[] {crossedBoundsX, crossedBoundsY};
    }

    /**
     * Sets new text according to the changed node number
     */
    private void setText() {
        Text numText = (Text) circle.getChildren().get(1);
        numText.setText("" + num);

    }

    public String getShapeId(){
        return circle.getId();
    }


    public void addNeighbour(Node neighbour) {
        neighbours.add(neighbour);
    }

    /**
     * Removes the node from the neighbours by its num
     * @param n number of the node to remove
     */
    void removeNeighbour(Node n) {
        neighbours.removeIf(x -> x == n);
    }

    /**
     * Returns the number of the node
     * @return node number
     */
    @Override
    public String toString(){
        return "" + num;
    }

}
