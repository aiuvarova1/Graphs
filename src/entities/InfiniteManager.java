package entities;

import main.Drawer;
import main.Visualizer;

public class InfiniteManager {
    private static InfiniteGraph graph;
    private static Type currentType = Type.SIMPLE;

    public enum Type{
        LATTICE, LINE, SIMPLE;
    }

    public static void init(Type type)
    {
        if(currentType == type)
            return;

        if(currentType == Type.SIMPLE)
            SimpleGraph.hideGraph();
        else
            graph.erase();

        currentType = type;
        switch (type){
            case LATTICE:
                graph = new LatticeGraph();
                break;
            case LINE:
                graph = new LineGraph();
                break;
            case SIMPLE:
                SimpleGraph.showGraph();
                graph = null;
                break;
            default:
                throw new IllegalArgumentException("Unknown graph type");
        }
    }


    public static boolean canEdit(){
        return currentType == Type.SIMPLE;
    }

    public static void visualize(){
        graph.visualize();
    }

    public static void stop(){
        graph.stop();

        graph.erase();
        switch (currentType) {
            case LATTICE:
                graph = new LatticeGraph();
                break;
            case LINE:
                graph = new LineGraph();
                break;
            default:
                break;
        }

    }

    public static void resetNodes(){
        if (canEdit())
            SimpleGraph.getInstance().resetNodes();
        else
            graph.resetNodes();

    }
}
