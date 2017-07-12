package com.john.librarys.net.core;

import com.android.volley.toolbox.HurlStack;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by LinYi.
 */
public class OkHttpStack extends HurlStack {
    private final OkUrlFactory okUrlFactory;
    public OkHttpStack() {
        this(new OkHttpClient());
    }
    public OkHttpStack(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            throw new NullPointerException("Client must not be null.");
        }
        this.okUrlFactory = new OkUrlFactory(okHttpClient);
    }
    @Override
    protected HttpURLConnection createConnection(URL url) throws IOException {
        return okUrlFactory.open(url);
    }
}
