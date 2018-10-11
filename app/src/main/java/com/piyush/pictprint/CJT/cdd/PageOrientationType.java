package com.piyush.pictprint.CJT.cdd;


public enum PageOrientationType {
    PORTRAIT(0),
    LANDSCAPE(1),
    AUTO(2);

    public static final int AUTO_VALUE = 2;
    public static final int LANDSCAPE_VALUE = 1;
    public static final int PORTRAIT_VALUE = 0;
    private final int value;


    public final int getNumber() {
        return this.value;
    }

    public static PageOrientationType forNumber(int i) {
        switch (i) {
            case 0:
                return PORTRAIT;
            case 1:
                return LANDSCAPE;
            case 2:
                return AUTO;
            default:
                return null;
        }
    }

    private PageOrientationType(int i) {
        this.value = i;
    }
}


