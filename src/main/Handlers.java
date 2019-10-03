package main;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class Handlers {

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
}
