package com.john.myapplication.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.john.librarys.uikit.activity.BaseActivity;
import com.john.myapplication.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author John Gu
 * @date 2017/11/23.
 */

public class WebViewActivity extends BaseActivity {

    private static final String mUrl="http://720yun.com/t/0a32acsvqyi?pano_id=1331709"; //你的地址
//    private static final String mUrl="http://xkjf2.xkjfxxw.com:8091/cotrunSpace/cotrunSpaceWebPage.do?proId=87&spaceUrl=http://720yun.com/t/739j5rkOtv6"; //你的地址

    @Bind(R.id.mWebView)
    WebView mWebView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);

        //如果访问的页面中有Javascript，则webview必须设置支持Javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setUserAgentString(mWebView.getSettings().getUserAgentString());
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        mWebView.setWebChromeClient(new mWebChromeClient());

        mWebView.loadUrl(mUrl);

        showLoadingDialog();
    }

    /***
     * 加载进度监听
     */
    private class mWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {

            if (newProgress == 100) {
                dismissLoadingDialog();
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            ((ViewGroup)mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
        }
        super.onDestroy();
    }

}
