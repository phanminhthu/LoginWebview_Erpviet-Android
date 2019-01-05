//package vn.izisolution.utils
//
//import android.annotation.TargetApi
//import android.content.Context
//import android.content.res.Configuration
//import android.content.res.Resources
//import android.os.Build
//
//import java.util.Locale
//
///**
// * Created by ToanNMDev on 3/29/2018.
// */
//
//object Language {
//
//    fun getSystemLocaleLegacy(config: Configuration): Locale {
//        return config.locale
//    }
//
//    @TargetApi(Build.VERSION_CODES.N)
//    fun getSystemLocale(config: Configuration): Locale {
//        return config.locales.get(0)
//    }
//
//    fun setSystemLocaleLegacy(config: Configuration, locale: Locale) {
//        config.locale = locale
//    }
//
//    @TargetApi(Build.VERSION_CODES.N)
//    fun setSystemLocale(config: Configuration, locale: Locale) {
//        config.setLocale(locale)
//    }
//
//    fun setLanguage(context: Context, languageCode: String) {
//        val locale = Locale(languageCode)
//        Locale.setDefault(locale)
//        val config = Configuration()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            setSystemLocale(config, locale)
//        } else {
//            setSystemLocaleLegacy(config, locale)
//        }
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
//            context.applicationContext.resources.updateConfiguration(config,
//                    context.resources.displayMetrics)
//    }
//
//}
