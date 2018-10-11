package com.piyush.pictprint.CJT.cdd;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Color {
    public static final Type DEFAULT_TYPE = Type.STANDARD_MONOCHROME;
    @SerializedName("option")
    private List<Option> option = null;

    public static class Option {
        @SerializedName("custom_display_name")
        private String customDisplayName = null;
        @SerializedName("is_default")
        private boolean isDefault = false;
        @SerializedName("type")
        private String type = Color.DEFAULT_TYPE.name();
        @SerializedName("vendor_id")
        private String vendorId = null;

        public String getVendorId() {
            return this.vendorId;
        }

        public Type getType() {
            try {
                return Type.valueOf(this.type);
            } catch (Exception e) {
                return Color.DEFAULT_TYPE;
            }
        }

        public String getCustomDisplayName() {
            return this.customDisplayName;
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