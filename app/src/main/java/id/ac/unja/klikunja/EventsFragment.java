package id.ac.unja.klikunja;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class EventsFragment extends Fragment {
    ProgressBar progressBar;
    LinearLayout wvLoading;
    Toolbar toolbar;
    View view;
    WebView webView;
    RelativeLayout errorLayout;
    ImageView errorImage;
    TextView errorTitle, errorMessage;
    Button btnRetry;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        view = getView();
        progressBar = view.findViewById(R.id.event_progressbar);
        toolbar = view.findViewById(R.id.events_toolbar);
        webView = view.findViewById(R.id.events_wv);
        errorLayout = view.findViewById(R.id.errorLayout);
        errorImage = view.findViewById(R.id.errorImage);
        errorTitle = view.findViewById(R.id.errorTitle);
        errorMessage = view.findViewById(R.id.errorMessage);
        btnRetry = view.findViewById(R.id.btnRetry);
        wvLoading = view.findViewById(R.id.wv_loading);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Events");

        init("https://www.unja.ac.id/events/hari-ini/");
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:{
                    webViewGoBack();
                }break;
            }
        }
    };

    private void webViewGoBack(){
        webView.goBack();
    }

    private void webViewHander(String url) {
        wvLoading.setVisibility(View.VISIBLE);
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
                super.onPageFinished(view, url);

                progressBar.setProgress(0);

                webView.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('top-bar')[0].style.display='none'; " +
                        "document.getElementsByClassName('container')[1].style.display='none'; " +
                        "document.getElementsByClassName('vertical-space2')[0].style.display='none'; " +
                        "document.getElementsByClassName('w-next-article')[0].style.display='none'; " +
                        "document.getElementsByClassName('w-prev-article')[0].style.display='none'; " +
                        "document.getElementsByClassName('sidebar')[0].style.display='none'; " +
                        "document.getElementsByClassName('rec-posts')[0].style.display='none'; " +
                        "document.getElementsByClassName('post-trait-w')[0].style.display='none'; " +
                        "document.getElementsByClassName('au-avatar-box')[0].style.display='none'; " +
                        "document.getElementsByClassName('postmetadata')[0].style.display='none'; " +
                        "})()");

                webView.loadUrl("javascript:(function() { " +
                        "document.getElementById('header').style.display='none'; " +
                        "})()");

                webView.loadUrl("javascript:(function() { " +
                        "document.getElementById('headline').style.display='none'; " +
                        "})()");

                webView.loadUrl("javascript:(function() { " +
                        "document.getElementById('pre-footer').style.display='none'; " +
                        "})()");

                webView.loadUrl("javascript:(function() { " +
                        "document.getElementById('footer').style.display='none'; " +
                        "})()");

                wvLoading.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                wvLoading.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
                view.loadUrl(url);
                return true;
            }
        });

        webView.setOnKeyListener(new View.OnKeyListener(){

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == MotionEvent.ACTION_UP
                        && webView.canGoBack()) {
                    handler.sendEmptyMessage(1);
                    return true;
                }

                return false;
            }

        });

        webView.loadUrl(url);

    }

    private void initWebView(String url){
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setVisibility(View.GONE);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                webView.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('top-bar')[0].style.display='none'; " +
                        "document.getElementsByClassName('container')[1].style.display='none'; " +
                        "document.getElementsByClassName('vertical-space2')[0].style.display='none'; " +
                        "document.getElementsByClassName('w-next-article')[0].style.display='none'; " +
                        "document.getElementsByClassName('w-prev-article')[0].style.display='none'; " +
                        "document.getElementsByClassName('sidebar')[0].style.display='none'; " +
                        "document.getElementsByClassName('rec-posts')[0].style.display='none'; " +
                        "document.getElementsByClassName('post-trait-w')[0].style.display='none'; " +
                        "document.getElementsByClassName('au-avatar-box')[0].style.display='none'; " +
                        "document.getElementsByClassName('postmetadata')[0].style.display='none'; " +
                        "document.getElementById('header').style.display='none'; " +
                        "document.getElementById('headline').style.display='none'; " +
                        "document.getElementById('pre-footer').style.display='none'; " +
                        "document.getElementById('footer').style.display='none'; " +
                        "})()");

                progressBar.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);

            }
        });

        webView.loadUrl(url);
    }

    private void showErrorMessage(int imageView, String title, String message, final String url) {
        if(errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
        }

        errorImage.setImageResource(imageView);
        errorTitle.setText(title);
        errorMessage.setText(message);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init(url);
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void init(String url) {
        if(isNetworkAvailable()) {

            if(errorLayout.getVisibility() == View.VISIBLE) {
                errorLayout.setVisibility(View.GONE);
            }

            webViewHander(url);
        }else{
            showErrorMessage(
                    R.drawable.oops,
                    "Network Error",
                    "Umm.. You need the Internet for this",
                    url);
        }
    }
}