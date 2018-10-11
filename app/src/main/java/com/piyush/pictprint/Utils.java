package com.piyush.pictprint;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;
import android.util.DisplayMetrics;
import android.view.View;

public class Utils {

    public static final int IMAGE_PRICE=2;
    public static final int SINGLE_PAGE_PRICE=1;

    public static float convertDpToPixel(float dp, Context context) {
        return dp * ((float) context.getResources()
                .getDisplayMetrics()
                .densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }


    public static Drawable getIcon(String type, Context context)
    {
        switch (type)
        {
            case "image/png":
            case "image/jpeg":
            case "image/jpg":
                return context.getDrawable(R.drawable.ic_png);
            case "application/pdf":
                return context.getDrawable(R.drawable.ic_pdf);
                default:
                    return context.getDrawable(R.drawable.ic_doc);
        }
    }

    public static String queryName(ContentResolver resolver, Uri uri) {
        Cursor returnCursor =
                resolver.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }

    public static void setLightStatusBar(View view, Activity context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            context.getWindow().setStatusBarColor(Color.parseColor("#F0F0F0"));
        }
    }

    public static void setDefaultStatusBar(View view, Activity context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int flags = view.getSystemUiVisibility();
            flags = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            context.getWindow().setStatusBarColor(Color.parseColor("#F0F0F0"));
        }
    }
}
