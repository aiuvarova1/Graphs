package entities;

import com.sun.webkit.network.data.Handler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import main.Drawer;
import main.Handlers;
import main.Invoker;
import org.jfree.fx.FXGraphics2D;
import org.scilab.forge.jlatexmath.ParseException;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

/**
 * Control with distance text label and input field
 */
public class Distance extends Pane {
    private TexLabel label;
    private TextField input;
    private String curText = TexLabel.DEFAULT;

    private static final int MAX_LENGTH = 20;


    public Distance(){
        label = new TexLabel();
        input = new TextField();

        input.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                showLabel();
                Handlers.endEdit();
            }
        });
        input.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue,
                                Boolean old, Boolean newVal) {
                System.out.println("changed");
                System.out.println(old + " " + newVal);
                if(!newVal){
                    showLabel();
                    Handlers.endEdit();
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

        if(Graph.getInstance().areDistancesShown())
            show();

        this.addEventFilter(MouseEvent.MOUSE_CLICKED, Handlers.clickFilter);
    }

    /**
     * Hides label and shows input field
     */
    public void showInput(){
        if(Handlers.isEditing()) return;

        this.getChildren().add(input);
        input.requestFocus();
        this.getChildren().remove(label);
        input.toFront();
    }

    /**
     * Hides input field and returns label
     */
    public void showLabel(){
        if(!Handlers.isEditing()) return;
//        setText(input.getText());

        Invoker.getInstance().changeDistance(this,input.getText());
        this.getChildren().add(label);
        this.getChildren().remove(input);
        label.toFront();
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

}



