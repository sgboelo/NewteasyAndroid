package com.SmartTech.teasyNew;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

/**
 * Hack from http://stackoverflow.com/questions/2711858/is-it-possible-to-set-font-for-entire-application to change the typeface
 * for the entire application
 */
public final class FontsOverride {

    public static void setDefaultFont(Context context,
                                      String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(),
                fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }

    protected static void replaceFont(String staticTypefaceFieldName,
                                      final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class
                    .getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Typeface getFontById(Context context, int id) {
        String fontAssetName = Fonts.getById(id).assetName;
        return Typeface.createFromAsset(context.getAssets(), fontAssetName);
    }

    public enum Fonts {

        AvenirBook                  (0, "fonts/Avenir-Book.ttf"),
        Muli                        (1, "fonts/Muli.ttf"),
        MuliLight                   (2, "fonts/Muli-Light.ttf"),
        RobotoLight                 (3, "fonts/Muli-Light.ttf"),
        RobotoMedium                (4, "fonts/Roboto-Medium.ttf"),
        RobotoRegular               (5, "fonts/Roboto-Regular.ttf"),
        MiliBoldItalic              (6, "fonts/Muli-BoldItalic.ttf"),
        AvenirNextMedium            (7, "fonts/AvenirNext-Medium.ttf"),
        RobotoBold                  (8, "fonts/Roboto-Bold.ttf"),
        AvenirBookBlack             (9, "fonts/Avenir-Book-Black.ttf"),
        MyriadProRegular            (10, "fonts/Myriad-Pro-Regular.ttf"),
        OpenSansLight               (11, "fonts/OpenSans-Light.ttf"),
        OpenSansSemibold            (12, "fonts/OpenSans-Semibold.ttf"),
        OpenSansRegular             (13, "fonts/OpenSans-Regular.ttf"),
        OswaldExtraLightItalic      (14, "fonts/Oswald-Extra-LightItalic.ttf"),
        OpenSansItalic              (15, "fonts/OpenSans-Italic.ttf"),
        RalewaySemiBold             (16, "fonts/Raleway-SemiBold.ttf"),
        RalewayBold                 (17, "fonts/Raleway-Bold.ttf"),
        OpenSansBold                (18, "fonts/OpenSans-Bold.ttf");

        private String assetName;
        private int id;

        Fonts(int id, String assetName) {
            this.assetName = assetName;
            this.id = id;
        }

        private static Fonts getById(int id) {
            for(Fonts font : values()) {
                if(font.id == id)
                    return font;
            }

            throw new IllegalArgumentException("Font with id " + id + " does not exists");
        }
    }
}