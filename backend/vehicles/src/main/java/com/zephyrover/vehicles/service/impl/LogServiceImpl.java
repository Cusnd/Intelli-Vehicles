package com.zephyrover.vehicles.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zephyrover.vehicles.entity.Log;
import com.zephyrover.vehicles.mapper.LogMapper;
import com.zephyrover.vehicles.service.LogService;

/**
 * 操作日志服务实现类
 */
@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogMapper logMapper;

    @Override
    public IPage<Log> getLogPage(int page, int size, Long userId, String operation) {
        Page<Log> pageInfo = new Page<>(page, size);
        QueryWrapper<Log> queryWrapper = new QueryWrapper<>();

        if (userId != null) {
            queryWrapper.eq("user_id", userId);
        }

        if (StringUtils.hasText(operation)) {
            queryWrapper.like("operation", operation);
        }

        queryWrapper.orderByDesc("create_time");



        return logMapper.selectPage(pageInfo, queryWrapper);
    }

    @Override
    public boolean saveLog(Log log) {
        if (log.getCreateTime() == null) {
            log.setCreateTime(LocalDateTime.now());
        }
        return logMapper.insert(log) > 0;
    }

    @Override
    public int cleanupLogs(int daysAgo) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysAgo);
        QueryWrapper<Log> wrapper = new QueryWrapper<>();
        wrapper.lt("create_time", cutoffDate);
        return logMapper.delete(wrapper);
    }

    @Override
    public void recordUserOperation(Long userId, String operation, String ipAddress) {
        Log log = new Log();
        log.setUserId(userId);
        log.setOperation(operation);
        log.setIpAddress(ipAddress);
        log.setCreateTime(LocalDateTime.now());

        // 考虑采取异步日志记录，防止其影响业务主功能
        try {
            saveLog(log);
        } catch (Exception e) {
            System.err.println("记录操作日志失败: " + e.getMessage());
        }
    }
}
