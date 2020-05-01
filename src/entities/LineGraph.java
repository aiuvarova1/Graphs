package entities;

import main.Drawer;

public class LineGraph extends InfiniteGraph {

    private static final double START_X = 50;
    private static final double START_Y = Drawer.getInstance().getBounds().getHeight()/2;


    public LineGraph(){
        Node prevNode = Drawer.getInstance().drawInfiniteNode(START_X, START_Y,0);
        getNodes().add(prevNode);
        Drawer.getInstance().addElem(prevNode);

        Node node;
        Edge edge;
        for (int i = 1; i < 10; i++)
        {
            node = Drawer.getInstance().drawInfiniteNode(prevNode.getLayoutX() + EDGE_LENGTH, START_Y,i);
            getNodes().add(node);
            Drawer.getInstance().addElem(node);
            edge = new Edge(0,0,0,0);
            edge.setNodes(prevNode, node);
            edge.connectNodes(prevNode, node);


            prevNode.addEdge(node, edge);
            node.addEdge(prevNode, edge);

            Drawer.getInstance().addElem(edge);
            edge.hideLength();

            prevNode = node;

        }
    }
}
