package com.piyush.pictprint.CJT;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class PageRangeTicketItem implements Serializable {

    @SerializedName("interval")
    private List<Interval> intervals;

    public static class Interval implements Serializable {

        @SerializedName("end")
        private int end;
        @SerializedName("start")
        private int start;

       

        public Interval(int i, int i2) {
            start = i;
            end = i2;
        }
        
    }

    public void setIntervals(List<Interval> list) {
        this.intervals = list;
    }

}