package com.john.myapplication.activity;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.AsyncTask;
import android.widget.ListView;

import com.john.librarys.uikit.activity.BaseActivity;
import com.john.librarys.uikit.waveswiperefreshlayout.WaveSwipeRefreshLayout;
import com.john.myapplication.R;
import com.john.myapplication.adapter.DataBindingListViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by guqh on 2017/8/3.
 */

public class DatabindingListViewActivity extends BaseActivity implements WaveSwipeRefreshLayout.OnRefreshListener {
    String url="http://wx.zshisong.com:8085/imgServer/upload/pic/02f3b3322a844014923453e95806f8a2.jpg";

    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.waveswipe)
    WaveSwipeRefreshLayout waveswipe;
    private ViewDataBinding binding;
    private DataBindingListViewAdapter adapter;
    private ArrayList<String> mList=new ArrayList<>();

    @Override
    protected void setBindingContentView() {
        super.setBindingContentView();
        binding=DataBindingUtil.setContentView(this, R.layout.activity_listview_databinding);
        ButterKnife.bind(this);

        adapter = new DataBindingListViewAdapter(mContext);
        listview.setAdapter(adapter);


        waveswipe.setOnRefreshListener(this);

        loadData();

    }

    private void loadData() {

        new Task().execute();
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    private class Task extends AsyncTask {

        @Override
        protected void onPostExecute(Object o) {
            // Call setRefreshing(false) when the list has been refreshed.
            waveswipe.setRefreshing(false);
            adapter.notifyDataSetChanged(mList);
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
