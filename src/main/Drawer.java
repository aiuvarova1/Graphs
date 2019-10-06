package main;

import entities.Graph;
import entities.Node;

import javafx.geometry.Bounds;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;


public class Drawer {

    public static final String NODE_TEXT = "-fx-font-family: \"Pristina\";" +
            "-fx-font-size: 26px;";

    private static Drawer instance;
    private AnchorPane pane;


    public static Drawer getInstance(){
        if(instance == null) {
            instance = new Drawer();
        }
        return instance;
    }

    public void setPane(AnchorPane pane){
        this.pane = pane;
    }

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
            xPos = bounds.getMaxX() - Node.RADIUS;

        if(yPos - Node.RADIUS < bounds.getMinY())
            yPos = bounds.getMinY() + Node.RADIUS + 5;
        else if (yPos + Node.RADIUS > bounds.getMaxY())
            yPos = bounds.getMaxY() - Node.RADIUS;
        return new double[] {xPos,yPos};
    }

    /**
     * Draws the node by calling needed methods and adds it to the scene
     * @param ev parameters of a click
     * @return Screen representation of the node
     */
     StackPane drawNode(MouseEvent ev){
        //double[] cors = new double[2];

        double[] cors = checkBounds(ev.getX(), ev.getY());

        StackPane node = createLayout(cors[0],cors[1]);
        instance.pane.getChildren().add(node);

        return node;
    }

    /**
     * Creates node's screen representation
     * @param xPos x coordinate of center
     * @param yPos y coordinate of center
     * @return node's representation on the screen (Circle + text united in stack pane)
     */
    private StackPane createLayout(double xPos, double yPos ){

        Circle node = new Circle(xPos, yPos, Node.RADIUS, Color.WHITE);

        node.addEventFilter(MouseEvent.MOUSE_DRAGGED, Handlers.dragFilter);
        node.setStroke(Color.BLACK);

        StackPane layout = new StackPane();

        Text numText = new Text("" + (Graph.getInstance().getSize() + 1));

        numText.setStyle(NODE_TEXT);

        layout.getChildren().addAll(node,numText);
        layout.setLayoutX(xPos - Node.RADIUS);
        layout.setLayoutY(yPos - Node.RADIUS);

        return layout;
    }


}
