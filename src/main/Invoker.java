package main;

import entities.Node;
import entities.Undoable;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;

/**
 * Processes commands instances and stores them
 * in Cache stack
 */

public class Invoker {
    private Cache commands = new Cache();
    private static Invoker instance;
    private Command toUndo;
    private Command toRedo;

    /**
     * Singleton
     * @return an instance of Invoker
     */
    public static Invoker getInstance(){
        if(instance == null) instance = new Invoker();
        return instance;
    }

    /**
     * Calls create command
     * @param el element to create
     */
    public void createElement(Undoable el){
        Command c = new CreateCommand(el);
        if(c.execute())
            commands.push(c);
    }

    /**
     * Calls delete command
     * @param el element to delete
     */
    public void deleteElement(Undoable el){
        Command c = new DeleteCommand(el);
        if(c.execute())
            commands.push(c);
    }

    /**
     * Reverts last command in cache
     */
    public void undoLast(){

        toUndo = commands.pop();

        if(toUndo !=null)
            toUndo.undo();
    }

    public void redoLast(){
        toRedo = commands.getNext();
        if (toRedo != null) {
            toRedo.execute();
        }
    }

    public void moveElement(Node el, double[] init, double[] newPos){
        Command c = new MoveCommand(el,init,newPos);
        commands.push(c);
    }
}
