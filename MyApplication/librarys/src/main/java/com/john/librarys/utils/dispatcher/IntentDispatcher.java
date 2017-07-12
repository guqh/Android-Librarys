package com.john.librarys.utils.dispatcher;

import android.content.Context;
import android.content.Intent;

/**
 * intent 分发器
 */
public interface IntentDispatcher {
    /**
     * 分发intent
     *
     * 返回值：true 代表已经处理完毕，在 {@link com.zthink.ContextManager} 中不会继续后续动作。
     * @param context
     * @param intent
     * @return
     */
    boolean dispatch(Context context, Intent intent);
}
