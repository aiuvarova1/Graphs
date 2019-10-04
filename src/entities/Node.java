package entities;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import main.Drawer;
import main.Handlers;


import java.util.ArrayList;

public class Node {

    public static final double RADIUS = 27;

    private StackPane circle;

    private int num;
    private double amplitude;
    private ArrayList<Node> neighbours;
    private double[] initialPosition;

    Node(StackPane c, int num) {
        neighbours = new ArrayList<>(5);
        this.num = num;
        circle = c;
        initialPosition = new double[]{circle.getLayoutX(), circle.getLayoutY()};

        setHandlers();

        //setText();
    }

    private void setHandlers() {
        circle.addEventFilter(MouseEvent.MOUSE_CLICKED, Handlers.clickFilter);
        circle.addEventFilter(MouseEvent.MOUSE_DRAGGED, Handlers.dragFilter);

        circle.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                initialPosition[0] = circle.getLayoutX();
                initialPosition[1] = circle.getLayoutY();
                circle.getScene().setCursor(Cursor.MOVE);

            }
        });

        circle.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                circle.getScene().setCursor(Cursor.HAND);

                //double[] cors = new double[2];
                //Drawer.getInstance().checkBounds(cors);
                // (Circle)circle.getChildren().get(0)
            }
        });
        circle.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                circle.setTranslateX(circle.getTranslateX() + event.getX() - RADIUS);
                circle.setTranslateY(circle.getTranslateY() + event.getY() - RADIUS);

                double[] cors = Drawer.getInstance().checkBounds(circle.getLayoutX(), circle.getLayoutY());

               // if(circle.)


            }
        });
        circle.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!mouseEvent.isPrimaryButtonDown()) {
                    circle.getScene().setCursor(Cursor.HAND);
                }
            }
        });
        circle.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!mouseEvent.isPrimaryButtonDown()) {
                    circle.getScene().setCursor(Cursor.DEFAULT);
                }
            }
        });
    }

    private void setText() {
        Text numText = (Text) circle.getChildren().get(1);
        numText.setText("" + num);

    }


    public int getNum() {
        return num;
    }

    ArrayList<Node> getNeighbours() {
        return neighbours;
    }

    public void addNeighbour(Node neighbour) {
        neighbours.add(neighbour);
    }

    void removeNeighbour(Node n) {
        neighbours.remove(n);
    }

    public void drag(MouseEvent event) {
        circle.setLayoutX(event.getX() - RADIUS);
        circle.setLayoutY(event.getY() - RADIUS);
    }


    public double getX() {
        return circle.getLayoutX();
    }

    public double getY() {
        return circle.getLayoutY();
    }


}
