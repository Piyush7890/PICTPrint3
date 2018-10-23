package com.piyush.pictprint.model;

import android.net.Uri;

import com.piyush.pictprint.CJT.CloudJobTicket;

import org.parceler.Parcel;

@Parcel
public class Document {
     String name;
     double size;
     int price;
     long time;
     String contentType;
     int pages;
     String uri;
     String  cloudJobTicket;

    public int getCopies() {
        return copies;
    }

    int copies;

    public  Document()
     {}

    public Document(String name, double size, long time, String contentType) {
        this.name = name;
        this.size = size;
        this.price = price;
        this.time = time;
        this.contentType = contentType;
        pages=1;
        copies=1;
    }

    public void setCopies(int copies)
    {

        this.copies = copies;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getpages() {
        return pages;
    }

    public void setpages(int pages) {
        this.pages = pages;
    }

    public Uri getUri() {
        return Uri.parse(uri);
    }

    public void setUri(Uri uri) {
        this.uri = uri.toString();
    }

    public String  getCloudJobTicket() {
        return cloudJobTicket;
    }

    public void setCloudJobTicket(String cloudJobTicket) {
        this.cloudJobTicket = cloudJobTicket;
    }
}
