package entities;

import java.util.ArrayList;
import java.util.List;

public abstract class InfiniteGraph implements Graph{

    private List<Node> nodes;

    public static int EDGE_LENGTH = 100;

    InfiniteGraph(){
        nodes = new ArrayList<>();
    }

    protected List<Node> getNodes(){
        return nodes;
    }

    /**
     * Resets nodes' visualization info
     */
    public void resetNodes(){
        for(Node n: nodes){
            n.resetNode();
        }
    }

}
