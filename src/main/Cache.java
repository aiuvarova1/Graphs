package main;

import java.util.ArrayList;
import java.util.Stack;


/**
 * Represents a cycled stack of commands
 */
public class Cache extends Stack<Command> {
    private static final int CAPACITY = 20;
    private Command[] stack = new Command[CAPACITY];

    private int curCapacity;

    private int pointer = -1;
    private int redoPointer = 0;


    @Override
    public Command push(Command elem){
        pointer++;
        redoPointer = 0;

        if(pointer == CAPACITY)
            pointer = 0;
        stack[pointer] = elem;
        System.out.println(pointer + " " );
        if(curCapacity < CAPACITY) curCapacity ++;
        return elem;
    }

    @Override
    public Command pop(){
        System.out.println(curCapacity);
        if (stack.length > 0 && curCapacity > 0){
            Command toReturn = stack[pointer];
            pointer--;
            if(pointer < 0)
                pointer = CAPACITY - 1;
            curCapacity--;
            redoPointer++;
            return toReturn;
        }else{
            return null;
        }
    }

    public Command getNext(){

        if(curCapacity == CAPACITY || redoPointer == 0) return null;

        pointer++;
        redoPointer--;

        if(pointer == CAPACITY)
            pointer = 0;
        curCapacity++;
        System.out.println(pointer + " " + redoPointer + " " +
                curCapacity + " ");
        return stack[pointer];
    }
}
