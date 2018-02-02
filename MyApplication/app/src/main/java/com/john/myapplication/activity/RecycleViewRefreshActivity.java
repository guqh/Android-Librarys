package com.john.myapplication.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.john.librarys.uikit.activity.BaseActivity;
import com.john.librarys.uikit.widget.Divider;
import com.john.myapplication.Bean.RefreshModel;
import com.john.myapplication.R;
import com.john.myapplication.adapter.RecyclerRefreshViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder;

/**
 * @author John Gu
 * @date 2017/11/24.
 *
 * 下拉刷新 和s上拉加载
 *
 * */

public class RecycleViewRefreshActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate, BGAOnRVItemClickListener, BGAOnItemChildClickListener {

    @Bind(R.id.rl_recyclerview_refresh)
    BGARefreshLayout mRefreshLayout;
    @Bind(R.id.rv_recyclerview_data)
    RecyclerView mDataRv;
    private RecyclerRefreshViewAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_refresh);
        ButterKnife.bind(this);


        mRefreshLayout.setDelegate(this);
        mAdapter = new RecyclerRefreshViewAdapter(mDataRv);
        mAdapter.setOnRVItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);

        //头部轮播图
        View headerView = View.inflate(mContext, R.layout.view_custom_header, null);
        BGABanner banner=headerView.findViewById(R.id.banner);
        mRefreshLayout.setCustomHeaderView(headerView,true);
        List<String>list=new ArrayList<>();
        list.add("http://7xk9dj.com1.z0.glb.clouddn.com/banner/imgs/7.png");
        list.add("http://7xk9dj.com1.z0.glb.clouddn.com/banner/imgs/8.png");
        list.add("http://7xk9dj.com1.z0.glb.clouddn.com/banner/imgs/9.png");
        list.add("http://7xk9dj.com1.z0.glb.clouddn.com/banner/imgs/10.png");
        list.add("http://7xk9dj.com1.z0.glb.clouddn.com/banner/imgs/11.png");
        banner.setData(list,null);
        banner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                Glide.with(itemView.getContext())
                        .load(model)
                        .placeholder(R.mipmap.holder)
                        .error(R.mipmap.holder)
                        .dontAnimate()
                        .centerCrop()
                        .into(itemView);
            }
        });
        banner.setDelegate(new BGABanner.Delegate<ImageView, String>() {
            @Override
            public void onBannerItemClick(BGABanner banner, ImageView imageView, String model, int position) {
                Toast.makeText(banner.getContext(), "点击了第" + (position + 1) + "页", Toast.LENGTH_SHORT).show();
            }
        });


//        BGAStickinessRefreshViewHolder stickinessRefreshViewHolder = new BGAStickinessRefreshViewHolder(mContext, true);
//        stickinessRefreshViewHolder.setStickinessColor(R.color.colorPrimary);
//        stickinessRefreshViewHolder.setRotateImage(R.mipmap.bga_refresh_stickiness);
//        mRefreshLayout.setRefreshViewHolder(stickinessRefreshViewHolder);

        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(mContext, true);
        moocStyleRefreshViewHolder.setOriginalImage(R.mipmap.ic_launcher);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.colorPrimary);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

//        mDataRv.setLayoutManager(new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false));
//        mDataRv.setLayoutManager(new LinearLayoutManager(mApp, LinearLayoutManager.VERTICAL, false));

        mDataRv.addItemDecoration(new Divider(mContext));
        mDataRv.setLayoutManager(new LinearLayoutManager(mContext));

        mDataRv.setAdapter(mAdapter.getHeaderAndFooterAdapter());

        onLazyLoadOnce();

    }

    private void onLazyLoadOnce() {
        List<RefreshModel>list=new ArrayList<>();
        for (int i=0;i<10;i++){
            RefreshModel model=new RefreshModel("item"+i,"info");
            list.add(model);
        }
        mAdapter.setData(list);
    }

    /**
     * 下拉刷新
     * @param refreshLayout
     */
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        showLoadingDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showLoadingDialog("马上就好了！！");
            }
        },1000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<RefreshModel>list=new ArrayList<>();
                for (int i=0;i<10;i++){
                    RefreshModel model=new RefreshModel("item"+i,"info");
                    list.add(model);
                }

                dismissLoadingDialog();
                mRefreshLayout.endRefreshing();

                mAdapter.addNewData(list);
                mDataRv.scrollToPosition(0);
            }
        }, 3000);
    }

    /**
     * 上拉加载
     * @param refreshLayout
     * @return
     */
    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        showLoadingDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<RefreshModel>list=new ArrayList<>();
                for (int i=0;i<7;i++){
                    RefreshModel model=new RefreshModel("item"+i,"info");
                    list.add(model);
                }

                dismissLoadingDialog();
                mRefreshLayout.endLoadingMore();
                mAdapter.addMoreData(list);
            }
        }, 3000);
        return true;
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        showToast("点击了条目 " + position);
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        if (childView.getId() == R.id.tv_item_normal_delete) {
            mAdapter.removeItem(position);
        }
    }
}
