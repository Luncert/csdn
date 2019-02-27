package org.luncert.csdn2.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.luncert.csdn2.model.h2.SpiderProcess;
import org.luncert.csdn2.model.normal.Category;
import org.luncert.csdn2.repository.h2.SpiderProcessRepos;
import org.luncert.csdn2.service.ArticleService;
import org.luncert.csdn2.service.LogService;
import org.luncert.csdn2.util.NormalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 根据Category爬取csdn所有文章
 * TODO: 0 downtime code updating
 */
@Service
public class SpiderService
{

    private ExecutorService threadPool = Executors.newCachedThreadPool();

    @Autowired
    private ArticleService articleService;

    @Autowired
    private LogService logService;

    @Autowired
    private EventService eventService;

    @Autowired
    private SpiderProcessRepos spiderProcessRepos;

    private Status status = Status.Stopped;

    private boolean startedOnce = false;

    /**
     * volatile操作的缺陷在于：很多java运算是非
     * 原子性的，所以仍然后数据不一致的问题
     */
    private volatile boolean wannaStop = false;

    public void start()
    {
        if (status != Status.Stopped) {
            logService.warn("spider is " + status.name() + ", start is not allowed");
            return;
        }

        changeStatus(Status.Starting);
        // 为每一个category都创建一个线程运行爬虫
        wannaStop = false;
        for (Category category : Category.categories) {
            threadPool.execute(() -> {
                int errorTime = 0;
                String shownOffset = null;
                if (startedOnce) {
                    // 已经启动过一次了，则从数据库恢复运行
                    SpiderProcess process = spiderProcessRepos.findByCategory(category);
                    if (process != null) {
                        shownOffset = process.getShownOffset();
                        logService.info("spider(category = " + category +
                            ") resume processing from shownOffset = " + shownOffset);
                    }
                }
                // 如果连续三次getMoreArticle抛出异常，则结束执行该线程的爬虫
                while (errorTime < 3) {
                    // 持久化当前进度，用于恢复运行
                    if (wannaStop) {
                        spiderProcessRepos.save(
                            new SpiderProcess(category, shownOffset));
                        break;
                    }
                    try {
                        shownOffset = articleService.getMoreArticle(category, shownOffset);
                        errorTime = 0;
                    } catch (Exception e) {
                        // save log to Mongodb
                        logService.error("@SpiderService.start.getMoreArticle(" + category + ", " + shownOffset + ")",
                                NormalUtil.throwableToString(e));
                        errorTime++;
                    }
                    try {
                        Thread.sleep(1500);
                    } catch (Exception e) {}
                }
                // 退出日志
                if (errorTime == 3) {
                    logService.warn("spider(category = " + category +
                        ") stoped after 3 continuous exception occured");
                }
                else {
                    logService.info("spider(category = " + category +
                        ") stoped gracefully");
                }
            });
        }
        // mark startedOnce
        startedOnce = true;
        changeStatus(Status.Running);
    }

    public boolean stop()
    {
        if (status != Status.Running) {
            logService.warn("spider is " + status.name().toLowerCase() + ", stop is not allowed");
            return false;
        }

        changeStatus(Status.Stopping);
        wannaStop = true;
        try {
            while (true) {
                try {
                    // 无限等待，并且返回false时抛出异常
                    assert threadPool.awaitTermination(0, TimeUnit.SECONDS) == true;
                    changeStatus(Status.Stopped);
                    return true;
                } catch (InterruptedException e) {
                    // 如果是被中断等待，则中心进入等待
                    logService.error("interrupted on awaitTermination",
                            NormalUtil.throwableToString(e));
                }
            }
        } catch (AssertionError e) {
            // NOTICE: assert失败应该是不可能的，如果出现了，那就是
            // 无法预料的情况了
            logService.error("assertion error on awaitTermination",
                    NormalUtil.throwableToString(e));
            return false;
        }
    }

    public static final String ON_CHANGE_STATUS = "OnChangeStatus";

    /**
     * 同步更新状态
     */
    private synchronized void changeStatus(Status newStatus) {
        eventService.submit(ON_CHANGE_STATUS, newStatus);
        status = newStatus;
    }

    public Status status() {
        return status;
    }

    public enum Status {
        Starting, Running, Stopping, Stopped
    }

}