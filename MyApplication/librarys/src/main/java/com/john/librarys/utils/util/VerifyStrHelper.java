package com.john.librarys.utils.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by LinYi.
 * 验证字符串是否合格
 */
public class VerifyStrHelper {

    private VerifyStrHelper() {
        throw new IllegalArgumentException("VerifyStrHelper can not be instantiated");
    }

    public static final String NICK_NAME_PATTERN = "[\\u4e00-\\u9fa5_a-zA-Z0-9_-]{4,20}";
    public static final String MOBILE_PATTERN = "(13\\d|14[57]|15[^4,\\D]|17[3678]|18\\d)\\d{8}|170[059]\\d{7}";


    /**
     * 验证昵称是否合法
     *
     * @param nickName
     * @return
     */
    public static boolean verifyNickName(String nickName) {
        return verify(NICK_NAME_PATTERN, nickName);
    }

    /**
     * 验证是否为手机号码格式
     *
     * @param mobile
     * @return
     */
    public static boolean verifyMobile(String mobile) {
        return verify(MOBILE_PATTERN, mobile);
    }

    private static boolean verify(String patternStr, String str) {
        if (TextUtils.isEmpty(str))
            return false;
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 验证密码是否正确,字母，特殊符号，数字，三者要有包含其中两者， 判断密码不能是纯字母和纯数字， 长度要大于8并且小于16，
     *
     * @param password
     * @return
     */
    public static boolean verifyPwd(String password) {
        if (password != null && password.length() <= 16 && password.length() >= 6) {
            boolean b1 = regex(password, "^.*[\\d]+.*$");
            boolean b2 = regex(password, "^.*[A-Za-z]+.*$");
            boolean b3 = regex(password, "^.*[_@#%&^+-/*\\/]+.*$");
            if ((b1 && b2) || (b1 && b3) || (b2 && b3)) {
                return true;
            }
        }
        return false;
    }

    public static boolean regex(String value, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

}
