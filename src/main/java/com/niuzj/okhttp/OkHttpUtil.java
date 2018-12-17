package com.niuzj.okhttp;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

public final class OkHttpUtil {

    public static String sendGet(String url) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.code() < 400) {
                ResponseBody body = response.body();
                if (body != null) {
                    return body.string();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
        String s = sendGet("http://www.baidu.comui");
        System.out.println(s);
    }
}
