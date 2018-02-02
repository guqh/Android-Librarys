package com.john.librarys.utils.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.john.librarys.R;

/**
 * Created by xstar on 2016-07-26.
 */
public class DialogHelper {
	private static Context mContext;

	public static void init(Context context) {
		mContext = context;
	}

	private static AlertDialog alertDialog;

	public static void showDialogOne(String msg, String buttn_text, DialogInterface.OnClickListener listener, DialogInterface.OnCancelListener cancelListener, boolean outCancel) {
		showDialogTwo(msg, buttn_text, listener, null, null, cancelListener, outCancel);
	}

	public static void showDialogOne(String msg, DialogInterface.OnClickListener listener) {
		showDialogTwo(msg, "确定", listener, null, null, null, false);
	}

	public static void showDialogOne(String msg) {
		showDialogOne(msg, "确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}, null, false);
	}

	public static void dismissDialog() {
		if (alertDialog != null&&alertDialog.isShowing()) alertDialog.dismiss();
		alertDialog = null;
	}

	public static void showDialogTwo(String msg, String buttn_text1, final DialogInterface.OnClickListener listener1, String buttn_text2, final DialogInterface.OnClickListener listener2, DialogInterface.OnCancelListener cancelListener, boolean outCancel) {
		if (mContext == null) return;
		dismissDialog();
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.AlertDialogCustom);
		View contentView = LayoutInflater.from(mContext).inflate(R.layout.base_dialog_layout, null, false);
		TextView message_tv = contentView.findViewById(R.id.message_tv);
		Button cancel_btn = contentView.findViewById( R.id.cancel_btn);
		Button enter_btn = contentView.findViewById( R.id.enter_btn);
		View gap = contentView.findViewById(R.id.gap);
		message_tv.setText(msg);
		builder.setView(contentView);
		if (TextUtils.isEmpty(buttn_text1) || TextUtils.isEmpty(buttn_text2)) gap.setVisibility(View.GONE);
		else gap.setVisibility(View.VISIBLE);
		if (buttn_text2 != null) {
			cancel_btn.setText(buttn_text2);
			cancel_btn.setVisibility(View.VISIBLE);
		} else {
			cancel_btn.setVisibility(View.GONE);
			enter_btn.setBackgroundResource(R.drawable.dialog_one_btn_selector);
		}
		if (buttn_text1 != null) {
			enter_btn.setText(buttn_text1);
			enter_btn.setVisibility(View.VISIBLE);
		} else {
			enter_btn.setVisibility(View.GONE);
			cancel_btn.setBackgroundResource(R.drawable.dialog_one_btn_selector);
		}
		alertDialog = builder.create();
		if (listener1 != null) enter_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener1.onClick(alertDialog, DialogInterface.BUTTON_NEGATIVE);
			}
		});
		if (listener2 != null) cancel_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener2.onClick(alertDialog, DialogInterface.BUTTON_NEGATIVE);
			}
		});
		if (cancelListener != null) alertDialog.setOnCancelListener(cancelListener);
        else alertDialog.setCancelable(false);
		alertDialog.setCanceledOnTouchOutside(outCancel);
		alertDialog.show();
		alertDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
	}

	public static void showDialogTwo(String msg, DialogInterface.OnClickListener enterListner, DialogInterface.OnClickListener cancelListner, boolean outCancel) {
		showDialogTwo(msg, "确定", enterListner, "取消", cancelListner, null, outCancel);
	}
    public static void showDialogTwo(String msg, DialogInterface.OnClickListener enterListner, DialogInterface.OnClickListener cancelListner) {
        showDialogTwo(msg, enterListner, cancelListner, false);
    }
	public static void showDialogTwo(String msg, DialogInterface.OnClickListener enterListner) {
		showDialogTwo(msg, enterListner, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
	}

	public static void showDialogOne(int resid) {
		showDialogOne(mContext.getString(resid));
	}

	public static void showNumDialog(Context activity, String title, String num, MyOnKeyListner myOnKeyListner) {
		showNumDialog(activity, title, num, new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				dismissDialog();
			}
		}, myOnKeyListner);
	}

	public static void showNumDialog(Context activity, String title, String num, DialogInterface.OnCancelListener onCancelListener, MyOnKeyListner myOnKeyListner) {
		showNumDialog(activity, title, num, -1, onCancelListener, myOnKeyListner);
	}

    public static void showNumDialog(Context activity, String title, String num, int type, DialogInterface.OnCancelListener onCancelListener, MyOnKeyListner myOnKeyListner) {
        showNumDialog(activity,title,num,type,onCancelListener,myOnKeyListner,-1);
    }

	public static void showNumDialog(Context activity, String title, String num, int type, DialogInterface.OnCancelListener onCancelListener, MyOnKeyListner myOnKeyListner, int maxLength) {
		View contentView = LayoutInflater.from(activity).inflate(R.layout.num_input_pop_layout, null);
		final EditText editText = contentView.findViewById( R.id.table_remark_et);
		if (type != -1) editText.setInputType(type);
		TextView table_remark_tv = contentView.findViewById( R.id.table_remark_tv);
		table_remark_tv.setText(title);
        if (maxLength!=-1)editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
		if (!TextUtils.isEmpty(num)) {
			editText.setText(num);
			editText.setSelection(num.length());
			editText.setSelectAllOnFocus(true);
		}
		alertDialog = new AlertDialog.Builder(activity, R.style.popupDialog).setView(contentView).create();
		Window window = alertDialog.getWindow();
		window.setGravity(Gravity.BOTTOM);
		window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		if (onCancelListener instanceof MyOnCancelListner) {
			((MyOnCancelListner) onCancelListener).editText = editText;
			((MyOnCancelListner) onCancelListener).textView = table_remark_tv;
		}
		alertDialog.setOnCancelListener(onCancelListener);
		if (myOnKeyListner != null) {
			myOnKeyListner.editText = editText;
			myOnKeyListner.textView = table_remark_tv;
			editText.setOnKeyListener(myOnKeyListner);
		}
		alertDialog.show();
		WindowManager.LayoutParams layoutParams = window.getAttributes();
		layoutParams.width =  WindowManager.LayoutParams.MATCH_PARENT;
		layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setAttributes(layoutParams);
	}

	public static class MyOnKeyListner implements View.OnKeyListener {
		public TextView textView;
		public EditText editText;

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			return false;
		}
	}

	public static class MyOnCancelListner implements DialogInterface.OnCancelListener {

		public TextView textView;
		public EditText editText;

		@Override
		public void onCancel(DialogInterface dialog) {

		}
	}
}
