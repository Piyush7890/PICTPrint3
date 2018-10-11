package com.piyush.pictprint.CJT;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CloudJobTicket implements Serializable {

    @SerializedName("print")
    private PrintTicketSection printer;
    @SerializedName("version")
    private String version;

    public void setVersion(String str) {
        this.version = str;
    }

    public void setPrinter(PrintTicketSection printTicketSection) {
        this.printer = printTicketSection;
    }

    public PrintTicketSection getPrinter() {
        return this.printer;
    }

}