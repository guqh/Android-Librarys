package com.john.librarys.net;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by 红哥哥 on 2016/7/29.
 * baseBody
 */
public class BaseData implements Serializable {

    @SerializedName("code")
    Integer code;
    @SerializedName("msg")
    String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
