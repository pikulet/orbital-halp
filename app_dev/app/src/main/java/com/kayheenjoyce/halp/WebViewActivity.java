package com.kayheenjoyce.halp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@SuppressWarnings("deprecated")
@SuppressLint("SetJavaScriptEnabled")
public class WebViewActivity extends AppCompatActivity {

    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webview = (WebView)findViewById(R.id.webView);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");


        webview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String uri) {
                //authentication successful
                if (uri.contains("/api/login/login_result.ashx") && uri.contains("&r=0")) {
                    //collect the token on the webview body
                    webview.loadUrl("javascript:HTMLOUT.processHTML(document.documentElement.innerText);");
                   // new GetUrlContentTask().execute(uri);
                    finish();
                    // to start the next activity
                    Intent regComplete = new Intent(WebViewActivity.this, WaitingClinic.class);
                    startActivity(regComplete);
                }

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String uri){
                if(uri.contains("/api/login/login_result.ashx") && uri.contains("&r=0")) {
                    onPageFinished(view, uri);
                    return false;
                } else {
                    view.loadUrl(uri);
                    return true;
                }
            }
        });

        webview.loadUrl("https://ivle.nus.edu.sg/api/login/?apikey=RZBCGrDddNHkXGG7Y9Q4Q");
    }

    private class MyJavaScriptInterface {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(String html) {
            Log.d("html", html);
            // process the html as needed by the app
        }
    }

    public static void printResult(String x) {
        Log.d("result", x);
    }


    private class GetUrlContentTask extends AsyncTask<String, Integer, String> {

        protected String doInBackground(String... urls) {
            String content = "", line;
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoOutput(true);
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.connect();

                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = rd.readLine()) != null) {
                    content += line + "\n";
                }
            } catch(Exception e) {
                Log.d("content", "exception occured");

            }

            Log.d("content", content);
            return content;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(String result) {
            // this is executed on the main thread after the process is over
            // update your UI here
            WebViewActivity.printResult(result);
        }

    }

}
