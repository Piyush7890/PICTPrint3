package com.piyush.pictprint.CJT;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MarginsTicketItem implements Serializable {

    @SerializedName("bottom_microns")
    private int bottomMicrons;
    @SerializedName("left_microns")
    private int leftMicrons;
    @SerializedName("right_microns")
    private int rightMicrons;
    @SerializedName("top_microns")
    private int topMicrons;

  

    public void setTopMicrons(int i) {
        this.topMicrons = i;
    }

    public int getTopMicrons() {
        return this.topMicrons;
    }

    public void setRightMicrons(int i) {
        this.rightMicrons = i;
    }

    public int getRightMicrons() {
        return this.rightMicrons;
    }

    public void setBottomMicrons(int i) {
        this.bottomMicrons = i;
    }

    public int getBottomMicrons() {
        return this.bottomMicrons;
    }

    public void setLeftMicrons(int i) {
        this.leftMicrons = i;
    }

    public int getLeftMicrons() {
        return this.leftMicrons;
    }

   
   
}