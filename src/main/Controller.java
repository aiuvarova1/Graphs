package main;

import entities.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;



public class Controller {

    private Drawer drawer;

    @FXML
    private ToggleButton showDistances;

//    @FXML
//    private ToggleGroup showHide;

    @FXML
    private ToggleButton hideDistances;

    @FXML
    private Button setAll;

    @FXML
    private ImageView setAllIcon;

    @FXML
    private CheckBox numeric;

    @FXML
    private CheckBox colour;

    @FXML
    private CheckBox arrows;

    @FXML
    private Button stopVisualize;

    @FXML
    private ImageView stopIcon;

    @FXML
    private TextField allLengths;


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

    @FXML
    private Label minColor;

    @FXML
    private Label maxColor;

    @FXML
    private Button saveButton;

    @FXML
    private ImageView saveIcon;

    @FXML
    private Button openButton;

    @FXML
    private ImageView openIcon;

    @FXML
    private Button saveAsButton;

    @FXML
    private ImageView saveAsIcon;

    @FXML
    private StackPane dialog;

    @FXML
    private Button saveButton2;

    @FXML
    private ImageView saveIcon2;

    @FXML
    private Button discardButton;

    @FXML
    private ImageView discardIcon;

    @FXML
    private Button cancelButton;

    @FXML
    private ImageView cancelIcon;

    public Controller() {
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
        setAllIcon.setImage(new Image(Manager.class.getResource("/assets/confirm.png").toExternalForm()));
        openIcon.setImage(new Image(Manager.class.getResource("/assets/open.png").toExternalForm()));
        saveIcon.setImage(new Image(Manager.class.getResource("/assets/save.png").toExternalForm()));
        saveAsIcon.setImage(new Image(Manager.class.getResource("/assets/save.png").toExternalForm()));
        saveIcon2.setImage(new Image(Manager.class.getResource("/assets/save.png").toExternalForm()));
        discardIcon.setImage(new Image(Manager.class.getResource("/assets/discard.png").toExternalForm()));
        cancelIcon.setImage(new Image(Manager.class.getResource("/assets/close.png").toExternalForm()));
    }

    @FXML
    private void setButtons() {
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

        setAll.addEventHandler(MouseEvent.MOUSE_ENTERED, event ->
        {
            setAllIcon.setScaleX(11 / 10.0);
            setAllIcon.setScaleY(11 / 10.0);
        });
        setAll.addEventHandler(MouseEvent.MOUSE_EXITED, event ->
        {
            setAllIcon.setScaleX(1);
            setAllIcon.setScaleY(1);

        });

        saveButton.addEventFilter(MouseEvent.MOUSE_ENTERED, Filter.buttonEnterHandler);
        saveButton.addEventHandler(MouseEvent.MOUSE_EXITED, Filter.buttonExitHandler);

        saveAsButton.addEventFilter(MouseEvent.MOUSE_ENTERED, Filter.buttonEnterHandler);
        saveAsButton.addEventHandler(MouseEvent.MOUSE_EXITED, Filter.buttonExitHandler);

        openButton.addEventFilter(MouseEvent.MOUSE_ENTERED, Filter.buttonEnterHandler);
        openButton.addEventHandler(MouseEvent.MOUSE_EXITED, Filter.buttonExitHandler);

        String unselected = "-fx-background-color: #e1e1e1;" + "-fx-font-size: 16px;"
                + "-fx-font-family: \"Constantia\";";

        String selected = "-fx-background-color: #e1e1e1;" + "-fx-font-size: 17px;"
                + "-fx-font-family: \"Constantia\";";

        cancelButton.addEventFilter(MouseEvent.MOUSE_ENTERED, event ->
            ((Button)event.getSource()).setStyle(selected));
        cancelButton.addEventHandler(MouseEvent.MOUSE_EXITED, event ->
                ((Button)event.getSource()).setStyle(unselected));

        saveButton2.addEventFilter(MouseEvent.MOUSE_ENTERED,event ->
            ((Button)event.getSource()).setStyle(selected));
        saveButton2.addEventHandler(MouseEvent.MOUSE_EXITED, event ->
            ((Button)event.getSource()).setStyle(unselected)
        );

        discardButton.addEventHandler(MouseEvent.MOUSE_ENTERED, event ->
            ((Button)event.getSource()).setStyle(selected));

        discardButton.addEventHandler(MouseEvent.MOUSE_EXITED, event ->
            ((Button)event.getSource()).setStyle(unselected)
        );


    }

    private void addListeners() {
        drawingArea.widthProperty().addListener((axis, oldVal, newVal) -> {

            if (Visualizer.isRunning()) return;
            drawingArea.setPrefWidth(newVal.doubleValue());
            Graph.getInstance().rescale('x', oldVal.doubleValue(), newVal.doubleValue());
        });

        drawingArea.heightProperty().addListener((axis, oldVal, newVal) -> {

            if (Visualizer.isRunning()) return;
            // drawingArea.setPrefHeight(newVal.doubleValue());
            Graph.getInstance().rescale('y', oldVal.doubleValue(), newVal.doubleValue());
        });

        calculate.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue)
                drawingArea.getChildren().filtered(x -> x instanceof Distance).
                        forEach((x) -> ((Distance) x).calculate());
            else
                drawingArea.getChildren().filtered(x -> x instanceof Distance).
                        forEach((x) -> ((Distance) x).decalculate());
            Distance.setCalc(newValue);

        });

        numeric.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue)
                drawingArea.getChildren().filtered(x -> x instanceof Point).
                        forEach((x) -> ((Point) x).showNumbers());
            else
                drawingArea.getChildren().filtered(x -> x instanceof Point).
                        forEach((x) -> ((Point) x).hideNumbers());
            Visualizer.setNumeric(newValue);

        });

        colour.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue)
                drawingArea.getChildren().filtered(x -> x instanceof Point).
                        forEach((x) -> ((Point) x).showColour());
            else
                drawingArea.getChildren().filtered(x -> x instanceof Point).
                        forEach((x) -> ((Point) x).hideColour());
            Visualizer.setColour(newValue);

        });

        arrows.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue)
                drawingArea.getChildren().filtered(x -> x instanceof Point).
                        forEach((x) -> ((Point) x).showArrow());
            else
                drawingArea.getChildren().filtered(x -> x instanceof Point).
                        forEach((x) -> ((Point) x).hideArrow());
            Visualizer.setArrows(newValue);

        });

        allLengths.setOnAction(event -> changeDist());
        allLengths.setOnKeyTyped(event -> {
            String string = allLengths.getText();

            if (string.length() > Distance.MAX_LENGTH) {
                allLengths.setText(string.substring(0, Distance.MAX_LENGTH));
                allLengths.positionCaret(string.length());
            }
        });

        saveButton.setOnAction(event -> FileManager.save());
        saveAsButton.setOnAction(event -> FileManager.saveAs());
        openButton.setOnAction(event -> openFile());

        saveButton.disableProperty().bind(FileManager.getDisable());

    }

    private void openFile(){
        if(FileManager.isSaveNeeded())
        {
            dialog.setDisable(false);
            dialog.setVisible(true);

        }else{
            FileManager.open();
        }

    }

    @FXML
    void saveUnchanged(){
        hideDialog();
        FileManager.save();
        FileManager.open();
    }

    @FXML
    void discardAndOpen(){
        hideDialog();
        FileManager.open();
    }

    @FXML
    void hideDialog(){
        dialog.setDisable(true);
        dialog.setVisible(false);
    }



    @FXML
    void initialize() {
        drawingArea.addEventFilter(MouseEvent.MOUSE_CLICKED, Filter.dragFilter);
        drawingArea.addEventFilter(MouseEvent.MOUSE_CLICKED, Filter.clickFilter);

        setOldIcon();

        drawer.setPane(drawingArea);

        setButtons();

        addListeners();
        Visualizer.bindBounds(minColor, maxColor);

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
        if (Visualizer.isRunning()) return;

        if (event.getButton() != MouseButton.PRIMARY) return;
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
        Graph.getInstance().clearGraph();
    }



    @FXML
    void undoAction() {
        Invoker.getInstance().undoLast();
    }

    @FXML
    void redoAction() {
        Invoker.getInstance().redoLast();
    }

    /**
     * Shows lengths of the edges
     */
    @FXML
    void showDist() {

        if(Graph.areDistancesShown()) {
            showDistances.setSelected(true);
            return;
        }
        Graph.getInstance().setLengths();
        calculate.setDisable(false);
        setAll.setDisable(false);
        allLengths.setDisable(false);

    }

    /**
     * Makes current graph not weighed
     */
    @FXML
    void hideDist() {

        if(!Graph.areDistancesShown()) {
            hideDistances.setSelected(true);
            return;
        }

        Graph.getInstance().hideLengths();
        calculate.setDisable(true);
        setAll.setDisable(true);
        allLengths.setDisable(true);
    }

    @FXML
    void resetDist() {

        Graph.getInstance().resetDistances();
    }

    @FXML
    private void changeDist(){
        Graph.getInstance().changeDistances(allLengths.getText());
    }

    /**
     * Shortcuts event handlers for undo, redo, save
     */
    @FXML
    static final EventHandler<KeyEvent> shortCuts = new EventHandler<KeyEvent>() {
        final KeyCodeCombination undoComb = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
        final KeyCodeCombination redoComb = new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN);
        final KeyCodeCombination saveComb =
                new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        final KeyCodeCombination saveAsComb =
                new KeyCodeCombination(KeyCode.S, KeyCombination.SHIFT_ANY, KeyCombination.CONTROL_DOWN);
        final KeyCodeCombination openComb =
                new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN);


        @Override
        public void handle(KeyEvent event) {
            if (Visualizer.isRunning()) return;

            if (undoComb.match(event))
                Invoker.getInstance().undoLast();
            else if (redoComb.match(event))
                Invoker.getInstance().redoLast();
            else if (saveComb.match(event) && FileManager.isSaveNeeded())
                FileManager.save();
            else if(saveAsComb.match(event))
                FileManager.saveAs();
//            else if(openComb.match(event))
//                Controller.openFile();

        }
    };

    /**
     * Starts amplitudes' distribution
     */
    @FXML
    void visualizeAmplitudes() {


        if (!Graph.areDistancesShown()) {
            PopupMessage.showMessage("The distances are disabled");
            return;
        }

        for (javafx.scene.Node dist : drawingArea.getChildren().filtered(x -> x instanceof Distance)) {
            if (((Distance) dist).isInfty()) {
                PopupMessage.showMessage("There must be no infinities in distances");
                return;
            }
        }

        Graph.getInstance().visualizeAmplitudes();
        if (Visualizer.isRunning()) {
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
    void stopVisualizing() {
        Visualizer.stopVisualization();

        visualizeAmplitudes.setDisable(false);
        stopVisualize.setDisable(true);

        drawTitledPane.setDisable(false);
        distancesTitledPane.setDisable(false);
    }


}
