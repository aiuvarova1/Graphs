package entities;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import main.*;

import javax.swing.text.NumberFormatter;
import java.text.DecimalFormat;

/**
 * Control with distance text label and input field
 */
public class Distance extends Pane {
    private TexLabel label;
    private TextField input;

    private double value = Double.MAX_VALUE;
    private String curText = TexLabel.DEFAULT;

    private static final int MAX_LENGTH = 70;
    private static boolean isCalculated = false;
    private static DecimalFormat formatter = new DecimalFormat();
    static{
        formatter.setMaximumFractionDigits(4);
        formatter.setMinimumFractionDigits(0);
    }

    public static void setCalc(boolean val){isCalculated = val;}


    public Distance(){
        label = new TexLabel();
        input = new TextField();

        input.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showLabel();
                Filter.endEdit();
            }
        });
        input.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue,
                                Boolean old, Boolean newVal) {
                if(!newVal){
                    showLabel();
                    Filter.endEdit();
                }
            }
        });

        input.setOnKeyTyped(event -> {
            String string = input.getText();

            if (string.length() > MAX_LENGTH) {
                input.setText(string.substring(0, MAX_LENGTH));
                input.positionCaret(string.length());
            }
        });

        this.setHeight(label.getHeight());
        this.setWidth(label.getWidth());

        this.getChildren().add(label);
        input.setDisable(true);

        if(Graph.getInstance().areDistancesShown())
            show();

        this.addEventFilter(MouseEvent.MOUSE_CLICKED, Filter.clickFilter);
    }

    /**
     * Hides label and shows input field
     */
    public void showInput(){
        if(Filter.isEditing()) return;

        this.getChildren().add(input);
        input.setDisable(false);
        input.requestFocus();
        this.getChildren().remove(label);
        input.toFront();
    }

    /**
     * Hides input field and returns label
     */
    public void showLabel(){
        if(!Filter.isEditing()) return;
//        setText(input.getText());

        try{
            value = Parser.parseDistance(input.getText());
            System.out.println(value);
            if(!isCalculated)
                Invoker.getInstance().changeDistance(this,input.getText());
            else
                Invoker.getInstance().changeDistance(this, formatter.format(value) );
        }catch(IllegalArgumentException ex){
            System.out.println(ex.getMessage());
            PopupMessage.showMessage(ex.getMessage());
        }finally {
            this.getChildren().add(label);
            this.getChildren().remove(input);
            label.toFront();
            input.setDisable(true);
        }
    }

    public void show(){
        Drawer.getInstance().addElem(this);
    }

    public void hide(){
        Drawer.getInstance().removeElement(this);
    }

    public void setText(String text){
        curText = label.setText(text);
    }
    public String getText(){return curText;}

    /**
     * Calculates the length in input
     */
    public void calculate(){
        System.out.println(value);
        if(value != Double.MAX_VALUE && value!= Double.MIN_VALUE)
            label.setText(formatter.format(value));
    }

    /**
     * Returns the length to the initial state (before any computations)
     */
    public void decalculate(){
        if(value != Double.MAX_VALUE && value!= Double.MIN_VALUE)
            label.setText(curText);
    }

    /**
     * Resets the length to infinity
     */
    public void reset(){
        value = Double.MAX_VALUE;
        curText = label.setText("\\infty");
    }

    /**
     * @return Is length infinite
     */
    public boolean isInfty(){
        return value==Double.MAX_VALUE || value==Double.MIN_VALUE;
    }

}



