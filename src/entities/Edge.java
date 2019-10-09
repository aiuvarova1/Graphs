package entities;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import main.Drawer;
import main.Handlers;


/**
 * Represents an edge between 2 nodes
 */
public class Edge extends Line implements  Undoable{

    private Node n1;
    private Node n2;

    public Edge(double v1, double v2, double v3, double v4){

        super(v1,v2,v3,v4);
        this.setStrokeWidth(1.7);

        setStroke(Color.DIMGRAY);
    }

    /**
     * Sets nodes on the ends of the edge
     * @param n1 start node
     * @param n2 end node
     */
    public void setNodes(Node n1, Node n2){
        this.n1 = n1;
        this.n2 = n2;
        setHandlers();
    }


    /**
     * Nodes getter
     * @return nodes on the ends of the edge
     */
    public Node[] getNodes(){
        return new Node[] {n1, n2};
    }

    /**
     * Returns the node on the other side of the edge
     * @param n node to get neighbour for
     * @return neighbour
     */
    public Node getNeighbour(Node n){
        return n == n1 ? n2 : n1;
    }

    /**
     * Sets mouse events handlers
     */
    private void setHandlers(){

        setOnMouseEntered(x -> {
            this.setStroke(Color.DARKGRAY);
            setStrokeWidth(2.5);
            getScene().setCursor(Cursor.HAND);
        });
        setOnMouseExited(x ->
        {
            this.setStrokeWidth(1.7);
            this.setStroke(Color.DIMGRAY);
            getScene().setCursor(Cursor.DEFAULT);
        });

        addEventFilter(MouseEvent.MOUSE_CLICKED, Handlers.clickFilter);
    }

    /**
     * Calculates needed start and end of the edge
     * Than connects two nodes
     * @param node1 first node to connect
     * @param node2 second node to connect
     */
    public void connectNodes(Circle node1, Circle node2) {
        double dist = getDistance(node1.getCenterX(), node1.getCenterY(),
                node2.getCenterX(), node2.getCenterY());

        double[] startCordsNode = getStartCoordinates(node1.getCenterX(), node1.getCenterY(),
                node2.getCenterX(), node2.getCenterY(), dist);

        double[] startCordsPretender = getStartCoordinates(node2.getCenterX(),
                node2.getCenterY(),
                node1.getCenterX(), node1.getCenterY(), dist);

        this.setStartX(startCordsNode[0]);
        this.setStartY(startCordsNode[1]);

        this.setEndX(startCordsPretender[0]);
        this.setEndY(startCordsPretender[1]);
    }

    /**
     *
     * @param xPos first point's x
     * @param yPos first point's y
     * @param centerX second point's x
     * @param centerY second point's y
     * @return distance between 2 points
     */

    public static double getDistance(double xPos, double yPos, double centerX,
                                      double centerY) {
        return Math.sqrt((xPos - centerX) * (xPos - centerX) +
                (yPos - centerY) * (yPos - centerY));
    }

    /**
     * Counts start coordinates on circle
     *
     * @param xPos     mouse x pos
     * @param yPos     mouse y pos
     * @param centerX  circle centre x pos
     * @param centerY  circle centre y pos
     * @param distance distance between mouse and circle centre
     * @return start coordinates of the line
     */
    public static double[] getStartCoordinates(double xPos, double yPos, double centerX,
                                                double centerY, double distance) {

        double xSide = xPos - centerX;
        double ySide = yPos - centerY;

        return new double[]{centerX + xSide * Node.RADIUS / distance,
                centerY + ySide * Node.RADIUS / distance};
    }

    @Override
    public boolean create(){

        if(this.n1.addEdge(this.n2,this))
            this.n2.addEdge(this.n1,this);
        else {
            Drawer.getInstance().removeElement(this);
            return false;
        }

        try {
            Drawer.getInstance().addElem(this);
        }catch (IllegalArgumentException ex){
            System.out.println("Already drawn");
        }
        return true;
    }

    @Override
    public void remove(){
        System.out.println("Remove " + n1 + " " + n2);
        n1.removeNeighbour(n2);
        n2.removeNeighbour(n1);
        Drawer.getInstance().removeElement(this);
    }

    @Override
    public Edge clone() throws CloneNotSupportedException{
        Edge clone = (Edge)super.clone();
        clone.setNodes(this.n1,this.n2);
        return clone;
    }

}
