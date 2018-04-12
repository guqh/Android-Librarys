package com.john.librarys.uikit.helper;

import com.john.librarys.pulltorefresh.PullToRefreshAdapterViewBase;
import com.john.librarys.pulltorefresh.PullToRefreshBase;
import com.john.librarys.utils.Constants;
import com.john.librarys.net.interf.ServiceTask;
import com.john.librarys.uikit.adapter.DynamicListAdapter;
import com.john.librarys.uikit.entity.PageResult;

import java.util.Date;

/**
 * 下拉分页helper，更加方便的使用下拉刷新更新 分页数据
 *
 * @param <T> T:目标的类型，比如通知列表的话 就是 Notice
 */
public abstract class PullToRefreshPageHelper<T> extends PullToRefreshHelper {

    //下面两个参数用于 与服务器查询分页列表用
    private Date mTimestamp;//下拉刷新时间戳
    private int mPageNo = 0;//当前页数
    private int mTotalPages = 0;//总页面数量
    private int mTotalCount = 0;//总共数量


    ServiceTask<PageResult<T>> mDataUpdateTask;//任务更新task

    @Override
    public void setUp(PullToRefreshAdapterViewBase refreshView, DynamicListAdapter adapter) {
        super.setUp(refreshView, adapter);
    }

    /**
     * 获取任务更新task
     *
     * @return
     */
    public ServiceTask<PageResult<T>> getDataUpdateServiceTask() {
        return mDataUpdateTask;
    }

    /**
     * 获取查询时间戳
     *
     * @return
     */
    public Date getTimestamp() {
        return mTimestamp;
    }

    /**
     * 获取页码
     *
     * @return
     */
    public int getPageNo() {
        return mPageNo;
    }

    /**
     * 设置时间戳
     */
    public void setTimestamp(Date timestamp) {
        mTimestamp = timestamp;
    }

    /**
     * 设置页码
     *
     * @param pageNo
     */
    public void setPageNo(int pageNo) {
        mPageNo = pageNo;
    }

    /**
     * 获取下一页页码
     *
     * @param clear 是否需要清除数据？
     * @return
     */
    public int getNextPage(boolean clear) {
        return clear ? 1 : getPageNo() + 1;
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        mTimestamp = new Date();
        super.onPullDownToRefresh(refreshView);
    }

    @Override
    public void onLastItemVisible() {
        if (isLastPage()) {
            onLastPage();
        } else if(getPageNo() != 0){//当拉到底部时刷新数据，判断是否从来没有加载过数据
            super.onLastItemVisible();
        }
    }

    @Override
    public void updateData(final PullToRefreshBase refreshView, final DynamicListAdapter adapter, final boolean clear) {
        final int pageNo = getNextPage(clear);

        mDataUpdateTask = new ServiceTask<PageResult<T>>() {
            @Override
            public void onComplete(int resultCode, String msg,PageResult<T> pageResult) {

                //预处理
                resultCode = preTaskResult(resultCode, pageResult);
                //根据状态刷新数据
                if (resultCode == Constants.STATE_CODE_SUCCESS) {
                    //由于有可能没数据，但是服务端返回200，还是认为是成功

                    if(pageResult != null) {
                        setPageNo(pageNo);
                        setTotalCount(pageResult.getTotalCount());
                        setTotalPages(pageResult.getTotalPages());
                        if (clear) {
                            adapter.clear();
                        }

                        adapter.addItems(pageResult.getResult());
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    onTaskError(resultCode, pageResult);
                }
                //后处理
                postTaskResult(resultCode, pageResult);

                refreshView.onRefreshComplete();
            }
        };

        startTask(this.getTimestamp(), pageNo, mDataUpdateTask);
    }

    /**
     * 启动更新任务，例子： NoticeService.getNoticeList(timestamp, pageNo, task);
     *
     * @param timestamp
     * @param pageNo
     * @param task
     */
    public abstract void startTask(Date timestamp, int pageNo, ServiceTask<PageResult<T>> task);

    /**
     * 重写该方法 可以用于service执行完毕后 ,预处理数据
     * 处理完毕后可以返回 处理结果
     *
     * @param resultCode
     * @param pageResult
     * @return resultCode 处理完毕后的结果
     */
    public int preTaskResult(int resultCode, PageResult<T> pageResult) {
        //数据预处理
        return resultCode;
    }

    /**
     * 重写该方法 可以用于service执行完毕后 ,数据更新到adapter后，后置处理数据
     *
     * @param resultCode
     * @param pageResult
     */
    public void postTaskResult(int resultCode, PageResult<T> pageResult) {

    }

    /**
     * 最后一页还尝试上拉更新的操作
     */
    public void onLastPage() {

    }

    /**
     * 重写该方法 可以用于service执行完毕后 ，处理错误
     *
     * @param resultCode
     * @param data
     */
    public void onTaskError(int resultCode, PageResult<T> data) {
        //错误处理
    }

    /**
     * 是否最后一页
     *
     * @return
     */
    public boolean isLastPage() {
        return mPageNo >= mTotalPages && mPageNo != 0;
    }

    public int getTotalCount() {
        return mTotalCount;
    }

    public void setTotalCount(int mTotalCount) {
        this.mTotalCount = mTotalCount;
    }

    public int getTotalPages() {
        return mTotalPages;
    }

    public void setTotalPages(int mTotalPages) {
        this.mTotalPages = mTotalPages;
    }
}
