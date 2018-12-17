package com.niuzj.okhttp;

import okhttp3.*;
import okio.BufferedSink;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * okhttp使用
 * https://www.jianshu.com/p/da4a806e599b
 */
public class OkHttpTest {

    @After
    public void after() throws InterruptedException {
        Thread.sleep(3000L);
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

    /**
     * post方式提交文件
     */
    @Test
    public void test05(){
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(RequestBody.create(mediaType, new File("E:\\sim.txt")))
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new InterCallback());
    }

    /**
     * post方式提交表单
     */
    @Test
    public void test06(){
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("text/plain");
        FormBody formBody = new FormBody.Builder().add("name", "nzj").build();
        Request request = new Request.Builder().url("https://en.wikipedia.org/w/index.php").post(formBody).build();
        Call call = client.newCall(request);
        call.enqueue(new InterCallback());
    }

    @Test
    public void test07(){
        OkHttpClient client = new OkHttpClient();
        MultipartBody multipartBody = new MultipartBody.Builder("AaB03x")
                .setType(MultipartBody.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"title\""),
                        RequestBody.create(null, "Square Logo"))
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"image\""),
                        RequestBody.create(MediaType.parse("image/jpg"), new File("E:\\壁纸\\1.jpg")))
                .build();
        Request request = new Request.Builder()
                .addHeader("Authorization", "Client-ID...")
                .url("https://api.imgur.com/3/image")
                .post(multipartBody)
                .build();
        client.newCall(request).enqueue(new InterCallback());

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
