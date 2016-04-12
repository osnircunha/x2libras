package com.ocunha.librasapp.utils;

import android.graphics.Bitmap;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import cz.msebera.android.httpclient.Header;

/**
 * Created by osnircunha on 4/10/16.
 */
public class HttpRequests {
    private static final String KEY_IMAGE = "images_file";

    private static HttpRequests INSTANCE = new HttpRequests();

    private HttpRequests(){}

    public static HttpRequests getIntance(){
        return INSTANCE;
    }



    public void translateImage(String url, Bitmap bitmap, final ResponseHandler responseHandler){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
        byte[] data = bos.toByteArray();

        RequestParams requestParams = new RequestParams();
        requestParams.put("images_file", new ByteArrayInputStream(data), "image.jpg", "image/jpeg");

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60000);
        client.setEnableRedirects(true);

    }

    private static AsyncHttpResponseHandler createDefaultResponseHandler(final ResponseHandler responseHandler){
       return new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                responseHandler.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                responseHandler.onSuccess(new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                responseHandler.onError(responseBody != null ? new String(responseBody) : null, error);
            }

        };
    }

    public interface ResponseHandler {
        void onStart();

        void onSuccess(String response);

        void onError(String response, Throwable error);
    }

}