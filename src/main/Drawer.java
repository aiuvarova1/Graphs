package main;

import entities.Edge;
import entities.Graph;
import entities.Node;

import entities.Point;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;


/**
 * Draws Nodes and stores some needed references connected
 * with drawing
 */
public class Drawer {

    public static final int BOUNDS_GAP = 25;

    public static final String NODE_TEXT = "-fx-font-family: \"Pristina\";" +
            "-fx-font-size: 26px;";

    private static Drawer instance;
    private AnchorPane pane;


    /**
     * Singleton
     * @return static instance
     */
    public static Drawer getInstance(){
        if(instance == null) {
            instance = new Drawer();
        }
        return instance;
    }

    /**
     * Removes element from the drawing pane
     * @param node node to remove
     */
    public void removeElement(javafx.scene.Node node){
        pane.getChildren().remove(node);
    }

    public void setFocus(){
        pane.requestFocus();
    }

    public void setPane(AnchorPane pane){
        this.pane = pane;
    }

    /**
     * Sets handler for the drawing pane
     * @param h handler to set
     */
    public void setMoveHandler(EventHandler h){
        pane.setOnMouseMoved(h);
    }

    /**
     * Removes onMouseMove handler
     */
    public void removeMoveHandler(){
        pane.setOnMouseMoved(null);
    }

    /**
     * Adds element to the pane
     * @param el element to add
     */
    public void addElem(javafx.scene.Node el){
        pane.getChildren().add(el);
    }

    /**
     * Removes all points from the graph
     */
    public void removePoints(){
        pane.getChildren().removeIf(x->x instanceof Point);
       // pane.getChildren().filtered(x->x instanceof Edge).forEach();
    }

    /**
     *
     * @return bounds of the drawing area
     */
    public Bounds getBounds(){
        //return instance.pane.localToParent(instance.pane.getBoundsInLocal());
        return instance.pane.getBoundsInLocal();
    }

    /**
     * Checks whether the click position crosses the bounds and changes it if needed
     * @param xPos x-coordinate of a click
     * @param yPos y-coordinate of a click
     * @return renewed (if needed) coordinates
     */
    public double[] checkBounds(double xPos, double yPos){

        Bounds bounds = (instance.pane.getBoundsInLocal());

        if(xPos - Node.RADIUS < bounds.getMinX())
            xPos = bounds.getMinX() + Node.RADIUS;
        else if(xPos + Node.RADIUS > bounds.getMaxX())
            xPos = bounds.getMaxX() - Node.RADIUS - BOUNDS_GAP;

        if(yPos - Node.RADIUS < bounds.getMinY())
            yPos = bounds.getMinY() + Node.RADIUS + BOUNDS_GAP;
        else if (yPos + Node.RADIUS > bounds.getMaxY())
            yPos = bounds.getMaxY() - Node.RADIUS - BOUNDS_GAP;

        return new double[] {xPos,yPos};
    }

    /**
     * Draws the node by calling needed methods and adds it to the scene
     * @param ev parameters of a click
     * @return Screen representation of the node
     */
     Node drawNode(MouseEvent ev){
        double[] cors = checkBounds(ev.getX(), ev.getY());
        return createLayout(cors[0],cors[1]);
    }

    /**
     * Creates node's screen representation
     * @param xPos x coordinate of center
     * @param yPos y coordinate of center
     * @return node's representation on the screen (Circle + text united in stack pane)
     */
    private Node createLayout(double xPos, double yPos ){

        Circle node = new Circle(xPos, yPos, Node.RADIUS, Color.WHITE);

        node.addEventFilter(MouseEvent.MOUSE_DRAGGED, Filter.dragFilter);
        node.setStroke(Color.BLACK);

        Node layout = new Node(Graph.getInstance().getSize() + 1);

        Text numText = new Text("" + (Graph.getInstance().getSize() + 1));

        numText.setStyle(NODE_TEXT);

        layout.getChildren().addAll(node,numText);
        layout.setLayoutX(xPos - Node.RADIUS);
        layout.setLayoutY(yPos - Node.RADIUS);

        return layout;
    }

}
