package main;

import entities.Edge;
import entities.Graph;
import entities.Node;
import entities.Point;
import javafx.animation.PathTransition;
import javafx.concurrent.Task;

import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class that starts and stops amplitudes' distribution
 */

public class Visualizer {

    private static final int MAX_POINTS = 500;

    private static double curMin = 1000;
    private static ExecutorService threadPool = Executors.newCachedThreadPool();
    private static HashSet<PathTransition> animations = new HashSet<>();
    //private static HashSet<ParallelTransition> parallel = new HashSet<>();
    private static boolean isRunning = false;
    private static int numOfPoints = 0;

    private static boolean numeric = true;
    private static boolean colour = false;
    private static boolean arrows = false;



    public static void runTask(Task t){
        threadPool.submit(t);
    }

    static void setNumeric(boolean val){
        numeric = val;
    }
    public static boolean isNumeric(){
        return numeric;
    }

    static void setColour(boolean val){
        colour = val;
    }
    public static boolean isColoured(){
        return colour;
    }

    static void setArrows(boolean val){
        arrows = val;
    }
    public static boolean isArrows(){
        return arrows;
    }

    public static void setMin(double pretender){
        curMin = Math.min(pretender,curMin);
    }

    public static double getCurMin(){
        return curMin;
    }

    public static boolean checkOOM(){
        return ++numOfPoints < MAX_POINTS;
    }

    /**
     * Prepares and starts the distribution
     * @param startEdge edge from which the first point goes
     * @param startNode node from which the first point goes
     */
    public static void startVisualization(Edge startEdge, Node startNode){

        numOfPoints = 1;
        isRunning = true;
        double[] start;
        double[] end;

        start = startEdge.getNodesNearest(startNode);
        end = startEdge.getNodesNearest(startEdge.getNeighbour(startNode));

        Point point = new Point(startEdge.getNeighbour(startNode), startEdge);
        Drawer.getInstance().addElem(point);

        //point.setCenterX(start[0]);
       // point.setCenterY(start[1]);

        PathTransition par = point.startPath(start, end,startEdge.getLength());
        //par.setDuration(new Duration(startEdge.getLength()/curMin * 2000));
        //par.getChildren().add(point.startPath(start, end));
       // par.setOnFinished(event -> removeParallel((ParallelTransition)event.getSource()));
        animations.add(par);
        par.play();

    }



    public static void addPath(PathTransition p){
        animations.add(p);
    }


    /**
     * Stops the visualization and refreshes the needed data
     */
    static void stopVisualization(){
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
     * @return isRunning
     */
    public static boolean isRunning(){
        return  isRunning;
    }


}
