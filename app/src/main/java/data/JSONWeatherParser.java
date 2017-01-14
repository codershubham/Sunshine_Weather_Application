package data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Util.Utils;
import model.Place;
import model.Weather;

/**
 * Created by topaz on 9/1/16.
 */
public class JSONWeatherParser {
    public static Weather getWeather(String data){
        Weather weather = new Weather();

        //create JSONObject from data

        try {
            JSONObject jsonObject = new JSONObject(data);

            Place place = new Place();

            // Get the coord object
            JSONObject coordObj = Utils.getObject("coord", jsonObject);
            place.setLat(Utils.getFloat("lat", coordObj));
            place.setLon(Utils.getFloat("lon", coordObj));

            // Get the sys object
            JSONObject sysObj = Utils.getObject("sys", jsonObject);
            place.setCountry(Utils.getString("country", sysObj));
            place.setSunrise(Utils.getInt("sunrise", sysObj));
            place.setSunset(Utils.getInt("sunset", sysObj));

            // Get other place info
            place.setCity(Utils.getString("name", jsonObject));
            place.setLastUpdate(Utils.getInt("dt", jsonObject));

            //set weather.place info
            weather.place = place;

            // Get weather object
            JSONArray jsonArray = jsonObject.getJSONArray("weather");
            JSONObject jsonWeather = jsonArray.getJSONObject(0);
            weather.currentcondition.setWeatherId(Utils.getInt("id", jsonWeather));
            weather.currentcondition.setDescription(Utils.getString("description", jsonWeather));
            Log.v("--Icon text", "setIcon Start");
            weather.currentcondition.setIcon(Utils.getString("icon", jsonWeather));
            Log.v("--Icon text", "setIcon End");
            Log.v("--Icon text", String.valueOf(weather.currentcondition.getIcon()));

            // Get Main object
            JSONObject mainObj = Utils.getObject("main", jsonObject);
            weather.currentcondition.setCondition(Utils.getString("main", jsonWeather));
            weather.temperature.setTemp(Utils.getDouble("temp", mainObj));
            weather.currentcondition.setTemperature(Utils.getDouble("temp",mainObj));
            weather.temperature.setMaxTemp(Utils.getFloat("temp_max", mainObj));
            weather.temperature.setMinTemp(Utils.getFloat("temp_min", mainObj));
            weather.currentcondition.setHumidity(Utils.getFloat("humidity", mainObj));
            weather.currentcondition.setPressure(Utils.getFloat("pressure", mainObj));

            //Get wind object
            JSONObject windObj = Utils.getObject("wind", jsonObject);
            weather.wind.setDeg(Utils.getFloat("deg", windObj));
            weather.wind.setSpeed(Utils.getFloat("speed", windObj));

            //Get cloud obj
            JSONObject cloudObj = Utils.getObject("clouds", jsonObject);
            weather.clouds.setPrecipitation(Utils.getInt("all", cloudObj));

            return weather;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

}