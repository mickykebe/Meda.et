package org.telegram.Meda;

import android.content.pm.PackageInfo;
import android.util.Log;

import org.telegram.messenger.ApplicationLoader;

/**
 * Created by Micky on 12/7/2015.
 */
public class Utility {
    public static final String LOG_TAG = Utility.class.getSimpleName();

    public static String getInstalledAppVersion(){
        try {
            PackageInfo pInfo = ApplicationLoader.applicationContext.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
            return pInfo.versionName + "(" + pInfo.versionCode + ")";
        }
        catch (Exception e){
            Log.e(LOG_TAG, "Unable to retrieve installed app version");
        }
        return "App Version Unknown";
    }
}
