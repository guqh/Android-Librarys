package com.john.librarys.uikit.dialog;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.john.librarys.R;
import com.john.librarys.net.interf.ServiceTask;


/**
 * 加载框
 */
public class LoadingDialogFragment extends BaseDialogFragment {
    final String TAG = LoadingDialogFragment.class.getSimpleName();

    public LoadingDialogFragment() {
        setStyle(STYLE_NO_FRAME, 0);
    }
    TextView message;
    String mMsg;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_loading, null);
        message = (TextView) view.findViewById(R.id.msg);
        getDialog().setCanceledOnTouchOutside(false);
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        message.setText(mMsg);
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

    private static LoadingDialogFragment loadingDialog;

    public static void showLoading(Activity activity, String msg) {
        showLoading(activity, msg, true,null);
    }

    public static void showLoading(Activity activity) {
        showLoading(activity, activity.getString(R.string.str_loading), false,null);
    }

    public static void showLoading(Activity activity, boolean outCancel) {
        showLoading(activity, activity.getString(R.string.str_loading), outCancel,null);
    }
    public static void showLoading(Activity activity,  ServiceTask task) {
        showLoading(activity, activity.getString(R.string.str_loading), false,task);
    }
    /**
     * 显示文字的 dialog
     * @param activity
     * @param msg
     * @param outCancel
     */
    public static void showLoading(Activity activity, String msg, boolean outCancel, ServiceTask task) {
        if (loadingDialog != null && loadingDialog.isVisible()) {
            return;
        } else {
            dismissDialog();
            loadingDialog = new LoadingDialogFragment();
            loadingDialog.setCancelable(outCancel);
            loadingDialog.show(activity.getFragmentManager());
            loadingDialog.mMsg=msg;
        }
        if (task!=null){
            loadingDialog.addServiceTask(task);
        }
    }

    /**
     * 取消dialog
     */
    public static void dismissDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismissAllowingStateLoss();
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }
}
