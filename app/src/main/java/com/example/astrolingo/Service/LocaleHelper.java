package com.example.astrolingo.Service;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import java.util.Locale;

public class LocaleHelper {
    public static void toggleLanguage(Context context) {
        // Lấy ngôn ngữ hiện tại
        String currentLanguage = Locale.getDefault().getLanguage();

        // Kiểm tra nếu ngôn ngữ hiện tại là tiếng Việt thì chuyển sang tiếng Anh, ngược lại
        if ("vi".equals(currentLanguage)) {
            setLocale(context, "en");  // Chuyển sang tiếng Anh
        } else {
            setLocale(context, "vi");  // Chuyển sang tiếng Việt
        }
    }

    public static void setLocale(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }

        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
}