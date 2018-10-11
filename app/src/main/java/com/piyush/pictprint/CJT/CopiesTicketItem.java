package com.piyush.pictprint.CJT;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CopiesTicketItem implements Serializable {

    @SerializedName("copies")
    private int copies = 1;

    public void setCopies(int i) {
        this.copies = i;
    }

    public int getCopies() {
        return this.copies;
    }


}