package com.john.myapplication.adapter;

import android.support.v7.widget.RecyclerView;
import com.john.librarys.uikit.adapter.baseadapter.MyBaseRecyclerViewAdapter;
import com.john.librarys.uikit.adapter.baseadapter.MyBaseViewHolderHelper;
import com.john.myapplication.Bean.RefreshModel;
import com.john.myapplication.R;

/**
 * @author John Gu
 * @date 2017/11/24.
 */

public class RecyclerRefreshViewAdapter extends MyBaseRecyclerViewAdapter<RefreshModel> {

    public RecyclerRefreshViewAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_normal);
    }

    @Override
    protected void fillData(MyBaseViewHolderHelper helper, int position, RefreshModel model) {
        helper.setText(R.id.tv_item_normal_title, model.title).setText(R.id.tv_item_normal_detail, model.detail);
    }

    @Override
    protected void setItemChildListener(MyBaseViewHolderHelper helper, int viewType) {
        helper.setItemChildClickListener(R.id.tv_item_normal_delete);
    }
}
