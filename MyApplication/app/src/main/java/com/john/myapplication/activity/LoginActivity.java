package com.john.myapplication.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.dd.CircularProgressButton;
import com.john.librarys.uikit.activity.BaseActivity;
import com.john.librarys.utils.ContextManager;
import com.john.myapplication.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author John Gu
 * @date 2017/11/22.
 */

public class LoginActivity extends BaseActivity {

    @Bind(R.id.login)
    CircularProgressButton circularButton1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        circularButton1.setIndeterminateProgressMode(true);

        final CircularProgressButton circularButton2 = (CircularProgressButton) findViewById(R.id.circularButton2);
        circularButton2.setIndeterminateProgressMode(true);
        circularButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circularButton2.getProgress() == 0) {
                    circularButton2.setProgress(50);
                } else if (circularButton2.getProgress() == -1) {
                    circularButton2.setProgress(0);
                } else {
                    circularButton2.setProgress(-1);
                }
            }
        });
    }

    @OnClick(R.id.login)
    void onLogin(){
        circularButton1.setProgress(50);
        new Task().execute();
    }

    private class Task extends AsyncTask {
        @Override
        protected void onPostExecute(Object o) {
            circularButton1.setProgress(100);
            new Task2().execute();
            super.onPostExecute(o);
        }


        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private class Task2 extends AsyncTask {
        @Override
        protected void onPostExecute(Object o) {
            ContextManager.startActivity(mContext,MainActivity.class);
            super.onPostExecute(o);
        }


        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
