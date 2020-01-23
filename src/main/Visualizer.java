package main;

import entities.Edge;
import entities.Graph;
import entities.Node;
import entities.Point;
import javafx.animation.PathTransition;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Task;
import javafx.scene.control.Label;


import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class that starts and stops amplitudes' distribution
 */

public class Visualizer {

    private static final int MAX_POINTS = 500;


    private static ExecutorService threadPool = Executors.newCachedThreadPool();
    private static HashSet<PathTransition> animations = new HashSet<>();

    private static boolean isRunning = false;
    private static int numOfPoints = 0;

    private static boolean numeric = true;
    private static boolean colour = false;
    private static boolean arrows = false;

    /**
     * Current lowest possible value of the amplitude
     **/
    private static LongProperty lowerBound = new SimpleLongProperty(0);

    /**
     * Current biggest possible value of the amplitude
     **/
    private static LongProperty upperBound = new SimpleLongProperty(1);


    /**
     * Submits new task to the common pool
     *
     * @param t task to submit
     */
    public static void runTask(Task t) {
        threadPool.submit(t);
    }

    static void setNumeric(boolean val) {
        numeric = val;
    }

    public static boolean isNumeric() {
        return numeric;
    }

    static void setColour(boolean val) {
        colour = val;
    }

    public static boolean isColoured() {
        return colour;
    }

    static void setArrows(boolean val) {
        arrows = val;
    }

    public static boolean isArrows() {
        return arrows;
    }




    /**
     * Controls the number of points in order not to fail with
     * OutOfMemoryException.
     *
     * @return whether the limit of points is exceeded
     */
    public static boolean checkOOM() {
        return ++numOfPoints < MAX_POINTS;
    }

    public static LongProperty getLowerBound() {
        return lowerBound;
    }

    public static LongProperty getUpperBound() {
        return upperBound;
    }


    /**
     * Checks whether an upper/lower bound changes after a new amplitude
     * appearance.
     *
     * @param val new amplitude to check
     */
    public static void checkMinMaxAmplitudes(double val) {

        //val = Math.round(val);

        if (val > upperBound.get())
            upperBound.set((long) (val + 1));

        if (val < lowerBound.get())
            lowerBound.set((long) (val - 1));
    }

    /**
     * Binds corresponding colour labels to properties
     *
     * @param lower lower bound of amplitudes
     * @param upper upper bound of amplitudes
     */
    static void bindBounds(Label lower, Label upper) {
        lowerBound.addListener((observable, oldValue, newValue) ->
                lower.setText(Long.toString((newValue.intValue()))));
        upperBound.addListener((observable, oldValue, newValue) ->
                upper.setText(Long.toString((newValue.intValue()))));
    }


    /**
     * Prepares and starts the distribution
     *
     * @param startEdge edge from which the first point goes
     * @param startNode node from which the first point goes
     */
    public static void startVisualization(Edge startEdge, Node startNode) {

       // System.out.println("start v");

        lowerBound.set(0);
        upperBound.set(1);

        numOfPoints = 1;
        isRunning = true;
        double[] start;
        double[] end;

        start = startEdge.getNodesNearest(startNode);
        end = startEdge.getNodesNearest(startEdge.getNeighbour(startNode));

        Point point = new Point(startEdge.getNeighbour(startNode), startEdge);
        Drawer.getInstance().addElem(point);

        PathTransition par = point.startPath(start, end, startEdge.getLength());
        animations.add(par);
        par.play();

    }

    /**
     * Adds animation of the new point to the list
     *
     * @param p animation to add
     */
    public static void addPath(PathTransition p) {
        animations.add(p);
    }


    /**
     * Stops the visualization and refreshes the needed data
     */
    static void stopVisualization() {
        isRunning = false;
        for (PathTransition p : animations)
            p.stop();
        animations.clear();

        threadPool.shutdownNow();
        threadPool = Executors.newCachedThreadPool();

        Drawer.getInstance().removePoints();
        Graph.getInstance().resetNodes();
    }

    /**
     * Whether the visualization is running now
     *
     * @return isRunning
     */
    public static boolean isRunning() {
        return isRunning;
    }

}
