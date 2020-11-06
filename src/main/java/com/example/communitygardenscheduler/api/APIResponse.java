/**
 * APIResponse.java
 * Ravikumar Patel(B00840678)
 * This class contains methods for constructing the stringrequest and getting information received
 * by the application.
 */
package com.example.communitygardenscheduler.api;

import android.annotation.SuppressLint;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class APIResponse {
    final static private String key = "d2f6b57f3221fd50be633880bb970124";
    final static private String url_temp = "https://api.openweathermap.org/data/2.5/weather?q=%1$s&appid=%2$s&units=metric";
    static private JSONObject result;
    static private JSONObject weatherInfo;
    static private JSONObject weatherMain;

    /**
     * This method is used to construct the API request to receive the weather data.
     *
     * @param city -- a string representing the city of API request.
     * @return -- StringRequest object containing the actual API request.
     */
    public static StringRequest getStringRequest(String city){
        String url = String.format(url_temp,city,key);
        return new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @SuppressLint("Assert")
                    @Override
                    public void onResponse(String response) {
                        try {
                            // load JSON response, once received.
                            result = new JSONObject(response);
                            weatherMain = (JSONObject) result.get("main");

                            // make sure we got the correct data and not any error.
                            assert result.getInt("cod") == 200;

                            // extract weather information from all the information
                            JSONArray weather = result.getJSONArray("weather");
                            weatherInfo  = new JSONObject(weather.getString(0));

                            WeatherTaskScheduler.update();

                        } catch (JSONException e) {
                            //error handling
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error log.
            }
        });
    }

    /**
     * This method is used to extract main weather data
     *
     * @return -- String representing the main weather condition
     */
    static String getWeatherMain(){
        String returnVal = "";
        try {
            returnVal = weatherInfo.getString("main");
        }
        catch (JSONException e){
            //error handling
        }
        catch (Exception e){
            //error
        }
        return returnVal;
    }

    /**
     * This method is used to extract the temp from the weather data
     *
     * @return -- double representing the temperature about the weather condition
     */
    static double getTemperature(){
        double returnVal = Double.MAX_VALUE;
        try {
            returnVal = weatherMain.getDouble("temp");
        }
        catch (JSONException e){
            //error handling
        }
        catch (Exception e){
            //error
        }
        return returnVal;
    }

}

