package top.lyfzn.music.douyinquick;


import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Douyin {
    private String user_name,video_id;
    private String real_url,abstract_url;
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

    public String getAbstract_url() {
        return abstract_url;
    }

    class DYtask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            return new HttpUtil().HttpRequest(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            Document doc= Jsoup.parse(s);
            Element e1= doc.select("#theVideo").get(0);
            Attributes attrs =e1.attributes();
            String src=attrs.get("src");
            String src_no=src.replace("playwm","play");
            String user=doc.select("#videoUser > div.user-info > p.user-info-name").get(0).text();
            String id=src.split("video_id=|&line=0")[1];

            user_name=user;
            video_id=id;
            real_url=src_no;
            abstract_url=src;

            callBack.HttpSuccessDo(Douyin.this);
        }
    }

    public interface DYCallBack {
        void HttpSuccessDo(Douyin douyin);//获取抖音页面内容后做的事
    }
}
