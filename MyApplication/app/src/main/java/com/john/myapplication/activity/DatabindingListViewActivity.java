package com.john.myapplication.activity;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.widget.ListView;

import com.john.librarys.uikit.activity.BaseActivity;
import com.john.myapplication.R;
import com.john.myapplication.adapter.DataBindingListViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by guqh on 2017/8/3.
 */

public class DatabindingListViewActivity extends BaseActivity {
    String url="http://wx.zshisong.com:8085/imgServer/upload/pic/02f3b3322a844014923453e95806f8a2.jpg";

    @Bind(R.id.listview)
    ListView listview;
    private ViewDataBinding binding;

    @Override
    protected void setBindingContentView() {
        super.setBindingContentView();
        binding=DataBindingUtil.setContentView(this, R.layout.activity_listview_databinding);
        ButterKnife.bind(this);

        DataBindingListViewAdapter adapter = new DataBindingListViewAdapter(mContext);
        listview.setAdapter(adapter);

        List<String> mList=new ArrayList<>();
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
        //==================模拟数据==========================//
        adapter.notifyDataSetChanged(mList);

    }
}
