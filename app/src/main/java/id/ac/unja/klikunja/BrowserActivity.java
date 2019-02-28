package id.ac.unja.klikunja;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class BrowserActivity extends AppCompatActivity {
    Toolbar mToolbar;
    WebView webView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        mToolbar = findViewById(R.id.browser_toolbar);
        webView = findViewById(R.id.wv);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setIndeterminate(false);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String category = getIntent().getExtras().getString("category");

        if(category != null) {
            switch (category) {
                case "survey":
                    getSupportActionBar().setTitle("Survey");
                    webViewHander("https://angket.unja.ac.id");
                    break;

                case "repo":
                    getSupportActionBar().setTitle("Repository");
                    webViewHander("https://repository.unja.ac.id");
                    break;

                case "borang":
                    getSupportActionBar().setTitle("Borang");
                    webViewHander("https://borang.unja.ac.id");
                    break;

                case "simpeg":
                    getSupportActionBar().setTitle("SIMPEG");
                    webViewHander("https://simpeg.unja.ac.id");
                    break;

                case "dss":
                    getSupportActionBar().setTitle("DSS");
                    webViewHander("https://dss.unja.ac.id");
                    break;

                case "journal":
                    getSupportActionBar().setTitle("Journal");
                    webViewHander("https://online-journal.unja.ac.id");
                    break;
            }
        }

    }

    private void webViewHander(String url) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setFocusable(true);
        webView.setFocusableInTouchMode(true);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode((WebSettings.LOAD_NO_CACHE));
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setProgress(0);
            }

            public void onPageFinished(WebView view, String url) {
                progressBar.setProgress(0);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return true;
            }
        });

        webView.loadUrl(url);

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack())  {
            webView.goBack();
        } else {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
