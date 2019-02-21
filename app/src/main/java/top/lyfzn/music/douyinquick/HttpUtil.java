package top.lyfzn.music.douyinquick;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
private OkHttpClient okHttpClient=new OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build();
    public String HttpRequest(String request_url){



        //MediaType  设置Content-Type 标头中包含的媒体类型值


        Request request = new Request.Builder()
                .url(request_url)//请求的url
                .addHeader("Content-Type","text/html;charset=utf-8")
                .addHeader("User-Agent","Mozilla/5.0 (Linux; Android 8.0.0; MI 6 Build/OPR1.170623.027; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/62.0.3202.84 Mobile Safari/537.36")
                .get()
                .build();

        //创建/Call
        Call call = okHttpClient.newCall(request);
        // 异步操作
        StringBuilder re=new StringBuilder("空");
        try{
            re=new StringBuilder(call.execute().body().string());

        }catch (IOException e){
            e.printStackTrace();

        }
        if(re.toString().equals("空")){

            return "";
        }else{
            return re.toString();
        }



    }
}
