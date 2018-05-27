package com.mree.inc.track.util;

import android.graphics.drawable.Drawable;

import com.mree.inc.track.TrackApp;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

/**
 * Created by MREE on 28.03.2018.
 */

public class IconUtils {

    public static Drawable getIcon(MaterialDrawableBuilder.IconValue icon) {
        return MaterialDrawableBuilder.with(TrackApp.getContext())
                .setIcon(icon) // provide an icon
                .build();
    }

    public static Drawable getIcon(MaterialDrawableBuilder.IconValue icon, int color) {
        return MaterialDrawableBuilder.with(TrackApp.getContext())
                .setIcon(icon) // provide an icon
                .setColor(color) // set the icon color
                .build();
    }

    public static Drawable getIcon(MaterialDrawableBuilder.IconValue icon, int color, int size) {
        return MaterialDrawableBuilder.with(TrackApp.getContext())
                .setIcon(icon) // provide an icon
                .setColor(color) // set the icon color
                .setSizeDp(size)
                .build();
    }

    public static Drawable getActionBarIcon(MaterialDrawableBuilder.IconValue icon, int
            color) {
        return MaterialDrawableBuilder.with(TrackApp.getContext())
                .setIcon(icon) // provide an icon
                .setColor(color) // set the icon color
                .setToActionbarSize()
                .build();
    }


}
