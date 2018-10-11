package com.piyush.pictprint.CJT;

import com.google.gson.annotations.SerializedName;
import com.piyush.pictprint.CJT.cdd.Color;
import com.piyush.pictprint.CJT.cdd.Type;


import java.io.Serializable;

public class ColorTicketItem implements Serializable {
  
    @SerializedName("type")
    private String type;
    @SerializedName("vendor_id")
    private String vendorId;




    public ColorTicketItem() {
        this.type = Color.DEFAULT_TYPE.name();
    }

    public void setVendorId(String str) {
        this.vendorId = str;
    }

    public void setType(Type type) {
        this.type = type != null ? type.name() : Color.DEFAULT_TYPE.name();
    }

    public Type getType() {
        try {
            return Type.valueOf(this.type);
        } catch (Exception e) {
            return Color.DEFAULT_TYPE;
        }
    }

}