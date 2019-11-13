package main;

import entities.Distance;
import entities.Edge;
import entities.Graph;
import entities.Node;
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
    private Button resetDistances;

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
    private ImageView resetIcon;

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
        trashIcon.setImage(new Image(Manager.class.getResource("/assets/opened.png").toExternalForm()));
    }

    @FXML
    public void setOldIcon() {
        trashIcon.setImage(new Image(Manager.class.getResource("/assets/trash.png").toExternalForm()));
    }

    @FXML
    private void setIcons() {
        leftClick.setImage(new Image(Manager.class.getResource("/assets/leftClick.png").toExternalForm()));
        rightClick.setImage(new Image(Manager.class.getResource("/assets/rightClick.png").toExternalForm()));
        nodeClick.setImage(new Image(Manager.class.getResource("/assets/nodeClick.png").toExternalForm()));
        drag.setImage(new Image(Manager.class.getResource("/assets/drag.png").toExternalForm()));
        undoIcon.setImage(new Image(Manager.class.getResource("/assets/undo.png").toExternalForm()));
        redoIcon.setImage(new Image(Manager.class.getResource("/assets/redo.png").toExternalForm()));
        resetIcon.setImage(new Image(Manager.class.getResource("/assets/reset.png").toExternalForm()));
    }

    @FXML
    void setButtons(){
        clearButton.addEventHandler(MouseEvent.MOUSE_ENTERED, Filter.buttonEnterHandler);
        clearButton.addEventHandler(MouseEvent.MOUSE_EXITED, Filter.buttonExitHandler);

        undoButton.addEventHandler(MouseEvent.MOUSE_ENTERED, Filter.buttonEnterHandler);
        undoButton.addEventHandler(MouseEvent.MOUSE_EXITED, Filter.buttonExitHandler);

        redoButton.addEventHandler(MouseEvent.MOUSE_ENTERED, Filter.buttonEnterHandler);
        redoButton.addEventHandler(MouseEvent.MOUSE_EXITED, Filter.buttonExitHandler);

        resetDistances.addEventHandler(MouseEvent.MOUSE_ENTERED, Filter.buttonEnterHandler);
        resetDistances.addEventHandler(MouseEvent.MOUSE_EXITED, Filter.buttonExitHandler);
    }

    @FXML
    void initialize() {
        drawingArea.addEventFilter(MouseEvent.MOUSE_CLICKED, Filter.dragFilter);
        drawingArea.addEventFilter(MouseEvent.MOUSE_CLICKED, Filter.clickFilter);

        setOldIcon();

        drawer.setPane(drawingArea);

        setButtons();

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

        new Distance();

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
        || x.getClass() == Distance.class);
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
        graph.setLengths();
    }

    @FXML
    void hideDist(){
        graph.hideLengths();
    }

    @FXML
    void resetDist(){
        graph.resetDistances();
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
