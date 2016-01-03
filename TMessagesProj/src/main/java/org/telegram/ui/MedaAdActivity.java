package org.telegram.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import org.telegram.messenger.R;

public class MedaAdActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meda_ad_activity);

        Intent intent = getIntent();
        if(intent == null){
            return;
        }

        String adUrl = intent.getStringExtra(Intent.EXTRA_TEXT);
        if(adUrl == null){
            return;
        }

        Button closeBtn = (Button)findViewById(R.id.btn_ad_close);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.meda_ad_progressbar);

        WebView adWebView = (WebView) findViewById(R.id.meda_ad_webview);
        adWebView.getSettings();
        adWebView.setBackgroundColor(0xffff822a);

        adWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });

        adWebView.loadUrl(adUrl);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
    }
}
