/**
 * Copyright 2015 bingoogolapple
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.john.librarys.uikit.adapter.baseadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 上午1:00
 * 描述:适用于AdapterView的item的ViewHolder
 */
public class MyBaseAdapterViewHolder {
    protected View mConvertView;
    protected MyBaseViewHolderHelper mViewHolderHelper;

    private MyBaseAdapterViewHolder(ViewGroup parent, int layoutId) {
        mConvertView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        AutoUtils.auto(mConvertView);
        mConvertView.setTag(this);
        mViewHolderHelper = new MyBaseViewHolderHelper(parent, mConvertView);
    }

    /**
     * 拿到一个可重用的ViewHolder对象
     *
     * @param convertView
     * @param parent
     * @param layoutId
     * @return
     */
    public static MyBaseAdapterViewHolder dequeueReusableAdapterViewHolder(View convertView, ViewGroup parent, int layoutId) {
        if (convertView == null) {
            return new MyBaseAdapterViewHolder(parent, layoutId);
        }
        return (MyBaseAdapterViewHolder) convertView.getTag();
    }

    public MyBaseViewHolderHelper getViewHolderHelper() {
        return mViewHolderHelper;
    }

    public View getConvertView() {
        return mConvertView;
    }

}