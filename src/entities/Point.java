package entities;

import javafx.animation.PathTransition;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.util.Duration;
import main.Drawer;
import main.Formatter;
import main.Visualizer;


/**
 * Represents the point moving along the edge
 */
public class Point extends Circle {

    private static final int RADIUS = 6;
    private static final int SHIFT = 10;

    private double amplitude;
    private Node destination;
    private PathTransition pathTransition;
    private Edge edge;

    private LineTo line;
    private Path path;
    private MoveTo move;

    private Text numAmplitude;

    public Point() {

        super(RADIUS, Color.DARKGREY);

        numAmplitude = new Text();

        setStroke(Color.BLACK);
        amplitude = 1;
        pathTransition = new PathTransition();
        line = new LineTo();
        path = new Path();
        move = new MoveTo();

        pathTransition.setOnFinished(event -> {

            Visualizer.runTask(new Task() {
                @Override
                protected Object call() {
                    if (!destination.processed.get())
                        destination.processed.setValue(true);
                    setPointToEdge();
                    destination.increaseAmplitudesSum(amplitude);
                    destination.guests.incrementAndGet();
                    return null;
                }
            });
        });
        numAmplitude.setText("1");
        numAmplitude.getStyleClass().add("pointLabel");

        if(Visualizer.isNumeric())
            showNumbers();


        translateXProperty().addListener(((observable, oldValue, newValue) ->
                numAmplitude.setTranslateX(newValue.doubleValue() + SHIFT)));

        translateYProperty().addListener(((observable, oldValue, newValue) ->
                numAmplitude.setTranslateY(newValue.doubleValue() + SHIFT)));

    }

    public Point(Node n, Edge e) {
        this();
        destination = n;
        this.edge = e;
    }


    public void showNumbers() {
        Drawer.getInstance().addElem(numAmplitude);
    }

    public void hideNumbers() {
        Drawer.getInstance().removeElement(numAmplitude);
    }

    public void showColour() {

    }

    public void hideColour() {

    }

    public void showArrow() {

    }

    public void hideArrow() {

    }

    public void hideEnabled(){
        Drawer.getInstance().removeElement(numAmplitude);
    }

    /**
     * Notifies the edge that this point must be proceeded
     */
    public void setPointToEdge() {
        edge.addToProceed(destination, this);
    }

    /**
     * Sets the new destination node
     *
     * @param n node that will be reached in the end of the way
     */
    public void setDestination(Node n) {
        destination = n;
    }

    /**
     * Changes amplitude with the given rule
     *
     * @param degree degree of the node
     */
    public void changeAmplitude(int degree) {
        if (degree == 1)
            amplitude = amplitude != 0 ? -amplitude : 0;
        else
            amplitude = ( 2.0 / degree - 1) * amplitude
                    + (destination.getAmplitudesSum() - amplitude) * (2.0 / degree);
        numAmplitude.setText(Formatter.format(amplitude));
    }

    /**
     * Sets the amplitude for the new point
     *
     * @param degree degree of the node
     */
    public void setAmplitude(int degree) {
        amplitude = edge.getNeighbour(destination).getAmplitudesSum() * (2.0 / degree);
        numAmplitude.setText(Formatter.format(amplitude));
    }

    /**
     * Creates the animation instance for the point
     *
     * @param start start coordinates
     * @param end   end coordinates
     * @return instance of animation
     */
    public PathTransition startPath(double[] start, double[] end, double startEdge) {

        // pathTransition.setDuration(Duration.millis(2000));
        path.getElements().clear();

        line.setX(end[0]);
        line.setY(end[1]);

        move.setX(start[0]);
        move.setY(start[1]);

        numAmplitude.setTranslateX(start[0]);
        numAmplitude.setTranslateY(start[1]);

        path.getElements().add(move);
        path.getElements().add(line);

        pathTransition.setPath(path);
        pathTransition.setDuration(new Duration(startEdge / Visualizer.getCurMin() * 2000));

        pathTransition.setNode(this);
        return pathTransition;
    }
}
