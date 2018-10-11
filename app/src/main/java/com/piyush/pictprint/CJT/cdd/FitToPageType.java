package com.piyush.pictprint.CJT.cdd;


public enum FitToPageType  {
    NO_FITTING(0),
    FIT_TO_PAGE(1),
    GROW_TO_PAGE(2),
    SHRINK_TO_PAGE(3),
    FILL_PAGE(4);

    public static final int FILL_PAGE_VALUE = 4;
    public static final int FIT_TO_PAGE_VALUE = 1;
    public static final int GROW_TO_PAGE_VALUE = 2;
    public static final int NO_FITTING_VALUE = 0;
    public static final int SHRINK_TO_PAGE_VALUE = 3;
    private final int value;


    public final int getNumber() {
        return this.value;
    }

    public static FitToPageType forNumber(int i) {
        switch (i) {
            case 0:
                return NO_FITTING;
            case 1:
                return FIT_TO_PAGE;
            case 2:
                return GROW_TO_PAGE;
            case 3:
                return SHRINK_TO_PAGE;
            case 4:
                return FILL_PAGE;
            default:
                return null;
        }
    }

    private FitToPageType(int i) {
        this.value = i;
    }
}


