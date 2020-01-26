package entities;

import javafx.animation.PathTransition;
import javafx.beans.binding.Bindings;
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

    static final int RADIUS = 8;
    private static final int SHIFT = 10;
    private static final Color BASE_COLOR = Color.GRAY;

    private double amplitude;
    private Node destination;

    /**Animation object**/
    private PathTransition pathTransition;

    /**Edge on which the point exists**/
    private Edge edge;

    private LineTo line;
    private Path path;
    private MoveTo move;

    /**Text representation of the amplitude**/
    private Text numAmplitude;

    private Arrow arrow;

    public Point() {

        super(RADIUS, BASE_COLOR);

        numAmplitude = new Text();
        arrow = new Arrow(getCenterX(), getCenterY() - RADIUS);

        setStroke(Color.BLACK);
        amplitude = 1;

        pathTransition = new PathTransition();
        line = new LineTo();
        path = new Path();
        move = new MoveTo();

        numAmplitude.setText("1");
        numAmplitude.getStyleClass().add("pointLabel");

        if(Visualizer.isNumeric())
            showNumbers();

        if(Visualizer.isColoured())
            showColour();

        if(Visualizer.isArrows())
            showArrow();

        setBindings();
    }

    public Point(Node n, Edge e) {
        this();
        destination = n;
        this.edge = e;
    }

    public String getAmplitude(){
        return numAmplitude.getText();
    }


    /**
     * Shows numeric value of the amplitude
     */
    public void showNumbers() {
        Drawer.getInstance().addElem(numAmplitude);
    }

    /**
     * Hides numeric value of the amplitude
     */
    public void hideNumbers() {
        Drawer.getInstance().removeElement(numAmplitude);
    }

    /**
     * Applies corresponding color to the point
     */
    public void showColour() {
        fillProperty().set(calculateInterpolation());

        fillProperty().bind(Bindings.createObjectBinding(this::calculateInterpolation,
                Visualizer.getLowerBound(), Visualizer.getUpperBound(),numAmplitude.textProperty()));
    }

    /**
     * Returns base point's color
     */
    public void hideColour() {
        fillProperty().unbind();
        fillProperty().set(BASE_COLOR);
    }

    /**
     * Shows a vector of an amplitude
     */
    public void showArrow() {
        arrow.addArrow();
        arrow.redrawArrow(amplitude);
    }

    /**
     * Hides the vector of an amplitude
     */
    public void hideArrow() {
        arrow.removeArrow();
    }

    /**
     * Hides amplitude's number and arrow
     */
    public void hideEnabled(){
        hideNumbers();
        hideArrow();
    }


    /**
     * Sets the new destination node
     *
     * @param n node that will be reached in the end of the way
     */
    void setDestination(Node n) {
        destination = n;
    }

    /**
     * Changes amplitude with the given rule
     *
     * @param degree degree of the node
     */
    void changeAmplitude(int degree) {
        if (degree == 1)
            amplitude = amplitude != 0 ? -amplitude : 0;
        else
            amplitude = ( 2.0 / degree - 1) * amplitude
                    + (destination.getAmplitudesSum() - amplitude) * (2.0 / degree);
        updateInfo();
    }

    /**
     * Sets the amplitude for the new point
     *
     * @param degree degree of the node
     */
    void setAmplitude(int degree) {
        amplitude = edge.getNeighbour(destination).getAmplitudesSum() * (2.0 / degree);
        updateInfo();
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
        pathTransition.setDuration(new Duration(startEdge / Graph.getInstance().getCurMinEdge() * 2000));

        pathTransition.setNode(this);
        return pathTransition;
    }


    /**
     * Notifies the edge that this point must be proceeded
     */
    private void setPointToEdge() {
        edge.addToProceed(destination, this);
    }

    /**
     * Calculates corresponding to the amplitude Color
     * @return current amplitude's color
     */
    private Color calculateInterpolation(){
        return Color.ROYALBLUE.interpolate(Color.INDIANRED,
                (amplitude - Visualizer.getLowerBound().get())/
                        (Visualizer.getUpperBound().get() -
                                Visualizer.getLowerBound().get()));
    }

    /**
     * Binds needed properties
     */
    private void setBindings(){

        pathTransition.setOnFinished(event -> Visualizer.runTask(new Task() {
            @Override
            protected Object call() {
                if (!destination.processed.get())
                    destination.processed.setValue(true);
                setPointToEdge();
                destination.increaseAmplitudesSum(amplitude);
                destination.guests.incrementAndGet();
                return null;
            }
        }));

        translateXProperty().addListener(((observable, oldValue, newValue) ->
        {
            numAmplitude.setTranslateX(newValue.doubleValue() + SHIFT);
            arrow.setArrowTranslateX(newValue.doubleValue());
        }));

        translateYProperty().addListener(((observable, oldValue, newValue) ->
        {
            numAmplitude.setTranslateY(newValue.doubleValue() + SHIFT);
            arrow.setArrowTranslateY(newValue.doubleValue());
        }));

        numAmplitude.textProperty().addListener(((observable, oldValue, newValue) ->
        {
            if(Visualizer.isArrows())
                arrow.redrawArrow(Double.valueOf(newValue));
        }
        ));
    }

    /**
     * Updates all needed information after amplitude's updating
     */
    private void updateInfo(){
        numAmplitude.setText(Formatter.format(amplitude));
        Visualizer.checkMinMaxAmplitudes(amplitude);

    }
}
