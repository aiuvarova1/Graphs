package main;

import entities.Graph;
import entities.Node;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Handlers {

    private static String SELECTED_BUTTON = "-fx-background-color: #ebebeb;" + "-fx-font-size: 22px;"
            + "-fx-font-family: \"Constantia\";";

    private static String UNSELECTED_BUTTON = "-fx-background-color: #f5f5f5;" + "-fx-font-size: 21px;"
            + "-fx-font-family: \"Constantia\";";

    private static boolean dragging = false;

    public static boolean edgeStarted = false;

    private static Circle pretendent;
    private static Line edgePretendent;

    private static final int CURSOR_GAP = 5;

    /**
     * Distinguishes dragging from pane click
     */
    public static final EventHandler<MouseEvent> dragFilter = new EventHandler<MouseEvent>() {
        @Override
        public void handle(final MouseEvent event) {

            if (edgeStarted) {
                event.consume();
                return;
            }
            if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
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
            if (event.getSource().getClass() == StackPane.class) {
                event.consume();
                System.out.println("here i am");
                if (event.getButton() == MouseButton.SECONDARY) {
                    System.out.println("delete");

                    StackPane circle = (StackPane) event.getSource();

                    String label = circle.getId();
                    try {
                        System.out.println(label);
                        Graph.getInstance().removeNode(Integer.parseInt(label) - 1);
                    } catch (RuntimeException ex) {
                        System.out.println(ex.getMessage());
                        System.out.println("invalid label");
                        return;
                    }

                    Drawer.getInstance().removeNode(circle);
                } else if (event.getButton() == MouseButton.PRIMARY) {
                    if (!edgeStarted) {
                        edgeStarted = true;
                        StackPane pane = (StackPane) event.getSource();
                        pretendent = (Circle) pane.getChildren().get(0);

                        edgePretendent = new Line(0, 0, 0, 0);
                        edgePretendent.setVisible(false);
                        Drawer.getInstance().setMoveHandler(edgeMoveHandler);
                        Drawer.getInstance().addLine(edgePretendent);
                        System.out.println("here");
                    } else {
                        System.out.println("node");
                    }
                }
            } else if (edgeStarted) {
                event.consume();
                edgeStarted = false;
                edgePretendent.setVisible(false);
                Drawer.getInstance().removeMoveHandler();
                Drawer.getInstance().removeLine(edgePretendent);
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

            double dist = getDistance(xPos, yPos, pretendent.getCenterX(), pretendent.getCenterY());

            if (dist > Node.RADIUS + CURSOR_GAP && isInBounds(event.getX(), event.getY(), b)) {
                double[] cords = getStartCoordinates(xPos, yPos, pretendent.getCenterX(),
                        pretendent.getCenterY(), dist);

                int signX = cords[0] <= event.getX() ? -1 : 1;
                int signY = cords[1] <= event.getY() ? -1 : 1;
                edgePretendent.setStartX(cords[0]);
                edgePretendent.setEndX(event.getX() + CURSOR_GAP * signX);

                edgePretendent.setStartY(cords[1]);
                edgePretendent.setEndY(event.getY() + CURSOR_GAP * signY);

                edgePretendent.setVisible(true);
            } else
                edgePretendent.setVisible(false);
        }

        private boolean isInBounds(double x, double y, Bounds b) {
            return x > b.getMinX() && x < b.getMaxX() &&
                    y > b.getMinY() && y < b.getMaxY();
        }


        private double getDistance(double xPos, double yPos, double centerX,
                                   double centerY) {
            return Math.sqrt((xPos - centerX) * (xPos - centerX) +
                    (yPos - centerY) * (yPos - centerY));
        }

        /**
         * Counts start coordinates on circle
         * @param xPos mouse x pos
         * @param yPos mouse y pos
         * @param centerX circle centre x pos
         * @param centerY circle centre y pos
         * @param distance distance between mouse and circle centre
         * @return start coordinates of the line
         */
        private double[] getStartCoordinates(double xPos, double yPos, double centerX,
                                             double centerY, double distance) {

            double xSide = xPos - centerX;
            double ySide = yPos - centerY;

            return new double[]{centerX + xSide * Node.RADIUS / distance,
                    centerY + ySide * Node.RADIUS / distance};
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
