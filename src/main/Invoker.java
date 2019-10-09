package main;

import entities.Node;
import entities.Undoable;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;

public class Invoker {
    Cache commands = new Cache();
    private static Invoker instance;

    public static Invoker getInstance(){
        if(instance == null) instance = new Invoker();
        return instance;
    }

    public void createElement(Undoable el){
        Command c = new CreateCommand(el);
        if(c.execute())
            commands.push(c);
    }

    public void deleteElement(Undoable el){
        Command c = new DeleteCommand(el);
        if(c.execute())
            commands.push(c);
    }

    public void undoLast(){

        Command toUndo = commands.pop();

        if(toUndo !=null)
            toUndo.undo();
    }

    public void moveElement(Node el, double[] init, double[] newPos){
        Command c = new MoveCommand(el,init,newPos);
        commands.push(c);
    }
}
