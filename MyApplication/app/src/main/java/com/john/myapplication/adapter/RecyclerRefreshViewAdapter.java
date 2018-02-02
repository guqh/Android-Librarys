package com.john.myapplication.adapter;

import android.support.v7.widget.RecyclerView;

import com.john.myapplication.Bean.RefreshModel;
import com.john.myapplication.R;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * @author John Gu
 * @date 2017/11/24.
 */

public class RecyclerRefreshViewAdapter extends BGARecyclerViewAdapter<RefreshModel> {

    public RecyclerRefreshViewAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_normal);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, RefreshModel model) {
        helper.setText(R.id.tv_item_normal_title, model.title).setText(R.id.tv_item_normal_detail, model.detail);
    }

    @Override
    protected void setItemChildListener(BGAViewHolderHelper helper, int viewType) {
        helper.setItemChildClickListener(R.id.tv_item_normal_delete);
    }
}
