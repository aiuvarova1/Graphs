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

        currentType = type;
        switch (type){
            case LATTICE:
                graph = new LatticeGraph();
                break;
            case LINE:
                graph = new LineGraph();
                break;
            case SIMPLE:
                erase();
                graph = null;
                break;
            default:
                throw new IllegalArgumentException("Unknown graph type");
        }
    }


    public static void erase(){

        for (Node node: graph.getNodes())
        {
            Drawer.getInstance().removeElement(node);

            for (Edge edge: node.getEdges())
            {
                try{
                    Drawer.getInstance().removeElement(edge);
                }catch(IllegalArgumentException ex){}
            }
        }


        SimpleGraph.showGraph();
    }

    public static boolean canEdit(){
        return currentType == Type.SIMPLE;
    }

    public static void visualize(){
        final Node startNode = graph.getNodes().get(0);
        Visualizer.startVisualization(startNode.getEdges().get(0), startNode);
    }

    public static void resetNodes(){
        if (canEdit())
            SimpleGraph.getInstance().resetNodes();
        else
            graph.resetNodes();

    }
}
