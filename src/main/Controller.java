package main;

import entities.Graph;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

public class Controller {

    private Graph graph;
    private Drawer drawer;

    @FXML
    private Label label;

    @FXML
    private AnchorPane menu;

    @FXML
    private Canvas canvas;

    @FXML
    private AnchorPane drawingArea;

    public Controller(){
        graph = Graph.getInstance();
        drawer = Drawer.getInstance();
    }


    @FXML
    void initialize(){
        drawingArea.addEventFilter(MouseEvent.MOUSE_CLICKED, Handlers.dragFilter);
        drawingArea.addEventFilter(MouseEvent.MOUSE_CLICKED, Handlers.clickFilter);

        drawer.setPane(drawingArea);

        drawingArea.widthProperty().addListener(e -> System.out.println("resize"));
    }

    /**
     * Creates the node on click
     * @param event click-info
     */
    @FXML
    void createNode(MouseEvent event){
        System.out.println("Canvas clicked");
        if(Graph.getInstance().getSize() < Graph.MAX_SIZE) {

            StackPane node = drawer.drawNode(event);
            graph.addNode(node);
        }
    }

    @FXML
    void resize(MouseEvent event){
        System.out.println("Resize");
    }

}
