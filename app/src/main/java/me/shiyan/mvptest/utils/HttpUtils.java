package me.shiyan.mvptest.utils;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by shiyan on 2016/7/26.
 */
public class HttpUtils {

    private static OkHttpClient client = null ;

    private static void getInstance(){
        if (client == null)
            client = new OkHttpClient();
    }

    public static void getBooksClassify(Callback callback) throws IOException,JSONException{
        runByGet(AppConfig.API_URL, callback);
    }

    public static void getBooksDetail(int id, int page, int rows,Callback callback) throws IOException,JSONException{
        runByGet(String.format("%s?id=%d&page=%d&rows=%d"
                , AppConfig.API_URL_BOOKS
                , id, page, rows), callback);
    }

    //httpmethod : get
    private static void runByGet(String url, Callback callback) throws IOException {
        getInstance();
        Request request = new Request.Builder()
                .url(url)
                .header("apikey",AppConfig.API_KEY)
                .build();
        client.newCall(request).enqueue(callback);
    }
    //httpmethod : get image from internet

    public static void getBookImg(String subUrl, Callback callback){

        Request request = new Request.Builder()
                .url(AppConfig.BASE_API_URL_BOOK_IMG+subUrl+"_180x120")
                .build();
        client.newCall(request).enqueue(callback);
    }

}
