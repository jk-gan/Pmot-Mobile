package com.example.jkgan.pmot.Http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by JKGan on 28/11/2015.
 */
public class HttpRequest {

    public JSONObject makeHttpRequest(String url, String method,
                                      List<NameValuePair> params) {

        InputStream is = null;
        String json = "";
        JSONObject jObj = null;

        // Making HTTP request
        try {

            // check for request method
            if (method.equals("POST")) {
                // request method is POST
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    is = httpEntity.getContent();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }




            } else if (method.equals("GET")) {
                // request method is GET
                HttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                url = URLDecoder.decode(url);
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            //Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            // Log.e("JSON Parser", "Error parsing data " + e.toString());
            try{

                // TODO Auto-generated method stub
                JSONArray jsnArr = new JSONArray(json);
//						jobj = jsnArr.getJSONObject(1);
                jObj = new JSONObject();
                jObj.put("Shops", jsnArr);

            } catch (JSONException e1){
                e.printStackTrace();
            }
        }

        // return JSON String
        return jObj;

    }
}
