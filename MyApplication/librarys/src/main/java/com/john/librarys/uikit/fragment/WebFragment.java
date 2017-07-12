package com.john.librarys.uikit.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.john.librarys.R;


public abstract class WebFragment extends BaseFragment {
    public static final String ARG_TITLE = "title";
    public static final String ARG_URL = "url";

    private String mBlankPageUrl = "about:blank";
    private String mErrorPageUrl = "file:///android_asset/none.html";

    protected WebView mWebView;
    protected ProgressBar mProgressBar;

    private String mTitle = "";

    private WebDelegate mWebDelegate;//代理

    private boolean mIsFixedTitlte = false;//是否固定标题

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_web, container, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.web_progressbar);

        mWebView = (WebView) view.findViewById(R.id.webview);
        initWebView();

        handleArgs(getArguments());
        return view;
    }


    public WebView getWebView() {
        return mWebView;
    }
    /**
     * webview的设置
     */
    private void initWebView() {
        mWebView.setWebChromeClient(mWebChromeClient);
        mWebView.setWebViewClient(mWebClient);

        mWebView.setVerticalScrollBarEnabled(false);//设置无垂直方向的scrollbar
        mWebView.setHorizontalScrollBarEnabled(false);//设置无水平方向的scrollbar
        WebSettings settings = mWebView.getSettings();
        settings.setSupportZoom(false); // 支持缩放
        settings.setBuiltInZoomControls(false); // 启用内置缩放装置
        settings.setJavaScriptEnabled(true); // 启用JS脚本
        // 设置WebView可触摸放大缩小
        settings.setBuiltInZoomControls(false);

        // 点击后退按钮,让WebView后退
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
                        mWebView.goBack();
                        return true;
                    }
                }
                return false;
            }
        });
    }


    WebViewClient mWebClient = new WebViewClient() {
        // 当点击链接时,希望覆盖而不是打开浏览器窗口
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //特殊处理
            if (url.startsWith("mailto:") || url.startsWith("geo:") || url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            } else {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (!isFixedTitle()) {
                setTopbarTitle(view.getTitle());
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (!isFixedTitle()) {
                setTopbarTitle(view.getTitle());
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description,
                                    String failingUrl) {
            //用javascript隐藏系统定义的404页面信息
            view.loadUrl(getErrorPageUrl());
        }
    };

    private WebChromeClient mWebChromeClient = new WebChromeClient() {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            mProgressBar.setProgress(newProgress);
            if (newProgress >= 100) {
                mProgressBar.setVisibility(View.GONE);
            } else {
                mProgressBar.setVisibility(View.VISIBLE);
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return onJsAlerting(view, url, message, result);
        }

    };

    protected abstract boolean onJsAlerting(WebView view, String url, String message, JsResult result);

    protected void handleArgs(Bundle args) {
        String url = null;
        if (null != args) {
            mTitle = args.getString(ARG_TITLE);
            url = args.getString(ARG_URL);
        }

        if (!isFixedTitle()) {
            setTopbarTitle(getTopbarTitle());
        }

        if (TextUtils.isEmpty(url)) {
            url = getBlankPageUrl();
        }

        mWebView.loadUrl(url);
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    public void onPause() {
        mWebView.onPause();
        super.onPause();
    }

    public void setBlankPageUrl(String blankPageUrl) {
        mBlankPageUrl = blankPageUrl;
    }

    public void setErrorPageUrl(String errorPageUrl) {
        mErrorPageUrl = errorPageUrl;
    }

    public String getBlankPageUrl() {
        return mBlankPageUrl;
    }

    public String getErrorPageUrl() {
        return mErrorPageUrl;
    }

    /**
     * web 代理
     * 用于处理各种时间回调
     */
    public static interface WebDelegate {
        void setTopbarTitle(String title);
    }

    public WebDelegate getWebDelegate() {
        return mWebDelegate;
    }

    /***
     * 设置 topbar 代理，
     * 用于 当 {@link #isFixedTitle()} == false; webview 自动加载完成后
     * 自动把网页的title 设置为网页title
     *
     * @param webDelegate
     */
    public void setWebDelegate(WebDelegate webDelegate) {
        mWebDelegate = webDelegate;
    }

    /**
     * 设置Topbar
     *
     * @param title
     */
    public void setTopbarTitle(String title) {
        mTitle = title;
        if (mWebDelegate != null) {
            mWebDelegate.setTopbarTitle(mTitle);
        }
    }

    public String getTopbarTitle() {
        return mTitle;
    }


    public boolean isFixedTitle() {
        return mIsFixedTitlte;
    }

    public void setIsFixedTitlte(boolean isFixedTitlte) {
        mIsFixedTitlte = isFixedTitlte;
    }
}
