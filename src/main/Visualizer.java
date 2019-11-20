package main;

import entities.Edge;
import entities.Graph;
import entities.Node;
import entities.Point;

/**
 * Class that starts and stops amplitudes' distribution
 */

public class Visualizer {


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

        point.startPath(start, end).play();

    }

    /**
     * Stops the visualization and refreshes the needed data
     */
    public static void stopVisualization(){
        isRunning = false;

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
