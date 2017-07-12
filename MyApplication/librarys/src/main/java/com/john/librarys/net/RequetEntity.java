package com.john.librarys.net;

import java.io.Serializable;

/**
 * Created by 红哥哥 on 2016/7/29.
 * 登陆请求
 */
public class RequetEntity implements Serializable {

    private Object body;

    private String oper;

    private String type;

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public String getOper() {
        return oper;
    }

    public void setOper(String oper) {
        this.oper = oper;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
