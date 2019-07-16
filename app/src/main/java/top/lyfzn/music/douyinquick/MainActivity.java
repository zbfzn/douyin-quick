package top.lyfzn.music.douyinquick;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Intent intent_service;
    private Button stop,about;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stop=findViewById(R.id.stop_finish);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(intent_service);
                Toast.makeText(MainActivity.this,"即将退出",Toast.LENGTH_LONG).show();
                MainActivity.this.finish();
                System.exit(0);
            }
        });

        about=findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab=new AlertDialog.Builder(MainActivity.this);
                ab.setTitle("关于")
                    .setMessage("作者：zbfzn\n" +
                            "GitHub:https://github.com/zbfzn\n"+
                            "项目地址：https://github.com/zbfzn/douyin-quick\n" +
                            "使用帮助：请赋予相关权限，视频下载在DouyinQuick文件夹下，使用用户昵称+视频id命名。")
                    .setCancelable(true);
                AlertDialog ad=ab.create();
                ad.show();
            }
        });
        /**
         * 权限检查、声明
         */
        List<String> permisions=new ArrayList<>();

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permisions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permisions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if(!permisions.isEmpty()){
            String []permisions_s=permisions.toArray(new String[permisions.size()]);
            ActivityCompat.requestPermissions(MainActivity.this,permisions_s,1);
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(MainActivity.this)) {
                    openDY();
                } else {
                    //若没有权限，提示获取.
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    Toast.makeText(MainActivity.this,"需要取得权限以使用悬浮窗",Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }

            }else {
                //SDK在23以下，不用管.
                openDY();
            }
        }

    }
    void openDY(){
        intent_service=new Intent();
        intent_service.setAction("com.douyinquick.com");
        intent_service.setPackage(getPackageName());;
        startService(intent_service);
        try {
            PackageManager packageManager = getPackageManager();
            Intent intent = new Intent();
            intent = packageManager.getLaunchIntentForPackage("com.ss.android.ugc.aweme");
            startActivity(intent);

        }catch (Exception e){
            Toast.makeText(MainActivity.this,"打开抖音出错！请允许打开抖音或者安装抖音app",Toast.LENGTH_SHORT).show();
            this.finish();
            System.exit(0);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(intent_service!=null) {
            stopService(intent_service);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 1:
                if(grantResults.length>0){
                    for(int result:grantResults){
                        if(result!= PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(MainActivity.this,"必须同意所有权限才能使用本软件",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    /////
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (Settings.canDrawOverlays(MainActivity.this)) {
                            openDY();
                        } else {
                            //若没有权限，提示获取.
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                            Toast.makeText(MainActivity.this,"需要取得权限以使用悬浮窗",Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();
                        }

                    }else {
                        //SDK在23以下，不用管.
                        openDY();
                    }
                }else{
                    Toast.makeText(MainActivity.this,"即将退出",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }
}
