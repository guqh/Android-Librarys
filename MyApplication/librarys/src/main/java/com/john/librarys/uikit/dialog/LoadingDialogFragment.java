package com.john.librarys.uikit.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.john.librarys.R;


/**
 * 加载框
 */
public class LoadingDialogFragment extends BaseDialogFragment {
    final String TAG = LoadingDialogFragment.class.getSimpleName();

    public LoadingDialogFragment() {
        setStyle(STYLE_NO_FRAME, 0);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_loading, null);
        getDialog().setCanceledOnTouchOutside(false);
        return view;
    }

    /**
     * 显示
     *
     * @param fragmentManager
     */
    public void show(FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            show(fragmentManager, TAG);
        }
    }
}
