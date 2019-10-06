package main;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class Handlers {

    private static String SELECTED_BUTTON = "-fx-background-color: #ebebeb;" + "-fx-font-size: 22px;"
            + "-fx-font-family: \"Constantia\";";

    private static String UNSELECTED_BUTTON = "-fx-background-color: #f5f5f5;" + "-fx-font-size: 21px;"
            + "-fx-font-family: \"Constantia\";";

    private static boolean dragging = false;

    public static final EventHandler<MouseEvent> dragFilter = new EventHandler<MouseEvent>() {
        @Override
        public void handle(final MouseEvent event) {

            if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                dragging = true;
            }else if(event.getEventType() == MouseEvent.MOUSE_CLICKED){
                if(dragging) event.consume();
                dragging = false;
            }

        }
    };

    public static final EventHandler<MouseEvent> clickFilter = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
                if(event.getSource().getClass() == StackPane.class) {
                    event.consume();
                    System.out.println("here i am");
                }
        }
    };

    public static final EventHandler<MouseEvent> buttonEnterHandler= new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            Button b = (Button)event.getSource();
            //b.setTextFill(Color.);
            b.setStyle(SELECTED_BUTTON);
            ((Button) event.getSource()).getScene().setCursor(Cursor.HAND);
        }
    };

    public static final EventHandler<MouseEvent> buttonExitHandler= new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            Button b = (Button)event.getSource();
           // b.setTextFill(b.getTextFill().);

            b.setStyle(UNSELECTED_BUTTON);
            ((Button) event.getSource()).getScene().setCursor(Cursor.DEFAULT);
        }
    };


}
