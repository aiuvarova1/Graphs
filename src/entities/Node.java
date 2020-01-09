package entities;

import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import main.Drawer;
import main.Filter;
import main.MenuManager;
import main.Visualizer;


import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Represents one node of the graph
 */
public class Node extends StackPane implements Undoable, Visitable {

    public static final double RADIUS = 22;

    private int num;
    private boolean visited;

    private ArrayList<Edge> edges;
    private double[] initialPosition;

    private static final Color color = Color.WHITE;
    private static final Color selectedColor = Color.LIGHTBLUE;

    private Color curColor = color;

    public Circle getCircle() {
        return (Circle) getChildren().get(0);
    }

    public int getNumOfEdges() {
        return edges.size();
    }

    public volatile BooleanProperty processed = new SimpleBooleanProperty(false);
    public volatile AtomicInteger guests = new AtomicInteger(0);
    public volatile double amplitudesSum = 0;


    public double getAmplitudesSum()
    {
        return amplitudesSum;
    }

    public synchronized  void increaseAmplitudesSum(double amplitude){
        amplitudesSum += amplitude;
    }

    public Node(int num) {
        edges = new ArrayList<>(5);
        this.num = num;
        initialPosition = new double[]{getLayoutX(), getLayoutY()};

        setId("" + num);
        setHandlers();
        processed.addListener((observable, oldValue, newValue) -> {
            if (oldValue) return;
            // processed.setValue(false);

            try {

                Visualizer.runTask(new Task() {
                    @Override
                    protected Object call() throws Exception {

                        synchronized (Node.this) {
                            try {
                                Node.this.wait(110);
                            } catch (InterruptedException ex) {
                                System.out.println("Interrupted in waiting points");
                                return null;
                            }
                            Platform.runLater(() -> {
                                handlePoints();
                                //guests.set(0);
                                processed.setValue(false);
                                amplitudesSum = 0;
                            });


                        }
                        return null;
                    }
                });


//                task = new TimerTask() {
//                    @Override
//                    public void run() {
//
//                        while (guests.get() != guestsExpected.get()) {
//                            if(!Visualizer.isRunning())
//                                return;
//                        }
//
//                        Platform.runLater(() -> {
//
//                            if(!Visualizer.isRunning())
//                                return;
//                            handlePoints(guests.get());
//                        });
//                        guests.set(0);
//                        guestsExpected.set(0);
//                        processed.setValue(false);
//                    }
//
//                };
//                wait.schedule(task, 120);
            } catch (IllegalStateException e) {
                System.out.println("illegal state");
                return;
            }
        });
    }

    public void restartTimer() {
        //wait = new Timer();
    }


    /**
     * Resets info needed for visualization
     */
    public void resetNode() {
//        if(task!=null)
//            task.cancel();
//        wait.cancel();
//        wait = new Timer();
        //  guestsExpected.set(0);
        guests.set(0);
        processed.set(false);
        for (Edge e : edges)
            e.resetProceed();

    }

    /**
     * Selects the node as the start one
     */
    public void select() {
        getCircle().setFill(selectedColor);
        curColor = selectedColor;
    }

    /**
     * Deselects the node as the start one
     */
    public void deselect() {
        getCircle().setFill(color);
        curColor = color;
    }

    /**
     * Proceeds all points which came to the node and restarts their animations
     *
     */
    public void handlePoints() {

        if (!Visualizer.isRunning())
            return;
        //ParallelTransition tr = new ParallelTransition();
        ArrayList<PathTransition> toPlay = new ArrayList<>();
        PathTransition p;
        for (Edge e : edges) {
            p = e.handlePoint(this, edges.size());
            if(p!=null) {
                Visualizer.addPath(p);
                toPlay.add(p);
            }
            // tr.getChildren().add(p);
            // Visualizer.addAnimation(p);
        }
        if (!Visualizer.isRunning())
            return;

        for (PathTransition path : toPlay) {
            path.play();
        }
        //Visualizer.addParallel(tr);
        // tr.play();
        //tr.setOnFinished(event -> Visualizer.removeParallel((ParallelTransition)event.getSource()));

    }

    /**
     * Gets list of neighbours through passing the list of edges
     *
     * @return list of neighbour nodes
     */
    Set<Node> getNeighbours() {
        Set<Node> nodes = new HashSet<>(edges.size());
        for (Edge e : edges) {
            nodes.add(e.getNeighbour(this));
        }
        return nodes;
    }

    /**
     * @return the list of nodes' edges
     */
    public ArrayList<Edge> getEdges() {
        return edges;
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
     * @return Nodes' text on the label
     */
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
            System.out.println(e.getNeighbour(this) + " edge neigh " + this);
            if (e.getNeighbour(this).equals(neighbour)) {
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

        Edge toRemove = null;
        for (Edge e : edges) {
            if (e.getNeighbour(this) == n) {
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
     * Fixes node's position after dragging
     *
     * @param xPos x final coordinate
     * @param yPos y final coordinate
     */
    public void fixPosition(double xPos, double yPos) {
        setLayoutX(xPos);
        setTranslateX(0);

        setLayoutY(yPos);
        setTranslateY(0);

        relocateCircleCenter(getLayoutX(), getLayoutY());
    }

    /**
     * Removes the node from the drawing ares
     */
    @Override
    public void remove() {
        Graph.getInstance().removeNode(this);
    }


    /**
     * (Re)creates the node
     *
     * @return whether succeeded in creation
     */
    @Override
    public boolean create() {
        Graph.getInstance().addNode(this);

        try {
            Drawer.getInstance().addElem(this);
        } catch (IllegalArgumentException ex) {
            System.out.println("Already drawn node");
        }
        Graph.getInstance().refreshLabels(this);
        getCircle().setFill(color);
        curColor = color;

        return true;
    }

    public boolean isVisited() {
        return visited;
    }

    public void visit() {
        visited = true;
    }

    /**
     * Marks the node as not visited (for dfs)
     */
    public void unvisit() {
        visited = false;

        for (Edge e : edges)
            e.unvisit();
    }

    /**
     * Shows labels of all edges
     */
    public void showLengths() {
        handleEdges(Edge::showLength);
    }

    /**
     * Hides labels of all edges
     */
    public void hideLengths() {
        handleEdges(Edge::hideLength);
    }

    /**
     * Resets edges' lengths
     */
    public void resetLengths() {
        handleEdges(Edge::changeLength);
    }

    /**
     * Visits all edges and handles them
     *
     * @param handler method to handle with each edge
     */
    private void handleEdges(Consumer<Edge> handler) {
        for (Edge e : edges) {
            if (!e.isVisited()) {
                e.visit();
                handler.accept(e);
            }
        }
    }

    /**
     * Calls redrawing for all edges
     */
    private void recalculateEdges() {
        for (Edge e : edges) {
            e.connectNodes(this, e.getNeighbour(this));
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


    /**
     * Sets filters and handlers for mouse events
     * (dragging, clicking, etc)
     */
    private void setHandlers() {
        addEventFilter(MouseEvent.MOUSE_CLICKED, Filter.clickFilter);
        addEventFilter(MouseEvent.MOUSE_DRAGGED, Filter.dragFilter);

        setOnMousePressed(event -> {

            if (Filter.isEdgeStarted() ||
                    Filter.isEditing() || event.isSecondaryButtonDown()
                    || Visualizer.isRunning()) return;

            initialPosition[0] = getLayoutX();
            initialPosition[1] = getLayoutY();
            getScene().setCursor(Cursor.MOVE);
            toFront();

            MenuManager.getNodeMenu().hide();
        });

        this.setOnContextMenuRequested(contextMenuEvent -> {
            if (Filter.isEdgeStarted() || Visualizer.isRunning()) return;
            MenuManager.getNodeMenu().bindElem((javafx.scene.Node) contextMenuEvent.getSource());
            MenuManager.getNodeMenu().show((javafx.scene.Node) contextMenuEvent.getSource(),
                    contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
        });


        setOnMouseReleased(mouseEvent -> {
            if (mouseEvent.getButton() != MouseButton.PRIMARY ||
                    Visualizer.isRunning()) return;

            getScene().setCursor(Cursor.HAND);
          //  Node n = (Node) mouseEvent.getSource();
            // Invoker.getInstance().moveElement(n, initialPosition,new double[]{getLayoutX() + getTranslateX(),
            // getLayoutY() + getTranslateY()});
            fixPosition(getLayoutX() + getTranslateX(), getLayoutY() + getTranslateY());

        });
        setOnMouseDragged(event -> {

            if (Filter.isEdgeStarted() || event.getButton() != MouseButton.PRIMARY
                    || Visualizer.isRunning()) return;

            boolean[] crossedBounds = checkBoundsCrossed(event);
            if (!crossedBounds[0])
                setTranslateX(getTranslateX() + event.getX() - RADIUS);
            if (!crossedBounds[1])
                setTranslateY(getTranslateY() + event.getY() - RADIUS);

            recalculateEdges();

            relocateCircleCenter(getLayoutX() + getTranslateX(),
                    getLayoutY() + getTranslateY());
        });
        setOnMouseEntered(mouseEvent -> {
            if (!mouseEvent.isPrimaryButtonDown()) {
                getScene().setCursor(Cursor.HAND);
            }
            getCircle().setFill(Color.AZURE);
        });
        setOnMouseExited(mouseEvent -> {
            if (!mouseEvent.isPrimaryButtonDown()) {
                getScene().setCursor(Cursor.DEFAULT);
            }
            getCircle().setFill(curColor);
        });

    }
}
