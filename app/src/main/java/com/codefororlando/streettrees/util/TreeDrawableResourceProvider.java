package com.codefororlando.streettrees.util;

import android.support.annotation.DrawableRes;
import android.util.Log;

import com.codefororlando.streettrees.R;

import javax.inject.Inject;

public class TreeDrawableResourceProvider {
    private static final String TAG = "TreeDrawableResourcePro";

    public
    @Inject
    TreeDrawableResourceProvider() {
    }

    public
    @DrawableRes
    int getDrawable(final String treeName) {
        switch (treeName) {
            case "Live Oak":
                return R.drawable.cfo_live_oak;
            case "Nuttall Oak":
                return R.drawable.cfo_nuttal_oak;
            case "Southern Magnolia":
                return R.drawable.cfo_magnolia;
            case "Winged Elm":
                return R.drawable.cfo_elm;
            case "Crape Myrtle":
                return R.drawable.cfo_myrtle;
            case "Eagleston Holly":
                return R.drawable.cfo_eagleston_holly;
            case "Chinese Pistache":
                return R.drawable.cfo_chinese_pistache;
            case "Dahoon Holly":
                return R.drawable.cfo_dahoon_holly;
            case "Tuliptree":
                return R.drawable.cfo_tulip_popular;
            case "Tabebuia Ipe":
            default:
                Log.w(TAG, String.format("getDrawable: no drawable set for tree [%s]", treeName));
                //TODO Use a question mark or no picture found image.
                return R.drawable.cfo_yellow_trumpet;
        }
    }
}
