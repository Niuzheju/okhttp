package com.niuzj.okhttp;

import okhttp3.*;
import okio.BufferedSink;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;

/**
 * okhttp使用
 * https://www.jianshu.com/p/da4a806e599b
 */
public class OkHttpTest {

    @After
    public void after() throws InterruptedException {
        Thread.sleep(2000L);
    }

    /**
     * get方式, 异步调用
     */
    @Test
    public void test01() {
        String url = "https://www.jianshu.com/p/da4a806e599b";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();
        Call call = client.newCall(request);
        call.enqueue(new InterCallback());
    }

    /**
     * get方式, 同步调用,会阻塞当前线程
     */
    @Test
    public void test02() throws IOException {
        String url = "https://www.jianshu.com/p/da4a806e599b";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();
        Call call = client.newCall(request);
        Response response = call.execute();
        System.out.println(response.body().string());
    }

    /**
     * post方式, 发送string参数
     */
    @Test
    public void test03() {
        //contentType
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        Request req = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(RequestBody.create(mediaType, "i am wini7"))
                .build();
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(req);
        call.enqueue(new InterCallback());
    }

    /**
     * post方式, 发送流式数据
     */
    @Test
    public void test04() {
        RequestBody requestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MediaType.parse("text/x-markdown; charset=utf-8");
            }

            @Override
            public void writeTo(BufferedSink bufferedSink) throws IOException {
                bufferedSink.writeUtf8("i am win7");
            }
        };

        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(requestBody)
                .build();
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        call.enqueue(new InterCallback());

    }

    class InterCallback implements Callback {

        public void onFailure(Call call, IOException e) {
            System.out.println(call.request().url().toString() + "link error");
            e.printStackTrace();
        }

        public void onResponse(Call call, Response response) throws IOException {
            if (response == null || response.body() == null)
                return;
            System.out.println(response.protocol() + " " + response.code() + " " + response.message());
            System.out.println("headers:");
            Headers headers = response.headers();
            for (int i = 0; i < headers.size(); i++) {
                System.out.println(headers.name(i) + ":" + headers.value(i));
            }
            System.out.println("响应数据：");
            System.out.println(new String(response.body().bytes()));
        }
    }
}
