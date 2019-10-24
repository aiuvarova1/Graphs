package main;

import entities.Undoable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class EdgeContextMenu extends ContextMenu {
    private MenuItem deletion;
    private MenuItem addLength;
    private Undoable elem;

    public EdgeContextMenu(){

        deletion = new MenuItem("Delete");

        deletion.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Invoker.getInstance().deleteElement(elem);
            }
        });

        addLength = new MenuItem("Add length");

        addLength.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("add length");
            }
        });
        this.getItems().addAll(deletion, addLength);

        System.out.println(getStyle());
        //setStyle("MyStyle");
    }


    @Override
    public void show(javafx.scene.Node node, double x, double y){
        System.out.println(getStyle());
        super.show(node, x, y);
        elem = (Undoable) node;
    }
}
