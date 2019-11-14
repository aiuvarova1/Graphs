package main;

import javafx.animation.FadeTransition;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class PopupMessage {
    private static Label popup;
    private static FadeTransition ft;

    public static  void setPopup(Label label){
        popup = label;
       // Drawer.getInstance().removeElement(popup);

        ft = new FadeTransition(Duration.millis(3000),popup);
        ft.setFromValue(0.9);
        ft.setToValue(0);
        ft.setDelay(Duration.millis(3000));
        ft.setOnFinished((x)->popup.setVisible(false));
    }

    public static void showMessage(String mes){
        ft.stop();

        popup.setVisible(true);
        popup.setText(mes);
        popup.setOpacity(0.9);
        popup.toFront();
        //Drawer.getInstance().addElem(popup);
        ft.play();
    }
}
