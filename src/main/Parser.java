package main;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Parser {

    private static class Token{
        String val;
        Token(String val){
            this.val = val;
        }
    }

    private static class Operation extends Token{
        int priority;

        Operation(char sign, int priority){
            super(String.valueOf(sign));
            this.priority = priority;

        }

        double execute(double x, double y){
            switch(val) {
                case "+":
                    return x+y;
                case "-":
                    return x-y;
                case "*":
                    return x*y;
                case "/":
                    if(y == 0)
                        throw new IllegalArgumentException("Division by zero");
                    return x/y;
                case "%":
                    if(y == 0)
                        throw new IllegalArgumentException("Division by zero");
                    return x%y;
                case "^":
                    return Math.pow(x,y);
                default:
                    return 0;
            }
        }

    }

    private static class Function extends Operation{
        double data;
        Function(char sign, int priority, double data){
            super(sign, priority);
            this.data = data;
        }

        double execute(double x){
            switch(val){
                case "r":
                    if(data==0 || x==0 || (x<0 && data%2==0))
                        return -1;
                    return Math.pow(x,1.0/data);
                default:
                    return -1;
            }
        }
    }



    private static HashMap<Character,Operation> operations;
    static {
        operations = new HashMap<>();
        operations.put('+',new Operation('+',0));
        operations.put('-',new Operation('-',0));
        operations.put('*',new Operation('*',1));
        operations.put('/',new Operation('/',1));
        operations.put('%',new Operation('%',1));
        operations.put('^',new Operation('^',2));
    }

    /**
     * Parses input into a double
     * @param input entered distance
     * @return incorrect input ? -1 : result distance
     */
    public static double parseDistance(String input){
       // input = input.strip();
        input = input.chars().mapToObj(x->String.valueOf((char)x)).filter(x -> !x.equals(" ")).
                collect(Collectors.joining());

        if(input.equals("\\infty") || input.equals("+\\infty"))
            return Double.POSITIVE_INFINITY;
        if(input.equals("-\\infty"))
            return Double.NEGATIVE_INFINITY;

        ArrayDeque<Token> queue = new ArrayDeque<>();
        ArrayDeque<Token> stack = new ArrayDeque<>();

        if(!createPolandNotation(queue, stack, input))
            return -1;

        double cur;
        Token op;


        ArrayDeque<Double> res = new ArrayDeque<>();
        while(!queue.isEmpty()){
            op = queue.removeFirst();
            try {
                cur = Double.parseDouble(op.val);
                res.addLast(cur);
            }catch(NumberFormatException ex) {
                if(!(op instanceof  Operation) || res.size() < 1) return -1;
                double second = res.removeLast();
                if (op instanceof  Function)
                    res.addLast(((Function)op).execute(second));
               // if(&& res.size()op.val.equals(("-")) )
                else if (res.size()>=1)
                    res.addLast(((Operation)op).execute(res.removeLast(), second));
            }
        }

        if(res.size()!= 1 || res.getFirst() <= 0 || res.getFirst() > 200000)
            return -1;
        return res.getFirst();
    }


    /**
     * Creates a Poland Notation from the input
     * @param queue here the notation itself is stored
     * @param stack keeps operations and braces
     * @param input user's input
     * @return poland notation in the queue
     */
    private static boolean createPolandNotation(ArrayDeque<Token> queue, ArrayDeque<Token> stack,
                                             String input){

        Operation op;
        StringBuilder number = new StringBuilder();
        boolean isFunction = false;
        boolean prevFunc = false;
        int openedFunc = 0;
        if(input.length() == 0) return false;

        for(int cur = 0; cur < input.length(); cur++){
            if(prevFunc && input.charAt(cur) != '{' ) return false;
            //if digit or point - try to collect a number
            if(Character.isDigit(input.charAt(cur)) || (cur > 0 && input.charAt(cur) == '.' &&
                    Character.isDigit(input.charAt(cur-1)) &&
                    cur < input.length()-1 && Character.isDigit(input.charAt(cur+1)))){

                number.append(input.charAt(cur));

            }else if (input.charAt(cur) == '(' || (prevFunc && input.charAt(cur) == '{')) {
                stack.addLast(new Token(String.valueOf(input.charAt(cur))));
                prevFunc = false;
            }
            else if (input.charAt(cur) == ')'|| input.charAt(cur) == '}') {

                if(number.length() != 0)
                    queue.addLast(new Token(number.toString()));
                number.delete(0,number.length());

                if(input.charAt(cur) == '}'){
                    while(!stack.isEmpty() && !stack.peekLast().val.equals("{")) {
                        if(stack.peekLast() instanceof Function || !(stack.peekLast() instanceof Operation))
                            return false;
                        queue.addLast(stack.removeLast());
                    }
                    if(stack.isEmpty()) return false;
                    stack.removeLast();
                    queue.addLast(stack.removeLast());
                }else {
                    while (!stack.isEmpty() && !stack.peekLast().val.equals("(")) {
                        if(stack.peekLast() instanceof Function || !(stack.peekLast() instanceof Operation))
                            return false;
                        queue.addLast(stack.removeLast());
                    }
                    if(stack.isEmpty()) return false;
                    stack.removeLast();
                }

            } else {
                if(number.length() != 0)
                    queue.addLast(new Token(number.toString()));
                number.delete(0,number.length());

                if(input.charAt(cur) == '\\'){
                    if(input.substring(cur,cur+5).equals("\\sqrt")){
                        cur = cur+5;
                        if(input.charAt(cur) == '['){
                            while(cur + 1 < input.length() && Character.isDigit(input.charAt(++cur)))
                                number.append(input.charAt(cur));
                            if(input.charAt(cur) != ']') return false;
                        }else
                            cur--;
                        stack.addLast(new Function('r',2,
                                number.length() == 0 ? 2 : Integer.parseInt(number.toString())));
                        number.delete(0, number.length());
                        prevFunc = true;
                    }
                }
                else {
                    op = operations.get(input.charAt(cur));
                    if (op != null) {
                        while (!stack.isEmpty() && stack.peekLast() instanceof Operation &&
                                ((Operation) (stack.peekLast())).priority >= op.priority)
                            queue.addLast(stack.removeLast());
                        stack.addLast(op);
                        if(op == operations.get('^'))
                            prevFunc = true;
                    } else
                        return false;
                }
            }
        }

        if(number.length() != 0)
            queue.addLast(new Token(number.toString()));

        while(!stack.isEmpty())
        {
            if(stack.peekLast().val.equals("(") || stack.peekLast().val.equals("{"))
                return false;
            queue.addLast(stack.removeLast());
        }
        return true;
    }

}
