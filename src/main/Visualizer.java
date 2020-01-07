package main;

import entities.Edge;
import entities.Graph;
import entities.Node;
import entities.Point;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;

import java.util.HashSet;

/**
 * Class that starts and stops amplitudes' distribution
 */

public class Visualizer {



   // private static HashSet<PathTransition> animations = new HashSet<>();
    private static PathTransition initial ;
    private static HashSet<ParallelTransition> parallel = new HashSet<>();
    private static boolean isRunning = false;

    /**
     * Prepares and starts the distribution
     * @param startEdge edge from which the first point goes
     * @param startNode node from which the first point goes
     */
    public static void startVisualization(Edge startEdge, Node startNode){

        isRunning = true;
        double[] start;
        double[] end;

        start = startEdge.getNodesNearest(startNode);
        end = startEdge.getNodesNearest(startEdge.getNeighbour(startNode));

        Point point = new Point(startEdge.getNeighbour(startNode), startEdge);
        Drawer.getInstance().addElem(point);

        point.setCenterX(start[0]);
        point.setCenterY(start[1]);

        ParallelTransition par = new ParallelTransition();
        par.getChildren().add(point.startPath(start, end));
        par.setOnFinished(event -> removeParallel((ParallelTransition)event.getSource()));
        parallel.add(par);
        par.play();

    }



    public static void addParallel(ParallelTransition p){
        parallel.add(p);
    }

    public static void removeParallel(ParallelTransition p){
        parallel.remove(p);
    }

    /**
     * Stops the visualization and refreshes the needed data
     */
    public static void stopVisualization(){
        isRunning = false;
        for (ParallelTransition p : parallel)
            p.stop();
        parallel.clear();

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
