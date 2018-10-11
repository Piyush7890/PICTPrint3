package com.piyush.pictprint.CJT.cdd;

public enum Type {
    STANDARD_COLOR(0),
    STANDARD_MONOCHROME(1),
    CUSTOM_COLOR(2),
    CUSTOM_MONOCHROME(3),
    AUTO(4);

    public static final int AUTO_VALUE = 4;
    public static final int CUSTOM_COLOR_VALUE = 2;
    public static final int CUSTOM_MONOCHROME_VALUE = 3;
    public static final int STANDARD_COLOR_VALUE = 0;
    public static final int STANDARD_MONOCHROME_VALUE = 1;
    private final int value;


    public final int getNumber() {
        return this.value;
    }

    public static Type forNumber(int i) {
        switch (i) {
            case 0:
                return STANDARD_COLOR;
            case 1:
                return STANDARD_MONOCHROME;
            case 2:
                return CUSTOM_COLOR;
            case 3:
                return CUSTOM_MONOCHROME;
            case 4:
                return AUTO;
            default:
                return null;
        }
    }

    private Type(int i) {
        this.value = i;
    }
}
