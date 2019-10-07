package main;

import entities.Graph;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private ImageView leftClick;

    @FXML
    private ImageView rightClick;

    @FXML
    private TitledPane helpTitledPane;

    @FXML
    private AnchorPane menu;

    @FXML
    private ImageView trashIcon;

    @FXML
    private Accordion accordion;

    @FXML
    private TitledPane drawTitledPane;

    @FXML
    private Canvas canvas;

    @FXML
    private Button clearButton;

    @FXML
    private AnchorPane drawingArea;

    public Controller() {
        graph = Graph.getInstance();
        drawer = Drawer.getInstance();
    }

    @FXML
    public void changeIcon() {

        trashIcon.setImage(new Image("/assets/opened.png"));
        // trashIcon.
    }

    @FXML
    public void setOldIcon() {
        trashIcon.setImage(new Image("/assets/trash.png"));
    }

    @FXML
    private void setIcons() {
        leftClick.setImage(new Image("/assets/leftClick.png"));
        rightClick.setImage(new Image("/assets/rightClick.png"));
    }

    @FXML
    void initialize() {
        drawingArea.addEventFilter(MouseEvent.MOUSE_CLICKED, Handlers.dragFilter);
        drawingArea.addEventFilter(MouseEvent.MOUSE_CLICKED, Handlers.clickFilter);

        setOldIcon();

        drawer.setPane(drawingArea);

        clearButton.addEventHandler(MouseEvent.MOUSE_ENTERED, Handlers.buttonEnterHandler);
        clearButton.addEventHandler(MouseEvent.MOUSE_EXITED, Handlers.buttonExitHandler);

        drawingArea.widthProperty().addListener((axis, oldVal, newVal) -> {
            System.out.println("resize");
            drawingArea.setPrefWidth(newVal.doubleValue());
            graph.rescale('x', oldVal.doubleValue(), newVal.doubleValue());
        });

        drawingArea.heightProperty().addListener((axis, oldVal, newVal) -> {
            System.out.println("resize");
            // drawingArea.setPrefHeight(newVal.doubleValue());
            graph.rescale('y', oldVal.doubleValue(), newVal.doubleValue());
        });

        drawTitledPane.setAnimated(true);
        helpTitledPane.setAnimated(true);
        accordion.setExpandedPane(drawTitledPane);
        setIcons();
    }

    /**
     * Creates the node on click
     *
     * @param event click-info
     */
    @FXML
    void createNode(MouseEvent event) {
        System.out.println("Canvas clicked");
        if (Graph.getInstance().getSize() < Graph.MAX_SIZE) {

            StackPane node = drawer.drawNode(event);
            graph.addNode(node);
        }
    }

    @FXML
    void resize(MouseEvent event) {
        System.out.println("Resize");
    }

    /**
     * Removes all nodes from the pane
     */
    @FXML
    void clearWorkingArea() {
        System.out.println("clear");
        drawingArea.getChildren().removeIf(x -> x.getClass() == StackPane.class);
        graph.clearGraph();
    }


}
