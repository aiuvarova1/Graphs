package main;

import java.text.DecimalFormat;

public class Formatter {
    private static DecimalFormat formatter = new DecimalFormat();
    static{
        formatter.setMaximumFractionDigits(3);
        formatter.setMinimumFractionDigits(0);
    }

    public static String format (double val){
        String res =  formatter.format(val);
        return res.equals("-0") ? "0" : res;
    }
}
