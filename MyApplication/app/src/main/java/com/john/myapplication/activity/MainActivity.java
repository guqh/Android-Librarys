package com.john.myapplication.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.john.librarys.floatwindow.FloatWindowService;
import com.john.librarys.uikit.activity.BaseActivity;
import com.john.librarys.utils.ContextManager;
import com.john.librarys.utils.util.DialogHelper;
import com.john.myapplication.MyConstants;
import com.john.myapplication.R;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by guqh on 2017/8/2.
 *  权限处理
 *  启用1像素悬浮窗 保活程序进程
 */
public class MainActivity extends BaseActivity {

    @Bind(R.id.enableCamera)
    Button enableCamera;
    @Bind(R.id.chronometer)
    Chronometer chronometer;

    private Intent floatIntent;
    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;
    private LayoutInflater layoutInflater;
    private DisplayMetrics dm;
    private PopupWindow fabuYijiPopup;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setWillDoubleClickExitApp(true);
        ButterKnife.bind(this);

//        showLoadingDialog("哈哈哈哈");

        //初始化 动态更新 图标
        initS11AndS12();

        //启动1像素悬浮窗
        floatIntent=new Intent(this, FloatWindowService.class);
        askForePrmission();


        // 权限处理 必须在初始化阶段调用,例如onCreate()方法中
        new RxPermissions(this)
                .requestEach(Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.KILL_BACKGROUND_PROCESSES,
                        Manifest.permission.GET_TASKS,
                        Manifest.permission.SYSTEM_ALERT_WINDOW,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                        Manifest.permission.INSTALL_SHORTCUT)
                .subscribe(new Action1<Permission>() {
                    @Override
                    public void call(Permission permission) {
                        if (permission.granted) {
                            LogUtils.e("permission允许");
                        } else if (permission.shouldShowRequestPermissionRationale){
                            LogUtils.e("permission用户拒绝了该权限，没有选中『不再询问』");
                        } else {
                            LogUtils.e("permission禁止");
                        }
                    }
                });

        //点击触发 权限处理
        RxView.clicks(enableCamera)
                .compose(new RxPermissions(this).ensureEach(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.KILL_BACKGROUND_PROCESSES,
                        Manifest.permission.CAMERA,
                        Manifest.permission.GET_TASKS,
                        Manifest.permission.SYSTEM_ALERT_WINDOW,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                        Manifest.permission.INSTALL_SHORTCUT))
                .subscribe(new Action1<Permission>() {
                    @Override
                    public void call(Permission permission) {
                        if (permission.granted) {
                            LogUtils.e("permission允许");
                            showToast("已授权");
                        } else if (permission.shouldShowRequestPermissionRationale){
                            LogUtils.e("permission用户拒绝了该权限，没有选中『不再询问』");
                            showToast("已拒绝");
                        } else {
                            LogUtils.e("permission禁止");
                            showToast("已禁止");
                        }
                    }
                });

    }

    /**
     * 请求用户给予悬浮窗的权限
     */
    public void askForePrmission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                showDialog();
            } else {
                startService(floatIntent);
            }
        }else {
            startService(floatIntent);
        }
    }

    //弹窗提示 权限授权
    private void showDialog() {
        new AlertDialog.Builder(this).setCancelable(false).setTitle("应用权限授权").setMessage("请开启悬浮窗的权限，否则部分功能不能正常使用！").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            }
        }).show();
    }

    //返回值判断悬浮窗权限是否授权成功
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, "开启失败，悬浮窗功能不能正常使用", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "开启成功！", Toast.LENGTH_SHORT).show();
                    //启动FxService
                    startService(floatIntent);
                }
            }
        }
    }

    @OnClick(R.id.openweb)
    void openWebViewActivity(){
        String url = "http://720yun.com/t/739j5rkOtv6?pano_id=6999722";
        Intent intent= new Intent(mContext,WebViewActivity.class);
        intent.putExtra(MyConstants.EXTRA_URL,url);
        startActivity(intent);
    }
    @OnClick(R.id.fab)
    void openIssueSelectorActivity(){
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        showYijiFabu(findViewById(R.id.fab));

    }
    @OnClick(R.id.recycl_refresh)
    void openRecycleViewRefreshActivity(){
        startActivity(new Intent(mContext,RecycleViewRefreshActivity.class));
    }
    @OnClick(R.id.openvr)
    void openGoogleVRActivity(){
        startActivity(new Intent(mContext,GoogleVRActivity.class));
    }
    @OnClick(R.id.opengl)
    void openOpenGlActivity(){
        startActivity(new Intent(mContext,OpenGlActivity.class));
    }
    @OnClick(R.id.button)
    void openKotlinAndDataBindingActivity(){
        startActivity(new Intent(mContext,KotlinAndDataBindingActivity.class));
    }
    @OnClick(R.id.button2)
    void openDatabindingRecyclerActivity(){
        startActivity(new Intent(mContext,DatabindingRecyclerActivity.class));
    }
    @OnClick(R.id.button3)
    void openDatabindinglistviewActivity(){
//        startActivity(new Intent(mContext,DatabindingListViewActivity.class));
        ContextManager.startActivity(mContext,DatabindingListViewActivity.class);
    }
    @OnClick(R.id.button4)
    void openStepViewActivity(){
        ContextManager.startActivity(mContext,StepViewActivity.class);
    }

    @OnClick({R.id.btnStart,R.id.btnStop,R.id.btnReset,R.id.btn_format})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnStart:
                chronometer.start();// 开始计时
                break;
            case R.id.btnStop:
                chronometer.stop();// 停止计时
                break;
            case R.id.btnReset:
                chronometer.setBase(SystemClock.elapsedRealtime());// 复位
                break;
            case R.id.btn_format:
                chronometer.setFormat("Time：%s");// 更改时间显示格式
                break;
        }
    }

    //=========================================图标动态更换==================================
    private ComponentName mDefault;
    private ComponentName mDouble11;
    private ComponentName mDouble12;
    private PackageManager mPm;

    private void initS11AndS12(){
        mPm = getApplicationContext().getPackageManager();
        mDefault=getComponentName();
        mDouble11=new ComponentName(getBaseContext(),"com.john.myapplication.Test11");
        mDouble12=new ComponentName(getBaseContext(),"com.john.myapplication.Test12");
    }
    private void enableComponent(ComponentName componentName) {
        mPm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);
        //Intent 重启 Launcher 应用
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        List<ResolveInfo> resolves = mPm.queryIntentActivities(intent, 0);
        for (ResolveInfo res : resolves) {
            if (res.activityInfo != null) {
                ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                am.killBackgroundProcesses(res.activityInfo.packageName);
            }
        }
    }
    private void disableComponent(ComponentName componentName) {
        mPm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
    }
    @OnClick(R.id.s11)
    void onS11(){
        disableComponent(mDefault);
        disableComponent(mDouble12);
        enableComponent(mDouble11);
    }
    @OnClick(R.id.s12)
    void onS12(){
        disableComponent(mDefault);
        disableComponent(mDouble11);
        enableComponent(mDouble12);
    }






//悬浮按钮 启动窗口
    TextView fabu_popup_dongtai;
    TextView fabu_popup_changdi;
    ImageView fabu_popup_close_img;
    private void showYijiFabu(View parent) {
        View yijiView = layoutInflater.inflate(R.layout.fabu_popwindow_layout, null);
        fabu_popup_dongtai= yijiView.findViewById(R.id.fabu_popup_dongtai);
        fabu_popup_changdi= yijiView.findViewById(R.id.fabu_popup_changdi);
        fabu_popup_close_img= yijiView.findViewById(R.id.fabu_popup_close_img);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fabu_popup_dongtai.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.pop_btn_in));
                fabu_popup_dongtai.setVisibility(View.VISIBLE);

                fabu_popup_changdi.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.pop_btn_in2));
                fabu_popup_changdi.setVisibility(View.VISIBLE);

                fabu_popup_close_img.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.anim_rotate));
                fabu_popup_close_img.setVisibility(View.VISIBLE);
            }
        },300);

        // 创建一个PopuWidow对象
        if (fabuYijiPopup == null) {
            fabuYijiPopup = new PopupWindow(yijiView, dm.widthPixels, LinearLayout.LayoutParams.MATCH_PARENT);
        }
        fabuYijiPopup.setAnimationStyle(R.style.popWindow_animation_in2out);

        fabuYijiPopup.setFocusable(true);
        // 设置允许在外点击消失
//        fabuYijiPopup.setOutsideTouchable(true);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
//        fabuYijiPopup.setBackgroundDrawable(new BitmapDrawable());
        // PopupWindow的显示及位置设置
        fabuYijiPopup.showAtLocation(parent, Gravity.CENTER, 0, -500);

        fabu_popup_dongtai.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fabuYijiPopup.dismiss();
                        DialogHelper.showDialogOne("hahahah");
                    }
                });
        fabu_popup_changdi.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fabuYijiPopup.dismiss();
                        DialogHelper.showNumDialog(mContext,"这是标题","666",new DialogHelper.MyOnKeyListner(){
                            @Override
                            public boolean onKey(View v, int keyCode, KeyEvent event) {
                                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                                    DialogHelper.dismissDialog();
                                    showToast(editText.getText().toString());
                                }
                                return super.onKey(v, keyCode, event);
                            }
                        });
                    }
                });
        fabu_popup_close_img.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fabuYijiPopup.dismiss();
                        fabuYijiPopup=null;                    }
                });
    }
}
