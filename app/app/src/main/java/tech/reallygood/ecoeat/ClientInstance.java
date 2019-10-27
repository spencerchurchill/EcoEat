package tech.reallygood.ecoeat;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ClientInstance {
    private static final String BASE_URL = "https://reallygood.tech";


    public static JSONObject getData(String dir, String query, String args) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();
        client.newBuilder()
                .readTimeout(1000*30, TimeUnit.MILLISECONDS)
                .connectTimeout(1000*30, TimeUnit.MILLISECONDS)
                .build();

        HttpUrl.Builder urlBuilder
            = HttpUrl.parse(BASE_URL + '/' + dir).newBuilder();
            urlBuilder.addQueryParameter(query, args);

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Log.v("LOGS:", url);
        try (Response response = client.newCall(request).execute()) {
            String jsonData = response.body().string();
            return new JSONObject(jsonData);
        }
        catch (NullPointerException n) {
            Log.v("LOGS:", "FAIL TO GET DATA FROM SERVER");
            return null;
        }


    }

    public static String getGraph(String co2, String water) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();
        client.newBuilder()
                .readTimeout(1000*30, TimeUnit.MILLISECONDS)
                .connectTimeout(1000*30, TimeUnit.MILLISECONDS)
                .build();

        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(BASE_URL + "/graph").newBuilder();
        urlBuilder.addQueryParameter("water", water)
                .addQueryParameter("co2", co2);

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Log.v("LOGS:", url);
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
        catch (NullPointerException n) {
            Log.v("LOGS:", "FAIL TO GET GRAPH FROM SERVER");
            return null;
        }
    }


}

