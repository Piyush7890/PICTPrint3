package com.piyush.pictprint.CJT;

import com.google.gson.annotations.SerializedName;
import com.piyush.pictprint.CJT.cdd.PageOrientation;
import com.piyush.pictprint.CJT.cdd.PageOrientationType;


import java.io.Serializable;


public class PageOrientationTicketItem implements Serializable {
   
    @SerializedName("type")
    private String type = PageOrientation.DEFAULT_TYPE.name();


    public void setType(PageOrientationType type) {
        this.type = type != null ? type.name() : PageOrientation.DEFAULT_TYPE.name();
    }

    public PageOrientationType getType() {
        try {
            return PageOrientationType.valueOf(this.type);
        } catch (Exception e) {
            return PageOrientation.DEFAULT_TYPE;
        }
    }
}