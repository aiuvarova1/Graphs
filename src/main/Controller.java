package main;

import entities.Distance;
import entities.Edge;
import entities.Graph;
import entities.Node;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

    @FXML
    private Button stopVisualize;

    @FXML
    private ImageView stopIcon;


    @FXML
    private ImageView leftClick;

    @FXML
    private ImageView rightClick;

    @FXML
    private TitledPane helpTitledPane;

    @FXML
    private ImageView trashIcon;

    @FXML
    private Accordion accordion;

    @FXML
    private TitledPane drawTitledPane;

    @FXML
    private Button visualizeAmplitudes;

    @FXML
    private ImageView startIcon;

    @FXML
    private Button resetDistances;

    @FXML
    private Button clearButton;

    @FXML
    private CheckBox calculate;

    @FXML
    private Button undoButton;

    @FXML
    private ImageView undoIcon;

    @FXML
    private Button redoButton;

    @FXML
    private TitledPane distancesTitledPane;

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
    private Label tip;


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
        startIcon.setImage(new Image(Manager.class.getResource("/assets/play.png").toExternalForm()));
        stopIcon.setImage(new Image(Manager.class.getResource("/assets/stop.png").toExternalForm()));
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

        visualizeAmplitudes.addEventHandler(MouseEvent.MOUSE_ENTERED, Filter.buttonEnterHandler);
        visualizeAmplitudes.addEventHandler(MouseEvent.MOUSE_EXITED, Filter.buttonExitHandler);

        stopVisualize.addEventFilter(MouseEvent.MOUSE_ENTERED, Filter.buttonEnterHandler);
        stopVisualize.addEventHandler(MouseEvent.MOUSE_EXITED, Filter.buttonExitHandler);
    }

    @FXML
    void initialize() {
        drawingArea.addEventFilter(MouseEvent.MOUSE_CLICKED, Filter.dragFilter);
        drawingArea.addEventFilter(MouseEvent.MOUSE_CLICKED, Filter.clickFilter);

        setOldIcon();

        drawer.setPane(drawingArea);

        setButtons();

        drawingArea.widthProperty().addListener((axis, oldVal, newVal) -> {

            if(Visualizer.isRunning()) return;
            drawingArea.setPrefWidth(newVal.doubleValue());
            graph.rescale('x', oldVal.doubleValue(), newVal.doubleValue());
        });

        drawingArea.heightProperty().addListener((axis, oldVal, newVal) -> {

            if(Visualizer.isRunning()) return;
            // drawingArea.setPrefHeight(newVal.doubleValue());
            graph.rescale('y', oldVal.doubleValue(), newVal.doubleValue());
        });

        calculate.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue)
                    drawingArea.getChildren().filtered(x -> x instanceof Distance).
                            forEach((x)->((Distance)x).calculate());
                else
                    drawingArea.getChildren().filtered(x -> x instanceof Distance).
                            forEach((x)->((Distance)x).decalculate());

            }
        });

        drawTitledPane.setAnimated(true);
        helpTitledPane.setAnimated(true);
        accordion.setExpandedPane(drawTitledPane);
        setIcons();

        new Distance();
        PopupMessage.setPopup(tip);

    }

    /**
     * Creates the node on click
     *
     * @param event click-info
     */
    @FXML
    void createNode(MouseEvent event) {
        if(Visualizer.isRunning()) return;

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
        calculate.setDisable(false);
    }

    @FXML
    void hideDist(){

        graph.hideLengths();
        calculate.setDisable(true);
    }

    @FXML
    void resetDist(){

        graph.resetDistances();
    }

    /**
     * Shortcuts event handlers for undo and redo
     */
    @FXML
    public static final EventHandler<KeyEvent> shortCuts = new EventHandler<KeyEvent>() {
        final KeyCodeCombination undoComb = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
        final KeyCodeCombination redoComb = new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN);
        @Override
        public void handle(KeyEvent event) {
            if(Visualizer.isRunning()) return;

            if(undoComb.match(event))
                Invoker.getInstance().undoLast();
            else if(redoComb.match(event))
                Invoker.getInstance().redoLast();
        }
    };

    /**
     * Starts amplitudes' distribution
     */
    @FXML
    void visualizeAmplitudes(){


        if(!graph.areDistancesShown()){
            PopupMessage.showMessage("The distances are disabled");
            return;
        }
        for(javafx.scene.Node dist: drawingArea.getChildren().filtered(x-> x instanceof Distance)){
            if(((Distance)dist).isInfty()) {
                PopupMessage.showMessage("There must be no infinities in distances");
                return;
            }
        }

        graph.visualizeAmplitudes();
        if(Visualizer.isRunning()) {
            drawTitledPane.setDisable(true);
            distancesTitledPane.setDisable(true);
            visualizeAmplitudes.setDisable(true);
            stopVisualize.setDisable(false);
        }
    }

    /**
     * Stops amplitudes' distribution
     */
    @FXML
    void stopVisualizing(){
        Visualizer.stopVisualization();

        visualizeAmplitudes.setDisable(false);
        stopVisualize.setDisable(true);

        drawTitledPane.setDisable(false);
        distancesTitledPane.setDisable(false);
    }

}
