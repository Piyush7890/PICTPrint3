package com.piyush.pictprint;

public class Document {
    private String name;
    private double size;
    private int price;
    private long time;
    private String contentType;
    private int pages;

    public Document(String name, double size, long time, String contentType) {
        this.name = name;
        this.size = size;
        this.price = price;
        this.time = time;
        this.contentType = contentType;
        pages=1;
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
}
