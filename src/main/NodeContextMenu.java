package main;

import entities.Edge;
import entities.Graph;
import entities.Node;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;

public class NodeContextMenu extends MyContextMenu{
    public NodeContextMenu(){
        MenuItem selection = new MenuItem("Select as a beginning node");
        selection.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if(Graph.getInstance().getStartEdge()!=null &&
                        ((Node) elem) != Graph.getInstance().getStartEdge().getNodes()[0] &&
                        ((Node) elem) != Graph.getInstance().getStartEdge().getNodes()[1] )
                {
                        Graph.getInstance().getStartEdge().deselect();
                        Graph.getInstance().setStartEdge(null);
                }
                if(Graph.getInstance().getStartNode()!=null)
                    Graph.getInstance().getStartNode().deselect();
                Graph.getInstance().setStartNode((Node)elem);
                ((Node) elem).select();
            }
        });
        this.getItems().add(selection);
    }


}
