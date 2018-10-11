package com.piyush.pictprint.CJT.cdd;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PageOrientation {
    public static final PageOrientationType DEFAULT_TYPE = PageOrientationType.AUTO;
    @SerializedName("option")
    private List<Option> option;

    public static class Option {
        @SerializedName("is_default")
        private boolean isDefault = false;
        @SerializedName("type")
        private String type = PageOrientation.DEFAULT_TYPE.name();

        public PageOrientationType getType() {
            try {
                return PageOrientationType.valueOf(this.type);
            } catch (Exception e) {
                return PageOrientation.DEFAULT_TYPE;
            }
        }

        public boolean isDefault() {
            return this.isDefault;
        }
    }

    public List<Option> getOption() {
        return this.option;
    }

    public Option getDefaultOption() {
        for (Option option : getOption()) {
            if (option.isDefault()) {
                return option;
            }
        }
        return null;
    }
}