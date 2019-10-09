package main;

import java.util.ArrayList;
import java.util.Stack;



public class Cache extends Stack<Command> {
    private static final int CAPACITY = 20;
    private Command[] stack = new Command[CAPACITY];

    private int curCapacity;

    private int pointer = -1;


    public void clear(){

    }

    @Override
    public Command push(Command elem){
        pointer++;

        if(pointer == CAPACITY)
            pointer = 0;
        stack[pointer] = elem;
        System.out.println(pointer + " " );
        if(curCapacity < 5) curCapacity ++;
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
            return toReturn;
        }else{
            return null;
        }
    }
}
