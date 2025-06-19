package com.zephyrover.vehicles.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zephyrover.vehicles.entity.Log;

/**
 * 操作日志服务接口
 */
public interface LogService {

    /**
     * 分页查询操作日志
     */
    IPage<Log> getLogPage(int page, int size, Long userId, String operation);

    /**
     * 记录操作日志
     */
    boolean saveLog(Log log);

    /**
     * 清理过期日志
     */
    int cleanupLogs(int daysAgo);

    /**
     * 记录用户操作（便捷方法）
     */
    void recordUserOperation(Long userId, String operation, String ipAddress);
}
