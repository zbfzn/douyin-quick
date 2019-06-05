package top.lyfzn.music.douyinquick;


import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Douyin {
    private String user_name,video_id;
    private String real_url;

    public boolean isHas_long() {
        return has_long;
    }

    private boolean has_long=false;

    public String getLong_video() {
        return long_video;
    }

    private void setLong_video(String long_video) {
        this.long_video = long_video;
        if(!this.long_video.equals("")){
            this.has_long=true;
        }
    }

    private String long_video;
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
                String long_video;
                JSONArray arr_l=object.getJSONArray("long_video");
              for (int i=0;i<arr_l.size();i++){
                  JSONObject lv=arr_l.getJSONObject(i);
                  if(lv.getString("gear_name").equals("normal_720")){
                      setLong_video(lv.getJSONObject("play_addr").getJSONArray("url_list").getString(0));
                      break;
                  }
              }

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
