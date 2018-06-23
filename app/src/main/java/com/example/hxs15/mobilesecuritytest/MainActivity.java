package com.example.hxs15.mobilesecuritytest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    private WebView webView;
    private String JWBUrl="http://jwb.sysu.edu.cn/";
    private String JWXTUrl="http://uems.sysu.edu.cn/jwxt/";
    private String SYSUUrl="http://www.sysu.edu.cn/2012/cn/index.htm";
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWebView();
    }

    public void initWebView(){
        progressBar=findViewById(R.id.main_web_progressbar);
        webView=findViewById(R.id.main_web_view);
        webView.setWebViewClient(webViewClient);
        webView.loadUrl(JWBUrl);
    }

    public void changeWeb(View view){
        int id=view.getId();
        if(id==R.id.jwb_btn){
            webView.loadUrl(JWBUrl);
        }
        else if(id==R.id.jwxt_btn){
            webView.loadUrl(JWXTUrl);
        }
        else if(id==R.id.sysu_btn){
            webView.loadUrl(SYSUUrl);
        }
    }

    public void logout(View view){
        //退出不会清空用户数据，仅仅除去登陆状态
        sharedPreferences=getApplicationContext().getSharedPreferences("MyPreference",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("lastPswd",null);
        editor.apply();
        gotoLogin();
    }

    public void gotoLogin(){
        Intent intent =new Intent(this,SignInActivity.class);
        startActivity(intent);
        this.finish();
    }


    //WebViewClient主要帮助WebView处理各种通知、请求事件
    private WebViewClient webViewClient=new WebViewClient(){
        @Override
        public void onPageFinished(WebView view, String url) {//页面加载完成
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
/*            if(url.equals("http://www.google.com/")){
                Toast.makeText(MainActivity.this,"国内不能访问google,拦截该url",Toast.LENGTH_LONG).show();
                return true;//表示我已经处理过了
            }
            return super.shouldOverrideUrlLoading(view, url);*/
            webView.loadUrl(url);
            return true;
        }
    };


}
