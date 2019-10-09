package main;

import entities.Edge;
import entities.Graph;
import entities.Node;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;


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
    private Button undoButton;

    @FXML
    private ImageView undoIcon;

    @FXML
    private AnchorPane drawingArea;

    @FXML
    private ImageView nodeClick;

    @FXML
    private ImageView drag;

    public Controller() {
        graph = Graph.getInstance();
        drawer = Drawer.getInstance();
    }

    @FXML
    public void changeIcon() {
        trashIcon.setImage(new Image("/assets/opened.png"));

    }

    @FXML
    public void setOldIcon() {
        trashIcon.setImage(new Image("/assets/trash.png"));
    }

    @FXML
    private void setIcons() {
        leftClick.setImage(new Image("/assets/leftClick.png"));
        rightClick.setImage(new Image("/assets/rightClick.png"));
        nodeClick.setImage(new Image("/assets/nodeClick.png"));
        drag.setImage(new Image("/assets/drag.png"));
        undoIcon.setImage(new Image("/assets/undo.png"));
    }

    @FXML
    void initialize() {
        drawingArea.addEventFilter(MouseEvent.MOUSE_CLICKED, Handlers.dragFilter);
        drawingArea.addEventFilter(MouseEvent.MOUSE_CLICKED, Handlers.clickFilter);

        setOldIcon();

        drawer.setPane(drawingArea);

        clearButton.addEventHandler(MouseEvent.MOUSE_ENTERED, Handlers.buttonEnterHandler);
        clearButton.addEventHandler(MouseEvent.MOUSE_EXITED, Handlers.buttonExitHandler);

        undoButton.addEventHandler(MouseEvent.MOUSE_ENTERED, Handlers.buttonEnterHandler);
        undoButton.addEventHandler(MouseEvent.MOUSE_EXITED, Handlers.buttonExitHandler);

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
        if(event.getButton() != MouseButton.PRIMARY) return;
        if (Graph.getInstance().getSize() < Graph.MAX_SIZE) {

            Node node = drawer.drawNode(event);
            Invoker.getInstance().createElement(node);
            //node.create();
        }
    }

    /**
     * Removes all nodes from the pane
     */
    @FXML
    void clearWorkingArea() {
        System.out.println("clear");
        drawingArea.getChildren().removeIf(x -> x.getClass() == Node.class || x.getClass() == Edge.class);
        graph.clearGraph();
    }

    @FXML
    void undoAction(){
        Invoker.getInstance().undoLast();
    }


}
