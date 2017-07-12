package com.john.librarys.uikit.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.john.librarys.R;
import com.john.librarys.uikit.adapter.DynamicPagerAdapter;


public class AlbumViewPagerAdapter extends DynamicPagerAdapter<AlbumView.AlbumPic> implements View.OnClickListener {
    private AlbumPicView mCurrentView;
    private AlbumView mAlbumView;

    public AlbumViewPagerAdapter(AlbumView albumView) {
        super(albumView.getContext());
        mAlbumView = albumView;
    }

    @Override
    public View instantiateView(ViewGroup container, int position) {
        AlbumView.AlbumPic albumPic = getItem(position);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_album_pic, container, false);
        AlbumPicView imgView = (AlbumPicView) view.findViewById(R.id.album_pic);
        imgView.setUrl(albumPic.imgUrl);
        imgView.setOnClickListener(this);
        return view;
    }


    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentView = (AlbumPicView) object;
    }

    public AlbumPicView getPrimaryItem() {
        return mCurrentView;
    }

    //控制 点击 显示，隐藏 contentview
    @Override
    public void onClick(View v) {
        mAlbumView.setContentVisible(mAlbumView.isContentVisible() ? View.GONE : View.VISIBLE);
    }
}
