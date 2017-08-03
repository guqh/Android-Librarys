package com.john.myapplication.activity;


import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.john.librarys.uikit.activity.BaseActivity;
import com.john.librarys.uikit.widget.WrapContentLinearLayoutManager;
import com.john.myapplication.Bean.User;
import com.john.myapplication.R;
import com.john.myapplication.adapter.DatabindingRecyclerAdapter;
import com.john.myapplication.adapter.SimpleRecucleAdapter;
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

    String url="http://wx.zshisong.com:8085/imgServer/upload/pic/02f3b3322a844014923453e95806f8a2.jpg";
    @Override
    protected void setBindingContentView() {
        mDatabinding= DataBindingUtil.setContentView(this,R.layout.activity_databinding_recycler);
        ButterKnife.bind(this);

        User mUser=new User();
        mUser.setName("这是名字");
        mUser.setPhoto(url);
        mDatabinding.setUser(mUser);


//        recycleview.setLayoutManager(new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        DatabindingRecyclerAdapter mAdapter=new DatabindingRecyclerAdapter(mContext);
//        SimpleRecucleAdapter mAdapter=new SimpleRecucleAdapter(mContext);
        recycleview.setAdapter(mAdapter);


        //==================模拟数据==========================//
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


        mAdapter.clear();
        mAdapter.addItems(mList);
        mAdapter.notifyDataSetChanged();
    }
}
