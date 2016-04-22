package com.channelsoft.appframe.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NumberFormatUtils {
    
    public static String getNumberStr (double f) {
        NumberFormat nf = new DecimalFormat("###0.00");
        return nf.format(f);
    }
    
    public static String getNumberStr (String f) {
        NumberFormat nf = new DecimalFormat("###0.00");
        double d = Double.parseDouble(f);  
        return nf.format(d);
    }
}
