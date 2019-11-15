package main;

import entities.Edge;
import entities.Graph;
import entities.Node;
import entities.Point;
import javafx.animation.AnimationTimer;
import javafx.animation.PathTransition;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Visualizer {

    private static ArrayList<Integer> nodesToUpdate = new ArrayList<>();
    private static AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            for(int i: nodesToUpdate)
                Graph.getInstance().getNode(i).handlePoints();
            nodesToUpdate.clear();
        }
    };

    public static void startVisualization(Edge startEdge, Node startNode){

        double[] start;
        double[] end;

        start = startNode == startEdge.getNodes()[0] ? startEdge.getFirstNodesNearest():
                startEdge.getSecondNodesNearest();
        end = start == startEdge.getFirstNodesNearest() ? startEdge.getSecondNodesNearest() :
                startEdge.getFirstNodesNearest();

        PathTransition path = new PathTransition();
        path.setDuration(Duration.millis(3000));
        Path p = new Path();
        p.getElements().add(new MoveTo(start[0], start[1]));
        p.getElements().add(new LineTo(end[0], end[1]));

        Point point = new Point();
        point.setCenterX(start[0]);
        point.setCenterY(start[1]);
        Drawer.getInstance().addElem(point);
        path.setPath(p);
        // path.
        path.setNode(point);
        path.play();
    }

    private static void onReachDestination(Point p){
        nodesToUpdate.add(p.getNodesNum());
    }

    private static void handleNode(){

    }
}
