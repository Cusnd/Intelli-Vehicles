package com.zephyrover.vehicles.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zephyrover.vehicles.common.Result;
import com.zephyrover.vehicles.entity.Log;
import com.zephyrover.vehicles.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 操作日志控制器
 */
@RestController
@RequestMapping("/api/logs")
@CrossOrigin(origins = "*")
public class LogController {

    @Autowired
    private LogService logService;

    /**
     * 分页查询操作日志
     */
    @GetMapping
    public Result<IPage<Log>> getLogPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String operation) {
        try {
            IPage<Log> pageResult = logService.getLogPage(page, size, userId, operation);
            return Result.success(pageResult);
        } catch (Exception e) {
            return Result.error("查询操作日志失败：" + e.getMessage());
        }
    }

    /**
     * 记录操作日志
     */
    @PostMapping
    public Result<String> saveLog(@RequestBody Log log) {
        try {
            boolean success = logService.saveLog(log);
            if (success) {
                return Result.success("记录日志成功");
            } else {
                return Result.error("记录日志失败");
            }
        } catch (Exception e) {
            return Result.error("记录日志失败：" + e.getMessage());
        }
    }

    /**
     * 清理过期日志
     */
    @DeleteMapping("/cleanup")
    public Result<String> cleanupLogs(@RequestParam(defaultValue = "30") int daysAgo) {
        try {
            int count = logService.cleanupLogs(daysAgo);
            return Result.success("清理了 " + count + " 条过期日志");
        } catch (Exception e) {
            return Result.error("清理日志失败：" + e.getMessage());
        }
    }
}
