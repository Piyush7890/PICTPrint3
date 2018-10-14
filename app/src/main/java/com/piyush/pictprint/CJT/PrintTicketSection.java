package com.piyush.pictprint.CJT;



import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PrintTicketSection implements Serializable {

    @SerializedName("vendor_ticket_item")
    private List<Object> vendor_ticket_item = new ArrayList<>();

    @SerializedName("color")
    private ColorTicketItem color;
    @SerializedName("copies")
    private CopiesTicketItem copies;

    @SerializedName("margins")
    private MarginsTicketItem margins;
    @SerializedName("page_orientation")
    private PageOrientationTicketItem pageOrientation;
    @SerializedName("page_range")
    private PageRangeTicketItem pageRange;

   

    public void setColor(ColorTicketItem colorTicketItem) {
        this.color = colorTicketItem;
    }

    public ColorTicketItem getColor() {
        return this.color;
    }

    public void setPageOrientation(PageOrientationTicketItem pageOrientationTicketItem) {
        this.pageOrientation = pageOrientationTicketItem;
    }

    public PageOrientationTicketItem getPageOrientation() {
        return this.pageOrientation;
    }

    public void setCopies(CopiesTicketItem copiesTicketItem) {
        this.copies = copiesTicketItem;
    }

    public CopiesTicketItem getCopies() {
        return this.copies;
    }

    public void setMargins(MarginsTicketItem marginsTicketItem) {
        this.margins = marginsTicketItem;
    }

    public MarginsTicketItem getMargins() {
        return this.margins;
    }


    public PageRangeTicketItem getPageRange()
    {
        return pageRange;
    }

    public void setPageRange(PageRangeTicketItem pageRangeTicketItem) {
        this.pageRange = pageRangeTicketItem;
    }

    
}