package main;

import entities.Graph;
import entities.Undoable;
import entities.Edge;
import entities.Node;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class MyContextMenu extends ContextMenu {
    private MenuItem deletion;
    protected Undoable elem;

    public MyContextMenu(){

        deletion = new MenuItem("Delete");

        deletion.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Invoker.getInstance().deleteElement(elem);
            }
        });

        this.getItems().addAll(deletion);

        //setStyle("MyStyle");
    }


    @Override
    public void show(javafx.scene.Node node, double x, double y){

        super.show(node, x, y);
       // elem = (Undoable) node;
    }

    public void bindElem(javafx.scene.Node el){
        elem =(Undoable) el;
    }
}
