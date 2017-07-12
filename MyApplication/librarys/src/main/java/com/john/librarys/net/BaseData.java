package com.john.librarys.net;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by 红哥哥 on 2016/7/29.
 * baseBody
 */
public class BaseData implements Serializable {

    @SerializedName("succes")
    int success;
    @SerializedName("msg")
    String msg;
    @SerializedName("type")
    String type;
    @SerializedName("oper")
    String oper;
    @SerializedName("body")
    Object body;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOper() {
        return oper;
    }

    public void setOper(String oper) {
        this.oper = oper;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
