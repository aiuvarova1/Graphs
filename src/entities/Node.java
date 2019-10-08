package entities;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
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
 * Represents one node of the graph
 */
public class Node extends StackPane {

    public static final double RADIUS = 27;

    private int num;
    private double amplitude;

    private ArrayList<Edge> edges;
    private double[] initialPosition;

    public double getX() {
        return getLayoutX();
    }

    public double getY() {
        return getLayoutY();
    }

    public Circle getCircle() {
        return (Circle) getChildren().get(0);
    }

    public int getNumOfEdges() {
        return edges.size();
    }


    public int getNum() {
        return num;
    }

    /**
     * Gets list of neighbours through passing the list of edges
     * @return list of neighbour nodes
     */
    ArrayList<Node> getNeighbours() {
        ArrayList<Node> nodes = new ArrayList<>(edges.size());
        for (Edge e : edges) {
            nodes.add(e.getNeighbour(this));
        }
        return nodes;
    }


    public Node(int num) {
        edges = new ArrayList<>(5);
        this.num = num;
        initialPosition = new double[]{getLayoutX(), getLayoutY()};

        setId("" + num);
        setHandlers();
    }

    /**
     * Renews number label
     *
     * @param num new number
     */
    public void renewNum(int num) {
        this.num = num;
        setId("" + num);
        setText();
    }

    /**
     * Rescales the node bt x-axis
     *
     * @param scale scale param
     */
    public void rescaleX(double scale) {

        Bounds b = Drawer.getInstance().getBounds();

        double oldX = getLayoutX();


        if (getLayoutX() * scale > b.getMaxX())
            setLayoutX(b.getMaxX() - 2 * RADIUS - 2 * Drawer.BOUNDS_GAP);
        else
            setLayoutX(getLayoutX() * scale);

        relocate(getLayoutX(), getLayoutY());
        relocateCircleCenter(getLayoutX(), getLayoutY());

        if (!(oldX == getLayoutX())) {
            recalculateEdges();
        }
    }


    /**
     * Rescales the node by y-axis
     *
     * @param scale scale param
     */
    public void rescaleY(double scale) {
        Bounds b = Drawer.getInstance().getBounds();

        double oldY = getLayoutY();
        //System.out.println("scale" + circle.getLayoutY() + " " + b.getMaxY());
        if (getLayoutY() * scale > b.getMaxY())
            setLayoutY(b.getMaxY() - 2 * RADIUS - 2 * Drawer.BOUNDS_GAP);
        else
            setLayoutY(getLayoutY() * scale);

        relocate(getLayoutX(), getLayoutY());
        relocateCircleCenter(getLayoutX(), getLayoutY());

        if (!(oldY == getLayoutY())) {
            recalculateEdges();
        }
    }

    /**
     * Calls redrawing for all edges
     */
    private void recalculateEdges() {
        for (Edge e : edges) {
            e.connectNodes(getCircle(), e.getNeighbour(this).getCircle());
        }
    }


    /**
     * Checks whether the node will cross the bounds of the drawing area after moving
     * on cursor position
     *
     * @param event contains info about cursor
     * @return returns {xBound is crossed, yBound is crossed}
     */
    private boolean[] checkBoundsCrossed(MouseEvent event) {

        Bounds b = Drawer.getInstance().getBounds();

        boolean crossedBoundsX = false;
        boolean crossedBoundsY = false;
        if (getTranslateX() + event.getX() - RADIUS + getLayoutX() < 0) {
//            System.out.println("bounds " + b.getMinX() + " " + circle.getTranslateX() + " " + circle.getLayoutX());
            setLayoutX(0);
            setTranslateX(0);
            crossedBoundsX = true;

            //was 2.5
        } else if (getTranslateX() + event.getX() + 2 * RADIUS + getLayoutX() > b.getMaxX()) {
            setLayoutX(b.getMaxX() - 2 * RADIUS - Drawer.BOUNDS_GAP);
            setTranslateX(0);
            //System.out.println("crossed " + b.getMaxX() + " " + circle.getLayoutX() + " " + circle.getTranslateX());
            crossedBoundsX = true;
        }

        if (getTranslateY() + event.getY() - RADIUS + getLayoutY() < b.getMinY()) {
            setLayoutY(10);
            setTranslateY(0);
            crossedBoundsY = true;
        } else if (getTranslateY() + event.getY() + 2 * RADIUS + getLayoutY() > b.getMaxY()) {

            setLayoutY(b.getMaxY() - 2 * RADIUS - Drawer.BOUNDS_GAP);
            setTranslateY(0);
            // System.out.println("crossed " + b.getMaxY() + " " + circle.getLayoutY() + " " + circle.getTranslateY());
            crossedBoundsY = true;
        }

        return new boolean[]{crossedBoundsX, crossedBoundsY};
    }

    /**
     * Sets new text according to the changed node number
     */
    private void setText() {
        //Text numText = (Text) circle.getChildren().get(1);
        getText().setText("" + num);

    }

    public Text getText() {
        return (Text) getChildren().get(1);
    }

    /**
     * Adds an edge to the list
     *
     * @param neighbour node on the other end of the edge
     * @param edge      edge to add
     * @return whether the adding was successful
     */
    public Boolean addEdge(Node neighbour, Edge edge) {
        //NO MULTIPLE EDGES
        for (Edge e : edges) {
            Node[] nodes = e.getNodes();

            if (e.getNeighbour(this) == neighbour) {
                return false;
            }
        }

        edges.add(edge);
        return true;
    }

    /**
     * Removes the node from the neighbours and destructs edges
     *
     * @param n number of the node to remove
     */
    public void removeNeighbour(Node n) {

        System.out.println("remove");
        //can be multiple now
        System.out.println(edges.size());
        Edge toRemove = null;
        for (Edge e : edges) {
            if (e.getNeighbour(this) == n) {
                Drawer.getInstance().removeElement(e);
                toRemove = e;
            }
        }
        edges.remove(toRemove);
    }

    /**
     * Returns the number of the node
     *
     * @return node number
     */
    @Override
    public String toString() {
        return "" + num;
    }

    /**
     * Sets circle centre to the given point
     *
     * @param x center's x
     * @param y center's y
     */
    private void relocateCircleCenter(double x, double y) {
        getCircle().setCenterX(x + Node.RADIUS);
        getCircle().setCenterY(y + Node.RADIUS);
    }


    /**
     * Sets filters and handlers for mouse events
     * (dragging, clicking, etc)
     */
    private void setHandlers() {
        addEventFilter(MouseEvent.MOUSE_CLICKED, Handlers.clickFilter);
        addEventFilter(MouseEvent.MOUSE_DRAGGED, Handlers.dragFilter);

        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (Handlers.edgeStarted) return;
                initialPosition[0] = getLayoutX();
                initialPosition[1] = getLayoutY();
                getScene().setCursor(Cursor.MOVE);
                toFront();

            }
        });

        setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() != MouseButton.PRIMARY) return;

                getScene().setCursor(Cursor.HAND);

                setLayoutX(getLayoutX() + getTranslateX());
                setTranslateX(0);

                setLayoutY(getLayoutY() + getTranslateY());
                setTranslateY(0);

                relocateCircleCenter(getLayoutX(), getLayoutY());
            }
        });
        setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (Handlers.edgeStarted || event.getButton() != MouseButton.PRIMARY) return;

                boolean[] crossedBounds = checkBoundsCrossed(event);
                if (!crossedBounds[0])
                    setTranslateX(getTranslateX() + event.getX() - RADIUS);
                if (!crossedBounds[1])
                    setTranslateY(getTranslateY() + event.getY() - RADIUS);

                recalculateEdges();

                relocateCircleCenter(getLayoutX() + getTranslateX(),
                        getLayoutY() + getTranslateY());
            }
        });
        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!mouseEvent.isPrimaryButtonDown()) {
                    getScene().setCursor(Cursor.HAND);
                }
                getCircle().setFill(Color.AZURE);
            }

        });
        setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!mouseEvent.isPrimaryButtonDown()) {
                    getScene().setCursor(Cursor.DEFAULT);
                }
                getCircle().setFill(Color.WHITE);
            }
        });

    }
}
