package top.lyfzn.music.douyinquick;


import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Douyin {
    private String user_name,video_id;
    private String real_url;
    private DYCallBack callBack;

    public Douyin( String share_url,  DYCallBack callBack){
        this.callBack=callBack;
        DYtask task=new DYtask();
        task.execute(share_url);

    }

    public String getUser_name() {
        return user_name;
    }

    public String getVideo_id() {
        return video_id;
    }

    public String getReal_url() {
        return real_url;
    }


    class DYtask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            return new HttpUtil().HttpRequest(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                JSONObject object= JSON.parseObject(s);
                String src_no=object.getJSONArray("urls").getString(0);
                String user=object.getString("nickname");
                String id=object.getString("awemeId");

                user_name=user;
                video_id=id;
                real_url=src_no;
            }catch (Exception e){
                callBack.HttpSuccessDo(Douyin.this,true);
                return;
            }
            callBack.HttpSuccessDo(Douyin.this,false);
        }
    }

    public interface DYCallBack {
        void HttpSuccessDo(Douyin douyin,boolean error);//获取抖音页面内容后做的事
    }
}
