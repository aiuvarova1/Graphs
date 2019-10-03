package main;

import entities.Graph;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

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
    }

    @FXML
    void createNode(MouseEvent event){

        graph.addNode(drawer.drawNode(drawingArea, event));
    }

}
