package main;

import entities.*;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

public class Handlers {

    private static String SELECTED_BUTTON = "-fx-background-color: #ebebeb;" + "-fx-font-size: 22px;"
            + "-fx-font-family: \"Constantia\";";

    private static String UNSELECTED_BUTTON = "-fx-background-color: #f5f5f5;" + "-fx-font-size: 21px;"
            + "-fx-font-family: \"Constantia\";";

    private static boolean dragging = false;

    public static boolean edgeStarted = false;

    private static Node pretender;
    private static Edge edgePretender;

    private static final int CURSOR_GAP = 5;

    /**
     * Distinguishes dragging from pane click
     */
    public static final EventHandler<MouseEvent> dragFilter = new EventHandler<MouseEvent>() {
        @Override
        public void handle(final MouseEvent event) {

            if (event.getEventType() == MouseEvent.MOUSE_DRAGGED &&
                    event.getButton() == MouseButton.PRIMARY) {
                dragging = true;
            } else if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                if (dragging) event.consume();
                dragging = false;
            }
        }
    };

    /**
     * Distinguishes pane clicks from node clicks
     */

    public static final EventHandler<MouseEvent> clickFilter = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            System.out.println(event.getSource().getClass());
            System.out.println(event.getTarget());

            if (event.getSource().getClass() == Node.class) {
                event.consume();
                if (event.getButton() == MouseButton.SECONDARY) {

                    Node circle = (Node) event.getSource();

                    String label = circle.getId();
                    //  try {
                    System.out.println(label);
                    System.out.println(circle.getNumOfEdges() + " neighbours");
                    Graph.getInstance().removeNode(Integer.parseInt(label) - 1);
//                    } catch (RuntimeException ex) {
//                        System.out.println(ex.getMessage());
//                        System.out.println("invalid label");
//                        return;
//                    }

                    Drawer.getInstance().removeElement(circle);
                } else if (event.getButton() == MouseButton.PRIMARY) {
                    System.out.println(edgeStarted);
                    if (!edgeStarted) {
                        edgeStarted = true;

                        pretender = (Node) event.getSource();

                        edgePretender = new Edge(0, 0, 0, 0);
                        edgePretender.setVisible(false);
                        Drawer.getInstance().setMoveHandler(edgeMoveHandler);
                        Drawer.getInstance().addLine(edgePretender);
                    } else {
                        System.out.println("Node");

                        event.consume();
                        edgeStarted = false;
                        Node node = (Node) event.getSource();

                        edgePretender.connectNodes(node.getCircle(), pretender.getCircle());
                        Graph.getInstance().connectNodes(node, pretender, edgePretender);

                        Drawer.getInstance().removeMoveHandler();
                    }
                }
            } else if (edgeStarted && event.getTarget().getClass() == AnchorPane.class) {

                System.out.println(event.getTarget().getClass() + " target");
                event.consume();
                edgeStarted = false;

                edgePretender.setVisible(false);
                Drawer.getInstance().removeElement(edgePretender);

                Drawer.getInstance().removeMoveHandler();
            } else if (event.getSource().getClass() == Edge.class) {
                event.consume();
                if (!edgeStarted && event.getButton() == MouseButton.SECONDARY) {
                    Edge e = (Edge) event.getSource();
                    Node[] nodes = e.getNodes();
                    nodes[0].removeNeighbour(nodes[1]);
                    nodes[1].removeNeighbour(nodes[0]);
                }
            }
        }


    };


    /**
     * Controls potential edge's movements
     */
    private static final EventHandler<MouseEvent> edgeMoveHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {

            double xPos = event.getX();
            double yPos = event.getY();

            Bounds b = Drawer.getInstance().getBounds();

            double dist = Edge.getDistance(xPos, yPos, pretender.getCircle().getCenterX(),
                    pretender.getCircle().getCenterY());

            if (dist > Node.RADIUS + CURSOR_GAP && isInBounds(event.getX(), event.getY(), b)) {
                double[] cords = Edge.getStartCoordinates(xPos, yPos, pretender.getCircle().getCenterX(),
                        pretender.getCircle().getCenterY(), dist);

                int signX = cords[0] <= event.getX() ? -1 : 1;
                int signY = cords[1] <= event.getY() ? -1 : 1;
                edgePretender.setStartX(cords[0]);
                edgePretender.setEndX(event.getX() + CURSOR_GAP * signX);

                edgePretender.setStartY(cords[1]);
                edgePretender.setEndY(event.getY() + CURSOR_GAP * signY);

                edgePretender.setVisible(true);
            } else
                edgePretender.setVisible(false);
        }

        private boolean isInBounds(double x, double y, Bounds b) {
            return x > b.getMinX() && x < b.getMaxX() &&
                    y > b.getMinY() && y < b.getMaxY();
        }
    };

    /**
     * Controls button style on mouse enter
     */
    public static final EventHandler<MouseEvent> buttonEnterHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {

            Button b = (Button) event.getSource();
            b.setStyle(SELECTED_BUTTON);
            ((Button) event.getSource()).getScene().setCursor(Cursor.HAND);
        }
    };

    /**
     * Controls button style on mouse exit
     */
    public static final EventHandler<MouseEvent> buttonExitHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {

            Button b = (Button) event.getSource();
            b.setStyle(UNSELECTED_BUTTON);
            ((Button) event.getSource()).getScene().setCursor(Cursor.DEFAULT);
        }
    };
}
