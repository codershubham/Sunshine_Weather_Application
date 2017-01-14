package com.shubham.newweatherapp;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;

import data.CityPreferences;
import data.JSONWeatherParser;
import data.WeatherHttpClient;
import model.Weather;

public class MainActivity extends AppCompatActivity {

    private TextView cityName;
    private TextView temp;
    private ImageView iconView;
    private TextView description;
    private TextView humidity;
    private TextView pressure;
    private TextView wind;
    private TextView sunrise;
    private TextView sunset;
    private TextView updated;
    private TextView minTemp;
    private TextView maxTemp;
    private RelativeLayout layout;

    Weather weather = new Weather();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = (TextView)findViewById(R.id.cityText);
        temp = (TextView)findViewById(R.id.tempText);
        iconView = (ImageView) findViewById(R.id.thumbnailIcon);
        description = (TextView)findViewById(R.id.cloudText);
        humidity = (TextView)findViewById(R.id.humidityText);
        pressure =  (TextView)findViewById(R.id.pressureText);
        wind   = (TextView)findViewById(R.id.windText);
        sunrise = (TextView)findViewById(R.id.riseText);
        sunset = (TextView)findViewById(R.id.setText);
        updated = (TextView)findViewById(R.id.updateText);
        minTemp = (TextView)findViewById(R.id.minTemp);
        maxTemp = (TextView)findViewById(R.id.maxTemp);
        layout = (RelativeLayout) findViewById(R.id.rlayout);

        CityPreferences cityPreferences = new CityPreferences(MainActivity.this);

        renderWeatherData(cityPreferences.getCity());
    }

    public void renderWeatherData(String city){
         WeatherTask weatherTask = new WeatherTask();
        weatherTask.execute(new String[]{city+"&APPID=5f52955432b4ee17fd1216d056c96608"});
    }

    private class WeatherTask extends AsyncTask<String , Void , Weather>{

        @Override
        protected Weather doInBackground(String... strings) {

            String data = ((new WeatherHttpClient()).getWeatherData(strings[0]));
            Log.v("data ",data);
            weather = JSONWeatherParser.getWeather(data);
            Log.v("data ",weather.currentcondition.getDescription());
            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            DateFormat df= DateFormat.getTimeInstance();

            String sunriseDate = df.format(new Date(weather.place.getSunrise()));
            String sunsetDate = df.format(new Date(weather.place.getSunset()));
            String updateDate = df.format(new Date(weather.place.getLastUpdate()));

            DecimalFormat decimalFormat = new DecimalFormat("#.#");

            String tempFormat = decimalFormat.format(weather.currentcondition.getTemperature());
            String miniTemp = decimalFormat.format(weather.temperature.getMinTemp());
            String maxiTemp = decimalFormat.format(weather.temperature.getMaxTemp());

            cityName.setText(weather.place.getCity()+", "+weather.place.getCountry());
            temp.setText(tempFormat+" °C");
            humidity.setText("Humidity           : "+weather.currentcondition.getHumidity()+"%");
            pressure.setText("Pressure           : "+weather.currentcondition.getPressure()+" hPa");
            wind.setText(    "Wind Speed      : "+ weather.wind.getSpeed()+" mps "+weather.wind.getDeg()+"°");
            sunrise.setText( "Sunrise             : "+sunriseDate+"(UTC)");
            sunset.setText(  "Sunset              : "+sunsetDate+"(UTC)");
            updated.setText( "Last Updated        : "+updateDate+"(UTC)");
            minTemp.setText( "Low Temperature  : "+miniTemp+" °C");
            maxTemp.setText( "High Temperature : "+maxiTemp+" °C");

            String condition = weather.currentcondition.getCondition();

         description.setText("Condition      : "+condition+" ("+ weather.currentcondition.getDescription()+")");

            String desc = weather.currentcondition.getDescription();

            if(condition.equals("Rain")){
                layout.setBackgroundResource(R.drawable.rainybackground);
                if (desc.equals("shower rain"))
                    iconView.setImageResource(R.drawable.rainyd);
                else
                    iconView.setImageResource(R.drawable.rain);
            }
            else if (condition.equals("Clear")){
                layout.setBackgroundResource(R.drawable.clearbackground);
                iconView.setImageResource(R.drawable.clear);
            }
            else if (condition.equals("Thunderstorm")){
                layout.setBackgroundResource(R.drawable.thunderbackground);
                iconView.setImageResource(R.drawable.thunderstorm);
            }
            else if (condition.equals("Drizzle")){
                iconView.setImageResource(R.drawable.rain);
            }
            else if (condition.equals("Snow")){
                layout.setBackgroundResource(R.drawable.snowbackground);
                iconView.setImageResource(R.drawable.snow);
            }
            else if (condition.equals("Mist")||condition.equals("Smog")){
                layout.setBackgroundResource(R.drawable.mistbackground);
                iconView.setImageResource(R.drawable.mist);
            }
            else if (condition.equals("Smoke")){
                layout.setBackgroundResource(R.drawable.smokebackground);
                iconView.setImageResource(R.drawable.foggy);
            }
            else if (condition.equals("Fog")){
                layout.setBackgroundResource(R.drawable.fogbackground);
                iconView.setImageResource(R.drawable.foggy);
            }
            else if (condition.equals("Clouds")){
                if (desc.equals("few clouds"))
                 iconView.setImageResource(R.drawable.cloudy);
                else if (desc.equals("broken clouds"))
                iconView.setImageResource(R.drawable.bclouds);
                else
                iconView.setImageResource(R.drawable.clouds);
            }
            else {
                iconView.setImageResource(R.drawable.clouds);
            }

        }
    }

    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Change City");

        final EditText cityInput = new EditText(MainActivity.this);
        cityInput.setInputType(InputType.TYPE_CLASS_TEXT);
        cityInput.setHint("New_Delhi,IN");
        builder.setView(cityInput);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CityPreferences cityPreferences = new CityPreferences(MainActivity.this);
                cityPreferences.setCity(cityInput.getText().toString());
                String newCity = cityPreferences.getCity();
                renderWeatherData(newCity);
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.change_cityId ){
            showInputDialog();
        }
        return super.onOptionsItemSelected(item);
    }
}
