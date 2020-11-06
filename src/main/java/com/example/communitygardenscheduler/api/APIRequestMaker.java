/**
 * APIRequestMaker.java
 * Ravikumar Patel(B00840678)
 * This class contains the code to make a request to API.
 */
package com.example.communitygardenscheduler.api;

import android.annotation.SuppressLint;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

class APIRequestMaker {
    @SuppressLint("StaticFieldLeak")
    private static APIRequestMaker requestMaker;
    private RequestQueue requestQueue;
    private Context context;

    /**
     * The constructor for the APIRequestMaker, set-ups the RequestQueue object within the class
     *
     * @param context -- calling objects handling entity
     */
    private APIRequestMaker(Context context){
        this.context = context;
        requestQueue = getRequestQueue();
    }

    /**
     * This method is used to add given request to the queue to perform
     *
     * @param context -- calling objects handling entity
     * @return APIReuestMaker -- it returns the static object of APIRequestMaker class
     */
    public static synchronized APIRequestMaker getInstance(Context context){
        if(requestMaker == null){
            requestMaker = new APIRequestMaker(context);
        }
        return requestMaker;
    }

    /**
     * This method is used to initialize requestQueue if not already initialized and get the
     * requestQueue object
     *
     * @return  RequestQueue -- it returns the requestqueue object
     */
    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    /**
     * This method is used to add given request to the queue to perform
     *
     * @param sr -- a StringRequest representing the actual API request
     */
    void addQueue(StringRequest sr){
        requestQueue.add(sr);
    }
}
