package main;

import entities.Edge;
import entities.Graph;
import entities.Node;
import entities.TexLabel;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;


public class Controller {

    private Graph graph;
    private Drawer drawer;
    private EdgeContextMenu edgeMenu = new EdgeContextMenu();

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
    private Button redoButton;

    @FXML
    private ImageView redoIcon;

    @FXML
    private AnchorPane drawingArea;

    @FXML
    private ImageView nodeClick;

    @FXML
    private ImageView drag;

    @FXML
    private ToggleButton showDictances;

    @FXML
    private ToggleButton hideDistances;

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
        redoIcon.setImage(new Image("/assets/redo.png"));
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

        redoButton.addEventHandler(MouseEvent.MOUSE_ENTERED, Handlers.buttonEnterHandler);
        redoButton.addEventHandler(MouseEvent.MOUSE_EXITED, Handlers.buttonExitHandler);

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

        new TexLabel();
        
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
        drawingArea.getChildren().removeIf(x -> x.getClass() == Node.class || x.getClass() == Edge.class
        || x.getClass() == TexLabel.class);
        graph.clearGraph();
    }

    @FXML
    void undoAction(){
        Invoker.getInstance().undoLast();
    }

    @FXML
    void redoAction(){Invoker.getInstance().redoLast();}

    @FXML
    void showDist(){

        System.out.println("dist");
        graph.setLengths();

       // new TexLabel();
    }



    @FXML
    public static final EventHandler<KeyEvent> shortCuts = new EventHandler<KeyEvent>() {
        final KeyCodeCombination undoComb = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
        final KeyCodeCombination redoComb = new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN);
        @Override
        public void handle(KeyEvent event) {
            if(undoComb.match(event))
                Invoker.getInstance().undoLast();
            else if(redoComb.match(event))
                Invoker.getInstance().redoLast();
        }
    };

}
