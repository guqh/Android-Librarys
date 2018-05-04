package com.john.librarys.uikit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.john.librarys.R;
import com.john.librarys.utils.util.DateHelper;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期选择对话框
 * @author chenzipeng
 *
 */
public class CustomDateTimePickerDialog extends Dialog implements OnDateChangedListener,OnTimeChangedListener,View.OnClickListener{
	private View mContentView;
	private DatePicker mDatePicker;
	private TimePicker mTimePicker;
	private Date mInitDate;
	 
	private OnDateChangedListener mOnDateChangedListener;
	private OnTimeChangedListener mOnTimeChangedListener;
	private OnSelectedListener mOnSelectedListener;
	
	public CustomDateTimePickerDialog(Context context, Date initDate) {
		super(context, R.style.CustomDialog_CustomDateTimePickerDialog);
		mInitDate = initDate;
	}

	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_custom_datetimepicker);
		mContentView = findViewById(R.id.dialog_content);
		mDatePicker = (DatePicker) findViewById(R.id.datepicker);
		mTimePicker = (TimePicker) findViewById(R.id.timepicker);
		
		if(mInitDate == null){
			mInitDate = new Date();
		}
		
		Calendar ca = DateHelper.getCalendar(mInitDate);
		
		mDatePicker.init(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DAY_OF_MONTH), this);
		
		mTimePicker.setHour(ca.get(Calendar.HOUR_OF_DAY));
		mTimePicker.setMinute(ca.get(Calendar.MINUTE));
		mTimePicker.setOnTimeChangedListener(this);
		
		findViewById(R.id.submit).setOnClickListener(this);
		findViewById(R.id.cannel).setOnClickListener(this);
		
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
		
		if(mOnDateChangedListener != null){
			mOnDateChangedListener.onDateChanged(view, year, monthOfYear, dayOfMonth);
		}

	}
	
	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		
		if(mOnTimeChangedListener != null){
			mOnTimeChangedListener.onTimeChanged(view, hourOfDay, minute);
		}

	}
	
	
	
	/**
	 * 设置日期选择
	 * @param listener
	 */
	public void setOnDateChangedListener(OnDateChangedListener listener){
		mOnDateChangedListener = listener;
	}
	
	/**
	 * 设置日期选择
	 * @param listener
	 */
	public void setOnTimeChangedListener(OnTimeChangedListener listener){
		mOnTimeChangedListener = listener;
	}


	/**
	 * 确定后的日期选择
	 * @param listener
	 */
	public void setOnSelectedListener(OnSelectedListener listener){
		mOnSelectedListener = listener;
	}

	
	public static interface OnSelectedListener{
		public void onSelected(Date date);
	}


	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.submit) {
			dismiss();
			if(mOnSelectedListener != null){
				Calendar ca = Calendar.getInstance();
				ca.set(Calendar.YEAR, mDatePicker.getYear());
				ca.set(Calendar.MONTH, mDatePicker.getMonth());
				ca.set(Calendar.DAY_OF_MONTH, mDatePicker.getDayOfMonth());
				
				ca.set(Calendar.HOUR_OF_DAY, mTimePicker.getHour());
				ca.set(Calendar.MINUTE, mTimePicker.getMinute());
				Date date = ca.getTime();
				mOnSelectedListener.onSelected(date);
			}
		} else if (id == R.id.cannel) {
			cancel();
		} else {
			
		}
	}
	
	public void setMinDate(Date date){
		mDatePicker.setMinDate(date.getTime());
		//TODO 设置 TimePicker 也设置最小值
	}
	
	public void setMaxDate(Date date){
		mDatePicker.setMaxDate(date.getTime());
		//TODO 设置 TimePicker 也设置最大值
	}

	/**
	 * 设置 日期选择器显示
	 * @param show
	 */
	public void setDateCalendarView(boolean show){
		mDatePicker.setVisibility(show ? View.VISIBLE : View.GONE);
	}

	/**
	 * 设置 时间选择器显示
	 * @param show
	 */
	public void setDateSpinnersShow(boolean show){
		mTimePicker.setVisibility(show ? View.VISIBLE : View.GONE);
	}
}
