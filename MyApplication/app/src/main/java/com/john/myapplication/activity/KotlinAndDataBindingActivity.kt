package com.john.myapplication.activity

import android.databinding.DataBindingUtil
import com.john.librarys.uikit.activity.BaseActivity
import com.john.myapplication.Bean.User
import com.john.myapplication.R
import com.john.myapplication.databinding.ActivityDatabindingBinding

/**
 * Created by guqh on 2017/8/2.

 * databinding activty 实例
 */

class KotlinAndDataBindingActivity : BaseActivity() {

    override fun setBindingContentView() {
        val binding : ActivityDatabindingBinding = DataBindingUtil.setContentView(this, R.layout.activity_databinding)
        val mUser:User = User()
        mUser.name="这是User_Name"
        mUser.photo="http://wx.zshisong.com:8085/imgServer/upload/pic/02f3b3322a844014923453e95806f8a2.jpg"
        binding.mUser=mUser
    }


}
