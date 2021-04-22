package com.example.twitter;


import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    TextView textview1;
    WebView webView;
    StringBuffer tw_page;
    String tw_ps = "<html><head></head><body>get twitter page failed.<br>retry maybe ok.</body><html>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textview1 = (TextView) findViewById(R.id.id_refresh);

        //根据某位大佬的代码改的。。。我也没研究啥意思
        webView = (WebView) findViewById(R.id.id_tw);
        webView.getSettings().setJavaScriptEnabled(true);
//        webView.addJavascriptInterface(new MyJavaScriptInterface(),"HTMLOUT");
//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public void onPageFinished(WebView view,String url){
//                webView.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
//            }
//        });

        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.getSettings().setBlockNetworkImage(false);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        //  tw.setWebViewClient(new WebViewClient());
//        webView.loadUrl("https://space.bilibili.com/358003941/dynamic");

    }



    private class fetchtwd extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... url) {
            try {
                //用这个代理需要连接山大的vpn
//                Proxy px = new Proxy(java.net.Proxy.Type.HTTP,
//                        new InetSocketAddress("202.194.7.180", 19119));

                //可以用百度试一下能不能连上网
                // URL u = new URL("https://www.baidu.com/");
                //使用js
//                URL u = new URL("https://twitter.com/BTS_twt");
//                URL u = new URL("https://www.90lrc.cn/so.php?wd=%E4%B8%8B%E9%9B%A8%E4%BA%86");
                URL u = new URL("https://www.shicimingju.com/chaxun/zuozhe/28.html");
                HttpURLConnection conn = (HttpURLConnection) u.openConnection();

                //需要把这个注释掉才能用。。我也不知道为啥
                //该注释表明访问的是网页版
                conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");

                Log.i("tt", "ready to connect");
                conn.connect();
                Log.i("tt", "connection finished");

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Log.i("tt", "connection successed");
                    InputStream in = conn.getInputStream();
                    InputStreamReader r = new InputStreamReader(in);
                    BufferedReader buffer = new BufferedReader(r);
                    tw_page = new StringBuffer();
                    String line = null;
                    while ((line = buffer.readLine()) != null) {
                        //   System.out.println(line);
                        System.out.println("tw_page_length"+tw_page.toString().length());
                        Log.i("tt",line);
                        tw_page.append(line);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "return selected string"; // in tw_cont
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(String result) {

            //抄的，我找的tagA处的代码不对。。我也不知道为什么

            Log.i("tt", "getcontnet\n"+tw_page);
//            String tagA="<div data-v-8af6e2ec=\"\" class=\"content-ellipsis\">";
//            String tagB="</div>";
            String tagA="<div class=\"shici_content\">";
//            String tagA2="";
            String tagB="</";
            String tagB2="<a";
            String left = tw_page.toString();
            Log.i("tt",""+left.length());
            int tagai = -1;
            tagai = left.indexOf(tagA);
            System.out.println("i find tagert"+tagai);
            int len = left.length();
            if (len > 5000)
                tw_ps = "";
            while (tagai >= 0) {
                left = left.substring(tagai + tagA.length());
                int tagbi = left.indexOf(tagB);
                int tagb2=left.indexOf(tagB2);
                if(tagb2<tagbi){
                    tagbi=tagb2;
                }
                if (tagbi >= 0) {
                    String p1 = left.substring(0, tagbi);
                    Log.e("TAG",p1);
                    tw_ps += "<p>" + p1 + "<p>";
                }
//                Log.i("tt","this is tW_ps"+tw_ps);
                tagai = left.indexOf(tagA);
            }
            webView.loadData(tw_ps, "text/html", "utf-8");
        }
    }

    public void onClick_refresh(View view) {
            if (true) {
                String cont = "<html><head></head><body>fetching twitter of Trump ...</body><html>";
                webView.loadData(cont, "text/html", "utf-8");
            }
            new fetchtwd().execute("not-used-url");
        }


}

