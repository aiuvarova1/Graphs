package entities;

import javafx.animation.PathTransition;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import main.Visualizer;


import java.util.concurrent.atomic.AtomicReference;

/**
 * Represents the point moving along the edge
 */
public class Point extends Circle {
    private double amplitude;
    private Node destination;
    private AtomicReference<Node> ref;
    private PathTransition pathTransition;
    private AtomicReference<Edge> edge;

    private LineTo line;
    private Path path;
    private MoveTo move;

    public Point(){

        super(6,Color.DARKGREY);

        ref = new AtomicReference<>();
        edge = new AtomicReference<>();

        setStroke(Color.BLACK);
        amplitude = 1;
        pathTransition = new PathTransition();
        line = new LineTo();
        path = new Path();
        move = new MoveTo();

        pathTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){

               // Visualizer.removeAnimation((PathTransition)event.getSource());
                while(!ref.get().guestsExpected.compareAndSet(ref.get().guests.get(),
                        ref.get().guestsExpected.get()+1)){

                }
                Task<Void> t = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        setPointToEdge();
                        while(!ref.get().guests.compareAndSet(ref.get().guests.get(),
                                ref.get().guests.get()+1)){
                        }
                        if(ref.get().processed.get())
                            return null;
                        ref.get().processed.setValue(true);
                        return null;
                    }
                };
                if(!Visualizer.isRunning())
                    return;
                t.run();
            }
        });

    }

    public Point(Node n, Edge e){
        this();
        destination = n;
        ref.set(n);
        this.edge.set (e);
    }

    /**
     * Notifies the edge that this point must be proceeded
     */
    public synchronized void setPointToEdge(){
        edge.get().addToProceed(destination, this);
    }

    /**
     * Sets the new destination node
     * @param n node that will be reached in the end of the way
     */
    public void setDestination(Node n){
        destination = n;
        ref.set(n);
    }

    /**
     * Changes amplitude with the given rule
     * @param degree degree of the node
     * @param num total num of points in the node
     */
    public void changeAmplitude(int degree, int num){
        if(num == 1)
            amplitude = -amplitude;
        else
            amplitude = 1-2.0/degree + num*(2.0/degree);
    }

    /**
     * Sets the amplitude for the new point
     * @param degree degree of the node
     * @param num total num of accepted points
     */
    public void setAmplitude(int degree, int num){
        amplitude = num*(2.0/degree);
    }

    /**
     * Creates the animation instance for the point
     * @param start start coordinates
     * @param end end coordinates
     * @return instance of animation
     */
    public PathTransition startPath(double[] start, double[] end){

        pathTransition.setDuration(Duration.millis(2000));
        path.getElements().clear();

        line.setX(end[0]);
        line.setY(end[1]);

        move.setX(start[0]);
        move.setY(start[1]);

        path.getElements().add(move);
        path.getElements().add(line);

        pathTransition.setPath(path);

        pathTransition.setNode(this);
        return pathTransition;
    }
}
