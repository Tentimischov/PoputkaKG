package baktiyar.com.poputkakg.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

public class LocationsManager {

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        try {
            locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return locationMode != Settings.Secure.LOCATION_MODE_OFF;


    }

    public static void turnOnGPSLOcation(Activity activity){
        Intent gpsOptionsIntent = new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        activity.startActivity(gpsOptionsIntent);
    }

}
