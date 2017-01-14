package data;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by shubham on 14-10-2016.
 */
public class CityPreferences {
    SharedPreferences prefs;

    public CityPreferences(Activity activity){
        prefs = activity.getPreferences(Activity.MODE_PRIVATE);

    }

    public String getCity(){
        return prefs.getString("city","Delhi,IN");
    }

    public void setCity(String city){
        prefs.edit().putString("city",city).commit();
    }
}
