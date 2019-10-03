package main;

import entities.Graph;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import java.util.ResourceBundle;

public class Controller {
    
    private Graph graph;

    @FXML
    private Label label;

    @FXML
    private AnchorPane menu;

    @FXML
    private Canvas canvas;

    Controller(){
        graph = Graph.getInstance();
    }


    @FXML
    void initialize(){
        System.out.println(label.getId());
        System.out.println(canvas.getId());
        //label.prefHeightProperty().bind(menu.heightProperty());
    }

    @FXML
    void drawNode(){
        graph.addNode();

    }

}
