package com.john.myapplication.activity;


import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.john.librarys.net.interf.ServiceTask;
import com.john.librarys.uikit.activity.BaseActivity;
import com.john.myapplication.Bean.User;
import com.john.myapplication.R;
import com.john.myapplication.adapter.DatabindingRecycleViewAdapter;
import com.john.myapplication.databinding.ActivityDatabindingRecyclerBinding;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by guqh on 2017/8/2.
 */

public class DatabindingRecyclerActivity extends BaseActivity {
    @Bind(R.id.recycleview)
    RecyclerView recycleview;

    ActivityDatabindingRecyclerBinding mDatabinding;
    List<String> mList=new ArrayList<>();

    String url="http://wx.zshisong.com:8085/imgServer/upload/pic/02f3b3322a844014923453e95806f8a2.jpg";
    private DatabindingRecycleViewAdapter mAdapter;

    @Override
    protected void setBindingContentView() {
        mDatabinding= DataBindingUtil.setContentView(this,R.layout.activity_databinding_recycler);
        ButterKnife.bind(this);

        User mUser=new User();
        mUser.setName("这是名字");
        mUser.setPhoto(url);
        mDatabinding.setUser(mUser);


        mAdapter=new DatabindingRecycleViewAdapter(mContext);
//        SimpleRecucleAdapter mAdapter=new SimpleRecucleAdapter(mContext);
        recycleview.setAdapter(mAdapter);

        showLoadingDialog();
        new Task().execute();
    }

    private class Task extends AsyncTask {

        @Override
        protected void onPostExecute(Object o) {
            dismissLoadingDialog();
            mAdapter.notifyDataSetChanged(mList);
            super.onPostExecute(o);
        }


        @Override
        protected Object doInBackground(Object[] objects) {
            mList.add(url);
            mList.add(url);
            mList.add(url);
            mList.add(url);
            mList.add(url);
            mList.add(url);
            mList.add(url);
            mList.add(url);
            mList.add(url);
            mList.add(url);
            mList.add(url);
            mList.add(url);
            mList.add(url);
            mList.add(url);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
