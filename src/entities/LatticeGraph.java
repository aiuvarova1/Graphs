package entities;


import javafx.scene.control.ScrollPane;
import main.Drawer;

public class LatticeGraph extends InfiniteGraph {

    private ScrollPane pane = new ScrollPane();

    public LatticeGraph(){
        Drawer.getInstance().addElem(pane);

    }

    @Override
    public void erase() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void visualize() {

    }
}
