package com.john.librarys.uikit.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.HashSet;
import java.util.Set;

/**
 * 导航栏控件
 * @author chenzipeng
 *
 */
public class NavLayout extends LinearLayout {
	private Set<NavIconView> mNavIcons = new HashSet<>();

	public NavLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private OnClickListener mNavClicklistenner;

	public void setOnItemClickListener(OnClickListener listener){
		mNavClicklistenner = listener;
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			if (child instanceof NavIconView) {
				mNavIcons.add((NavIconView) child);
				child.setOnClickListener(mNavClicklistenner);
			}
		}
	}

	@Override
	protected void onFinishInflate() {
		setOnItemClickListener(mNavClicklistenner);
	};

	/**
	 * 设置导航栏图标选中状态，
	 * @param pageId
	 */
	public void setNavIconSelected(int pageId){
		for (NavIconView icon : mNavIcons) {
			icon.setSelected(icon.getPageId() == pageId);
		}
	}
}
