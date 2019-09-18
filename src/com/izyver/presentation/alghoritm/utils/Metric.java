package com.izyver.presentation.alghoritm.utils;

public class Metric {

    private final String tag;
    private long startTime = 0;
    private int i = 0;
    private int period = 0;

    public Metric(String tag) {
        this.tag = tag;
        this.period = -1;
    }

    public Metric(String tag, int period) {
        this.tag = tag;
        this.period = period;
    }

    public void start(){
        startTime = System.currentTimeMillis();
    }

    public void end(){
        if (i++ % period == 0 || period == -1) {
            System.out.println(tag + ": " + (System.currentTimeMillis() - startTime) + " "  + i);
        }
    }

    public void reset(){
        i = 0;
    }
}
