package com.kayheenjoyce.halp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

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

        // Horizontal transition between activities
        overridePendingTransition(R.anim.enter, R.anim.exit);

        // Set the webView, ensure that can extract the html code on the page.
        webview = (WebView)findViewById(R.id.webView);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");

        // This code allows us to check if sign in successful and retrieve the token
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String uri) {
                //authentication successful
                if (uri.contains("/api/login/login_result.ashx") && uri.contains("&r=0")) {

                    showPleaseWaitToast();
                    //collect the token on the webview body
                    webview.loadUrl("javascript:HTMLOUT.processHTML(document.documentElement.innerText);");
                }

            }

            private void showPleaseWaitToast() {
                Context context = getApplicationContext();
                String text = getString(R.string.web_view_toast);
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.BOTTOM, 0, 100);
                toast.show();
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

        // First page to load.
        webview.loadUrl("https://ivle.nus.edu.sg/api/login/?apikey=RZBCGrDddNHkXGG7Y9Q4Q");
    }

    /**
     * This class e will start the sendReg activity to process the html collected and closes this activity
     */
    private class MyJavaScriptInterface {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(String html) {
            Log.d("html", html);
            // process the html as needed by the app
            Intent sendReg = new Intent(WebViewActivity.this, SendingReg.class);
            sendReg.putExtra("token", html);
            startActivity(sendReg);
            finish();
        }

    }

}
